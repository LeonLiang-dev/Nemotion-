package com.wts.auth.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String loginname;
    private String type;
    private String state;
    private String comments;
    private String imgid;
}
