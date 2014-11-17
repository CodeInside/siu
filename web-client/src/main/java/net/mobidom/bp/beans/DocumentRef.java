package net.mobidom.bp.beans;

import java.io.Serializable;

import net.mobidom.bp.beans.types.DocumentRefType;

public abstract class DocumentRef implements Serializable {

  private static final long serialVersionUID = -5009645188422247844L;

  protected DocumentRefType documentRefType;

  public abstract String getLabelString();

  public abstract DocumentRefType getDocumentRefType();

}
