document.addEventListener('DOMContentLoaded', function () {
    const itemsPerPage = 10;
    let currentPage = 1;
    let totalPages = 1;
    let data = [];
    let selectedTeam = '전체';

    // 오늘 날짜를 로컬 시간대로 가져옴
    const today = new Date().toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).replace(/. /g, '-').replace('.', '');

    let selectedDate = today;  // 오늘 날짜를 기본으로 설정
    const calendarEl = document.getElementById('calendar');
    let selectedDateElement = null;
    let gameDates = [];

    // 팀 필터 함수
    window.filterByTeam = function(team) {
        selectedTeam = team;
        currentPage = 1;
        document.querySelectorAll('.tag').forEach(tag => tag.classList.remove('active'));
        const selectedTag = document.querySelector(`.tag[onclick="filterByTeam('${team}')"]`);
        if (selectedTag) {
            selectedTag.classList.add('active');
        }
        loadMeetings();
    }

    // 소모임 개설하기 버튼 함수
    window.registerMeeting = function() {
        fetch('/meeting/checkLogin')
            .then(response => response.json())
            .then(data => {
                if (data.loggedIn) {
                    window.location.href = '/meeting/register';
                } else {
                    Swal.fire({
                        title: '로그인이 필요한 서비스입니다.',
                        text: '로그인을 하시겠습니까?',
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonText: '로그인',
                        cancelButtonText: '취소'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = '/member/loginpage';
                        }
                    });
                }
            })
            .catch(error => {
                console.error('로그인 상태 확인 중 오류 발생:', error);
                Swal.fire('오류 발생', '로그인 상태 확인 중 오류가 발생했습니다.', 'error');
            });
    }

    // 서버에서 경기 날짜 가져오기
    fetch('/meeting/gameDates')
        .then(response => response.json())
        .then(dates => {
            gameDates = dates.map(date => new Date(date).toISOString().split('T')[0]);
            renderCalendar();
        })
        .catch(error => {
            console.error('경기 날짜 불러오기 중 오류 발생:', error);
            renderCalendar();
        });

    // 달력 렌더링 함수
    function renderCalendar() {
        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            height: 'auto',
            contentHeight: 'auto',
            dayCellContent: function(e) {
                e.dayNumberText = e.dayNumberText.replace('일', '');
            },
            dateClick: function(info) {
                selectedDate = info.dateStr;
                currentPage = 1;

                if (selectedDateElement) {
                    selectedDateElement.classList.remove('fc-day-selected');
                }
                selectedDateElement = info.dayEl;
                selectedDateElement.classList.add('fc-day-selected');
                loadMeetings();
            },
            dayCellDidMount: function(info) {
                const dateStr = info.date.toLocaleDateString('ko-KR', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                }).replace(/. /g, '-').replace('.', '');

                // 오늘 날짜에 fc-day-selected 클래스 추가
                if (dateStr === today) {
                    info.el.classList.add('fc-day-selected');
                    selectedDateElement = info.el;  // 오늘 날짜 엘리먼트 기억
                }

                // 오늘 이전 날짜에만 회색 배경 추가
                if (new Date(dateStr) < new Date(today)) {
                    info.el.classList.add('past-date');
                } else {
                    info.el.classList.remove('past-date');
                }

                // 경기 날짜 표시
                if (gameDates.includes(dateStr)) {
                    info.el.classList.add('game-date');
                }
            },
            datesSet: applyGameDates
        });
        calendar.render();
        loadMeetings();  // 페이지가 로드될 때 오늘 날짜의 소모임 목록 로드
    }

    // 달력의 경기 날짜에 스타일 적용
    function applyGameDates() {
        document.querySelectorAll('.fc-daygrid-day').forEach(dayCell => {
            const dateStr = dayCell.getAttribute('data-date');
            if (gameDates.includes(dateStr)) {
                dayCell.classList.add('game-date');
            } else {
                dayCell.classList.remove('game-date');
            }
        });
    }

    // 소모임 목록 불러오기 함수
    function loadMeetings() {
        const encodedTeam = encodeURIComponent(selectedTeam);
        const encodedDate = encodeURIComponent(selectedDate || "");

        fetch(`/meeting/listByDateAndTeam?date=${encodedDate}&team=${encodedTeam}`)
            .then(response => response.json())
            .then(fetchedData => {
                data = fetchedData;
                totalPages = Math.ceil(data.length / itemsPerPage);
                renderMeetings();
                renderPagination();
                updateFilterInfo();

                document.getElementById("total-count").textContent = data.length;
                document.getElementById("current-page").textContent = currentPage;
                document.getElementById("total-pages").textContent = totalPages;
            })
            .catch(error => {
                console.error('소모임 불러오기 중 오류 발생:', error);
            });
    }

    // 필터 정보 업데이트 함수
    function updateFilterInfo() {
        const filterInfo = `필터적용   [날짜] ${selectedDate}    [팀] ${selectedTeam}`;
        document.getElementById("filter-info").textContent = filterInfo;
    }

    // 소모임 목록 렌더링 함수
    function renderMeetings() {
        const meetingList = document.getElementById("meeting-list");
        meetingList.innerHTML = ''; // 기존 리스트 초기화

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, data.length);

        if (data.length === 0) {
            document.getElementById("no-meetings").style.display = 'block';
        } else {
            document.getElementById("no-meetings").style.display = 'none';
            for (let i = startIndex; i < endIndex; i++) {
                const meetingData = data[i];
                const meeting = meetingData.meeting;
                const currentAttendeesCount = meetingData.currentAttendeesCount;

                const item = document.createElement("div");
                item.classList.add("item");

                item.setAttribute("onclick", `window.location.href='/meeting/detail/${meeting.meetingNo}'`);

                fetch(`/meeting/gameInfo?gameNo=${meeting.gameNo}`)
                    .then(response => response.json())
                    .then(gameInfo => {
                        item.innerHTML = `
                            <div class="item-image">
                                <img src="${getTeamLogo(meeting.meetingTeamName)}" alt="${meeting.meetingTeamName}">
                                <div class="meeting-team">
                                    <strong>${meeting.meetingTeamName}</strong>
                                </div>
                            </div>
                            <div class="item-content">
                                <div class="meeting-title">
                                    <strong>${meeting.meetingTitle}</strong>
                                </div>
                                <div class="meeting-game-info">
                                    <img src="/img/info.png" alt="경기 정보 아이콘" class="icon">
                                    <span>${gameInfo.teamA} vs ${gameInfo.teamB} (${gameInfo.gameTime}, @ ${gameInfo.gameField})</span>
                                </div>
                                <div class="meeting-place">
                                    <img src="/img/where.png" alt="모임 장소 아이콘" class="icon">
                                    <span>${meeting.meetingPlace}</span>
                                </div>
                                <div class="meeting-time">
                                    <img src="/img/when.png" alt="모임 시간 아이콘" class="icon">
                                    <span>${meeting.meetingTime}</span>
                                </div>
                                <div class="meeting-max-people">
                                    <img src="/img/who.png" alt="참석자정보 아이콘" class="icon">
                                    <span>${currentAttendeesCount} / ${meeting.meetingMaxPeople}명</span>
                                </div>
                            </div>
                        `;
                        meetingList.appendChild(item);
                    })
                    .catch(error => {
                        console.error('경기 정보 가져오기 중 오류 발생:', error);
                    });
            }
        }
    }

    // 페이지네이션 렌더링
    function renderPagination() {
        const paginationContainer = document.getElementById("pagination");
        paginationContainer.innerHTML = '';

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement("li");
            li.classList.add("pagination-li");
            if (i === currentPage) {
                li.classList.add("pagination-li-active");
            }

            const pageButton = document.createElement("a");
            pageButton.href = "#";
            pageButton.textContent = i;
            pageButton.addEventListener("click", function(e) {
                e.preventDefault();
                currentPage = i;
                renderMeetings();
                renderPagination();
                document.getElementById("current-page").textContent = currentPage;
            });

            li.appendChild(pageButton);
            paginationContainer.appendChild(li);
        }
    }

    // 팀별 로고 이미지 반환
    function getTeamLogo(meetingTeamName) {
        switch (meetingTeamName) {
            case 'KT': return '/img/KT로고.png';
            case 'LG': return '/img/LG로고.png';
            case 'NC': return '/img/NC로고.png';
            case 'SSG': return '/img/SSG로고.png';
            case '기아': return '/img/기아로고.png';
            case '두산': return '/img/두산로고.png';
            case '롯데': return '/img/롯데로고.png';
            case '삼성': return '/img/삼성로고.png';
            case '키움': return '/img/키움로고.png';
            case '한화': return '/img/한화로고.png';
            default: return '/img/default-logo.png';
        }
    }
});