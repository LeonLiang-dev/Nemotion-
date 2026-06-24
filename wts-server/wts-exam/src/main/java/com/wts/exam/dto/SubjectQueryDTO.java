package com.wts.exam.dto;

import lombok.Data;

@Data
public class SubjectQueryDTO {
    private Integer page = 1;
    private Integer size = 20;
    private String keyword;
    private String typeid;
    private String tiptype;
    private String pstate;
}
