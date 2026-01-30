package com.libreriaGeneral.OX.validationOX;


import org.openxava.util.Messages;
import org.openxava.validators.ValidationException;

public class OXValidationErrorException extends ValidationException {

    public OXValidationErrorException(){
    }

    public OXValidationErrorException(String messageText) {
        super(messageText);
    }

    public OXValidationErrorException(ValidationException ex) {
        super(ex);
    }

    public OXValidationErrorException(Messages errors) {
        super(errors);
    }

    public OXValidationErrorException(String messageId, Object... ids){
        super(new Messages());
        this.getErrors().add(Messages.Type.ERROR, messageId, ids);
    }

    public void addMember(String member) {
        this.getErrors().getMembers().add(member);
    }

    public static OXValidationErrorException addMember(OXValidationErrorException ex, String member) {
        return addMember(ex,"error.campoConErrores", member);
    }

    public static OXValidationErrorException addError(OXValidationErrorException ex, String message, Object... ids) {
        if(ex == null){
            ex = new OXValidationErrorException(message, ids);
        }else{
            ex.getErrors().add(Messages.Type.ERROR, message, ids);
        }

        return ex;
    }

    public static OXValidationErrorException addMember(OXValidationErrorException ex, String message, String member) {
        if(ex == null) {
            ex = new OXValidationErrorException(message, new Object[]{member});

        }else {
            ex.addMember(member);
        }
        return ex;
    }
}
