package com.libreriaGeneral.libreria.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FicheroConfiguracion {

    private String schemaGeneral;

    private String nombreOrganizacion;

    private String direccionOrganizacion;

    private long telefonoOrganizacion;

    private String emailOrganizacion;
}
