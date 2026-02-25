package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.dto.ResumeAnalysisResponse;
import com.placementguru.model.ResumeReport;
import com.placementguru.model.User;
import com.placementguru.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(@AuthenticationPrincipal User user,
                                          @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Please upload a file"));
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Only PDF files are supported"));
            }

            ResumeAnalysisResponse response = resumeService.analyzeResume(user, file);
            return ResponseEntity.ok(new ApiResponse(true, "Resume analyzed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Error analyzing resume: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getReportHistory(@AuthenticationPrincipal User user) {
        try {
            List<ResumeReport> reports = resumeService.getReportHistory(user);
            return ResponseEntity.ok(new ApiResponse(true, "History retrieved", reports));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportDetails(@AuthenticationPrincipal User user,
                                             @PathVariable String reportId) {
        try {
            ResumeReport report = resumeService.getReportDetails(user, reportId);
            return ResponseEntity.ok(new ApiResponse(true, "Report details retrieved", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
