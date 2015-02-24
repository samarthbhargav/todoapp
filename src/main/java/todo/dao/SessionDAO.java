package todo.dao;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


public class SessionDAO
{
    private final DBCollection collection;


    public SessionDAO( final DB db )
    {
        this.collection = db.getCollection( "session" );
    }


    public String findUserNameBySessionId( String sessionId )
    {
        if ( sessionId == null ) {
            return null;
        }
        DBObject session = this.collection.findOne( new BasicDBObject( "_id", sessionId ) );

        if ( session == null ) {
            return null;
        } else {
            return session.get( "username" ).toString();
        }
    }


    public String startSession( String username )
    {

        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes( randomBytes );

        String sessionID = new String( Base64.encodeBase64( randomBytes ) );

        BasicDBObject session = new BasicDBObject( "username", username );

        session.append( "_id", sessionID );

        this.collection.insert( session );

        return session.getString( "_id" );
    }


    public void endSession( String sessionID )
    {
        this.collection.remove( new BasicDBObject( "_id", sessionID ) );
    }
}
