package ru.codeinside.jpa;

import com.google.common.base.Supplier;
import ru.codeinside.gses.webui.Flash;

import javax.persistence.EntityManager;
import java.io.Serializable;

final public class ActivitiEntityManager {

  public static final EntityManager INSTANCE = new EntityManagerProxy(new RequestContextSupplier());

  private final static class RequestContextSupplier implements Supplier<EntityManager>, Serializable {
    @Override
    public EntityManager get() {
      return Flash.flash().getEm();
    }
  }
}
