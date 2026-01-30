package com.libreriaGeneral.OX.acccionOX;

import com.libreriaGeneral.OX.utilOX.ViewUtil;
import com.libreriaGeneral.OX.validationOX.OXValidationErrorException;
import org.openxava.actions.TabBaseAction;
import org.openxava.util.Is;

import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;

public abstract class OXViewBaseAction<J> extends TabBaseAction {

    protected OXValidationErrorException ex;
    protected J json;

    protected OXViewBaseAction() {
    }

    protected abstract J setUp();

    protected abstract void executeAction() throws Exception;

    public void execute() throws Exception{

        this.json = this.setUp();
        if (this.ex != null) {

            throw this.ex;

        }else {

            if (this.getErrors().isEmpty()) {

                this.executeAction();
            }
        }
    }

    public String getValue(String memeber) {

        return (String)this.getValue(String.class, memeber);
    }

    public <T>T getValue(Class<T> clase, String memeber) {

        return ViewUtil.INSTANCE.getValue(this.getView(), memeber, clase);
    }

    public <T> T getValue(Class<T>clase, String memeber, T defaultValue) {

        return ViewUtil.INSTANCE.getValue(this.getView(), memeber, clase, defaultValue);
    }

    public String getValueNotEmpty(String memeber) {

        return(String)this.getValueNotEmpty(String.class, memeber);
    }

    public <T> T getValueNotEmpty(Class<T>clase, String memeber) {

        T value = ViewUtil.INSTANCE.getValue(this.getView(), memeber,clase);

        if(Is.empty(value)) {

            this.ex = OXValidationErrorException.addMember(this.ex, memeber);
        }

        return value;
    }

    public String getValueNotEmptySubview(String subview, String memeber) {

        String value = this.getView().getSubview(subview).getValueString(memeber);

        if(Is.empty(value)) {

            this.ex = OXValidationErrorException.addMember(this.ex, subview + "." + memeber);
        }

        return value;

    }

    public void validateResponse(Response response) throws ValidationException {

        int status = response.getStatus();

        if(response.getStatus() != 200) {

            throw new ValidationException(response.getEntity().toString());
        }
    }

    public <T> T readResponse(Response response, Class<T> responseClass) {

        return (T) response.getEntity();
    }
}
