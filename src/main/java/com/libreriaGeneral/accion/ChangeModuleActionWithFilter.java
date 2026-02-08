package com.libreriaGeneral.accion;

import com.libreriaGeneral.util.FlashRequest;
import org.openxava.actions.IChangeModuleAction;
import org.openxava.actions.ViewBaseAction;
import org.openxava.tab.Tab;
import org.openxava.util.Is;
import org.openxava.util.Strings;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ChangeModuleActionWithFilter extends ViewBaseAction implements IChangeModuleAction {

    public static final String KEY_BASE_CONDITION = "base_condition";

    public static final String KEY_PREVIOUS_MODULE = " previous_module";

    public static final Pattern PATTERN_NORMALIZE_STRING = Pattern.compile("NORMALIZESTRING\\(([^\\)]*)\\)");

    private static final String FORMAT_NORMALIZE_STRING_CONDITION = "TRIM(UPER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(%s,'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u'), 'Á', 'A'), 'É', 'E'),'Í', 'I'), 'Ó', 'O'), 'Ú', 'U')))";

    public ChangeModuleActionWithFilter(){}

    public void execute() throws Exception {

        FlashRequest.setAttributeOfSession("base_condition", this.getBaseCondition().toString());

        Tab tab = (Tab) getContext().get("GestHogarOX", "MuestraGastosPorMes", "xava_tab");

        tab.setBaseCondition(getBaseCondition().toString());

    }

    protected StringBuffer getBaseCondition() {
        StringBuffer sb = new StringBuffer("");
        Iterator var2 = this.getBaseConditions().entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var2.next();
            Object value = this.getView().getValue((String) entry.getKey());
            if (!Is.empty(value)) {
                this.addCondition(sb, String.format(Locale.ENGLISH, (String) entry.getValue(), this.normalizeString(value)));
            }
        }
        System.out.println("En el change " + sb);

        return sb;
    }

    protected void addCondition(StringBuffer sb, String condition) {
        if (sb.length() > 0) {
            sb.append(" AND ");
        }
        sb.append(this.parseCondition(condition));
    }

    protected String parseCondition(String condition) {
        for (Matcher matcherEsquema = PATTERN_NORMALIZE_STRING.matcher(condition); matcherEsquema.find(); condition = condition.replace(matcherEsquema.group(0), String.format("TRIM(UPER(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(%s,'á', 'a'), 'é', 'e'), 'í', 'i'), 'ó', 'o'), 'ú', 'u'), 'Á', 'A'), 'É', 'E'),'Í', 'I'), 'Ó', 'O'), 'Ú', 'U')))", matcherEsquema.group(1)))){

        }
        return condition;
    }

    private Object normalizeString(Object value) {
        return String.class.isInstance(value) ? Strings.removeAccents(value.toString().trim()) : value;
    }

    public boolean hasRinitNextModule() {return true;}

    protected abstract Map<String, String> getBaseConditions();

}
