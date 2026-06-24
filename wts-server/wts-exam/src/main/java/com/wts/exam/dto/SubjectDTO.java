package com.wts.exam.dto;

import lombok.Data;
import java.util.List;

@Data
public class SubjectDTO {
    private String typeid;
    private String tiptype;
    private String pcontent;
    private String tipstr;
    private String tipnote;
    private Integer level;
    private Integer point;
    private List<AnswerDTO> answers;
}
