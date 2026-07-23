package com.example.b07demosummer2024.user;


// A lexicographical ordering system called Fractional Index
public class FractionalIndex {
    private static final String BASE_62_DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DEFAULT_START_KEY = "a0";

    public static String generateKeyBetween(String prevKey, String nextKey) {
        if (prevKey == null && nextKey == null) {
            return DEFAULT_START_KEY;
        }

        if (prevKey != null && nextKey != null && prevKey.compareTo(nextKey) >= 0) {
            throw new IllegalArgumentException("prevKey must be less than nextKey");
        }

        // Appending to the end
        if (nextKey == null) {
            return incrementKey(prevKey);
        }

        // Prepending to the beginning
        if (prevKey == null) {
            return decrementKey(nextKey);
        }

        // Inserting between two existing keys
        return midpoint(prevKey, nextKey);
    }

    private static String incrementKey(String key) {
        char lastChar = key.charAt(key.length() - 1);
        int index = BASE_62_DIGITS.indexOf(lastChar);

        // If the last character isn't 'z' (the last character in Base62), bump it up
        if (index < BASE_62_DIGITS.length() - 1) {
            return key.substring(0, key.length() - 1) + BASE_62_DIGITS.charAt(index + 1);
        }

        // If it hit 'z', extend the string with the midpoint character ('V')
        return key + "V";
    }

    private static String decrementKey(String key) {
        char lastChar = key.charAt(key.length() - 1);
        int index = BASE_62_DIGITS.indexOf(lastChar);

        if (index > 0) {
            return key.substring(0, key.length() - 1) + BASE_62_DIGITS.charAt(index - 1);
        }

        return key.substring(0, key.length() - 1) + "0V";
    }

    private static String midpoint(String prevKey, String nextKey) {
        int maxLength = Math.max(prevKey.length(), nextKey.length()) + 1;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < maxLength; i++) {
            char pChar = i < prevKey.length() ? prevKey.charAt(i) : '0';
            char nChar = i < nextKey.length() ? nextKey.charAt(i) : 'z';

            int pVal = BASE_62_DIGITS.indexOf(pChar);
            int nVal = BASE_62_DIGITS.indexOf(nChar);

            if (pVal == nVal) {
                result.append(pChar);
            } else if (nVal - pVal > 1) {
                int midVal = pVal + (nVal - pVal) / 2;
                result.append(BASE_62_DIGITS.charAt(midVal));
                break;
            } else {
                result.append(pChar);
                prevKey = prevKey + "0";
            }
        }
        return result.toString();
    }
}
