package todo.dao;

import java.util.List;

import todo.entity.Todo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
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
    {}


    private DBObject convertToDBObject( Todo todo )
    {
        // TODO Convert to DBObject and return it
        return null;
    }


    private Todo convertToTodo( DBObject dbo )
    {
        // TODO Convert dbo to a Todo object and return it
        return null;
    }


    public void addTodo( Todo todo )
    {
        // TODO the function in TodoDAO called addTodo that adds a Todo        
    }


    public void completeTodo( String id )
    {
        // TODO Set the complete flag to true for todo with id=id
    }


    public void uncompleteTodo( String id )
    {
        // TODO Set the compelte flag to false for todo with id=id
    }


    public List<Todo> fetchCompletedTodoForUser( String user )
    {
        // TODO Fetch all todo's with user which are complete
        return null;
    }


    public List<Todo> fetchIncompleteTodoForUser( String user )
    {
        // TODO Fetch all todo's for user which are incomplete
        return null;
    }


    public List<Todo> fetchTodoByTagForUser( String user, String tag )
    {
        // TODO Fetch all todo's for user 
        return null;
    }
}
