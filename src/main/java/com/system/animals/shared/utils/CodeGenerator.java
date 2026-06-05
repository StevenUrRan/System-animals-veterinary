package com.system.animals.shared.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.system.animals.exception.CodeRangeInvalidException;
import com.system.animals.exception.InvalidCodeLengthException;

@Component
public class CodeGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final long UNIQUE_CODE_RANGE_START = 100_000_000L;
    private static final long UNIQUE_CODE_RANGE_END = 999_999_999L;
    private static final long UNIQUE_CODE_RANGE = UNIQUE_CODE_RANGE_END - UNIQUE_CODE_RANGE_START;

    public long generateUniqueCode() {
        return UNIQUE_CODE_RANGE_START + SECURE_RANDOM.nextLong(UNIQUE_CODE_RANGE);
    }

    public long generateCodeInRange(long min, long max) {
        if (min >= max) {
            throw new CodeRangeInvalidException();
        }
        return min + SECURE_RANDOM.nextLong(max - min);
    }


    public long generateNumericCode(int length) {
        if (length <= 0 || length > 18) {
            throw new InvalidCodeLengthException();
        }
        long min = (long) Math.pow(10, length - 1);
        long max = (long) Math.pow(10, length);
        return generateCodeInRange(min, max);
    }
}
