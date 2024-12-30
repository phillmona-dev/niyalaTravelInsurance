package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.entity.EvidenceDocument;
import com.medco.Travel.insurance.serviceImpl.EvidenceDocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/file")
public class EvidenceDocumentController {

    private final EvidenceDocumentService evidenceDocumentService;

    public EvidenceDocumentController(EvidenceDocumentService evidenceDocumentService) {
        this.evidenceDocumentService = evidenceDocumentService;
    }

    @GetMapping("/user/{userUuid}")
    public ResponseEntity<List<EvidenceDocument>> getDocumentsByUserUuid(
            @PathVariable String userUuid) {
        List<EvidenceDocument> documents = evidenceDocumentService.getDocumentsByUserUuid(userUuid);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewDocument(@PathVariable String fileName) {
        return evidenceDocumentService.getDocumentByFileName(fileName);
    }

    @PostMapping("/upload")
    public ResponseEntity<EvidenceDocument> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "id", required = false) Long id,
            @RequestBody EvidenceDocument evidenceDocument
    ){
        try{
            if (id != null){
                evidenceDocument.setId(id);
            }
            EvidenceDocument savedDocument = evidenceDocumentService.saveEvidenceDocument(evidenceDocument, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/allFile")
    public ResponseEntity<List<EvidenceDocument>> getAllDocuments(){
        List<EvidenceDocument> documents = evidenceDocumentService.getAllEvidenceDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvidenceDocument> getDocumentById(@PathVariable Long id) {
        return evidenceDocumentService.getEvidenceDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {

            byte[] fileContent = evidenceDocumentService.getFileContent(id);

            Optional<EvidenceDocument> documentOptional = evidenceDocumentService.downloadEvidenceDocumentById(id);
            String fileName = documentOptional.map(EvidenceDocument::getFileName).orElse("file");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(fileContent));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile newFile) {
        try {
            evidenceDocumentService.updateFile(id, newFile);
            return ResponseEntity.ok("File updated successfully.");
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the file.");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        evidenceDocumentService.deleteEvidenceDocument(id);
        return ResponseEntity.noContent().build();
    }
}

