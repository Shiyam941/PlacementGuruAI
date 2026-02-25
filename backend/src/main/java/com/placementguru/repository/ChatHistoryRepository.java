package com.placementguru.repository;

import com.placementguru.model.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatHistory, String> {
    
    List<ChatHistory> findByUserIdOrderByTimestampDesc(String userId);
    
    List<ChatHistory> findTop20ByUserIdOrderByTimestampDesc(String userId);
}
