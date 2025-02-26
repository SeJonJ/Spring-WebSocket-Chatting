package webChat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webChat.dto.FileDto;
import webChat.service.file.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    // 프론트에서 ajax 를 통해 /upload 로 MultipartFile 형태로 파일과 roomId 를 전달받는다.
    // 전달받은 file 를 uploadFile 메서드를 통해 업로드한다.
    @PostMapping("/upload")
    public FileDto uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("roomId") String roomId){

        FileDto uploadFile = fileService.uploadFile(file, roomId);
        log.info("최종 upload Data {}", uploadFile);

        // fileReq 객체 리턴
        return uploadFile;
    }

    // get 으로 요청이 오면 아래 download 메서드를 실행한다.
    // fileName 과 파라미터로 넘어온 fileDir 을 getObject 메서드에 매개변수로 넣는다.
    @PostMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(
            @RequestParam("fileName")String fileName,
            @RequestParam("filePath")String filePath){
        log.info("fileDir : fileName [{} : {}]", filePath, fileName);
        try {
            // 변환된 byte, httpHeader 와 HttpStatus 가 포함된 ResponseEntity 객체를 return 한다.
            return fileService.getObject(fileName, filePath);
        } catch (Exception e) {
            throw new ExceptionController.InternalServerError(e.getMessage());
        }
    }

}
