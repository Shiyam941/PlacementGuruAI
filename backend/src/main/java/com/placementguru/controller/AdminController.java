package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.model.CodingProblem;
import com.placementguru.model.User;
import com.placementguru.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/problems")
    public ResponseEntity<?> addCodingProblem(@AuthenticationPrincipal User admin,
                                             @RequestBody CodingProblem problem) {
        try {
            CodingProblem saved = adminService.addCodingProblem(admin, problem);
            return ResponseEntity.ok(new ApiResponse(true, "Problem added successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/problems/{problemId}")
    public ResponseEntity<?> updateCodingProblem(@PathVariable String problemId,
                                                @RequestBody CodingProblem problem) {
        try {
            CodingProblem updated = adminService.updateCodingProblem(problemId, problem);
            return ResponseEntity.ok(new ApiResponse(true, "Problem updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/problems/{problemId}")
    public ResponseEntity<?> deleteCodingProblem(@PathVariable String problemId) {
        try {
            adminService.deleteCodingProblem(problemId);
            return ResponseEntity.ok(new ApiResponse(true, "Problem deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/problems")
    public ResponseEntity<?> getAllProblems() {
        try {
            List<CodingProblem> problems = adminService.getAllProblems();
            return ResponseEntity.ok(new ApiResponse(true, "Problems retrieved", problems));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/students/progress")
    public ResponseEntity<?> getAllStudentsProgress() {
        try {
            List<Map<String, Object>> progress = adminService.getAllStudentsProgress();
            return ResponseEntity.ok(new ApiResponse(true, "Student progress retrieved", progress));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/students/{studentId}/progress")
    public ResponseEntity<?> getStudentProgress(@PathVariable String studentId) {
        try {
            Map<String, Object> progress = adminService.getStudentProgress(studentId);
            return ResponseEntity.ok(new ApiResponse(true, "Student progress retrieved", progress));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
