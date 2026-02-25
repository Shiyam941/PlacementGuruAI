package com.placementguru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeSubmissionRequest {
    
    private String problemId;
    private String code;
    private String language; // JAVA, PYTHON, CPP
}
