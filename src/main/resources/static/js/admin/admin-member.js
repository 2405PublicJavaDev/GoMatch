function search() {
    var searchType = document.getElementById("searchType").value;
    var searchInput = document.querySelector(".searchInput").value;
    console.log("Searching for: " + searchInput + " in " + searchType);
}

function suspendMember(memberId) {
    if (confirm('정말로 이 회원을 정지하시겠습니까?')) {
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
                    alert('회원이 정지되었습니다.');
                    location.reload();
                } else {
                    alert('회원 정지에 실패했습니다: ' + data.message);
                }
            });
    }
}

function activateMember(memberId) {
    if (confirm('이 회원을 다시 활성화하시겠습니까?')) {
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
                    alert('회원이 활성화되었습니다.');
                    location.reload();
                } else {
                    alert('회원 활성화에 실패했습니다: ' + data.message);
                }
            });
    }
}

function deleteMember(memberId) {
    if (confirm('정말로 이 회원을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
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
                    alert('회원이 삭제되었습니다.');
                    location.reload();
                } else {
                    alert('회원 삭제에 실패했습니다: ' + data.message);
                }
            });
    }
}