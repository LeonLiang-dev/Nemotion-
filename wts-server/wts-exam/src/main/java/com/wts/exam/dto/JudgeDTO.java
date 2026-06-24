package com.wts.exam.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class JudgeDTO implements Serializable {
    private List<JudgePointDTO> points;

    @Data
    public static class JudgePointDTO implements Serializable {
        private String versionId;
        private Integer point;
    }
}
