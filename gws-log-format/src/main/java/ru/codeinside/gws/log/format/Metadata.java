package ru.codeinside.gws.log.format;

import java.util.Date;

/**
 * Created by alexey on 12.03.14.
 */
public class Metadata {
  public String error;
  public Date date;
  public String processInstanceId;
  public Pack clientRequest;
  public Pack clientResponse;
  public Pack serverRequest;
  public Pack serverResponse;
  public String componentName;

}
