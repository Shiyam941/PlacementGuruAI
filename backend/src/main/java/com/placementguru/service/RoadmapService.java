package com.placementguru.service;

import com.placementguru.model.DailyPlan;
import com.placementguru.model.User;
import com.placementguru.repository.DailyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoadmapService {

    @Autowired
    private DailyPlanRepository planRepository;

    @Autowired
    private AIService aiService;

    public DailyPlan generateDailyPlan(User user, LocalDate date) {
        // Check if plan already exists
        return planRepository.findByUserIdAndDate(user.getId(), date)
                .orElseGet(() -> {
                    String aiPlan = aiService.generateDailyPlan(user.getSkillLevel());
                    DailyPlan plan = parseAIPlan(aiPlan);
                    plan.setUserId(user.getId());
                    plan.setDate(date);
                    plan.setSkillLevel(user.getSkillLevel());
                    return planRepository.save(plan);
                });
    }

    private DailyPlan parseAIPlan(String aiPlan) {
        DailyPlan plan = new DailyPlan();
        List<DailyPlan.Task> tasks = new ArrayList<>();
        
        String[] lines = aiPlan.split("\n");
        String motivation = "Stay focused and keep learning!";
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            if (line.toLowerCase().contains("motivat") || line.toLowerCase().contains("quote")) {
                motivation = line.replaceAll(".*:\\s*", "");
            } else if (line.contains("|")) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    DailyPlan.Task task = new DailyPlan.Task();
                    task.setTitle(parts[0].trim());
                    task.setDescription(parts[1].trim());
                    task.setCategory(parts[2].trim());
                    try {
                        task.setEstimatedMinutes(Integer.parseInt(parts[3].trim().replaceAll("[^0-9]", "")));
                    } catch (Exception e) {
                        task.setEstimatedMinutes(30);
                    }
                    task.setCompleted(false);
                    tasks.add(task);
                }
            }
        }
        
        // Add default tasks if parsing failed
        if (tasks.isEmpty()) {
            tasks.add(new DailyPlan.Task("Solve DSA Problems", "Solve 3 problems on Arrays", "CODING", 60, false, null));
            tasks.add(new DailyPlan.Task("Practice Aptitude", "Complete 20 aptitude questions", "APTITUDE", 45, false, null));
            tasks.add(new DailyPlan.Task("Mock Interview", "Practice 5 common interview questions", "INTERVIEW_PREP", 30, false, null));
            tasks.add(new DailyPlan.Task("Work on Project", "Add new feature to portfolio project", "PROJECT", 90, false, null));
            tasks.add(new DailyPlan.Task("Learn System Design", "Watch video on database sharding", "CODING", 45, false, null));
        }
        
        plan.setTasks(tasks);
        plan.setTotalTasks(tasks.size());
        plan.setCompletedTasks(0);
        plan.setMotivation(motivation);
        
        return plan;
    }

    public DailyPlan markTaskComplete(User user, String planId, int taskIndex) {
        DailyPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        
        if (!plan.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        
        if (taskIndex < 0 || taskIndex >= plan.getTasks().size()) {
            throw new RuntimeException("Invalid task index");
        }
        
        DailyPlan.Task task = plan.getTasks().get(taskIndex);
        if (!task.isCompleted()) {
            task.setCompleted(true);
            task.setCompletedAt(java.time.LocalDateTime.now().toString());
            plan.setCompletedTasks(plan.getCompletedTasks() + 1);
            return planRepository.save(plan);
        }
        
        return plan;
    }

    public List<DailyPlan> getPlanHistory(User user) {
        return planRepository.findByUserIdOrderByDateDesc(user.getId());
    }

    public DailyPlan getTodayPlan(User user) {
        return generateDailyPlan(user, LocalDate.now());
    }
}
