package com.team03.repository.business;

import com.team03.entity.business.DailyAdvertView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyAdvertViewRepository extends JpaRepository<DailyAdvertView, Long> {


}
