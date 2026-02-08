package com.libreriaGeneral.accion;

import com.libreriaGeneral.util.FlashRequest;
import com.libreriaGeneral.util.GestorModulos;
import org.openxava.actions.IForwardAction;
import org.openxava.actions.TabBaseAction;

import java.util.Map;

public class FiltrarTabAction extends TabBaseAction implements IForwardAction {

    private String urlDetalle = null;

    public static final String FORMAT_ENLACE_DETALLE = "/%s/%s?detail=%s";

    public FiltrarTabAction() {}

    public void execute() throws Exception {
        this.filtrarTab();
    }

    private void filtrarTab() {

        this.reiniciarTab();
        String propiedades = this.getTab().getPropertiesNamesAsString();
        String condicion = (String) FlashRequest.getAttributeOfSession(String.class, "base_condition", "");
        this.getTab().setBaseCondition(condicion);
        this.getTab().setPropertiesNames(propiedades);
        if (this.getTab().getTotalSize() == 1) {
            this.accederAlDetalle();
        }
    }

    private void reiniciarTab() {

        this.getTab().deselectAll();
        this.getTab().reset();
        this.getTab().setRowsHidden(false);
        this.getTab().goPage(1);
        this.getTab().setFilterVisible(false);
    }

    private void accederAlDetalle() {
        if (this.registroConIdUnico()) {
            Object id = ((Map.Entry) this.getTab().getAllKeys()[0].entrySet().iterator().next()).getValue();
            String module = (String) this.getManager().getPreviousModules().firstElement();
            this.urlDetalle = String.format("/%s/%s?detail=%s", GestorModulos.INSTANCE.getModuloActual(), module, String.valueOf(id));
        } else {
            this.setNextMode("detai");
            this.getView().addValues(this.getTab().getAllKeys()[0]);
            this.getView().refresh();
        }
    }

    private boolean registroConIdUnico() {
        return this.getTab().getAllKeys()[0].size() == 1;
    }

    public String getForwardURI() {
        return this.urlDetalle;
    }

    public boolean inNewWindow() {
        return false;
    }
}
