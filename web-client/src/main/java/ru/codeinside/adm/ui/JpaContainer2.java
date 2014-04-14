package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;

/**
 * Created by alexey on 10.04.14.
 */
public class JpaContainer2<T> extends JPAContainer<T> {
  /**
   * Creates a new <code>JPAContainer</code> instance for entities of class
   * <code>entityClass</code>. An entity provider must be provided using the
   * {@link #setEntityProvider(com.vaadin.addon.jpacontainer.EntityProvider) }
   * before the container can be used.
   *
   * @param entityClass the class of the entities that will reside in this container
   *                    (must not be null).
   */
  public JpaContainer2(Class<T> entityClass) {
    super(entityClass);
  }
}
