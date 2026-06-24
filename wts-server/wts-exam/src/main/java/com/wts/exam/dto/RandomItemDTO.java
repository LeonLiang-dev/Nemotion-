package com.wts.exam.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class RandomItemDTO implements Serializable {
    private String name;
    private List<RandomStepDTO> steps;

    @Data
    public static class RandomStepDTO implements Serializable {
        private String name;
        private Integer sort;
        private Integer subnum;
        private Integer subpoint;
        private String tiptype;
        private String typeid;
        private String knowid;
    }
}
