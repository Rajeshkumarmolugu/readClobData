package net.readClobData.model;

import jakarta.persistence.*;

@Entity
@Table(name = "File_Details")
public class FileDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "clob_column")
    private String clobContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClobContent() {
        return clobContent;
    }

    public void setClobContent(String clobContent) {
        this.clobContent = clobContent;
    }
}