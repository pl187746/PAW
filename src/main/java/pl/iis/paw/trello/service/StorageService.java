package pl.iis.paw.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.iis.paw.trello.config.StorageProperties;
import pl.iis.paw.trello.exception.StorageException;
import pl.iis.paw.trello.exception.StorageFileNotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    
    public void store(MultipartFile file) {

    }

    @PostConstruct
    public void createDirectory() throws IOException {
        try {
            Files.createDirectory(rootLocation);
        } catch (FileAlreadyExistsException e) { }
    }

    
    public void store(MultipartFile file, String subDirectory) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            try {
                Files.createDirectory(Paths.get(rootLocation + "/" + subDirectory));
            } catch (FileAlreadyExistsException e) { }

            Files.copy(file.getInputStream(), this.rootLocation.resolve(subDirectory + "/" + file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }
    
    public Path load(String filename, String subDir) {
        Path subDirectory = Paths.get(rootLocation + "/" + subDir);
        return subDirectory.resolve(filename);
    }
    
    public Resource loadAsResource(String filename, String subDir) {
        try {
            Path file = load(filename, subDir);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void delete(String fileName, String subDirectory) {
        try {
            Files.delete(Paths.get(rootLocation + "/" + subDirectory + "/" + fileName));
        } catch (IOException e) {
            throw new StorageException("Could not remove file" + fileName + " from directory" + subDirectory);
        }
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}