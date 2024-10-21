document.addEventListener("DOMContentLoaded", function () {
    const filterTabs = document.querySelectorAll('.filter-tab');
    const savedFilterType = sessionStorage.getItem('selectedFilterType') || 'all';  // 기본값 'all'

    // sessionStorage에 저장된 필터 타입에 따라 active 클래스 초기 설정
    setActiveFilter(savedFilterType);

    filterTabs.forEach(tab => {
        tab.addEventListener('click', function () {
            const filterType = this.getAttribute('data-filter-type');
            // 클릭한 탭에만 active 클래스 추가
            setActiveFilter(filterType);
            // 선택된 필터 타입을 sessionStorage에 저장
            sessionStorage.setItem('selectedFilterType', filterType);
            // 필터 타입 설정 후 폼 제출
            applyFilter(filterType);
        });
    });

    function setActiveFilter(filterType) {
        filterTabs.forEach(tab =>
            tab.classList.toggle('active', tab.getAttribute('data-filter-type') === filterType)
        );
    }

    function applyFilter(filterType) {
        const form = document.querySelector('.search-section form');
        let filterInput = form.querySelector('input[name="filterType"]') ||
            form.appendChild(document.createElement('input'));

        filterInput.type = 'hidden';
        filterInput.name = 'filterType';
        filterInput.value = filterType;
        form.submit();
    }
});

function writePost() {
    // 로그인 상태를 HTML의 data- 속성에서 읽어오기
    const loggedIn = document.body.dataset.loggedIn === 'true';

    if (!loggedIn) {
        Swal.fire({
            title: '로그인한 사용자만 이용이 가능한 서비스입니다.',
            text: '로그인을 하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No'
        }).then((result) => {
            if (result.isConfirmed) {
                // Yes 선택 시 로그인 페이지로 이동
                window.location.href = '/member/loginpage';
            }
        });
    } else {
        // 로그인한 상태에서는 글쓰기 페이지로 이동
        window.location.href = '/board/register';
    }
}