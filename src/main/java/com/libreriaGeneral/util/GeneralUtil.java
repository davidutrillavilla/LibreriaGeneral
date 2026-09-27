package com.libreriaGeneral.util;

import org.openxava.annotations.View;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum GeneralUtil {

    INSTANCE;

    public List capturarClaves(Map<String, String> map) {

        List<String> claves = new ArrayList<>();

        for (String clave : map.keySet()) {

            claves.add(clave);
        }

        return claves;
    }

    public List capturarValores(Map<String, String> map) {

        List<Object> valores = new ArrayList<>();

        for (String valor : map.values()) {

            valores.add(valor);

        }

        return valores;
    }

    public void imprimirListado(List<Object> listado) {

        System.out.println("Imprimir el listado de claves");

        for (Object elemento : listado) {

            System.out.println("Dentro del for");

            System.out.println(elemento.toString() + " - ");
        }
    }

    public <T> T parseViewAlDto(T objeto, Map<String, Object> datos) {

        if (objeto == null || datos == null) return objeto;

        Class<?> clase = objeto.getClass();

        for (Map.Entry<String, Object> entrada : datos.entrySet()) {

            String nombreAtributo = entrada.getKey();

            Object valor = entrada.getValue();

            if (valor == null) continue;

            try {
                Field campo = clase.getDeclaredField(nombreAtributo);

                campo.setAccessible(true);

                Class<?> tipoCampo = campo.getType();

                // LÓGICA REFORZADA PARA ENUMS
                if (tipoCampo.isEnum()) {

                    Object[] constantes = tipoCampo.getEnumConstants();

                    // Caso 1: El valor ya es el Enum correcto
                    if (tipoCampo.isInstance(valor)) {

                        campo.set(objeto, valor);
                    }
                    // Caso 2: El valor es un String (Nombre del enum)
                    else if (valor instanceof String) {

                        campo.set(objeto, Enum.valueOf((Class<Enum>) tipoCampo, (String) valor));
                    }
                    // Caso 3: El valor es un Integer (Ordinal del enum)
                    else if (valor instanceof Number) {

                        int ordinal = ((Number) valor).intValue();

                        if (ordinal >= 0 && ordinal < constantes.length) {

                            campo.set(objeto, constantes[ordinal]);
                        }
                    }
                }
                // LÓGICA PARA REFERENCIAS (idUsuario -> String)
                else if (tipoCampo == String.class && !(valor instanceof String)) {

                    if (valor instanceof Map) {

                        Object idReal = ((Map<?, ?>) valor).get("id");

                        campo.set(objeto, idReal != null ? idReal.toString() : null);

                    } else {

                        campo.set(objeto, valor.toString());
                    }
                }
                // ASIGNACIÓN POR DEFECTO
                else {

                    campo.set(objeto, valor);
                }

            } catch (NoSuchFieldException e) {
                // Ignorar campos sobrantes de la vista
            } catch (Exception e) {

                System.err.println("Error en atributo '" + nombreAtributo + "': " + e.getMessage());
            }
        }
        return objeto;
    }

    public <D, E> E parseDtoEntity(D dto, Class<E> claseEntity) {
        try {
            // 1. Instanciar la nueva Entidad
            E entidad = claseEntity.getDeclaredConstructor().newInstance();

            // 2. Obtener campos del DTO
            Field[] camposDto = dto.getClass().getDeclaredFields();

            for (Field campoDto : camposDto) {
                campoDto.setAccessible(true);
                Object valor = campoDto.get(dto);

                if (valor != null) {
                    try {
                        // 3. Buscar campo equivalente en la Entidad
                        Field campoEntidad = claseEntity.getDeclaredField(campoDto.getName());
                        campoEntidad.setAccessible(true);

                        // 4. VERIFICACIÓN: ¿Es una relación de JPA?
                        boolean esRelacion = campoEntidad.isAnnotationPresent(ManyToOne.class) ||
                            campoEntidad.isAnnotationPresent(OneToMany.class) ||
                            campoEntidad.isAnnotationPresent(ManyToMany.class) ||
                            campoEntidad.isAnnotationPresent(OneToOne.class) ||
                            campoEntidad.isAnnotationPresent(Enumerated.class);

                        // Solo seteamos si NO es una relación compleja
                        if (!esRelacion) {
                            campoEntidad.set(entidad, valor);
                        } else {
                            // Opcional: Log o traza para saber que se ignoró
                            // System.out.println("Ignorando relación: " + campoEntidad.getName());
                        }

                    } catch (NoSuchFieldException e) {
                        // El campo no existe en la entidad, se ignora
                    }
                }
            }
            return entidad;

        } catch (Exception e) {
            throw new RuntimeException("Error mapeando DTO a Entidad: " + e.getMessage(), e);
        }
    }

    public void validadorDatosEntrada(View view) {


    }
}

