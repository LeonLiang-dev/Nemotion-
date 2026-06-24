package com.wts.exam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PShowType {
    EXAM("1", "标准答题"),
    RANDOM("2", "抽卷答题"),
    PRACTICE("3", "练习"),
    LEARNING("4", "学习"),
    QUESTIONNAIRE("5", "问卷"),
    SCORE_INPUT("6", "成绩补录");

    private final String code;
    private final String name;
}
