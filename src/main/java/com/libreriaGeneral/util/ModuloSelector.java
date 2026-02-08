package com.libreriaGeneral.util;

public enum ModuloSelector {

    INSTANCE;

    private ModuloSelector() {}

    public void set(String modulo) {

        FlashRequest.setAttributeOfSession("MODULO", modulo);
    }

    public String getModulo() {

        return (String) FlashRequest.getAttributeOfSession(String.class, "MODULO");

    }

    public void uset() {

        FlashRequest.setAttributeOfSession("MODULO", (Object) null);

    }
}
