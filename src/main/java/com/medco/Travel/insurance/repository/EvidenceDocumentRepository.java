package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.EvidenceDocument;
import com.medco.Travel.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceDocumentRepository extends JpaRepository<EvidenceDocument,Long> {

    List<EvidenceDocument> findByUser(User user);

}
