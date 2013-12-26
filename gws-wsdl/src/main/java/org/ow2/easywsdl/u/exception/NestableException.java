package org.ow2.easywsdl.u.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestableException extends Exception implements Nestable {

  /**
   * Required for serialization support.
   *
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 1L;

  /**
   * The helper instance which contains much of the code which we
   * delegate to.
   */
  protected NestableDelegate delegate = new NestableDelegate(this);

  /**
   * Holds the reference to the exception or error that caused
   * this exception to be thrown.
   */
  private Throwable cause = null;


  /**
   * Constructs a new <code>NestableException</code> with specified
   * detail message and nested <code>Throwable</code>.
   *
   * @param msg   the error message
   * @param cause the exception or error that caused this exception to be
   *              thrown
   */
  public NestableException(String msg, Throwable cause) {
    super(msg);
    this.cause = cause;
  }

  /**
   * {@inheritDoc}
   */
  public Throwable getCause() {
    return cause;
  }

  /**
   * Returns the detail message string of this throwable. If it was
   * created with a null message, returns the following:
   * (cause==null ? null : cause.toString()).
   *
   * @return String message string of the throwable
   */
  public String getMessage() {
    if (super.getMessage() != null) {
      return super.getMessage();
    } else if (cause != null) {
      return cause.toString();
    } else {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getMessage(int index) {
    if (index == 0) {
      return super.getMessage();
    }
    return delegate.getMessage(index);
  }

  /**
   * {@inheritDoc}
   */
  public String[] getMessages() {
    return delegate.getMessages();
  }

  /**
   * {@inheritDoc}
   */
  public Throwable getThrowable(int index) {
    return delegate.getThrowable(index);
  }

  /**
   * {@inheritDoc}
   */
  public int getThrowableCount() {
    return delegate.getThrowableCount();
  }

  /**
   * {@inheritDoc}
   */
  public Throwable[] getThrowables() {
    return delegate.getThrowables();
  }

  /**
   * {@inheritDoc}
   */
  public int indexOfThrowable(Class type) {
    return delegate.indexOfThrowable(type, 0);
  }

  /**
   * {@inheritDoc}
   */
  public int indexOfThrowable(Class type, int fromIndex) {
    return delegate.indexOfThrowable(type, fromIndex);
  }

  /**
   * {@inheritDoc}
   */
  public void printStackTrace() {
    delegate.printStackTrace();
  }

  /**
   * {@inheritDoc}
   */
  public void printStackTrace(PrintStream out) {
    delegate.printStackTrace(out);
  }

  /**
   * {@inheritDoc}
   */
  public void printStackTrace(PrintWriter out) {
    delegate.printStackTrace(out);
  }

  /**
   * {@inheritDoc}
   */
  public final void printPartialStackTrace(PrintWriter out) {
    super.printStackTrace(out);
  }

}
