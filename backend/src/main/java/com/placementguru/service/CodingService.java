package com.placementguru.service;

import com.placementguru.dto.CodeSubmissionRequest;
import com.placementguru.model.CodingProblem;
import com.placementguru.model.Submission;
import com.placementguru.model.User;
import com.placementguru.repository.CodingProblemRepository;
import com.placementguru.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodingService {

    @Autowired
    private CodingProblemRepository problemRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public List<CodingProblem> getAllProblems() {
        return problemRepository.findAll();
    }

    public CodingProblem getProblemById(String id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
    }

    public List<CodingProblem> getProblemsByDifficulty(String difficulty) {
        return problemRepository.findByDifficulty(difficulty);
    }

    public Submission submitCode(User user, CodeSubmissionRequest request) {
        CodingProblem problem = getProblemById(request.getProblemId());
        
        Submission submission = new Submission();
        submission.setUserId(user.getId());
        submission.setProblemId(request.getProblemId());
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setSubmittedAt(LocalDateTime.now());

        if (request.getLanguage().equalsIgnoreCase("JAVA")) {
            executeJavaCode(submission, problem);
        } else {
            submission.setStatus("COMPILATION_ERROR");
            submission.setErrorMessage("Only Java is currently supported");
        }

        return submissionRepository.save(submission);
    }

    private void executeJavaCode(Submission submission, CodingProblem problem) {
        try {
            List<Submission.TestResult> results = new ArrayList<>();
            int passed = 0;
            
            for (int i = 0; i < problem.getTestCases().size(); i++) {
                CodingProblem.TestCase testCase = problem.getTestCases().get(i);
                Submission.TestResult result = new Submission.TestResult();
                result.setTestCaseNumber(i + 1);
                result.setExpectedOutput(testCase.getExpectedOutput().trim());
                
                try {
                    String output = runJavaCode(submission.getCode(), testCase.getInput());
                    result.setActualOutput(output.trim());
                    
                    if (output.trim().equals(testCase.getExpectedOutput().trim())) {
                        result.setPassed(true);
                        passed++;
                    } else {
                        result.setPassed(false);
                    }
                } catch (Exception e) {
                    result.setPassed(false);
                    result.setError(e.getMessage());
                }
                
                results.add(result);
            }
            
            submission.setTestResults(results);
            submission.setTestCasesPassed(passed);
            submission.setTotalTestCases(problem.getTestCases().size());
            
            if (passed == problem.getTestCases().size()) {
                submission.setStatus("ACCEPTED");
            } else {
                submission.setStatus("WRONG_ANSWER");
            }
            
        } catch (Exception e) {
            submission.setStatus("RUNTIME_ERROR");
            submission.setErrorMessage(e.getMessage());
        }
    }

    private String runJavaCode(String code, String input) throws Exception {
        // This is a simplified implementation
        // In production, use Docker containers or sandboxed environments
        
        // For demo purposes, we'll simulate execution
        // Real implementation would compile and run the Java code
        
        // Simulated execution - you would need to implement actual Java compilation
        // and execution with proper sandboxing for security
        
        return "Output would be generated here after actual execution";
    }

    public List<Submission> getUserSubmissions(User user) {
        return submissionRepository.findByUserIdOrderBySubmittedAtDesc(user.getId());
    }

    public List<Submission> getProblemSubmissions(User user, String problemId) {
        return submissionRepository.findByUserIdAndProblemIdOrderBySubmittedAtDesc(
                user.getId(), problemId);
    }
}
