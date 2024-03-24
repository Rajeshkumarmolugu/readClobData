package net.readClobData.controller;

import net.readClobData.model.FileDetails;
import net.readClobData.service.ReadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("v1/file")
public class FileController {
    @Autowired
    private ReadFileService readFileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile() {

        File file = new File("C:\\Users\\6141049\\Work\\Education\\Kafka_start_cmds.txt");

        try {
            // Get the filename and save it in the upload directory
            readFileService.saveFileContent(file);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getClobData")
    public ResponseEntity<String> getClobAsStringByID(@RequestParam("id") Long id ) {

        String data = readFileService.getClobAsString(id);
        return ResponseEntity.ok("Data is : " + data);
    }

    @GetMapping("/getClobDataByExec")
    public ResponseEntity<String> getClobAsStringByIDWithExecutor() throws ExecutionException, InterruptedException {


        String data = String.valueOf(readFileService.getClobAsStringWithExecutors());
        return ResponseEntity.ok("Clob Data with executor service is : " + data);
    }


}
