package com.wts.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipType {
    VACANCY("1", "填空题"),
    SELECT("2", "单选题"),
    CHECKBOX("3", "多选题"),
    JUDGE("4", "判断题"),
    INTERLOCUTION("5", "问答题"),
    FILEUP("6", "附件题");

    private final String code;
    private final String name;

    public static TipType fromCode(String code) {
        for (TipType t : values()) {
            if (t.code.equals(code)) return t;
        }
        return null;
    }
}
