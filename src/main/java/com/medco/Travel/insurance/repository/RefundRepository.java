package com.medco.Travel.insurance.repository;

import com.medco.Travel.insurance.entity.Refund;
import com.medco.Travel.insurance.shared.audit.enums.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    // Find refunds by status and request date range
    List<Refund> findByStatusAndRequestDateBetween(RefundStatus status, LocalDate startDate, LocalDate endDate);

    // Find refunds by status and request date after a certain date
    List<Refund> findByStatusAndRequestDateAfter(RefundStatus status, LocalDate startDate);

    // Find refunds by status and request date before a certain date
    List<Refund> findByStatusAndRequestDateBefore(RefundStatus status, LocalDate endDate);

//    @Query("SELECT r FROM Refund r WHERE r.createdDate BETWEEN :startDate AND :endDate")
//    Page<Refund> findByDateRange(@Param("startDate") LocalDate startDate,
//                                 @Param("endDate") LocalDate endDate,
//                                 Pageable pageable);
//
//    @Query("SELECT r FROM Refund r WHERE r.status = :status AND r.createdDate BETWEEN :startDate AND :endDate")
//    Page<Refund> findByStatusAndDateRange(@Param("status") RefundStatus status,
//                                          @Param("startDate") LocalDate startDate,
//                                          @Param("endDate") LocalDate endDate,
//                                          Pageable pageable);




    // Date range methods with pagination
    Page<Refund> findByRequestDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Refund> findByRequestDateAfter(LocalDate startDate, Pageable pageable);
    Page<Refund> findByRequestDateBefore(LocalDate endDate, Pageable pageable);

    // Status with date range methods with pagination
    Page<Refund> findByStatusAndRequestDateBetween(RefundStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Refund> findByStatusAndRequestDateAfter(RefundStatus status, LocalDate startDate, Pageable pageable);
    Page<Refund> findByStatusAndRequestDateBefore(RefundStatus status, LocalDate endDate, Pageable pageable);

    // Status only with pagination
    Page<Refund> findByStatus(RefundStatus status, Pageable pageable);

    // Policy ID with pagination
    Page<Refund> findByPolicyId(Long policyId, Pageable pageable);

    // Custom query methods for complex scenarios
    @Query("SELECT r FROM Refund r WHERE r.requestDate BETWEEN :startDate AND :endDate")
    Page<Refund> findByRequestDateRange(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        Pageable pageable);

    @Query("SELECT r FROM Refund r WHERE r.status = :status AND r.requestDate BETWEEN :startDate AND :endDate")
    Page<Refund> findByStatusAndRequestDateRange(@Param("status") RefundStatus status,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 Pageable pageable);

    // Keep existing non-paginated methods if needed for other use cases
    List<Refund> findByPolicyId(Long policyId);
    List<Refund> findByStatus(RefundStatus status);
    List<Refund> findByRequestDateBetween(LocalDate startDate, LocalDate endDate);
    List<Refund> findByRequestDateAfter(LocalDate startDate);
    List<Refund> findByRequestDateBefore(LocalDate endDate);
}

