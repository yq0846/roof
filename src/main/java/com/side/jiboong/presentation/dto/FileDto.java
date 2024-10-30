package com.side.jiboong.presentation.dto;

import com.side.jiboong.common.constant.FilePath;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileDto {

    @Getter
    @ToString
    public static class FileResponse {
        private String originalFileName;
        private String encodedFileName;

        @Builder(access = AccessLevel.PRIVATE)
        private FileResponse(String originalFileName, String encodedFileName) {
            this.originalFileName = originalFileName;
            this.encodedFileName = encodedFileName;
        }

        public static FileResponse of(String originalFileName, String encodedFileName) {
            return FileResponse.builder()
                    .originalFileName(originalFileName)
                    .encodedFileName(encodedFileName)
                    .build();
        }
    }

    @Getter
    @ToString
    public static class FileRequest {
        private MultipartFile file;
        private List<MultipartFile> files;
        private FilePath filePath;

        @Builder(access = AccessLevel.PRIVATE)
        private FileRequest(
                MultipartFile file,
                List<MultipartFile> files,
                FilePath filePath
        ) {
            this.file = file;
            this.files = files;
            this.filePath = filePath;
        }

        public static FileRequest create(MultipartFile file, FilePath filePath) {
            return FileRequest.builder()
                    .file(file)
                    .filePath(filePath)
                    .build();
        }

        public static FileRequest create(List<MultipartFile> files, FilePath filePath) {
            return FileRequest.builder()
                    .files(files)
                    .filePath(filePath)
                    .build();
        }
    }

}
