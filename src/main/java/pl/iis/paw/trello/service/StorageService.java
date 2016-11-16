package pl.iis.paw.trello.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    void store(MultipartFile file, String subDirectory);

    void delete(String fileName, String subDirectory);

    void deleteAll();

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);
}