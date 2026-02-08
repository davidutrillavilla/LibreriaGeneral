package com.libreriaGeneral.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Condition {

    private Condition(){}

    public static boolean empty(Object object) {
        return object == null;
    }

    public static boolean empty(BigDecimal bigDecimal) {
        return bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean empty(String string) { return string == null || string.trim().equals(""); }

    public static boolean emptyStrNumber(String string) { return empty(string) || string.equals("0"); }

    public static boolean empty(Integer integer) { return integer == null || integer == 0; }

    public static boolean empty(Long numero) { return numero == null || numero == 0L; }

    public static boolean empty(Map<?, ?> map) { return map == null || map.isEmpty(); }

    public static boolean empty(List<?> list) { return list == null || list.isEmpty(); }

    public static <V> V eval(boolean condition, V positive, V negative) { return condition ? positive : negative; }

    public static <V> V evalNotEmpty(V positive, V negative) {
        if (positive instanceof  String) {
            return !empty((String) positive) ? positive : negative;
        } else {
            return !empty(positive) ? positive : negative;
        }
    }

    public static <V> V evalNotEmpty(V positive) { return evalNotEmpty(positive, (V) null); }

    public static String evalNotEmptyStrNumber(String positive, String negative) {
        return !emptyStrNumber(positive) ? positive : negative;
    }

    public static String evalNotEmptyStrNumber(String positive) {
        return evalNotEmptyStrNumber(positive, (String) null);
    }
}
