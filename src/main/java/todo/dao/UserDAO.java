package todo.dao;

import todo.entity.User;
import todo.except.UserAlreadyExistsException;
import todo.except.UserDoesNotExistException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


public class UserDAO
{

    private DBCollection collection;


    private DBObject convertToDBObject( User u )
    {
        // TODO Convert a User object to a DBObject
        // TODO Consider what the _id field should be
        return null;
    }


    public UserDAO( DB db )
    {
        this.collection = db.getCollection( "users" );
    }


    public void addUser( User u ) throws UserAlreadyExistsException
    {
        // TODO add a user User to the collection. If a User already exists, a UserAlreadyExistsException must be thrown
        // TODO DO NOT Store the plaintext password - use PasswordUtil.getSaltedHash() to salt+hash the password and store that
    }


    public boolean authenticate( String username, String password ) throws UserDoesNotExistException
    {
        // TODO Authenticate a user
        // TODO If a user with id "username" does not exist, throw a UserDoesNotExistException
        // You can use the PasswordUtil.check() method to check if password is correct
        return false;
    }

}
