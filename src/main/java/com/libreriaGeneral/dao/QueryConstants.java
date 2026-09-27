package com.libreriaGeneral.dao;

public class QueryConstants {

    // Tablas

    public static final String TABLA_ESTABLECIMIENTO = "establecimiento";
    public static final String TABLA_GASTO = "gasto";
    public static final String TABLA_CIUDAD = "ciudad";
    public static final String TABLA_RESUMEN = "resumenmensualtotal";
    public static final String TABLA_USUARIO = "usuario";
    public static final String TABLA_ETIQUETA = "etiqueta";

    // Tipo comercio

    public static final String TIPO_CARNICERIA = "CARNICERIA";
    public static final String TIPO_PESCADERIA = "PESCADERIA";
    public static final String TIPO_CHINO_TIENDA_VARIOS = "CHINO_TIENDA_VARIOS";
    public static final String TIPO_PROVEEDOR_SERVICIOS = "PROVEEDOR_SERVICIOS";
    public static final String TIPO_BANCO_CAJA = "BANCO_CAJA";
    public static final String TIPO_SUPERMERCADO = "SUPERMERCADO";
    public static final String TIPO_FARMACIA = "FARMACIA";
    public static final String TIPO_OPTICA = "OPTICA";
    public static final String TIPO_GASOLINERA = "GASOLINERA";
    public static final String TIPO_RESTAURACION = "RESTAURACION";

    // Secciones

    public static final String SECCION_ALIMENTACION = "ALIMENTACION";
    public static final String SECCION_ROPA = "ROPA";
    public static final String SECCION_CALZADO = "CALZADO";
    public static final String SECCION_DEPORTE = "DEPORTE";
    public static final String SECCION_EXTRAESCOLARES = "EXTRAESCOLARES";
    public static final String SECCION_OCIO = "OCIO";
    public static final String SECCION_HIPOTECA = "HIPOTECA";
    public static final String SECCION_LUZ = "LUZ";
    public static final String SECCION_COMUNIDAD = "COMUNIDAD";
    public static final String SECCION_SEGUROS = "SEGUROS";
    public static final String SECCION_MEDICAMENTOS = "MEDICAMENTOS";
    public static final String SECCION_GAFAS = "GAFAS";
    public static final String SECCION_COMBUSTIBLE = "COMBUSTIBLE";
    public static final String SECCION_VEHICULOS = "VEHICULOS";

    // Supermercados

    public static final String SUPERMERCADO_MERCADONA = "Mercadona";
    public static final String SUPERMERCADO_LIDL = "Lidl";
    public static final String SUPERMERCADO_ALDI = "Aldi";
    public static final String SUPERMERCADO_ALCAMPO = "Alcampo";
    public static final String SUPERMERCADO_ALVIMAR = "Alvimar";
    public static final String SUPERMERCADO_HNOSSANCHEZ = "Hnos. Sanchez";

    //Consultas

    public static final String SQL_FIND = "SELECT * FROM %s%s WHERE id = ? ";
    public static final String SQL_PERSISTS = "INSERT INTO %s ";
    public static final String SQL_SELECT_RESUMEN = "SELECT COUNT(id) FROM ";
    public static final String SQL_SELECT_FROM_TOTAL = "SELECT SUM(importecompra) FROM ";
    public static final String SQL_WHERE_TOTAL = "WHERE ejercicio = %d and mes = '%s' ";
    public static final String SQL_WHERE_TOTAL_TIPO_COMERCIO = "WHERE ejercicio = %d and mes = '%s' and tipocomercio = '%s' ";
    public static final String SQL_WHERE_TOTAL_SECCION = "WHERE ejercicio = %d and mes = '%s' and secciongasto = '%s' ";
    public static final String SQL_WHERE_SUPERMERCADO = "WHERE ejercicio = %d and mes = '%s' and nombreestablecimiento = '%s' ";
    public static final String SQL_WHERE_EJERCICIO_MES = "WHERE ejercicio = %d and mes = '%s' ";
    public static final String SQL_DELETE_ID = "DELETE FROM %s%s WHERE id = ? ";
}
