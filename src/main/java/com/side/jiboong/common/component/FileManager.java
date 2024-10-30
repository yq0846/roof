package com.side.jiboong.common.component;

import com.side.jiboong.common.config.properties.PathProperties;
import com.side.jiboong.common.constant.FilePath;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.side.jiboong.presentation.dto.FileDto.FileRequest;
import static com.side.jiboong.presentation.dto.FileDto.FileResponse;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class FileManager {

    private final PathProperties pathProperties;

    public FileResponse saveFile(FileRequest request) {
        Path uploadPath = createPath(request.getFilePath());
        String originalFilename = request.getFile().getOriginalFilename();
        String encodedFileName = generateEncodedFileName(originalFilename);

        byte[] bytes = new byte[0];
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            bytes = request.getFile().getBytes();
            Path filePath = uploadPath.resolve(encodedFileName);
            Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW);

            return FileResponse.of(originalFilename, encodedFileName);
        } catch (IOException e) {
            // TODO: 삭제 로직
            throw new RuntimeException("파일을 생성하지 못했습니다");
        }

    }

    private Path createPath(FilePath directory) {
        return Path.of(Paths.get(pathProperties.getWebResources().file(), directory.name().toLowerCase()).toString());
    }

    private String generateEncodedFileName(String originalFilename) {
        String uuid = randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, Math.min(uuid.length(), 10)) + originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
