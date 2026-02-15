package com.libreriaGeneral.util.validador;


public interface IServiceValidator<E> {

    public void validate(E dto) throws Exception;
}
