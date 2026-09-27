package com.libreriaGeneral.OX.acccionOX;

import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import java.sql.SQLException;


public abstract class OXServiceAction<J> extends OXViewBaseAction<J> {

 //   private static final Logger log = LoggerFactory.getLogger(OXServiceAction.class);

    private static final String ERROR_SERVIDOR = "Ha ocurrido un error desconocido. Consulte al administrador";

    public OXServiceAction() {

    }

    protected void executeAction() throws Exception {

        try {

            Response response = this.invokeService(this.json);

            this.validateResponse(response);

            this.handleResponse(response);

        }catch(ValidationException var2) {

            throw var2;

        }catch (Exception var3){

            var3.printStackTrace(); // <--- ESTO te mostrará el error REAL en la consola
            throw new ValidationException(var3.getMessage());

        }
    }

    protected abstract Response invokeService(J var1) throws SQLException, IllegalAccessException;

    protected abstract void handleResponse(Response var1);
}
