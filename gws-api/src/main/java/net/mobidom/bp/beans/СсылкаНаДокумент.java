package net.mobidom.bp.beans;

import java.io.Serializable;

import net.mobidom.bp.beans.types.ТипСсылкиНаДокумент;

public abstract class СсылкаНаДокумент implements Serializable {

  private static final long serialVersionUID = -5009645188422247844L;

  protected ТипСсылкиНаДокумент типСсылкиНаДокумент;

  public abstract String getLabelString();

  public abstract ТипСсылкиНаДокумент getТипСсылкиНаДокумент();

}
