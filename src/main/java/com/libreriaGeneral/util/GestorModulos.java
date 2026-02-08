package com.libreriaGeneral.util;

public enum GestorModulos {

    INSTANCE;

    private static final String DEFAULT_MODULO = "modules";

    private GestorModulos() {
    }

    public String getModuloActual() {
        String modulo = ModuloSelector.INSTANCE.getModulo();
        if (Condition.empty(modulo)) {
            modulo= "modules";
        }

        return modulo;
    }
}
