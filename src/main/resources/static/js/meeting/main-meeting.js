document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');
    const selectedDateTitle = document.querySelector('.selected-date-title');
    let gameDates = [];

    // 오늘 날짜 가져오기
    const today = new Intl.DateTimeFormat('ko-KR', {
        timeZone: 'Asia/Seoul',
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).format(new Date()).replace(/. /g, '-').replace('.', '');

    // 서버에서 경기 날짜 가져오기
    fetch('/meeting/gameDates')
        .then(response => response.json())
        .then(dates => {
            gameDates = dates.map(date => new Date(date).toISOString().split('T')[0]);
            renderCalendar();
            // 초기화 시 오늘 날짜로 소모임 목록 로드
            loadMeetings(today);
            selectedDateTitle.textContent = `선택한 날짜 : ${today}`;
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
            dayCellContent: function (e) {
                e.dayNumberText = e.dayNumberText.replace('일', '');
            },
            dateClick: function (info) {
                // 모든 셀에서 fc-day-selected 제거
                document.querySelectorAll('.fc-day-selected').forEach(cell => cell.classList.remove('fc-day-selected'));

                // 선택된 날짜만 fc-day-selected 추가
                info.dayEl.classList.add('fc-day-selected');

                // 선택한 날짜를 표시
                selectedDateTitle.textContent = `선택한 날짜 : ${info.dateStr}`;

                // 소모임 목록 불러오기
                loadMeetings(info.dateStr);
            },
            dayCellDidMount: function (info) {
                const dateStr = info.date.toLocaleDateString('ko-KR', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                }).replace(/\. /g, '-').replace('.', '');

                // 오늘 날짜에만 fc-day-selected 추가
                if (dateStr === today) {
                    info.el.classList.add('fc-day-selected');
                } else {
                    info.el.classList.remove('fc-day-selected');
                }

                // 지난 날짜에 회색 배경 추가
                if (dateStr < today) {
                    info.el.classList.add('past-date');
                } else {
                    info.el.classList.remove('past-date');
                }

                // 경기 날짜에 아이콘 추가
                if (gameDates.includes(dateStr)) {
                    info.el.classList.add('game-date');
                }
            },
            datesSet: function () {
                const calendarDays = document.querySelectorAll('.fc-daygrid-day');
                calendarDays.forEach(dayCell => {
                    const dateStr = dayCell.getAttribute('data-date');

                    // 오늘 날짜 강조 표시
                    if (dateStr === today) {
                        dayCell.classList.add('fc-day-selected');
                    }

                    // 지난 날짜 스타일 적용
                    if (dateStr < today) {
                        dayCell.classList.add('past-date');
                    } else {
                        dayCell.classList.remove('past-date');
                    }

                    // 경기 날짜 스타일 적용
                    if (gameDates.includes(dateStr)) {
                        dayCell.classList.add('game-date');
                    } else {
                        dayCell.classList.remove('game-date');
                    }
                });
            }
        });
        calendar.render();
    }


});

function loadMeetings(selectedDate) {
    console.log(`Loading meetings for date: ${selectedDate}`);
    fetch(`/meeting/listByDateAndTeam?date=${selectedDate}`)
        .then(response => response.json())
        .then(meetings => {
            const meetingList = document.getElementById("meeting-list");
            meetingList.innerHTML = ''; // 기존 내용 초기화

            if (meetings.length === 0) {
                meetingList.innerHTML = '<p>선택한 날짜에 개설된 소모임이 없습니다.</p>';
                return;
            }

            meetings.slice(0, 3).forEach(meetingData => {
                const meeting = meetingData.meeting;
                const attendeeCount = meetingData.currentAttendeesCount;

                // 개별 meeting의 gameInfo 불러오기
                fetch(`/meeting/gameInfo?gameNo=${meeting.gameNo}`)
                    .then(gameResponse => gameResponse.json())
                    .then(gameInfo => {
                        const meetingItem = document.createElement("div");
                        meetingItem.classList.add("item");
                        meetingItem.innerHTML = `
                        <div class="item-image">
                            <img src="/img/${meeting.meetingTeamName}로고.png" alt="${meeting.meetingTeamName}">
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
                            <div class="meeting-max-people">
                                <img src="/img/who.png" alt="참석자 정보 아이콘" class="icon">
                                <span>${attendeeCount} / ${meeting.meetingMaxPeople}명</span>
                            </div>
                        </div>
                    `;
                        meetingItem.onclick = () => window.location.href = `/meeting/detail/${meeting.meetingNo}`;
                        meetingList.appendChild(meetingItem);
                    });
            });

            // 더 많은 소모임이 있을 경우 추가 메시지 표시
            if (meetings.length > 3) {
                const moreIndicator = document.createElement("p");
                moreIndicator.classList.add("more-indicator");
                moreIndicator.textContent = "... 더 많은 소모임이 있습니다";
                meetingList.appendChild(moreIndicator);
            }
        })
        .catch(error => {
            console.error('소모임 목록 불러오기 중 오류 발생:', error);
            meetingList.innerHTML = '<p>소모임 목록을 불러오는 중 오류가 발생했습니다.</p>';
        });
}
