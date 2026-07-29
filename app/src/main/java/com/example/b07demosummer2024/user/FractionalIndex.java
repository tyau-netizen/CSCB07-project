package com.example.b07demosummer2024.user;


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
            return stepIntegerHead(prevKey, 1);
        }

        // Prepending to the beginning
        if (prevKey == null) {
            return stepIntegerHead(nextKey, -1);
        }

        // Inserting between two existing keys
        return midpoint(prevKey, nextKey);
    }

    private static String stepIntegerHead(String key, int direction) {
        String intPart = getIntegerPart(key);
        String fracPart = key.substring(intPart.length());

        // Drop fractional tail on decrement if present
        if (direction < 0 && !fracPart.isEmpty()) {
            return intPart;
        }

        char head = intPart.charAt(0);
        String body = intPart.substring(1);

        // Convert body from Base-62 to integer, adjust by direction, and convert back
        long val = fromBase62(body);
        if (head < ZERO_HEAD) val = -(val + 1); // Account for negative offset

        val += direction;

        // Re-encode stepped value with updated head marker
        if (val >= 0) {
            String encoded = toBase62(val);
            char newHead = BASE_62_DIGITS.charAt(ZERO_HEAD_INDEX + (encoded.length() - 1));
            return newHead + encoded;
        } else {
            String encoded = toBase62(Math.abs(val) - 1);
            char newHead = BASE_62_DIGITS.charAt(ZERO_HEAD_INDEX - encoded.length());
            return newHead + encoded;
        }
    }

    private static String midpoint(String a, String b) {
        String intA = getIntegerPart(a);
        String intB = getIntegerPart(b);

        if (!intA.equals(intB)) {
            String nextA = stepIntegerHead(intA, 1);
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

    private static String toBase62(long num) {
        if (num == 0) return "0";
        StringBuilder sb = new StringBuilder();
        long n = num;
        while (n > 0) {
            sb.insert(0, BASE_62_DIGITS.charAt((int) (n % BASE)));
            n /= BASE;
        }
        return sb.toString();
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