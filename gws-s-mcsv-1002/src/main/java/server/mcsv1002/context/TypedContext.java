package server.mcsv1002.context;

import ru.codeinside.gws.api.ReceiptContext;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TypedContext {
  private ReceiptContext context;
  private String prefix_var_name;

  public TypedContext(ReceiptContext context, String prefix_var_name) {
    this.context = context;
    this.prefix_var_name = prefix_var_name;
  }

  public String getString(String varName) {
    return (String) getVariable(varName);
  }

  private Object getVariable(String varName) {
    return context.getVariable(prefix_var_name + varName);
  }

  public Long getLong(String varName) {
    final Object value = getVariable(varName);
    return value != null ? (Long) value : Long.valueOf(0l);
  }

  public XMLGregorianCalendar getCalendar(String varName) {
    Object result = getVariable(varName);
    if (result != null) {
      return date((Date) result);
    } else {
      return null;
    }
  }

  private XMLGregorianCalendar date(Date date) {
    if (date == null) {
      return null;
    }
    try {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      final XMLGregorianCalendar xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      xml.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setSecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setMinute(DatatypeConstants.FIELD_UNDEFINED);
      xml.setHour(DatatypeConstants.FIELD_UNDEFINED);
      xml.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      return xml;
    } catch (final DatatypeConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean getBoolean(String varName) {
    Object result = getVariable(varName);
    if (result == null) {
       return false;
    } else {
      return (Boolean)result;
    }
  }

	public boolean hasVariableInContext(String varName) {	
		return getVariable(varName) != null;
	}
}
