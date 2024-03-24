package net.readClobData.dto;

import java.sql.Clob;

public class FileDetailsDTO {

    Long id;
    Clob fileDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clob getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(Clob fileDetails) {
        this.fileDetails = fileDetails;
    }
}
