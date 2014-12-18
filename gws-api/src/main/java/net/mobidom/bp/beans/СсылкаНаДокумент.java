package net.mobidom.bp.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.mobidom.bp.beans.types.ТипСсылкиНаДокумент;

public abstract class СсылкаНаДокумент implements Serializable {

  private static final long serialVersionUID = -5009645188422247844L;

  protected ТипСсылкиНаДокумент типСсылкиНаДокумент;

  protected Map<String, String> additionalProperties;

  public String getLabelString() {
    return new String();
  }

  public abstract ТипСсылкиНаДокумент getТипСсылкиНаДокумент();

  public abstract Map<String, Serializable> getDocumentRequestParams();

  public Map<String, String> getDocumentReferencePropertiesForLabels() {
    return new HashMap<String, String>();
  }

  public Map<String, String> getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Map<String, String> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

}
