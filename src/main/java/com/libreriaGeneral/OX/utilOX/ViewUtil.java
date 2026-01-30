package com.libreriaGeneral.OX.utilOX;

import org.openxava.util.ElementNotFoundException;
import org.openxava.util.Is;
import org.openxava.view.View;

public enum ViewUtil {

    INSTANCE;

    private static final String TAG_ID = "di";
    public static final String FORMAT_MENSAJE_ERROR = "%s.%s.ValorNulo";
    public static final String FORMAT_DEFAULT_MENSAJE_ERROR="El valo de '%s' es obligatorio";

    private ViewUtil() {
    }

    public<C> C getValue(View view, String name, Class<C> clase, C valorPorDefecto) {

        try{
            Object object = view.getValue(name);
            C valor = (C) object;
            if(Is.empty(object)) {
                valor = valorPorDefecto;
            }

            return valor;
        }catch (ElementNotFoundException var7){
            return valorPorDefecto;
        }
    }

    public<C> C getValue(View view, String name, Class<C> clase) {
        return this.getValue(view, name, clase, null);
    }
}
