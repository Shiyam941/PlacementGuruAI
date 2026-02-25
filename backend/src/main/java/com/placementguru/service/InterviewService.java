package com.placementguru.service;

import com.placementguru.dto.InterviewRequest;
import com.placementguru.model.InterviewSession;
import com.placementguru.model.User;
import com.placementguru.repository.InterviewSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterviewService {

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private AIService aiService;

    public Map<String, Object> startInterview(User user, InterviewRequest request) {
        // Check if there's an active session
        sessionRepository.findByUserIdAndCompletedFalse(user.getId())
                .ifPresent(session -> {
                    throw new RuntimeException("You have an active interview session. Please complete it first.");
                });

        InterviewSession session = new InterviewSession();
        session.setUserId(user.getId());
        session.setInterviewType(request.getInterviewType());
        session.setStartTime(LocalDateTime.now());
        session.setCompleted(false);

        String firstQuestion = aiService.getInterviewQuestion(request.getInterviewType(), null);
        
        // Save the first question to the session
        InterviewSession.QnAPair firstQnA = new InterviewSession.QnAPair();
        firstQnA.setQuestion(firstQuestion);
        firstQnA.setTimestamp(LocalDateTime.now());
        session.getQnaPairs().add(firstQnA);

        InterviewSession saved = sessionRepository.save(session);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", saved.getId());
        response.put("question", firstQuestion);
        response.put("questionNumber", 1);

        return response;
    }

    public Map<String, Object> submitAnswer(User user, InterviewRequest request) {
        InterviewSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to session");
        }

        if (session.isCompleted()) {
            throw new RuntimeException("This interview session is already completed");
        }

        // Get the last question that hasn't been answered
        if (session.getQnaPairs().isEmpty()) {
            throw new RuntimeException("No question found in session");
        }
        
        InterviewSession.QnAPair currentQnA = session.getQnaPairs().get(session.getQnaPairs().size() - 1);
        String currentQuestion = currentQnA.getQuestion();
        
        // Check if this question already has an answer
        if (currentQnA.getAnswer() != null && !currentQnA.getAnswer().isEmpty()) {
            throw new RuntimeException("This question has already been answered");
        }

        // Get feedback for the answer
        String feedback = aiService.evaluateInterviewAnswer(currentQuestion, request.getAnswer());
        int rating = extractRating(feedback);

        // Update the current QnA pair with answer and feedback
        currentQnA.setAnswer(request.getAnswer());
        currentQnA.setFeedback(feedback);
        currentQnA.setRating(rating);

        // Generate next question or end interview
        Map<String, Object> response = new HashMap<>();
        
        // Count answered questions
        long answeredCount = session.getQnaPairs().stream()
                .filter(qa -> qa.getAnswer() != null && !qa.getAnswer().isEmpty())
                .count();
        
        if (answeredCount >= 5) {
            // End interview after 5 questions
            session.setCompleted(true);
            session.setEndTime(LocalDateTime.now());
            int totalScore = session.getQnaPairs().stream()
                    .filter(qa -> qa.getAnswer() != null)
                    .mapToInt(InterviewSession.QnAPair::getRating)
                    .sum();
            session.setScore(totalScore);
            session.setOverallFeedback("Interview completed. Total score: " + totalScore + "/25");
            
            sessionRepository.save(session);
            
            response.put("completed", true);
            response.put("feedback", feedback);
            response.put("totalScore", totalScore);
            response.put("maxScore", 25);
        } else {
            // Ask next question
            String context = "Previous questions: " + 
                    session.getQnaPairs().stream()
                    .filter(qa -> qa.getAnswer() != null)
                    .map(qa -> qa.getQuestion())
                    .reduce("", (a, b) -> a + "; " + b);
            
            String nextQuestion = aiService.getInterviewQuestion(session.getInterviewType(), context);
            
            // Add next question to session
            InterviewSession.QnAPair nextQnA = new InterviewSession.QnAPair();
            nextQnA.setQuestion(nextQuestion);
            nextQnA.setTimestamp(LocalDateTime.now());
            session.getQnaPairs().add(nextQnA);
            
            sessionRepository.save(session);
            
            response.put("completed", false);
            response.put("feedback", feedback);
            response.put("nextQuestion", nextQuestion);
            response.put("questionNumber", (int) answeredCount + 1);
        }

        return response;
    }

    public List<InterviewSession> getSessionHistory(User user) {
        return sessionRepository.findByUserIdOrderByStartTimeDesc(user.getId());
    }

    public InterviewSession getSessionDetails(User user, String sessionId) {
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        
        return session;
    }

    private int extractRating(String feedback) {
        // Simple logic to extract rating from feedback
        if (feedback.contains("5") || feedback.toLowerCase().contains("excellent")) return 5;
        if (feedback.contains("4") || feedback.toLowerCase().contains("good")) return 4;
        if (feedback.contains("3") || feedback.toLowerCase().contains("average")) return 3;
        if (feedback.contains("2") || feedback.toLowerCase().contains("poor")) return 2;
        return 1;
    }
}
