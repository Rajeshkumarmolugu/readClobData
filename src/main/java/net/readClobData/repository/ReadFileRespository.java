package net.readClobData.repository;

import net.readClobData.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadFileRespository extends JpaRepository<FileDetails, Long> {

}
