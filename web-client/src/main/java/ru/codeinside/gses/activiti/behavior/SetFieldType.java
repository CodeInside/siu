package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class SetFieldType implements FieldType<Set<String>> {
  final String name;
  final Usage usage;

  public SetFieldType(String name, Usage usage) {
    this.name = name;
    this.usage = usage;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Usage getUsage() {
    return usage;
  }

  @Override
  public Field<Set<String>> createField(final Expression expression) {
    if (expression == null) {
      if (usage == Usage.OPTIONAL) {
        return StaticField.of(null);
      }
      throw missedValueException();
    }
    if (expression instanceof FixedValue) {
      return StaticField.of(cast(expression.getValue(null)));
    }
    return new Field<Set<String>>() {
      @Override
      public Set<String> getValue(VariableScope scope) {
        return cast(expression.getValue(scope));
      }
    };
  }

  Set<String> cast(Object object) {
    if (object == null) {
      throw missedValueException();
    }
    String value = StringUtils.trimToNull(object.toString());
    if (value == null) {
      throw missedValueException();
    }
    Set<String> resultSet = new HashSet<String>();
    int beginIndex = 0;
    while (true) {
      int endIndex = value.indexOf(',', beginIndex + 1);
      if (endIndex > 0) {
        String e = StringUtils.trimToNull(value.substring(beginIndex, endIndex));
        if (e != null) {
          resultSet.add(e);
        }
        beginIndex = endIndex+1;
      } else {
        String e = StringUtils.trimToNull(value.substring(beginIndex));
        if (e != null) {
          resultSet.add(e);
        }
        break;
      }
    }
    return resultSet;
  }

  IllegalArgumentException missedValueException() {
    return new IllegalArgumentException(String.format(
      "Пропущено значение поля {%s}", name
    ));
  }
}
