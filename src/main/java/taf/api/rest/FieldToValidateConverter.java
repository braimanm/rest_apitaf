package taf.api.rest;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public class FieldToValidateConverter implements DataValueConverter {

    @Override
    public boolean canConvert(Class type) {
        return (FieldToValidate.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()));
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        FieldToValidate field = (FieldToValidate) value;
        String name = field.name;
        String path = field.path;
        String assertion = field.assertion;
        String active = field.active;
        String data = field.getData(false);
        if (name != null) writer.addAttribute("name", name);
        if (path != null) writer.addAttribute("path", path);
        if (assertion != null) writer.addAttribute("assertion", assertion);
        if (active != null) writer.addAttribute("active", active);
        if (data != null) writer.setValue(data);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        FieldToValidate field = new FieldToValidate();
        field.name = reader.getAttribute("name");
        field.path = reader.getAttribute("path");
        field.assertion = reader.getAttribute("assertion");
        field.active = reader.getAttribute("active");
        field.setData(reader.getValue());
        return field;
    }

    public <T> T fromString(String str, Class<T> cls) {
        return fromString(str, cls, null);
    }

    @Override
    public <T> T fromString(String str, Class<T> cls, Field field) {
        T obj;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Component " + cls.getCanonicalName() +
                    " must have default constructor !", e.getCause());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        FieldToValidate fieldToValidate = ((FieldToValidate) obj);
        fieldToValidate.setData(str);
        fieldToValidate.name = "name";
        fieldToValidate.path = "path";
        return obj;
    }
}
