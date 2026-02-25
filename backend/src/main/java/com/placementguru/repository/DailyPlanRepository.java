package com.placementguru.repository;

import com.placementguru.model.DailyPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPlanRepository extends MongoRepository<DailyPlan, String> {
    
    Optional<DailyPlan> findByUserIdAndDate(String userId, LocalDate date);
    
    List<DailyPlan> findByUserIdOrderByDateDesc(String userId);
}
