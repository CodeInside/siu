package ru.codeinside.gses.webui.form;

/**
 * Простая обертка на полем из {@link com.vaadin.ui.Form}
 */
public class BasicFormField implements FormField {
    private final Object propertyId;
    private final String name;
    private final Object value;

    public BasicFormField(Object propertyId, String name, Object value) {
        this.propertyId = propertyId;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getPropId() {
        return String.valueOf(propertyId);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
