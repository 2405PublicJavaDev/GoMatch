// function suspendMember(memberId) {
//     Swal.fire({
//         title: '회원 정지',
//         text: '이 회원을 정지하시겠습니까?',
//         icon: 'warning',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: '예',
//         cancelButtonText: '아니오'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             $.ajax({
//                 url: '/admin/suspend-member',
//                 type: 'POST',
//                 data: JSON.stringify({ memberId: memberId }),
//                 contentType: 'application/json',
//                 success: function(response) {
//                     if (response.success) {
//                         Swal.fire('완료', '회원이 정지되었습니다.', 'success').then(() => {
//                             location.reload();
//                         });
//                     } else {
//                         Swal.fire('실패', '회원 정지에 실패했습니다: ' + response.message, 'error');
//                     }
//                 },
//                 error: function() {
//                     Swal.fire('오류', '서버 오류가 발생했습니다.', 'error');
//                 }
//             });
//         }
//     });
// }
//
// function activateMember(memberId) {
//     Swal.fire({
//         title: '회원 활성화',
//         text: '이 회원을 활성화하시겠습니까?',
//         icon: 'question',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: '예',
//         cancelButtonText: '아니오'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             $.ajax({
//                 url: '/admin/activate-member',
//                 type: 'POST',
//                 data: JSON.stringify({ memberId: memberId }),
//                 contentType: 'application/json',
//                 success: function(response) {
//                     if (response.success) {
//                         Swal.fire('완료', '회원이 활성화되었습니다.', 'success').then(() => {
//                             location.reload();
//                         });
//                     } else {
//                         Swal.fire('실패', '회원 활성화에 실패했습니다: ' + response.message, 'error');
//                     }
//                 },
//                 error: function() {
//                     Swal.fire('오류', '서버 오류가 발생했습니다.', 'error');
//                 }
//             });
//         }
//     });
// }
//
// function deleteMember(memberId) {
//     Swal.fire({
//         title: '회원 삭제',
//         text: '이 회원을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.',
//         icon: 'warning',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: '예',
//         cancelButtonText: '아니오'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             $.ajax({
//                 url: '/admin/delete-member',
//                 type: 'POST',
//                 data: JSON.stringify({memberId: memberId}),
//                 contentType: 'application/json',
//                 success: function (response) {
//                     if (response.success) {
//                         Swal.fire('완료', '회원이 삭제되었습니다.', 'success').then(() => {
//                             location.reload();
//                         });
//                     } else {
//                         Swal.fire('실패', '회원 삭제에 실패했습니다: ' + response.message, 'error');
//                     }
//                 },
//                 error: function () {
//                     Swal.fire('오류', '서버 오류가 발생했습니다.', 'error');
//                 }
//             });
//         }
//     });
// }
//
//     $(document).ready(function () {
//     $('.delete-button').on('click', function () {
//         const meetingNo = $(this).data('meeting-no');
//         Swal.fire({
//             title: '소모임 삭제',
//             text: '이 소모임을 삭제하시겠습니까?',
//             icon: 'warning',
//             showCancelButton: true,
//             confirmButtonColor: '#3085d6',
//             cancelButtonColor: '#d33',
//             confirmButtonText: '예',
//             cancelButtonText: '아니오'
//         }).then((result) => {
//             if (result.isConfirmed) {
//                 $('#meeting-' + meetingNo).hide();
//             }
//         });
//     });
// });
//
//     function logout() {
//     Swal.fire({
//         title: '로그아웃',
//         text: '로그아웃하시겠습니까?',
//         icon: 'question',
//         showCancelButton: true,
//         confirmButtonColor: '#3085d6',
//         cancelButtonColor: '#d33',
//         confirmButtonText: '예',
//         cancelButtonText: '아니오'
//     }).then((result) => {
//         if (result.isConfirmed) {
//             window.location.href = '/admin/logout';
//         }
//     });
//
// }