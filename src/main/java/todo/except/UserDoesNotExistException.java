/**
 * 
 */
package todo.except;


public class UserDoesNotExistException extends Exception
{


    private static final long serialVersionUID = 1760456791497623313L;


    /**
     * 
     */
    public UserDoesNotExistException()
    {}


    /**
     * @param message
     */
    public UserDoesNotExistException( String message )
    {
        super( message );
    }


    /**
     * @param cause
     */
    public UserDoesNotExistException( Throwable cause )
    {
        super( cause );
        // TODO Auto-generated constructor stub
    }


    /**
     * @param message
     * @param cause
     */
    public UserDoesNotExistException( String message, Throwable cause )
    {
        super( message, cause );
    }


    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public UserDoesNotExistException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
