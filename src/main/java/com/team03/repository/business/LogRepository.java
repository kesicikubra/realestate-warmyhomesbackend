package com.team03.repository.business;

import com.team03.entity.business.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

  void deleteByUserId(Long id);

  @Query("SELECT l FROM Log l WHERE l.user.id = :id")
  List<Log>  findByUser(Long id);

  @Query("SELECT l FROM Log l WHERE l.user.id = :id")
  Page<Log> findAllLogsById(Long id, Pageable pageable);
}