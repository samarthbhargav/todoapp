package todo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import todo.entity.Todo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class TodoDAO
{
    private DBCollection collection;


    public TodoDAO( DB db )
    {
        this.collection = db.getCollection( "todo" );
        this.createIndexes();
    }


    private void createIndexes()
    {
        // TODO
    }


    private DBObject convertToDBObject( Todo todo )
    {
        return BasicDBObjectBuilder.start( "user", todo.getUser() ).add( "task", todo.getDescription() )
            .add( "dateCreated", todo.getDateCreated() ).add( "tags", todo.getTags() )
            .add( "dateCompleted", todo.getDateCompleted() ).add( "completed", todo.isCompleted() ).get();
    }


    private Todo convertToTodo( DBObject dbo )
    {
        Todo t = new Todo();
        t.setCompleted( (boolean) dbo.get( "completed" ) );
        t.setDateCompleted( (Date) dbo.get( "dateCompleted" ) );
        t.setDateCreated( (Date) dbo.get( "dateCreated" ) );
        t.setDescription( (String) dbo.get( "task" ) );
        t.setId( dbo.get( "_id" ).toString() );
        BasicDBList tagList = (BasicDBList) dbo.get( "tags" );
        Set<String> tagSet = new HashSet<>();
        for ( Object o : tagList ) {
            tagSet.add( o.toString() );
        }
        t.setTags( tagSet );
        t.setUser( dbo.get( "user" ).toString() );
        return t;
    }


    public void addTodo( Todo todo )
    {
        // TODO the function in TodoDAO called addTodo that adds a Todo        
        this.collection.insert( convertToDBObject( todo ) );
    }


    private void updateField( String id, String field, Object newValue )
    {
        this.collection.update( BasicDBObjectBuilder.start( "_id", new ObjectId( id ) ).get(),
            BasicDBObjectBuilder.start( "$set", new BasicDBObject( field, newValue ) ).get() );
    }


    public void completeTodo( String id )
    {
        this.collection.update( BasicDBObjectBuilder.start( "_id", new ObjectId( id ) ).get(),
            BasicDBObjectBuilder.start( "$set", new BasicDBObject( "completed", true ).append( "dateCompleted", new Date() ) )
                .get() );
    }


    // Bad name. Can't think of anything else, so what can you do?
    public void uncompleteTodo( String id )
    {
        this.updateField( id, "completed", false );
    }


    public List<Todo> fetchCompletedTodoForUser( String user )
    {
        return this.fetchTodoForUserByField( user, "completed", true );
    }


    public List<Todo> fetchIncompleteTodoForUser( String user )
    {
        return this.fetchTodoForUserByField( user, "completed", false );
    }


    public List<Todo> fetchTodoByTagForUser( String user, String tag )
    {
        return this.fetchTodoForUserByField( user, "tags", tag );
    }


    protected List<Todo> fetchTodoForUserByField( String user, String field, Object value )
    {
        DBCursor c = this.collection.find( BasicDBObjectBuilder.start( "user", user ).add( field, value ).get() );
        List<Todo> list = new ArrayList<>();
        for ( DBObject dbo : c ) {
            list.add( convertToTodo( dbo ) );
        }
        return list;
    }
}
