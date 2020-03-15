package com.thorschmidt.curso.organizze.helper;

import android.util.Base64;

public class Base64Custom {
    public static String encrypt64(String decodedText) {
        return Base64.encodeToString(decodedText.getBytes(),Base64.DEFAULT)
                .replaceAll("(\\n|\\r)", "");// remove all invalid characters
    }

    public static String decrypt64(String encodedText) {
        return new String(Base64.decode(encodedText,Base64.DEFAULT));
    }
}
