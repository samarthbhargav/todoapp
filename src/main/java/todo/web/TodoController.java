package todo.web;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import spark.Request;
import spark.Response;
import spark.Route;
import todo.dao.SessionDAO;
import todo.dao.TodoDAO;
import todo.dao.UserDAO;
import todo.entity.Todo;
import todo.entity.User;
import todo.except.UserAlreadyExistsException;
import todo.except.UserDoesNotExistException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class TodoController
{
    private final Configuration cfg;
    private TodoDAO todoDAO;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;


    public static void main( String[] args ) throws IOException
    {
        new TodoController();
    }


    public TodoController() throws IOException
    {
        final MongoClient mongoClient = new MongoClient();
        final DB db = mongoClient.getDB( "TodoApp" );

        this.todoDAO = new TodoDAO( db );
        this.userDAO = new UserDAO( db );
        this.sessionDAO = new SessionDAO( db );

        cfg = createFreemarkerConfiguration();
        staticFileLocation( "/public" );
        initializeRoutes();
    }


    abstract class FreemarkerBasedRoute extends Route
    {
        final Template template;


        /**
         * Constructor
         *
         * @param path The route path which is used for matching. (e.g. /hello, users/:name)
         */
        protected FreemarkerBasedRoute( final String path, final String templateName ) throws IOException
        {
            super( path );
            template = cfg.getTemplate( templateName );
        }


        @Override
        public Object handle( Request request, Response response )
        {
            StringWriter writer = new StringWriter();
            try {
                doHandle( request, response, writer );
            } catch ( Exception e ) {
                e.printStackTrace();
                response.redirect( "/internal_error" );
            }
            return writer;
        }


        protected abstract void doHandle( final Request request, final Response response, final Writer writer )
            throws IOException, TemplateException;

    }


    private void initializeRoutes() throws IOException
    {

        get( new FreemarkerBasedRoute( "/", "index.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                String username = getUsername( request );
                SimpleHash h = new SimpleHash();

                if ( username != null ) {
                    h.put( "username", username );
                }
                template.process( h, writer );
            }
        } );

        get( new FreemarkerBasedRoute( "/login", "login.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                String username = getUsername( request );

                // Not Logged in
                if ( username == null ) {
                    template.process( null, writer );
                } else {
                    response.redirect( "/home" );
                }
            }
        } );

        post( new FreemarkerBasedRoute( "/login", "login.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                String username = request.queryParams( "username" );
                SimpleHash h = new SimpleHash();
                if ( username == null || username.length() == 0 ) {
                    h.put( "message", "Username (Email) should be specified" );
                    template.process( h, writer );
                    return;
                }

                String password = request.queryParams( "password" );
                if ( password == null || password.length() < 6 ) {
                    h.put( "message", "Password should be at least 6 characters long" );
                    template.process( h, writer );
                    return;
                }

                System.out.println( "User " + username + " logged in" );

                boolean authenticated = false;
                try {
                    authenticated = userDAO.authenticate( username, password );
                } catch ( UserDoesNotExistException e ) {
                    h.put( "message", "User does not exist" );
                    template.process( h, writer );
                    return;
                }

                if ( !authenticated ) {
                    h.put( "message", "Invalid password" );
                    template.process( h, writer );
                    return;
                }

                String sessionID = sessionDAO.startSession( username );

                response.raw().addCookie( new Cookie( "session", sessionID ) );
                response.redirect( "/home" );
            }
        } );


        get( new FreemarkerBasedRoute( "/logout", "index.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                // End Session on Server
                sessionDAO.endSession( request.cookie( "session" ) );

                // Kill Cookie
                Cookie[] cookies = request.raw().getCookies();
                for ( Cookie c : cookies ) {
                    if ( c.getName().equals( "session" ) ) {
                        c.setMaxAge( 0 );
                        response.raw().addCookie( c );

                    }
                }


                SimpleHash h = new SimpleHash();
                h.put( "message", "Successfully logged out" );
                template.process( h, writer );
            }
        } );


        get( new FreemarkerBasedRoute( "/home", "home.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String username = getUsername( request );
                SimpleHash h = new SimpleHash();
                h.put( "username", username );
                h.put( "todolist", todoDAO.fetchIncompleteTodoForUser( username ) );
                template.process( h, writer );
            }
        } );

        // Routes for Adding a new Task
        get( new FreemarkerBasedRoute( "/add", "add.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                template.process( null, writer );
            }
        } );

        post( new FreemarkerBasedRoute( "/add", "add.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String username = getUsername( request );
                String task = request.queryParams( "task" );
                String tags = request.queryParams( "tags" );

                if ( task == null || task.length() == 0 ) {
                    SimpleHash h = new SimpleHash();
                    h.put( "message", "You need to specify a task" );
                    template.process( h, writer );
                    return;
                }

                if ( tags == null || tags.length() == 0 ) {
                    SimpleHash h = new SimpleHash();
                    h.put( "message", "You need to specify atleast one tag" );
                    template.process( h, writer );
                    return;
                }

                Todo t = new Todo();
                t.setCompleted( false );
                t.setDateCreated( new Date() );
                t.setDescription( task );
                Set<String> tagSet = new HashSet<>();
                for ( String tag : tags.split( "," ) ) {
                    if ( tag.length() != 0 ) {
                        tagSet.add( tag.trim() );
                    }
                }

                if ( tagSet.size() == 0 ) {
                    SimpleHash h = new SimpleHash();
                    h.put( "message", "You need to specify atleast one tag" );
                    template.process( h, writer );
                    return;
                }

                t.setTags( tagSet );
                t.setUser( username );
                todoDAO.addTodo( t );
                response.redirect( "/home" );
            }
        } );

        // Route for viewing completed tasks
        get( new FreemarkerBasedRoute( "/completed", "completed.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String username = getUsername( request );
                SimpleHash h = new SimpleHash();
                h.put( "todolist", todoDAO.fetchCompletedTodoForUser( username ) );
                h.put( "username", username );
                template.process( h, writer );
            }
        } );


        // Route for viewing tasks by tag
        get( new FreemarkerBasedRoute( "/tag/:tag", "tag_task.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String tag = URLDecoder.decode( request.params( ":tag" ), "UTF-8" );
                String username = getUsername( request );
                SimpleHash h = new SimpleHash();
                h.put( "username", username );
                h.put( "tag", tag );
                List<Todo> incomplete = new ArrayList<>();
                List<Todo> complete = new ArrayList<>();
                for ( Todo t : todoDAO.fetchTodoByTagForUser( username, tag ) ) {
                    if ( t.isCompleted() ) {
                        complete.add( t );
                    } else {
                        incomplete.add( t );
                    }
                }
                h.put( "complete", complete );
                h.put( "incomplete", incomplete );
                template.process( h, writer );
            }
        } );

        // Route for completing a task
        post( new FreemarkerBasedRoute( "/complete/:id", "home.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String id = URLDecoder.decode( request.params( ":id" ), "UTF-8" );
                todoDAO.completeTodo( id );
                response.redirect( "/home" );
            }
        } );

        // Route for 'uncompleting' a task
        post( new FreemarkerBasedRoute( "/uncomplete/:id", "home.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                redirectIfNotLoggedIn( request, response );
                String id = URLDecoder.decode( request.params( ":id" ), "UTF-8" );
                todoDAO.uncompleteTodo( id );
                response.redirect( "/home" );
            }
        } );


        get( new FreemarkerBasedRoute( "/signup", "signup.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                template.process( null, writer );
            }
        } );

        // Route for Signing up
        post( new FreemarkerBasedRoute( "/signup", "signup.ftl" ) {

            @Override
            protected void doHandle( Request request, Response response, Writer writer ) throws IOException, TemplateException
            {
                String username = request.queryParams( "username" );
                String password = request.queryParams( "password" );
                String confimPassword = request.queryParams( "conf-password" );


                SimpleHash h = new SimpleHash();
                if ( username == null || username.length() == 0 ) {
                    h.put( "message", "Username (Email) should be specified" );
                    template.process( h, writer );
                    return;
                }

                if ( password == null || password.length() < 6 ) {
                    h.put( "message", "Password should be at least 6 characters long" );
                    template.process( h, writer );
                    return;
                }

                if ( confimPassword == null || !confimPassword.equals( password ) ) {
                    h.put( "message", "Passwords do not match" );
                    template.process( h, writer );
                    return;
                }

                User u = new User();
                u.setUsername( username );
                u.setPassword( password );

                try {
                    userDAO.addUser( u );
                } catch ( UserAlreadyExistsException e ) {
                    h.put( "message", "User already exists" );
                    template.process( h, writer );
                    return;
                }

                // Log them in
                String sessionID = sessionDAO.startSession( username );

                response.raw().addCookie( new Cookie( "session", sessionID ) );
                response.redirect( "/home" );
            }
        } );

    }


    private String getUsername( Request request )
    {
        return sessionDAO.findUserNameBySessionId( request.cookie( "session" ) );
    }


    private void redirectIfNotLoggedIn( Request request, Response response )
    {
        String username = sessionDAO.findUserNameBySessionId( request.cookie( "session" ) );
        if ( username == null ) {
            response.redirect( "/login" );
        }
    }


    private Configuration createFreemarkerConfiguration()
    {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading( TodoController.class, "/freemarker" );
        return retVal;
    }
}
