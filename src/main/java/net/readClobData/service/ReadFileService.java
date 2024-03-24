package net.readClobData.service;

import net.readClobData.model.FileDetails;
import net.readClobData.repository.ReadFileRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Service
public class ReadFileService {

    @Autowired
    private ReadFileRespository repository;

    @Autowired
    @Qualifier("fixedThreadPool")
    private ExecutorService executorService;

    public ReadFileService() {
        executorService = Executors.newFixedThreadPool(5);
    }

    public void saveFileContent(File file) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        BufferedReader br
                = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            fileContent.append(st);
        }

        FileDetails entity = new FileDetails();
        entity.setClobContent(String.valueOf(fileContent));
        repository.save(entity);
    }

    public String getClobAsString(Long entityId) {
        FileDetails entity = repository.findById(entityId).orElse(null);
        if (entity != null) {
            return entity.getClobContent();
        }
        return null;
    }

    public List<String> getClobAsStringWithExecutors() throws InterruptedException, ExecutionException {
        List<Long> ids = List.of(1L, 2L, 3L); // IDs to fetch data for
        executorService = Executors.newFixedThreadPool(5);
        List<String> results = readClobDataFromDB(ids);
        shutdownExecutorService();

        for (String result : results) {
            System.out.println(result);
        }

        return results;
    }

    public List<String> readClobDataFromDB(List<Long> ids) throws InterruptedException, ExecutionException, ExecutionException {
        List<Future<String>> futures = new ArrayList<>();
        System.out.println(executorService.isTerminated());
        for (Long id : ids) {
            Callable<String> task = () -> {
                FileDetails entity = repository.findById(id).orElse(null);
                if (entity != null)
                    return Objects.requireNonNull(entity).getClobContent();
                else
                    return "";
            };
            futures.add(executorService.submit(task));
        }

        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            results.add(future.get());
        }

        return results;
    }


    private String clobToString(java.sql.Clob clob) throws SQLException, SQLException {
        StringBuilder sb = new StringBuilder();
        if (clob != null) {
            try (java.io.Reader reader = clob.getCharacterStream()) {
                char[] buffer = new char[4096];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    sb.append(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }

    public void shutdownExecutorService() {
        executorService.shutdown();
    }
}