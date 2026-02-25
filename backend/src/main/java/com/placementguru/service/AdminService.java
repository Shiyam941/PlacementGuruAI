package com.placementguru.service;

import com.placementguru.model.CodingProblem;
import com.placementguru.model.User;
import com.placementguru.repository.CodingProblemRepository;
import com.placementguru.repository.SubmissionRepository;
import com.placementguru.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    private CodingProblemRepository problemRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    public CodingProblem addCodingProblem(User admin, CodingProblem problem) {
        problem.setCreatedAt(LocalDateTime.now());
        problem.setCreatedBy(admin.getId());
        return problemRepository.save(problem);
    }

    public CodingProblem updateCodingProblem(String problemId, CodingProblem updatedProblem) {
        CodingProblem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        problem.setTitle(updatedProblem.getTitle());
        problem.setDescription(updatedProblem.getDescription());
        problem.setDifficulty(updatedProblem.getDifficulty());
        problem.setCategory(updatedProblem.getCategory());
        problem.setTags(updatedProblem.getTags());
        problem.setTestCases(updatedProblem.getTestCases());
        problem.setSampleInput(updatedProblem.getSampleInput());
        problem.setSampleOutput(updatedProblem.getSampleOutput());
        
        return problemRepository.save(problem);
    }

    public void deleteCodingProblem(String problemId) {
        problemRepository.deleteById(problemId);
    }

    public Map<String, Object> getStudentProgress(String studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        long totalSubmissions = submissionRepository.findByUserIdOrderBySubmittedAtDesc(studentId).size();
        long acceptedSubmissions = submissionRepository.countByUserIdAndStatus(studentId, "ACCEPTED");
        
        Map<String, Object> progress = new HashMap<>();
        progress.put("studentName", student.getName());
        progress.put("email", student.getEmail());
        progress.put("totalSubmissions", totalSubmissions);
        progress.put("acceptedSubmissions", acceptedSubmissions);
        progress.put("successRate", totalSubmissions > 0 ? (acceptedSubmissions * 100.0 / totalSubmissions) : 0);
        
        return progress;
    }

    public List<Map<String, Object>> getAllStudentsProgress() {
        List<User> students = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains("STUDENT"))
                .toList();
        
        List<Map<String, Object>> progressList = new ArrayList<>();
        
        for (User student : students) {
            Map<String, Object> progress = getStudentProgress(student.getId());
            progressList.add(progress);
        }
        
        return progressList;
    }

    public List<CodingProblem> getAllProblems() {
        return problemRepository.findAll();
    }
}
