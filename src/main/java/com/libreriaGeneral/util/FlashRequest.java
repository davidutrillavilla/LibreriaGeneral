package com.libreriaGeneral.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FlashRequest {

    private static ThreadLocal<HttpServletRequest> currentThread;

    public FlashRequest() {}

    public static synchronized ThreadLocal<HttpServletRequest> getCurrentThread() {

        if(currentThread == null) {
            currentThread = new ThreadLocal<>();
        }

        return currentThread;
    }

    public static synchronized HttpServletRequest getRequest() { return (HttpServletRequest) getCurrentThread().get(); }

    public static synchronized void setRequest(HttpServletRequest request) { getCurrentThread().set(request);}

    public static String getURL() { return (String) getAttributeOfRequest(String.class, "URL"); }

    public static <T> T getAttributeOfRequest(Class<T> T, String key) {
        return getAttributeOfRequest(T, key, (T) null);
    }

    public static<T> T getAttributeOfRequest(Class<T> T, String key, T defaultValue) {

        HttpServletRequest request = getRequest();
        T value = defaultValue;
        if (request != null) {
            T aux = (T)request.getAttribute(key);
            if(aux != null) {
                value = aux;
            }
        }
        return value;
    }

    public static synchronized HttpSession getSession() {

        HttpServletRequest request = getRequest();
        return request != null ? request.getSession(true) : null;
    }

    public static <T> T getAttributeOfSession(Class<T> T, String key) {

        return getAttributeOfSession(T, key, (T) null);
    }

    public static <T> T getAttributeOfSession(Class<T> T, String key, T defaultValue) {

        HttpSession session = getSession();
        T value = defaultValue;
        if (session != null) {
            T aux = (T) session.getAttribute(key);
            if(aux != null) {
                value = aux;
            }
        }
        return value;
    }

    public static void setAttributeOfSession(String key, Object value) {

        HttpSession session = getSession();
        if (session != null) {
            session.setAttribute(key, value);
        }
    }
}
