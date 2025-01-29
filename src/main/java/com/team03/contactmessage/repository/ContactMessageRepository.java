package com.team03.contactmessage.repository;


import com.team03.contactmessage.entity.ContactMessage;
import com.team03.contactmessage.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    @Query("SELECT c FROM ContactMessage c " +
            "WHERE (COALESCE(:q, '') = '' OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.message) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND (:status IS NULL OR c.status = :status)")
    Page<ContactMessage> findBySearchCriteria(@Param("q") String q, @Param("status") Status status, Pageable pageable);
}