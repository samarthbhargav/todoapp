package todo.dao;

import todo.entity.User;
import todo.except.UserAlreadyExistsException;
import todo.except.UserDoesNotExistException;
import todo.util.PasswordUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;


public class UserDAO
{

    private DBCollection collection;


    private DBObject convertToDBObject( User u )
    {
        return BasicDBObjectBuilder.start( "_id", u.getUsername() ).add( "pass", PasswordUtil.getSaltedHash( u.getPassword() ) )
            .get();
    }


    public UserDAO( DB db )
    {
        this.collection = db.getCollection( "users" );
    }


    public void addUser( User u ) throws UserAlreadyExistsException
    {
        try {
            this.collection.insert( convertToDBObject( u ) );
        } catch ( DuplicateKeyException e ) {
            throw new UserAlreadyExistsException();
        }
    }


    public boolean authenticate( String username, String password ) throws UserDoesNotExistException
    {
        DBObject obj = this.collection.findOne( new BasicDBObject( "_id", username ) );

        if ( obj == null ) {
            throw new UserDoesNotExistException();
        }

        if ( !PasswordUtil.check( password, obj.get( "pass" ).toString() ) ) {
            return false;
        }

        return true;
    }

}
