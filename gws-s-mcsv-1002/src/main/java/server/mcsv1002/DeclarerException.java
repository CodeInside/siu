package server.mcsv1002;


public class DeclarerException extends Exception{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int code;
  private String message;

  public DeclarerException(int code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
