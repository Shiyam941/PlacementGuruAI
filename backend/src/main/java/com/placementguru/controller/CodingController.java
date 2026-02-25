package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.dto.CodeSubmissionRequest;
import com.placementguru.model.CodingProblem;
import com.placementguru.model.Submission;
import com.placementguru.model.User;
import com.placementguru.service.CodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coding")
public class CodingController {

    @Autowired
    private CodingService codingService;

    @GetMapping("/problems")
    public ResponseEntity<?> getAllProblems() {
        try {
            List<CodingProblem> problems = codingService.getAllProblems();
            return ResponseEntity.ok(new ApiResponse(true, "Problems retrieved", problems));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<?> getProblemById(@PathVariable String id) {
        try {
            CodingProblem problem = codingService.getProblemById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Problem retrieved", problem));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/problems/difficulty/{difficulty}")
    public ResponseEntity<?> getProblemsByDifficulty(@PathVariable String difficulty) {
        try {
            List<CodingProblem> problems = codingService.getProblemsByDifficulty(difficulty);
            return ResponseEntity.ok(new ApiResponse(true, "Problems retrieved", problems));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitCode(@AuthenticationPrincipal User user,
                                       @RequestBody CodeSubmissionRequest request) {
        try {
            Submission submission = codingService.submitCode(user, request);
            return ResponseEntity.ok(new ApiResponse(true, "Code submitted", submission));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/submissions")
    public ResponseEntity<?> getUserSubmissions(@AuthenticationPrincipal User user) {
        try {
            List<Submission> submissions = codingService.getUserSubmissions(user);
            return ResponseEntity.ok(new ApiResponse(true, "Submissions retrieved", submissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/submissions/problem/{problemId}")
    public ResponseEntity<?> getProblemSubmissions(@AuthenticationPrincipal User user,
                                                   @PathVariable String problemId) {
        try {
            List<Submission> submissions = codingService.getProblemSubmissions(user, problemId);
            return ResponseEntity.ok(new ApiResponse(true, "Submissions retrieved", submissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
