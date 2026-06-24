package com.wts.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class MenuVO {
    private String id;
    private String name;
    private String icon;
    private String path;
    private Integer sort;
    private List<MenuVO> children;
}
