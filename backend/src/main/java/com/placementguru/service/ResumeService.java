package com.placementguru.service;

import com.placementguru.dto.ResumeAnalysisResponse;
import com.placementguru.model.ResumeReport;
import com.placementguru.model.User;
import com.placementguru.repository.ResumeReportRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ResumeReportRepository reportRepository;

    @Autowired
    private AIService aiService;

    public ResumeAnalysisResponse analyzeResume(User user, MultipartFile file) {
        try {
            String extractedText = extractTextFromPDF(file);

            String aiAnalysis = aiService.analyzeResume(extractedText);

            ResumeReport report = parseAIAnalysis(aiAnalysis);
            report.setUserId(user.getId());
            report.setFileName(file.getOriginalFilename());
            report.setExtractedText(extractedText.substring(0, Math.min(extractedText.length(), 5000)));
            report.setAnalyzedAt(LocalDateTime.now());

            ResumeReport saved = reportRepository.save(report);

            return new ResumeAnalysisResponse(
                    saved.getId(),
                    saved.getAtsScore(),
                    saved.getStrengths(),
                    saved.getWeaknesses(),
                    saved.getSuggestions(),
                    saved.getDetectedSkills()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze resume: " + e.getMessage());
        }
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private ResumeReport parseAIAnalysis(String analysis) {
        ResumeReport report = new ResumeReport();
        
        // Simple parsing logic - in production, use more robust parsing
        String[] lines = analysis.split("\n");
        
        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        List<String> skills = new ArrayList<>();
        int atsScore = 70; // Default score

        for (String line : lines) {
            line = line.trim();
            if (line.matches(".*\\d{1,3}.*") && line.toLowerCase().contains("score")) {
                try {
                    atsScore = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                } catch (Exception e) {
                    atsScore = 70;
                }
            } else if (line.toLowerCase().contains("strength")) {
                strengths.add(line.replaceAll(".*:\\s*", "").replaceAll("^\\d+\\.\\s*", ""));
            } else if (line.toLowerCase().contains("weakness")) {
                weaknesses.add(line.replaceAll(".*:\\s*", "").replaceAll("^\\d+\\.\\s*", ""));
            } else if (line.toLowerCase().contains("suggestion") || line.toLowerCase().contains("improvement")) {
                suggestions.add(line.replaceAll(".*:\\s*", "").replaceAll("^\\d+\\.\\s*", ""));
            } else if (line.toLowerCase().contains("skill")) {
                String skillLine = line.replaceAll(".*:\\s*", "");
                skills.addAll(Arrays.asList(skillLine.split(",\\s*")));
            }
        }

        // Ensure we have at least some data
        if (strengths.isEmpty()) {
            strengths.add("Experience in relevant technologies");
            strengths.add("Clear formatting and structure");
        }
        if (weaknesses.isEmpty()) {
            weaknesses.add("Could add more quantifiable achievements");
            weaknesses.add("Consider adding more keywords");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("Add measurable results to achievements");
            suggestions.add("Include relevant certifications");
            suggestions.add("Optimize for ATS with industry keywords");
        }
        if (skills.isEmpty()) {
            skills.addAll(Arrays.asList("Java", "Python", "SQL", "Problem Solving"));
        }

        report.setAtsScore(atsScore);
        report.setStrengths(strengths);
        report.setWeaknesses(weaknesses);
        report.setSuggestions(suggestions);
        report.setDetectedSkills(skills);

        return report;
    }

    public List<ResumeReport> getReportHistory(User user) {
        return reportRepository.findByUserIdOrderByAnalyzedAtDesc(user.getId());
    }

    public ResumeReport getReportDetails(User user, String reportId) {
        ResumeReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        
        if (!report.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        
        return report;
    }
}
