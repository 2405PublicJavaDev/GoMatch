// 오늘 날짜 이후로만 선택 가능하도록 설정
const today = new Date();
const sixMonthsLater = new Date();
sixMonthsLater.setMonth(today.getMonth() + 6);

const todayStr = today.toISOString().split('T')[0];
const sixMonthsLaterStr = sixMonthsLater.toISOString().split('T')[0];

document.getElementById('gameDate').setAttribute('min', todayStr);
document.getElementById('gameDate').setAttribute('max', sixMonthsLaterStr);

// 경기 날짜 선택 시 meetingDate에 자동으로 같은 날짜를 설정
document.getElementById('gameDate').addEventListener('change', function() {
    const selectedDate = this.value;
    document.getElementById('meetingDate').value = selectedDate;
});

// 경기 날짜 선택 시 AJAX로 해당 날짜의 경기 정보 조회
$('#gameDate').on('change', function() {
    const selectedDate = $(this).val();
    if (selectedDate) {
        $.ajax({
            url: '/meeting/games', // 서버의 매핑된 URL
            method: 'GET',
            data: { date: selectedDate },
            success: function(response) {
                $('#gameNo').empty(); // 기존 옵션 제거
                if (response.length > 0) {
                    response.forEach(function(game) {
                        const optionText = `${game.gameDate} (${game.gameTime}) ${game.teamA} vs ${game.teamB} (@${game.gameField})`;
                        const option = `<option value="${game.gameNo}">${optionText}</option>`;
                        $('#gameNo').append(option);
                    });
                } else {
                    $('#gameNo').append('<option value="">경기가 없습니다</option>');
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 에러:', error);
                Swal.fire('에러 발생', '경기 정보를 불러오는 중 오류가 발생했습니다.', 'error');
            }
        });
    }
});

// 주소 검색 API 호출
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고 항목 변수

            // 도로명 주소 선택 시
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                addr += (extraAddr !== '' ? ' (' + extraAddr + ')' : ''); // 상세 주소 추가
            } else {
                // 지번 주소 선택 시
                addr = data.jibunAddress;
            }

            // 주소 필드에 넣기
            document.querySelector("#meetingPlace").value = addr;
            document.querySelector("#meetingPlaceDetail").focus(); // 상세 주소 입력 필드로 포커스 이동
        }
    }).open();
}

// Form이 제출되기 전에 상세주소를 meetingPlace에 합쳐서 저장하는 함수
function combineAddress() {
    var basicAddress = document.querySelector("#meetingPlace").value;
    var detailAddress = document.querySelector("#meetingPlaceDetail").value;

    // 기본 주소와 상세 주소를 합침
    document.querySelector("#meetingPlace").value = basicAddress + ' ' + detailAddress;
}

// 시, 분 선택 후 hidden input에 값 저장
document.getElementById('hourSelect').addEventListener('change', updateMeetingTime);
document.getElementById('minuteSelect').addEventListener('change', updateMeetingTime);
function updateMeetingTime() {
    const hour = document.getElementById('hourSelect').value;
    const minute = document.getElementById('minuteSelect').value;
    if (hour && minute) {
        const meetingTime = hour + ':' + minute;
        document.getElementById('meetingTime').value = meetingTime;
        console.log('선택한 시간:', meetingTime);  // 선택한 시간 로그 출력
    }
}

// 경기 날짜 유효성 검사 함수
function validateForm() {
    const gameDate = document.getElementById('gameDate').value;
    if (gameDate < todayStr) {
        Swal.fire('유효하지 않은 날짜', '지난 날짜는 선택할 수 없습니다.', 'error');
        return false; // 제출 중지
    }
    return true;
}

let selectedFiles = []; // 선택된 파일들을 저장할 배열
// 파일 선택 시 최대 5장만 허용하고 썸네일을 가로로 나란히 보여주는 함수
function previewImages() {
    const previewContainer = document.getElementById('thumbnailContainer');
    const fileLimitMessage = document.getElementById('fileLimitMessage');
    previewContainer.innerHTML = ''; // 기존 썸네일 제거
    fileLimitMessage.style.display = 'none'; // 경고 메시지 숨김
    const files = document.getElementById('groupImage').files;
    // 선택된 파일 배열 초기화
    selectedFiles = Array.from(files);
    // 파일이 5개를 초과할 경우 경고 메시지 표시 및 리턴
    if (selectedFiles.length > 5) {
        Swal.fire('파일 초과', '최대 5개 파일만 업로드할 수 있습니다.', 'warning');
        return;
    }
    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const thumbnailWrapper = document.createElement('div');
            thumbnailWrapper.classList.add('file-thumbnail-wrapper');
            const thumbnail = document.createElement('img');
            thumbnail.classList.add('thumbnail');
            thumbnail.src = e.target.result;
            // X 버튼 생성
            const removeButton = document.createElement('button');
            removeButton.textContent = 'X';
            removeButton.classList.add('x-btn');
            removeButton.onclick = function() {
                removeImage(index); // X 버튼 클릭 시 파일 제거 함수 호출
            };
            thumbnailWrapper.appendChild(thumbnail);
            thumbnailWrapper.appendChild(removeButton);
            previewContainer.appendChild(thumbnailWrapper);
        };
        reader.readAsDataURL(file);
    });
}

// 파일 목록에서 선택된 파일을 제거하는 함수
function removeImage(index) {
    selectedFiles.splice(index, 1); // 배열에서 해당 파일 제거
    // 선택된 파일 목록을 다시 FileList 형식으로 갱신
    const dataTransfer = new DataTransfer();
    selectedFiles.forEach(file => dataTransfer.items.add(file));
    document.getElementById('groupImage').files = dataTransfer.files; // 파일 input에 갱신된 FileList 설정
    previewImages(); // 갱신된 파일 목록으로 썸네일 다시 렌더링
}

// Form 제출 시 사용자 확인을 요청하는 함수
function confirmSubmission() {
    return Swal.fire({
        title: '모임 정보를 등록하시겠습니까?',
        text: "경기 날짜, 경기 정보, 응원 팀명은 등록 후 수정이 불가능합니다.",
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '등록',
        cancelButtonText: '취소'
    }).then((result) => {
        return result.isConfirmed; // 확인 버튼이 눌렸을 때만 true 반환
    });
}

// Form 제출을 제어하는 함수
async function handleSubmitForm(event) {
    event.preventDefault(); // 기본 form 제출 동작 중지

    // 상세 주소와 시간 검증
    combineAddress();

    // validateForm 함수가 false를 반환하면 제출 중단
    if (!validateForm()) {
        return;
    }

    // confirmSubmission에서 true가 반환되면 form을 제출
    const isConfirmed = await confirmSubmission();
    if (isConfirmed) {
        event.target.submit(); // 사용자가 확인 버튼을 눌렀을 때만 form을 제출합니다.
    }
}

// form 요소에 submit 이벤트 연결
document.querySelector("form").addEventListener("submit", handleSubmitForm);
