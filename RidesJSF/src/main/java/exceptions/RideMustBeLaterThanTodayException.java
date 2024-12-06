package exceptions;
public class RideMustBeLaterThanTodayException extends Exception {
 
 public RideMustBeLaterThanTodayException(String message)
  {
    super(message);
  }
  /**This exception is triggered if the event has already finished
  *@param s String of the exception
  */
  public RideMustBeLaterThanTodayException(String message, Throwable cause)
  {
    super(message,cause);
  }
}