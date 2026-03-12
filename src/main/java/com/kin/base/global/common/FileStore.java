package com.kin.base.global.common;

import com.kin.base.domain.board.entity.BoardFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}") // application.yml
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    //다중 파일 처리
    public List<BoardFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<BoardFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;

    }

    //단일 파일 처리
    public BoardFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);

        //실제 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return BoardFile.builder()
                .originName(originalFileName)
                .saveName(storeFileName)
                .path(fileDir)
                .size(multipartFile.getSize())
                .build();

    }

    // 서버에 저장할 고유한 파일명 생성 (UUID 활용)
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자 추출 (예: png, jpg)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public void deleteRealFile(String saveName) {
        String fullPath = getFullPath(saveName);
        File file = new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
    }

}
