$(document).ready(function() {
    let idValid = false;
    let nicknameValid = false;
    let emailValid = false;
    let passwordValid = false;
    let emailVerified = false;

    function checkIdValidity(memberId) {
        var $idCheckResult = $('#idCheckResult');

        if (memberId.length < 5) {
            $idCheckResult.text('아이디는 5자 이상 사용해야 합니다.').css('color', 'red');
            idValid = false;
        } else if (memberId.length >= 20) {
            $idCheckResult.text('아이디는 20자 미만으로 사용 가능합니다.').css('color', 'red');
            idValid = false;
        } else {
            $.ajax({
                url: '/member/checkDuplicate',
                type: 'GET',
                data: {field: 'id', value: memberId},
                success: function (response) {
                    if (response.available) {
                        $idCheckResult.text('사용 가능한 아이디입니다.').css('color', 'green');
                        idValid = true;
                    } else {
                        $idCheckResult.text('이미 사용 중인 아이디입니다.').css('color', 'red');
                        idValid = false;
                    }
                },
                error: function () {
                    $idCheckResult.text('서버 오류가 발생했습니다.').css('color', 'red');
                    idValid = false;
                }
            });
        }
        checkAllValid();

    }

    function checkDuplicate(field, value) {
        $.ajax({
            url: '/member/checkDuplicate',
            type: 'GET',
            data: {field: field, value: value},
            success: function (response) {
                var resultSpan = $('#' + field + 'CheckResult');
                if (response.available) {
                    resultSpan.text('사용 가능합니다!').css('color', 'green');
                    if (field === 'nickname') nicknameValid = true;
                    if (field === 'email') emailValid = true;
                } else {
                    resultSpan.text('이미 사용 중입니다.').css('color', 'red');
                    if (field === 'nickname') nicknameValid = false;
                    if (field === 'email') emailValid = false;
                }
                checkAllValid();
            },
            error: function () {
                $('#' + field + 'CheckResult').text('확인 중 오류가 발생했습니다.').css('color', 'red');
            }
        });
    }

    function checkPasswordMatch() {
        var password = $('#memberPw').val();
        var confirmPassword = $('#password-confirm').val();
        var $resultSpan = $('#passwordMatchResult');

        if (password === confirmPassword) {
            $resultSpan.text('비밀번호가 일치합니다.').css('color', 'green');
            passwordValid = true;
        } else {
            $resultSpan.text('비밀번호가 일치하지 않습니다.').css('color', 'red');
            passwordValid = false;
        }
        checkAllValid();
    }
    0


    $('#memberId').on('input', function () {
        var memberId = $(this).val();
        checkIdValidity(memberId);
    });

    $('#memberNickName, #memberEmail').on('input', function () {
        var field = this.id.replace('member', '').toLowerCase();
        var value = $(this).val();
        if (value.length > 0) {
            checkDuplicate(field, value);
        } else {
            $('#' + field + 'CheckResult').text('');
            if (field === 'nickname') nicknameValid = false;
            if (field === 'email') emailValid = false;
            checkAllValid();
        }
    });

    $('#memberPw, #password-confirm').on('input', checkPasswordMatch);

    $('#generateNickname').on('click', function () {
        var adjectives = ['행복한', '즐거운', '신나는', '배고픈', '집가고싶은'];
        var middlename = ['KIA', '삼성', 'LG', '두산', 'KT', 'SSG', '롯데', '한화', 'NC', '키움'];
        var nouns = ['팬', '타자', '투수', '감독'];
        var adjective = adjectives[Math.floor(Math.random() * adjectives.length)];
        var middlename = middlename[Math.floor(Math.random() * middlename.length)];
        var noun = nouns[Math.floor(Math.random() * nouns.length)];
        var number = Math.floor(Math.random() * 1000);
        var nickname = adjective + ' ' + middlename + ' ' + noun + ' ' + number;
        $('#memberNickName').val(nickname).trigger('input');
    });

    $('#sendVerificationCode').on('click', function () {
        var email = $('#memberEmail').val();
        if (email && emailValid) {
            $.ajax({
                url: '/mail',
                type: 'POST',
                data: {mail: email},
                success: function (response) {
                    swal({
                        text: "인증번호가 이메일로 전송되었습니다.",
                        icon: "success",
                        timer: 1500,
                    })
                },
                error: function () {
                    swal({
                        text: "인증번호 전송에 실패했습니다. 다시 시도해주세요.",
                        icon: "error",
                        timer: 1500,
                    })
                }
            });
        } else {
            alert('유효한 이메일을 입력해주세요.');
        }
    });

    $('#verifyCode').on('click', function () {
        var email = $('#memberEmail').val();
        var inputCode = $('#email-verification').val();
        $.ajax({
            url: '/member/verifyCode',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({email: email, code: inputCode}),
            success: function (response) {
                if (response.verified) {
                    swal({
                        title: "GoMatch!",
                        text: "인증에 성공했습니다!",
                        icon: "success",
                        timer: 1500,
                    })
                    $('#verificationResult').text('인증이 완료 되었습니다.').css('color', 'green');
                    emailVerified = true;
                } else {
                    $('#verificationResult').text('인증번호가 일치하지 않습니다.').css('color', 'red');
                    emailVerified = false;
                }
                checkAllValid();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error:', textStatus, errorThrown);
                swal({
                    title: "GoMatch!",
                    text: "인증 확인에 실패했습니다. 다시 시도해주세요.",
                    icon: "error",
                    timer: 1500,
                })
            }
        });
    });

    $('#joinForm').on('submit', function (e) {
        e.preventDefault();

        // 필수 입력 필드 확인
        var requiredFields = [
            { id: 'memberId', name: '아이디' },
            { id: 'memberPw', name: '비밀번호' },
            { id: 'password-confirm', name: '비밀번호 확인' },
            { id: 'memberName', name: '이름' },
            { id: 'memberNickName', name: '닉네임' },
            { id: 'birthDate', name: '생년월일' },
            { id: 'memberEmail', name: '이메일' }
        ];

        for (var i = 0; i < requiredFields.length; i++) {
            var field = requiredFields[i];
            if (!$('#' + field.id).val()) {
                swal({
                    title: "GoMatch!",
                    text: field.name + '을(를) 입력해주세요.',
                    icon: "error",
                    timer: 1500,
                })
                return;
            }
        }

        if (!idValid) {
            alert('유효한 아이디를 입력해주세요.');
            swal({
                title: "GoMatch!",
                text: field.name + '을(를) 입력해주세요.',
                icon: "error",
                timer: 1500,
            })
            return;
        }

        if (!nicknameValid) {
            alert('유효한 닉네임을 입력해주세요.');
            swal({
                title: "GoMatch!",
                text: '유효한 닉네임을 입력해주세요.',
                icon: "error",
                timer: 1500,
            })
            return;
        }

        if (!emailValid) {
            alert('유효한 이메일을 입력해주세요.');
            swal({
                title: "GoMatch!",
                text: '유효한 이메일을 입력해주세요.',
                icon: "error",
                timer: 1500,
            })
            return;
        }

        if (!passwordValid) {
            alert('비밀번호가 일치하지 않습니다.');
            swal({
                title: "GoMatch!",
                text: '비밀번호가 일치하지 않습니다.',
                icon: "error",
                timer: 1500,
            })
            return;
        }

        if (!emailVerified) {
            swal({
                title: "GoMatch!",
                text: '이메일 인증을 완료해주세요.',
                icon: "error",
                timer: 1500,
            })
            return;
        }

        // 모든 검증을 통과한 경우
        $.ajax({
            url: $(this).attr('action'),
            type: 'POST',
            data: $(this).serialize(),
            success: function (response) {
                swal({
                    title: "GoMatch!",
                    text: "회원가입 성공!",
                    icon: "success",
                    timer: 1500,
                }).then(() => {
                    window.location.href = '/';
                });
            },
            error: function (xhr, status, error) {
                console.error('Error:', status, error);
                alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
});