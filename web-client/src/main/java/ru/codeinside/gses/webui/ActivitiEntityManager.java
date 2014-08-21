package ru.codeinside.gses.webui;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.Map;

final public class ActivitiEntityManager implements EntityManager, Serializable {

  public static  final EntityManager INSTANCE = new ActivitiEntityManager();
  @Override
  public void persist(Object o) {
   getEm().persist(o);
  }

  private EntityManager getEm() {
    return Flash.flash().getEm();
  }

  @Override
  public <T> T merge(T t) {
    return getEm().merge(t);
  }

  @Override
  public void remove(Object o) {
    getEm().remove(o);
  }

  @Override
  public <T> T find(Class<T> tClass, Object o) {
    return getEm().find(tClass, o);
  }

  @Override
  public <T> T find(Class<T> tClass, Object o, Map<String, Object> stringObjectMap) {
    return getEm().find(tClass, o, stringObjectMap);
  }

  @Override
  public <T> T find(Class<T> tClass, Object o, LockModeType lockModeType) {
    return getEm().find(tClass, o, lockModeType);
  }

  @Override
  public <T> T find(Class<T> tClass, Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
    return getEm().find(tClass, o, lockModeType, stringObjectMap);
  }

  @Override
  public <T> T getReference(Class<T> tClass, Object o) {
    return getEm().getReference(tClass, o);
  }

  @Override
  public void flush() {
    getEm().flush();
  }

  @Override
  public void setFlushMode(FlushModeType flushModeType) {
    getEm().setFlushMode(flushModeType);
  }

  @Override
  public FlushModeType getFlushMode() {
    return getEm().getFlushMode();
  }

  @Override
  public void lock(Object o, LockModeType lockModeType) {
    getEm().lock(o, lockModeType);
  }

  @Override
  public void lock(Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
    getEm().lock(o, lockModeType, stringObjectMap);
  }

  @Override
  public void refresh(Object o) {
    getEm().refresh(o);
  }

  @Override
  public void refresh(Object o, Map<String, Object> stringObjectMap) {
    getEm().refresh(o, stringObjectMap);
  }

  @Override
  public void refresh(Object o, LockModeType lockModeType) {
    getEm().refresh(o, lockModeType);
  }

  @Override
  public void refresh(Object o, LockModeType lockModeType, Map<String, Object> stringObjectMap) {
    getEm().refresh(o, lockModeType, stringObjectMap);
  }

  @Override
  public void clear() {
    getEm().clear();
  }

  @Override
  public void detach(Object o) {
    getEm().detach(o);
  }

  @Override
  public boolean contains(Object o) {
    return getEm().contains(o);
  }

  @Override
  public LockModeType getLockMode(Object o) {
    return getEm().getLockMode(o);
  }

  @Override
  public void setProperty(String s, Object o) {
    getEm().setProperty(s, o);
  }

  @Override
  public Map<String, Object> getProperties() {
    return getEm().getProperties();
  }

  @Override
  public Query createQuery(String s) {
    return getEm().createQuery(s);
  }

  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
    return getEm().createQuery(tCriteriaQuery);
  }

  @Override
  public <T> TypedQuery<T> createQuery(String s, Class<T> tClass) {
    return getEm().createQuery(s, tClass);
  }

  @Override
  public Query createNamedQuery(String s) {
    return getEm().createNamedQuery(s);
  }

  @Override
  public <T> TypedQuery<T> createNamedQuery(String s, Class<T> tClass) {
    return getEm().createNamedQuery(s, tClass);
  }

  @Override
  public Query createNativeQuery(String s) {
    return getEm().createNativeQuery(s);
  }

  @Override
  public Query createNativeQuery(String s, Class aClass) {
    return getEm().createNativeQuery(s, aClass);
  }

  @Override
  public Query createNativeQuery(String s, String s2) {
    return getEm().createNativeQuery(s, s2);
  }

  @Override
  public void joinTransaction() {
    getEm().joinTransaction();
  }

  @Override
  public <T> T unwrap(Class<T> tClass) {
    return getEm().unwrap(tClass);
  }

  @Override
  public Object getDelegate() {
    return getEm().getDelegate();
  }

  @Override
  public void close() {
    getEm().getDelegate();
  }

  @Override
  public boolean isOpen() {
    return getEm().isOpen();
  }

  @Override
  public EntityTransaction getTransaction() {
    return getEm().getTransaction();
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return getEm().getEntityManagerFactory();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return getEm().getCriteriaBuilder();
  }

  @Override
  public Metamodel getMetamodel() {
    return getEm().getMetamodel();
  }
}
