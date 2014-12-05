package net.mobidom.bp.beans.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.mobidom.bp.beans.Пол;
import net.mobidom.bp.beans.form.BeanItemSelectPropertyFieldDescriptor.ResultExtractor;

public class IdCaptionItem implements Serializable {
  private static final long serialVersionUID = 3502384982955303591L;

  public static ResultExtractor<IdCaptionItem> EXTRACTOR = new ResultExtractor<IdCaptionItem>() {

    @Override
    public Object getValue(IdCaptionItem value) {
      return value.getValue();
    }
  };

  public static List<IdCaptionItem> GENDER_ITEMS = new ArrayList<IdCaptionItem>() {
    private static final long serialVersionUID = -7824235365122787688L;
    {
      add(new IdCaptionItem(Пол.ЖЕНСКИЙ, "Женский"));
      add(new IdCaptionItem(Пол.МУЖСКОЙ, "Мужской"));
    }
  };

  private Object value;
  private String caption;

  public IdCaptionItem() {
  }

  public IdCaptionItem(Object value, String name) {
    super();
    this.value = value;
    this.caption = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object id) {
    this.value = id;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String name) {
    this.caption = name;
  }

}