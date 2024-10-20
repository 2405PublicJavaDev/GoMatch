function search() {
    var searchType = document.getElementById("searchType").value;
    var searchInput = document.querySelector(".searchInput").value;
    console.log("Searching for: " + searchInput + " in " + searchType);
}

function suspendMember(memberId) {
    Swal.fire({
        title: '회원 정지',
        text: '정말로 이 회원을 정지하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '예',
        cancelButtonText: '아니오'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/admin/suspend-member', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ memberId: memberId })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire('완료', '회원이 정지되었습니다.', 'success').then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire('실패', '회원 정지에 실패했습니다: ' + data.message, 'error');
                    }
                });
        }
    });
}

function activateMember(memberId) {
    Swal.fire({
        title: '회원 활성화',
        text: '이 회원을 다시 활성화하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '예',
        cancelButtonText: '아니오'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/admin/activate-member', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ memberId: memberId })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire('완료', '회원이 활성화되었습니다.', 'success').then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire('실패', '회원 활성화에 실패했습니다: ' + data.message, 'error');
                    }
                });
        }
    });
}

function deleteMember(memberId) {
    Swal.fire({
        title: '회원 삭제',
        text: '정말로 이 회원을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '예',
        cancelButtonText: '아니오'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/admin/delete-member', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ memberId: memberId })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Swal.fire('완료', '회원이 삭제되었습니다.', 'success').then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire('실패', '회원 삭제에 실패했습니다: ' + data.message, 'error');
                    }
                });
        }
    });
}