package com.wts.exam.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private String answer;
    private String answernote;
    private String rightanswer;
    private Integer sort;
    private Integer pointweight;
    private Integer groupno;
    private String pcontent;
}
