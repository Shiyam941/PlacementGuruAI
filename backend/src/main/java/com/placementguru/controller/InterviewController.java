package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.dto.InterviewRequest;
import com.placementguru.model.InterviewSession;
import com.placementguru.model.User;
import com.placementguru.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PostMapping("/start")
    public ResponseEntity<?> startInterview(@AuthenticationPrincipal User user,
                                           @RequestBody InterviewRequest request) {
        try {
            Map<String, Object> response = interviewService.startInterview(user, request);
            return ResponseEntity.ok(new ApiResponse(true, "Interview started", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<?> submitAnswer(@AuthenticationPrincipal User user,
                                         @RequestBody InterviewRequest request) {
        try {
            Map<String, Object> response = interviewService.submitAnswer(user, request);
            return ResponseEntity.ok(new ApiResponse(true, "Answer submitted", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getSessionHistory(@AuthenticationPrincipal User user) {
        try {
            List<InterviewSession> sessions = interviewService.getSessionHistory(user);
            return ResponseEntity.ok(new ApiResponse(true, "History retrieved", sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionDetails(@AuthenticationPrincipal User user,
                                               @PathVariable String sessionId) {
        try {
            InterviewSession session = interviewService.getSessionDetails(user, sessionId);
            return ResponseEntity.ok(new ApiResponse(true, "Session details retrieved", session));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
