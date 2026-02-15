package com.libreriaGeneral.util.validador;

import com.libreriaGeneral.dao.UtilDao;
import com.libreriaGeneral.util.Condition;

import com.libreriaGeneral.util.exception.ValidationErrorException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
public class BeanValidator {

    public static final String FORMAT_ERROR_GENERAL = "Debe rellenar todos los campos obligatorios";

    public static final String FORMAT_ERROR = "Error al validar el campo %s de la clase %s";

    public static final String FORMAT_ERROR_NULO = "error.%s_%s.valorNulo";

    public static final String FORMAT_ERROR_REFERENCIA_NULO = "No se ha encontrado la referencia %s con identificador %s";

    public static final String CONDITION_EMPTY_METHOD = "empty";

    public BeanValidator() {}

    public static void validateRequiredValue(Object value) throws ValidationErrorException {
        validateRequiredValue(value, FORMAT_ERROR_GENERAL);
    }

    public static void validateRequiredValue(Object value, String error) throws ValidationErrorException {

        if(empty(value)) {

            throw new ValidationErrorException(error);
        }
    }

    public static void validateAllRequiredValues(Object bean) throws ValidationErrorException {

        validateAllRequiredValues(bean, (List) null);
    }

    public static void validateAllRequiredValues(Object bean, List<String> attributesToValidate) throws ValidationErrorException {

        validateRequiredValue(bean);

        Class<?> validateClass = bean.getClass();

        validateRequiredFields(validateClass, bean, attributesToValidate);

        while (validateClass.getSuperclass() != null && !validateClass.getSuperclass().equals(Object.class)) {

            validateRequiredFields(validateClass.getSuperclass(), bean, attributesToValidate);

            validateClass = validateClass.getSuperclass();
        }
    }

    private static void validateRequiredFields(Class<?> validateClass, Object bean, List<String> attributesToValidate) throws ValidationErrorException {

        Field[] var3 = validateClass.getDeclaredFields();

        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {

            Field field = var3[var5];

            RequiredField annotation = (RequiredField) field.getAnnotation(RequiredField.class);

            if (!Condition.empty(attributesToValidate) && attributesToValidate.contains(field.getName()) || Condition.empty(attributesToValidate) && !Condition.empty(annotation)) {

                validateRequiredField(bean, field);
            }
        }
    }

    private static void validateRequiredField(Object bean, Field field) throws ValidationErrorException {

        try{
            String methodName = getMethodName(field);

            Method getMethod = bean.getClass().getMethod(methodName);

            if (empty(getMethod.invoke(bean))) {

                throw new ValidationErrorException(String.format(FORMAT_ERROR_NULO, bean.getClass().getSimpleName(), field.getName()));
            }
        } catch (ValidationErrorException var4) {

            throw var4;

        }catch (Exception var5) {

            String error = String.format(FORMAT_ERROR_REFERENCIA_NULO, field.getName(), bean.getClass());

            throw new ValidationErrorException(error);
        }
    }

    public static void validateNullReference(Class<?> claseReferencia, String id) throws ValidationErrorException, SQLException {

        String error = String.format(FORMAT_ERROR_REFERENCIA_NULO, claseReferencia.getSimpleName(), id);

        validateNotNullReference(claseReferencia, id, error);
    }

    public static void validateNotNullReference(Class<?> claseReferencia, String id, String error) throws ValidationErrorException, SQLException {

        validateRequiredValue(id, error);

        Object value = UtilDao.INSTANCE.find(claseReferencia.getSimpleName(), id);

        validateRequiredValue(value, error);
    }

    private static String getMethodName(Field field) {

        String fieldName = StringUtils.capitalize(field.getName());

        String methodName = "get" + fieldName;

        if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {

            methodName = "is" + fieldName;
        }

        return methodName;
    }

    private static boolean empty(Object value) {

        try {
            Class<?> valueClass = Object.class;

            if (value != null) {

                valueClass = value.getClass();
            }

            Method emptyMethod = Condition.class.getDeclaredMethod("empty", valueClass);

            return (Boolean) emptyMethod.invoke((Object) null, value);

        } catch (Exception var3) {

            return Condition.empty(value);
        }
    }
}
