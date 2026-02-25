package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.model.DailyPlan;
import com.placementguru.model.User;
import com.placementguru.service.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roadmap")

public class RoadmapController {

    @Autowired
    private RoadmapService roadmapService;

    @GetMapping("/today")
    public ResponseEntity<?> getTodayPlan(@AuthenticationPrincipal User user) {
        try {
            DailyPlan plan = roadmapService.getTodayPlan(user);
            return ResponseEntity.ok(new ApiResponse(true, "Today's plan retrieved", plan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generatePlan(@AuthenticationPrincipal User user,
                                         @RequestParam(required = false) 
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            LocalDate targetDate = date != null ? date : LocalDate.now();
            DailyPlan plan = roadmapService.generateDailyPlan(user, targetDate);
            return ResponseEntity.ok(new ApiResponse(true, "Plan generated", plan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{planId}/task/{taskIndex}/complete")
    public ResponseEntity<?> markTaskComplete(@AuthenticationPrincipal User user,
                                             @PathVariable String planId,
                                             @PathVariable int taskIndex) {
        try {
            DailyPlan plan = roadmapService.markTaskComplete(user, planId, taskIndex);
            return ResponseEntity.ok(new ApiResponse(true, "Task marked as complete", plan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPlanHistory(@AuthenticationPrincipal User user) {
        try {
            List<DailyPlan> plans = roadmapService.getPlanHistory(user);
            return ResponseEntity.ok(new ApiResponse(true, "History retrieved", plans));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
