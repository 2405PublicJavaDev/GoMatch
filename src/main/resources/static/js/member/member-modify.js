$(document).ready(function() {
    let originalValues = {};
    let changedFields = new Set();
    let passwordChangeRequested = false;

    // 원래 값 저장
    $('form input, form select').each(function() {
        originalValues[this.id] = $(this).val();
    });

    // 값 변경 감지
    $('form input, form select').on('input change', function() {
        if ($(this).val() !== originalValues[this.id]) {
            changedFields.add(this.id);
        } else {
            changedFields.delete(this.id);
        }
        checkFormValidity();
    });

    // 비밀번호 필드 변경 감지
    $('#currentPassword, #newPassword, #confirmNewPassword').on('input', function() {
        passwordChangeRequested = $('#currentPassword').val() || $('#newPassword').val() || $('#confirmNewPassword').val();
        checkFormValidity();
    });

    function checkDuplicate(field, value) {
        if (value === originalValues[field]) {
            return; // 값이 변경되지 않았으면 중복 체크 불필요
        }
        $.ajax({
            url: '/member/checkDuplicate',
            type: 'GET',
            data: { field: field, value: value },
            success: function(response) {
                var resultSpan = $('#' + field + 'CheckResult');
                if(response.available) {
                    resultSpan.text('사용 가능합니다.').css('color', 'green');
                } else {
                    resultSpan.text('이미 사용 중입니다.').css('color', 'red');
                }
                checkFormValidity();
            },
            error: function() {
                $('#' + field + 'CheckResult').text('확인 중 오류가 발생했습니다.').css('color', 'red');
            }
        });
    }

    function checkPasswordMatch() {
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmNewPassword').val();
        var $resultSpan = $('#passwordMatchResult');

        if(newPassword === confirmPassword) {
            $resultSpan.text('비밀번호가 일치합니다.').css('color', 'green');
        } else {
            $resultSpan.text('비밀번호가 일치하지 않습니다.').css('color', 'red');
        }
        checkFormValidity();
    }

    function checkCurrentPassword() {
        var currentPassword = $('#currentPassword').val();
        $.ajax({
            url: '/member/checkCurrentPassword',
            type: 'POST',
            data: { currentPassword: currentPassword },
            success: function(response) {
                var $resultSpan = $('#currentPasswordResult');
                if(response.valid) {
                    $resultSpan.text('비밀번호가 일치합니다.').css('color', 'green');
                } else {
                    $resultSpan.text('비밀번호가 일치하지 않습니다.').css('color', 'red');
                }
                checkFormValidity();
            },
            error: function(xhr) {
                $('#currentPasswordResult').text('확인 중 오류가 발생했습니다.').css('color', 'red');
                console.error('Error checking current password:', xhr.responseText);
            }
        });
    }

    function checkFormValidity() {
        let isValid = changedFields.size > 0 || passwordChangeRequested;

        if (passwordChangeRequested) {
            isValid = isValid && $('#currentPassword').val() && $('#newPassword').val() === $('#confirmNewPassword').val();
        }

        $('#updateButton').prop('disabled', !isValid);
    }

    $('#memberId, #memberNickName, #memberEmail').on('blur', function() {
        var field = this.id.replace('member', '').toLowerCase();
        var value = $(this).val();
        if(value.length > 0) {
            checkDuplicate(field, value);
        }
    });

    $('#newPassword, #confirmNewPassword').on('input', checkPasswordMatch);
    $('#currentPassword').on('blur', function() {
        var currentPassword = $(this).val();
        if (currentPassword) {
            $.ajax({
                url: '/member/checkCurrentPassword',
                type: 'POST',
                data: { currentPassword: currentPassword },
                success: function(response) {
                    if (response.valid) {
                        $('#currentPasswordResult').text('비밀번호가 일치합니다.').css('color', 'green');
                    } else {
                        $('#currentPasswordResult').text('현재 비밀번호가 일치하지 않습니다.').css('color', 'red');
                    }
                },
                error: function() {
                    $('#currentPasswordResult').text('비밀번호 확인 중 오류가 발생했습니다.').css('color', 'red');
                }
            });
        }
    });

    $('#uploadButton').click(function() {
        $('#profileImageInput').click();
    });

    // 이미지 프리뷰 기능
    $('#profileImageInput').on('change', function(event) {
        var file = event.target.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#profileImagePreview').attr('src', e.target.result);
            }
            reader.readAsDataURL(file);
        }
    });

    $('#updateForm').on('submit', function(e) {
        e.preventDefault();
        var formData = new FormData(this);

        // 현재 비밀번호와 새 비밀번호 처리
        var currentPassword = $('#currentPassword').val();
        var newPassword = $('#newPassword').val();

        if (currentPassword || newPassword) {
            formData.set('currentPassword', currentPassword);
            formData.set('newPassword', newPassword);
        } else {
            formData.delete('currentPassword');
            formData.delete('newPassword');
        }

        // 프로필 이미지 처리
        var profileImage = $('#profileImageInput')[0].files[0];
        if (profileImage) {
            formData.set('profileImage', profileImage);
        } else {
            formData.delete('profileImage');
        }

        $.ajax({
            url: $(this).attr('action'),
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                swal({
                    title: "성공!",
                    text: "회원정보가 성공적으로 수정되었습니다.",
                    icon: "success",
                }).then(() => {
                    window.location.href = '/member/mypage';
                });
            },
            error: function(xhr, status, error) {
                console.error('Error response:', xhr.responseText);
                swal("오류", "회원정보 수정 중 오류가 발생했습니다: " + xhr.responseText, "error");
            }
        });
    });
});