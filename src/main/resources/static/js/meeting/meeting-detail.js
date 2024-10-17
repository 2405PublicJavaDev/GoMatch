// 이미지 팝업 열기
function openPopup(src) {
    const popup = document.getElementById('popup');
    const popupImg = document.getElementById('popup-img');
    popup.style.display = 'flex';  // 팝업을 표시
    popupImg.src = src;  // 팝업 이미지 경로 설정
}

// 이미지 팝업 닫기
function closePopup() {
    const popup = document.getElementById('popup');
    popup.style.display = 'none';  // 팝업을 숨김
}

document.addEventListener('DOMContentLoaded', function () {
    // gameNo가 존재하는지 확인한 후 fetch 요청
    if (gameNo) {
        fetch(`/meeting/gameInfo?gameNo=${gameNo}`)
            .then(response => response.json())
            .then(gameInfo => {
                const gameInfoElement = document.getElementById('game-info');
                gameInfoElement.innerHTML = `${gameInfo.teamA} vs ${gameInfo.teamB} (${gameInfo.gameTime}, @ ${gameInfo.gameField})`;
            })
            .catch(error => {
                console.error('경기 정보 가져오기 중 오류 발생:', error);
                document.getElementById('game-info').textContent = '경기 정보를 불러오지 못했습니다.';
            });
    }
});

function attendMeeting() {
    Swal.fire({
        title: '참여하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '참여',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            if (meetingNo) {
                fetch(`/meeting/attend?meetingNo=${meetingNo}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => {
                        if (response.status === 401) {
                            return response.json().then(data => {
                                Swal.fire({
                                    title: data.message,
                                    icon: 'info',
                                    showCancelButton: true,
                                    confirmButtonText: '로그인',
                                    cancelButtonText: '취소'
                                }).then((result) => {
                                    if (result.isConfirmed) {
                                        window.location.href = '/member/loginpage';
                                    }
                                });
                            });
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data && data.message) {
                            Swal.fire({
                                title: data.message,
                                icon: data.message === "참석이 완료되었습니다." ? 'success' : 'info'
                            }).then(() => {
                                if (data.message === "참석이 완료되었습니다.") {
                                    location.reload(); // 참석 완료 후 페이지 새로고침
                                }
                            });
                        }
                    })
                    .catch(error => {
                        console.error('참석 처리 중 오류 발생:', error);
                        Swal.fire('참석 처리 중 오류가 발생했습니다.', '', 'error');
                    });
            } else {
                Swal.fire('소모임 정보를 찾을 수 없습니다.', '', 'error');
            }
        }
    });
}

function modifyMeeting() {
    if (meetingNo) {
        window.location.href = `/meeting/modify/${meetingNo}`;
    } else {
        Swal.fire('소모임 정보를 찾을 수 없습니다.', '', 'error');
    }
}

function cancelMeeting() {
    Swal.fire({
        title: '정말 참석을 취소하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '네',
        cancelButtonText: '아니오'
    }).then((result) => {
        if (result.isConfirmed) {
            if (meetingNo) {
                fetch(`/meeting/cancel?meetingNo=${meetingNo}`, {
                    method: 'POST',
                })
                    .then(response => response.text())
                    .then(message => {
                        Swal.fire({
                            title: message,
                            icon: message === "참석이 취소되었습니다." ? 'success' : 'info'
                        }).then(() => {
                            if (message === "참석이 취소되었습니다.") {
                                location.reload(); // 참석 취소 후 페이지 새로고침
                            }
                        });
                    })
                    .catch(error => {
                        console.error('참석 취소 처리 중 오류 발생:', error);
                        Swal.fire('참석 취소 처리 중 오류가 발생했습니다.', '', 'error');
                    });
            } else {
                Swal.fire('소모임 정보를 찾을 수 없습니다.', '', 'error');
            }
        }
    });
}

function deleteMeeting() {
    Swal.fire({
        title: '정말로 소모임을 삭제하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            if (meetingNo) {
                fetch(`/meeting/delete/${meetingNo}`, {
                    method: 'POST',
                })
                    .then(() => {
                        Swal.fire({
                            title: '삭제가 완료되었습니다.',
                            icon: 'success'
                        }).then(() => {
                            window.location.href = "/meeting/list";
                        });
                    })
                    .catch(error => {
                        console.error('삭제 처리 중 오류 발생:', error);
                        Swal.fire('삭제 처리 중 오류가 발생했습니다.', '', 'error');
                    });
            } else {
                Swal.fire('소모임 정보를 찾을 수 없습니다.', '', 'error');
            }
        }
    });
}