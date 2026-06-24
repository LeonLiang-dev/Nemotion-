package com.wts.exam.util;

import com.wts.common.exception.BizException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class ExamTimeUtils {
    private static final DateTimeFormatter COMPACT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final List<DateTimeFormatter> INPUT_FORMATS = List.of(
            COMPACT_FORMAT,
            DateTimeFormatter.ofPattern("yyyyMMddHHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    );

    private ExamTimeUtils() {
    }

    public static String nowCompact() {
        return format(LocalDateTime.now());
    }

    public static String format(LocalDateTime time) {
        return time.format(COMPACT_FORMAT);
    }

    public static String normalizeNullable(String value, String fieldName) {
        if (isBlank(value)) {
            return null;
        }
        LocalDateTime parsed = parseNullable(value);
        if (parsed == null) {
            throw BizException.fail(fieldName + "格式不正确，请使用 yyyy-MM-dd HH:mm 或 yyyyMMddHHmmss");
        }
        return format(parsed);
    }

    public static LocalDateTime parseRequired(String value, String fieldName) {
        if (isBlank(value)) {
            throw BizException.fail("请设置" + fieldName);
        }
        LocalDateTime parsed = parseNullable(value);
        if (parsed == null) {
            throw BizException.fail(fieldName + "格式不正确，请使用 yyyy-MM-dd HH:mm 或 yyyyMMddHHmmss");
        }
        return parsed;
    }

    public static LocalDateTime parseNullable(String value) {
        if (isBlank(value)) {
            return null;
        }

        String normalized = normalizeSeparators(value.trim());
        for (DateTimeFormatter formatter : INPUT_FORMATS) {
            try {
                return LocalDateTime.parse(normalized, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private static String normalizeSeparators(String value) {
        String normalized = value.replace('T', ' ');
        if (normalized.endsWith("Z")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        int dotIndex = normalized.indexOf('.');
        if (dotIndex > 0) {
            normalized = normalized.substring(0, dotIndex);
        }
        if (normalized.length() > 19) {
            normalized = normalized.substring(0, 19);
        }
        return normalized;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
