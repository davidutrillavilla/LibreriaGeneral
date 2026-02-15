package com.libreriaGeneral.dao;

import com.libreriaGeneral.util.ConstantesGenerales;

import javax.persistence.PrePersist;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public enum UtilDao {

    INSTANCE;

    private Logger log = Logger.getLogger("log");

    public DataBaseManager getDataBaseManager() throws SQLException {

        DataBaseManager dataBaseManager = new DataBaseManager();

        log.info("GetDataBaseManager");

        return dataBaseManager;
    }

    public PreparedStatement getPreparedStatement(DataBaseManager dataBaseManager, String sql) throws SQLException {

        log.info("Instancia del 'PreparedStatement'");

        return dataBaseManager.getConnection().prepareStatement(sql);
    }

    public void persistOrMerge (Object object) throws SQLException, IllegalAccessException {

        merge(object);
    }

    @Transactional
    public void merge(Object object) throws SQLException, IllegalAccessException {
        Class<?> clazz = object.getClass();

        // 1. Identificar el campo @Id (buscando en la jerarquía)

        Field idField = null;

        Class<?> currentClass = clazz;

        while (currentClass != null && idField == null) {

            for (Field f : currentClass.getDeclaredFields()) {

                if (f.isAnnotationPresent(javax.persistence.Id.class)) {

                    idField = f;

                    break;
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        if (idField == null) {

            throw new SQLException("No se puede hacer merge: La clase " + clazz.getSimpleName() + " no tiene un campo @Id");
        }

        idField.setAccessible(true);

        Object idValue = idField.get(object);

        // 2. Si el ID es nulo, delegar a persists
        if (idValue == null || idValue.toString().trim().isEmpty()) {

            persists(object);

            return;
        }

        // 3. Preparar el UPDATE filtrando Enums y el propio ID
        String schema = ConstantesGenerales.SCHEMA_GENERAL;

        String tableName = clazz.getSimpleName().toLowerCase();

        StringBuilder sql = new StringBuilder("UPDATE " + schema + tableName + " SET ");

        Field[] allFields = clazz.getDeclaredFields();

        boolean primerCampo = true;

        for (Field field : allFields) {
            // FILTROS: No Enums, No el campo ID
            if (field.getType().isEnum() || field.equals(idField)) continue;

            if (!primerCampo) sql.append(", ");

            sql.append(field.getName()).append(" = ?");

            primerCampo = false;
        }

        sql.append(" WHERE ").append(idField.getName()).append(" = ?");

        DataBaseManager dataBaseManager = getDataBaseManager();

        try (PreparedStatement ps = dataBaseManager.prepareStatement(sql.toString())) {

            int jdbcIndex = 1;

            // 4. Setear valores del SET (aplicando lógica de Entidades)
            for (Field field : allFields) {

                if (field.getType().isEnum() || field.equals(idField)) continue;

                field.setAccessible(true);

                Object value = field.get(object);

                Object valueToPersist = null;

                if (value != null) {

                    if (value.getClass().isAnnotationPresent(javax.persistence.Entity.class)) {

                        valueToPersist = obtenerIdDeEntidad(value);

                    } else {

                        valueToPersist = value;

                    }
                }

                ps.setObject(jdbcIndex++, valueToPersist);
            }

            // 5. Setear el valor del WHERE (el ID)
            ps.setObject(jdbcIndex, idValue);

            int rowsAffected = ps.executeUpdate();

            // 6. Si no existía en DB, insertamos
            if (rowsAffected == 0) {

                persists(object);
            }
        } catch (Exception e) {

            throw new SQLException("Error durante el merge de " + tableName + ": " + e.getMessage(), e);
        }
    }

    @Transactional
    public void persists(Object object) throws SQLException {

        Class<?> clazz = object.getClass();

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(PrePersist.class)) {

                method.setAccessible(true);

                try {
                    method.invoke(object);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        String schema = ConstantesGenerales.SCHEMA_GENERAL;

        String tableName = clazz.getSimpleName().toLowerCase(); // Suponiendo que el nombre de la tabla es igual al nombre de la clase

        StringBuilder sql = new StringBuilder("INSERT INTO " + schema + tableName + " (");

        StringBuilder values = new StringBuilder("VALUES (");

        Field[] fields = clazz.getDeclaredFields();

        boolean primerCampo = true;

        for (Field field : fields) {
            // FILTRO IDÉNTICO: No incluimos Enums en el SQL
            if (field.getType().isEnum()) continue;

            if (!primerCampo) {

                sql.append(", ");

                values.append(", ");
            }
            sql.append(field.getName()); // O el nombre de la columna real

            values.append("?");

            primerCampo = false;
        }

        sql.append(") ").append(values).append(")");

        DataBaseManager dataBaseManager = getDataBaseManager();

        try (PreparedStatement preparedStatement = dataBaseManager.prepareStatement(sql.toString())) {

            int jdbcIndex = 1; // El primer '?' es el 1

            for (int i = 0; i < fields.length; i++) {

                fields[i].setAccessible(true);

                // FILTRO IDÉNTICO: Saltamos los Enums
                if (fields[i].getType().isEnum()) {

                    continue;
                }

                Object value = fields[i].get(object);

                Object valueToPersist = null;

                if (value != null) {

                    if (value.getClass().isAnnotationPresent(javax.persistence.Entity.class)) {

                        valueToPersist = obtenerIdDeEntidad(value);

                    } else {

                        valueToPersist = value;
                    }
                }

                // Aquí asignamos el valor al '?' correspondiente
                preparedStatement.setObject(jdbcIndex, valueToPersist);

                jdbcIndex++; // Solo avanzamos el índice JDBC si realmente pusimos un valor
            }

            preparedStatement.executeUpdate(); // Ejecutar el INSERT

        } catch (Exception e) {

            throw new SQLException("Error al persistir: " + e.getMessage(), e);
        }
    }

    public ResultSet find(String tabla, String id) throws SQLException {

        try {
            DataBaseManager dataBaseManager = getDataBaseManager();

            String schema = ConstantesGenerales.SCHEMA_GENERAL;

            String sql = String.format(QueryConstants.SQL_FIND, schema, tabla);

            PreparedStatement preparedStatement = getPreparedStatement(dataBaseManager, sql);

            preparedStatement.setString(1, id);

            ResultSet rs = dataBaseManager.executeQuery(preparedStatement);

            if (rs.next()) {

                return rs;
            }

            rs.close();

            preparedStatement.close();

            dataBaseManager.close();

        } catch (SQLException e) {

            return null;
        }

        return null;
    }

    private Object obtenerIdDeEntidad(Object entity) {
        try {
            // Intentamos buscar el campo anotado con @Id en la entidad
            for (Field field : entity.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(javax.persistence.Id.class)) {

                    field.setAccessible(true);

                    return field.get(entity);
                }
            }
            // Si no lo encuentra en sus campos, buscamos en la superclase (por si usa Identifiable de OpenXava)
            for (Field field : entity.getClass().getSuperclass().getDeclaredFields()) {

                if (field.isAnnotationPresent(javax.persistence.Id.class)) {

                    field.setAccessible(true);

                    return field.get(entity);
                }
            }
        } catch (Exception e) {

            log.info("No se pudo extraer el ID de la entidad: " + entity.getClass().getName());
        }
        return null; // O lanza una excepción si el ID es obligatorio
    }

    public int getCodigoInsercion(Object object) throws SQLException {

        Class<?> clazz = object.getClass();

        String schema = ConstantesGenerales.SCHEMA_GENERAL;

        String tableName = clazz.getSimpleName().toLowerCase(); // Suponiendo que el nombre de la tabla es igual al nombre de la clase

        StringBuilder sql = new StringBuilder("SELECT MAX(codigo) FROM " + schema + tableName);

        log.info("Consulta : " + sql);

        DataBaseManager dataBaseManager = getDataBaseManager();

        try (PreparedStatement preparedStatement = dataBaseManager.prepareStatement(sql.toString())) {

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            return rs.getInt(1) + 1;

        } catch (Exception e) {

            throw new SQLException();
        }
    }

    public boolean delete(String tabla, String id) throws SQLException {

        try {
            DataBaseManager dataBaseManager = getDataBaseManager();

            String schema = ConstantesGenerales.SCHEMA_GENERAL;

            String sql = String.format(QueryConstants.SQL_DELETE_ID, schema, tabla);

            log.info("La consulta " + sql + " " + id);

            PreparedStatement preparedStatement = UtilDao.INSTANCE.getPreparedStatement(dataBaseManager, sql);

            preparedStatement.setString(1, id);

            int i = preparedStatement.executeUpdate();

            dataBaseManager.close();

            return i == 1;

        } catch (SQLException e) {

            log.info("Error al borrar " + e.getMessage());

            throw e;
        }
    }
}
