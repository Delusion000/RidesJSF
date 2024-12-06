package exceptions;

public class RideAlreadyExistException extends Exception {
 
 
 public RideAlreadyExistException(String message)
  {
    super(message);
  }
  /**This exception is triggered if the question already exists 
  *@param s String of the exception
  */
  public RideAlreadyExistException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
