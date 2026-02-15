package com.libreriaGeneral.util.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ValidationErrorException extends Exception{

    private static final int DEFAULT_CODIGO_ERROR = 400;

    private final int codigoError;

    private final String error;

    public ValidationErrorException(String error) { this(error, 400); }

    public ValidationErrorException(String error, int codigoError) { this(error, codigoError,(Throwable) null);}

    public ValidationErrorException(String error, int codigoError, Throwable e) {

        super(error, e);
        this.error = error;
        this.codigoError = codigoError;
    }
}
