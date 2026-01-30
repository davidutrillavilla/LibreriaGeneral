package com.libreriaGeneral.OX.acccionOX;


import org.openxava.actions.ViewBaseAction;
import org.openxava.util.Is;
import org.openxava.util.Labels;

public class OXAbrirDialogoAction extends ViewBaseAction {

    private String controlador;
    private String modelo;
    private String vista;
    private String titulo;

    public OXAbrirDialogoAction(){}

    @Override
    public void execute() throws Exception {
        this.showDialog();;
        this.getView().setTitle(Labels.get("AbrirDialogoAction.dialogo"));
        this.setModelo();
        this.setVista();
   //     this.setTitulo();
        this.setControladores();
    }

    private void setModelo() {
        if (!Is.empty(this.modelo)) {
            this.getView().setModelName(this.modelo);
        }
    }

    private void setVista() {
        if (!Is.empty(this.vista)) {
            this.getView().setViewName(this.vista);
        }
    }

//    private void setTitulo() {
//        if(!Is.empty(this.titulo)) {
//            this.getView().setTitle(StringEscapeUtils.unescapeHtml(Labels.get(this.titulo)));
//        }
//    }

    private void setControladores(){
        if(!Is.empty(this.controlador)) {
            this.setControllers(new String[]{this.controlador});
        }
    }

    public void setControlador(String controlador) {
        this.controlador = controlador;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    private void setVista(String vista) {
        this.vista = vista;
    }

    private void setTitulo(String titulo){
        this.titulo = titulo;
    }
}
