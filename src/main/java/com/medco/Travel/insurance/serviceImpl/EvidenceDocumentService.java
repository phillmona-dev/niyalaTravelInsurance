package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.EvidenceDocument;
import com.medco.Travel.insurance.entity.User;
import com.medco.Travel.insurance.repository.EvidenceDocumentRepository;
import com.medco.Travel.insurance.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class EvidenceDocumentService {

    private final String fileStorageDirectory = "C:/Users/hp/Documents/erxFile";

    private static final String FILE_DIRECTORY = "C:/Users/hp/Documents/erxFile";

    private final EvidenceDocumentRepository evidenceDocumentRepository;
    private final UserRepository userRepository;

    @Value("${file.dir-attachments}")
    private String fileStoragePath;

    public ResponseEntity<Resource> getDocumentByFileName(String fileName) {
        try {

            Path fileStorageLocation = Paths.get(FILE_DIRECTORY).toAbsolutePath().normalize();

            Path filePath = fileStorageLocation.resolve(fileName).normalize();

            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IOException("File not readable: " + fileName);
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public EvidenceDocumentService(EvidenceDocumentRepository evidenceDocumentRepository, UserRepository userRepository) {
        this.evidenceDocumentRepository = evidenceDocumentRepository;
        this.userRepository = userRepository;
    }

    public EvidenceDocument saveEvidenceDocument(EvidenceDocument evidenceDocument, MultipartFile file) throws IOException {

        Path storageDir = Paths.get(fileStoragePath);
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        String filePath = storageDir.resolve(file.getOriginalFilename()).toString();
        file.transferTo(new File(filePath));

        evidenceDocument.setFile(filePath);
        evidenceDocument.setFileName(file.getOriginalFilename());
        evidenceDocument.setFileSize(file.getSize());
        return evidenceDocumentRepository.save(evidenceDocument);
    }


    public List<EvidenceDocument> getAllEvidenceDocuments() {

        return evidenceDocumentRepository.findAll();
    }

    public Optional<EvidenceDocument> getEvidenceDocumentById(Long id) {
        return evidenceDocumentRepository.findById(id);
    }

    public byte[] getFileContent(Long id) throws IOException {
        Optional<EvidenceDocument> documentOptional = evidenceDocumentRepository.findById(id);
        if (documentOptional.isEmpty()) {
            throw new IllegalArgumentException("Document with ID " + id + " not found.");
        }

        EvidenceDocument document = documentOptional.get();
        Path filePath = Paths.get(fileStorageDirectory, document.getFile());
        if (!Files.exists(filePath)) {
            throw new IOException("File not found on the server: " + filePath.toString());
        }
        return Files.readAllBytes(filePath);
    }

    public Optional<EvidenceDocument> downloadEvidenceDocumentById(Long id) {
        return evidenceDocumentRepository.findById(id);
    }

    public void updateFile(Long id, MultipartFile newFile) throws IOException, FileNotFoundException {
        EvidenceDocument document = evidenceDocumentRepository.findById(id)
                .orElseThrow(()-> new FileNotFoundException("File not found with id:" + id));

        String fileStoragePath = "c:/Users/hp/Documents/erxFile/";
        Path oldFilePath = Paths.get(fileStoragePath, document.getFile());

        if (Files.exists(oldFilePath)){
            Files.delete(oldFilePath);
        }

        String newFileName = System.currentTimeMillis() + "_" + newFile.getOriginalFilename();
        Path newFilePath = Paths.get(fileStoragePath, newFileName);
        Files.copy(newFile.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        document.setFile(newFileName);
        document.setFileName(newFile.getOriginalFilename());
        document.setFileSize(newFile.getSize());
        evidenceDocumentRepository.save(document);
    }

    public void deleteEvidenceDocument(Long id) {
        Optional<EvidenceDocument> documentOptional = evidenceDocumentRepository.findById(id);
        if (documentOptional.isPresent()) {
            Path filePath = Paths.get(documentOptional.get().getFile());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting file from disk: " + filePath, e);
            }
        }
        evidenceDocumentRepository.deleteById(id);
    }

    public List<EvidenceDocument> getDocumentsByUserUuid(String userUuid) {
        User user = userRepository.findByUserUuid(userUuid);
        if (user == null){

            throw new RuntimeException("User not found with UUID: " + userUuid);

        }

        return evidenceDocumentRepository.findByUser(user);

    }


    public EvidenceDocument findById(Long id) {
        return evidenceDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvidenceDocument with id " + id + " not found"));
    }
}
