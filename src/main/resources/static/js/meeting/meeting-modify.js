// 전역 변수 선언
let maxFileCount = 5;
let newFileData = [];
let existingFileCount = 0;

function updateFileCount() {
    const totalFiles = existingFileCount + newFileData.length;
    document.getElementById('fileCount').innerText = `파일 ${totalFiles >= 0 ? totalFiles : 0}개`;
}

function removeExistingFile(fileId) {
    console.log(`삭제할 파일 ID: ${fileId} (타입: ${typeof fileId})`);  // 파일 ID와 타입 출력

    // fileId를 숫자로 변환하여 ID 찾기
    const targetId = `file-${parseInt(fileId)}`;
    console.log(`찾으려는 요소 ID: ${targetId}`);
    const fileElement = document.getElementById(targetId);
    console.log(`fileElement: `, fileElement);

    if (fileElement) {
        fileElement.remove(); // 썸네일 DOM에서 파일 제거
        console.log(`fileElement가 제거되었습니다.`);

        const deletedFileIdsInput = document.getElementById('deletedFileIds');
        let deletedFileIds = deletedFileIdsInput.value ? deletedFileIdsInput.value.split(',') : [];
        console.log(`현재 삭제할 파일 ID 목록(추가 전): ${deletedFileIds.join(',')}`);

        deletedFileIds.push(fileId);
        deletedFileIdsInput.value = deletedFileIds.join(',');

        console.log(`현재 삭제할 파일 ID 목록(추가 후): ${deletedFileIds.join(',')}`);  // 삭제 목록 출력

        // 기존 파일 개수 감소
        if (existingFileCount > 0) {
            existingFileCount--;
        }
        updateFileCount();
    } else {
        console.error(`fileElement가 null입니다. 파일 ID: ${fileId}`);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    existingFileCount = document.querySelectorAll("#existingFiles .file-thumbnail-wrapper").length;

    function triggerFileInput() {
        document.getElementById('newFiles').click();
    }

    function previewNewFiles() {
        const fileInput = document.getElementById('newFiles');
        const fileLimitMessage = document.getElementById('fileLimitMessage');
        const newFileThumbnails = document.getElementById('newFileThumbnails');
        const files = Array.from(fileInput.files);

        // 파일 개수 초과 체크
        if (existingFileCount + newFileData.length + files.length > maxFileCount) {
            fileLimitMessage.style.display = 'block';
            fileInput.value = ''; // 초과 시 선택한 파일 초기화
            return; // 초과하면 즉시 반환
        } else {
            fileLimitMessage.style.display = 'none';
        }

        files.forEach((file) => {
            newFileData.push(file);

            const reader = new FileReader();
            reader.onload = function(e) {
                const thumbnailWrapper = document.createElement('div');
                thumbnailWrapper.classList.add('file-thumbnail-wrapper');

                const thumbnail = document.createElement('img');
                thumbnail.classList.add('thumbnail');
                thumbnail.src = e.target.result;

                const removeButton = document.createElement('button');
                removeButton.textContent = 'X';
                removeButton.classList.add('x-btn');
                removeButton.addEventListener('click', function() {
                    newFileData.splice(newFileData.indexOf(file), 1);
                    thumbnailWrapper.remove();
                    updateFileCount();
                });

                thumbnailWrapper.appendChild(thumbnail);
                thumbnailWrapper.appendChild(removeButton);
                newFileThumbnails.appendChild(thumbnailWrapper);
            };
            reader.readAsDataURL(file);
        });

        updateFileCount();
    }

    document.querySelector('.add-image-btn').addEventListener('click', triggerFileInput);
    document.getElementById('newFiles').addEventListener('change', previewNewFiles);

    // 폼 제출 시 파일 개수 확인
    document.querySelector('form').addEventListener('submit', function(event) {
        const totalFiles = existingFileCount + newFileData.length;
        if (totalFiles > maxFileCount) {
            alert(`최대 ${maxFileCount}개의 파일만 첨부할 수 있습니다.`);
            event.preventDefault(); // 폼 제출 막음
        }
    });
});