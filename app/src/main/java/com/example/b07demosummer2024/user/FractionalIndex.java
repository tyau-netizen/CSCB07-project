package com.example.b07demosummer2024.user;


import static java.lang.Math.pow;

/*
 * A lexicographical ordering system called Fractional Index.
 *
 * Generated ordering strings consist of an integer part and a fraction part which results in
 * functionally infinite precision.
 *
 * Generated ordering strings are sortable alphabetically.
 */
public final class FractionalIndex {
    private static final String BASE_62_DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = BASE_62_DIGITS.length();
    private static final String DEFAULT_START_KEY = "a0";

    // The "integer head" indicates the length of the integer part of the string
    private static final char ZERO_HEAD = 'a';
    private static final int ZERO_HEAD_INDEX = BASE_62_DIGITS.indexOf(ZERO_HEAD);
    private static final char MIN_HEAD = 'A';
    private static final char MAX_HEAD = 'z';

    // Private constructor to prevent instantiation
    private FractionalIndex() {}

    public static String generateKeyBetween(String prevKey, String nextKey) {
        if (prevKey == null && nextKey == null) {
            return DEFAULT_START_KEY;
        }

        if (prevKey != null && nextKey != null && prevKey.compareTo(nextKey) >= 0) {
            throw new IllegalArgumentException("prevKey must be less than nextKey");
        }

        // Appending to the end
        if (nextKey == null) {
            return offsetIntegerHead(prevKey, 1);
        }

        // Prepending to the beginning
        if (prevKey == null) {
            return offsetIntegerHead(nextKey, -1);
        }

        // Inserting between two existing keys
        return midpoint(prevKey, nextKey);
    }

    private static String offsetIntegerHead(String key, int offset) {
        String integerPart = getIntegerPart(key);
        long value = decodeInteger(integerPart);
        return encodeInteger(value + offset);
    }

    private static String midpoint(String a, String b) {
        String intA = getIntegerPart(a);
        String intB = getIntegerPart(b);

        if (!intA.equals(intB)) {
            String nextA = offsetIntegerHead(intA, 1);
            if (nextA.compareTo(b) < 0) return nextA;
            return midpointFractional(a, b, intA.length());
        }

        return midpointFractional(a, b, intA.length());
    }

    private static String midpointFractional(String a, String b, int index) {
        StringBuilder result = new StringBuilder(a.substring(0, index));
        int i = index;

        while (true) {
            char ca = i < a.length() ? a.charAt(i) : '0';
            char cb = i < b.length() ? b.charAt(i) : BASE_62_DIGITS.charAt(BASE - 1);

            int digitA = BASE_62_DIGITS.indexOf(ca);
            int digitB = BASE_62_DIGITS.indexOf(cb);

            if (digitA == digitB) {
                result.append(ca);
                i++;
            } else if (digitB - digitA > 1) {
                result.append(BASE_62_DIGITS.charAt(digitA + (digitB - digitA) / 2));
                return result.toString();
            } else {
                result.append(ca);
                i++;
                // Append midpoint character 'V' (Base-62 index 31) when adjacent
                while (i < a.length() && a.charAt(i) == BASE_62_DIGITS.charAt(BASE - 1)) {
                    result.append(a.charAt(i));
                    i++;
                }
                result.append(BASE_62_DIGITS.charAt(31));
                return result.toString();
            }
        }
    }

    private static String getIntegerPart(String key) {
        char head = key.charAt(0);
        int length = getIntegerLength(head);
        return key.substring(0, Math.min(length, key.length()));
    }

    private static int getIntegerLength(char head) {
        if (head >= ZERO_HEAD && head <= MAX_HEAD) {
            // Positive integer: length = (head - 'a') + 2
            return (head - ZERO_HEAD) + 2;
        } else if (head >= MIN_HEAD && head < ZERO_HEAD) {
            // Negative integer: length = ('a' - head) + 1
            return (ZERO_HEAD - head) + 1;
        }
        throw new IllegalArgumentException("Invalid integer head character: " + head);
    }

    private static String encodeInteger(long value) {
        if (value >= 0) {
            String base62Val = toBase62(value);
            int length = base62Val.length();
            char head = BASE_62_DIGITS.charAt(ZERO_HEAD_INDEX + length - 1);
            return head + base62Val;
        } else {
            long absVal = Math.abs(value) - 1;
            int length = (int) Math.floor(Math.log(absVal * (BASE - 1) + BASE) / Math.log(BASE));
            long cumulativeCapacity = (long) (Math.pow(BASE, length) - BASE) / (BASE - 1);
            long invertedVal = (long) (Math.pow(BASE, length) - 1 - (absVal - cumulativeCapacity));

            StringBuilder sb = new StringBuilder(toBase62(invertedVal));
            sb.reverse();
            while (sb.length() < length) {
                sb.append(BASE_62_DIGITS.charAt(0));
            }
            String base62Val = sb.reverse().toString();
            char head = BASE_62_DIGITS.charAt(ZERO_HEAD_INDEX - length);
            return head + base62Val;
        }
    }

    private static long decodeInteger(String intPart) {
        char head = intPart.charAt(0);
        String body = intPart.substring(1);
        long value = fromBase62(body);

        if (head >= ZERO_HEAD) {
            return value;
        }
        int length = ZERO_HEAD_INDEX - BASE_62_DIGITS.indexOf(head);
        long cumulativeCapacity = (long) (Math.pow(BASE, length) - BASE) / (BASE - 1);
        long absVal = (long) (Math.pow(BASE, length) - 1 - value + cumulativeCapacity);
        return -(absVal + 1);

    }

    private static String toBase62(long num) {
        if (num == 0) return "0";
        StringBuilder sb = new StringBuilder();
        long n = Math.abs(num);
        while (n > 0) {
            sb.append(BASE_62_DIGITS.charAt((int) (n % BASE)));
            n /= BASE;
        }
        return sb.reverse().toString();
    }

    private static long fromBase62(String str) {
        if (str.isEmpty()) return 0;
        long num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = num * BASE + BASE_62_DIGITS.indexOf(str.charAt(i));
        }
        return num;
    }
}