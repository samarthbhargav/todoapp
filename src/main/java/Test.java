import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;


@SuppressWarnings ( "unused")
public class Test
{

    final DBCollection collection;


    public Test() throws UnknownHostException
    {
        this.collection = new MongoClient().getDB( "TestDB" ).getCollection( "Student" );
        this.collection.drop();
    }


    public void testAll() throws UnknownHostException
    {
        //this.sampleDBObject();
        //this.nestedDBObject();
        //this.dbObjectUsingMap();
        //this.dbObjectUsingBuilder();
        //this.establishConnectionExample();
        this.insertExample();
        //this.duplicateKeyExample();
        //this.emptyFindExampleWithIterator();
        //this.emptyFindExampleWithForLoop();
        //this.findExample();
        //this.findWithProjectionExample();
        //this.findWithQueryBuilderExample();
        //this.findOneExample();
        //this.updateExample();
        this.updateWithOptionsExample();
        //pojoExample();
    }


    private void printAll()
    {
        for ( DBObject obj : this.collection.find() ) {
            System.out.println( obj );
        }
    }


    private void sampleDBObject()
    {
        BasicDBObject b = new BasicDBObject(); // Empty {}
        System.out.println( b );
        BasicDBObject b1 = new BasicDBObject( "key", "value" ); // Creates {“key” : “value”}
        // You can append to old objects
        b.append( "key", "value" ); // Now b is also {“key” : “value”}
        b1.append( "Other key", 1.0 );
        System.out.println( b );
        System.out.println( b1 );

    }


    private void nestedDBObject()
    {
        BasicDBObject b2 = new BasicDBObject( "key", new BasicDBObject( "nested-key", "value" ) );
        System.out.println( b2 );
    }


    private void dbObjectUsingMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "key1", "value1" );
        map.put( "pi", Math.PI );
        BasicDBObject bmap = new BasicDBObject( map );
        System.out.println( bmap );
    }


    private void dbObjectUsingBuilder()
    {
        DBObject b = BasicDBObjectBuilder.start( "key1", "value1" ).add( "key2", Math.PI )
            .add( "nested", new BasicDBObject( "nested-key", "nested-value" ) ).get();
        System.out.println( b );

    }


    private void establishConnectionExample() throws UnknownHostException
    {
        // Establish a connection
        MongoClient mongoClient = new MongoClient();
        // Select a db
        DB db = mongoClient.getDB( "TestDatabase" );
        // Select a collection
        DBCollection testCollection = db.getCollection( "TestCollection" );
        System.out.println( testCollection.find() );
    }


    private void insertExample()
    {
        //DBObject suresh = BasicDBObjectBuilder.start( "name", "Suresh" ).add( "branch", "CSE" ).add( "semester", 4 ).get();
        DBObject suresh = new BasicDBObject();
        suresh.put( "name", "Suresh" );
        suresh.put( "semester", 4 );
        suresh.put( "branch", "CSE" );
        collection.insert( suresh );
    }


    private void duplicateKeyExample()
    {
        DBObject suresh = BasicDBObjectBuilder.start( "_id", "Suresh" ).add( "branch", "CSE" ).add( "semester", 4 ).get();
        DBObject sameId = BasicDBObjectBuilder.start( "_id", "Suresh" ).add( "branch", "CSE" ).add( "semester", 4 ).get();
        collection.insert( suresh );
        try {
            collection.insert( sameId );
        } catch ( DuplicateKeyException e ) {
            System.out.println( "Duplicate Key Inserted" );
        }
    }


    private void emptyFindExampleWithIterator()
    {
        DBCursor cursor = collection.find();
        try {
            while ( cursor.hasNext() ) {
                DBObject o = cursor.next();
                System.out.println( o );
            }
        } finally {
            cursor.close();
        }
    }


    private void emptyFindExampleWithForLoop()
    {
        for ( DBObject o : collection.find() ) {
            System.out.println( o );
        }
    }


    private void findExample()
    {
        DBObject query = new BasicDBObject( "name", "Suresh" );
        DBCursor cursor = collection.find( query );
        for ( DBObject o : cursor ) {
            System.out.println( "Found: " + o );
        }
    }


    private void findWithProjectionExample()
    {
        DBObject query = new BasicDBObject( "name", "Suresh" );
        DBObject projection = new BasicDBObject( "name", 1 ).append( "_id", 0 );
        DBCursor cursor = collection.find( query, projection );
        for ( DBObject o : cursor ) {

            System.out.println( "Found: " + o.get( "_id" ) );
        }
    }


    private void findWithQueryBuilderExample()
    {
        DBObject query = QueryBuilder.start( "semester" ).greaterThanEquals( 4 ).and( "name" ).is( "Suresh" ).get();
        System.out.println( "Query: " + query );
        DBCursor cursor = collection.find( query );
        for ( DBObject o : cursor ) {
            System.out.println( "Found: " + o );
        }
    }


    private void findOneExample()
    {
        DBObject query = new BasicDBObject( "name", "Suresh" );
        DBObject o1 = collection.findOne( query );
        DBObject query2 = new BasicDBObject( "name", "Ramesh" );
        DBObject o2 = collection.findOne( query2 );
        System.out.println( o1 + "\n" + o2 );
    }


    private void updateExample()
    {
        DBObject query = new BasicDBObject( "name", "Suresh" );
        DBObject set = new BasicDBObject( "$set", new BasicDBObject( "name", "Suresh Kumar" ) );
        System.out.println( set );

        collection.update( query, set );
        printAll();
    }


    private void updateWithOptionsExample()
    {
        // upsert
        DBObject query = new BasicDBObject( "name", "Ramesh" );
        DBObject set = new BasicDBObject( "name", "Ramesh Kumar" ).append( "branch", "CSE" ).append( "semester", 4 );
        collection.update( query, set, true, false );
        printAll();

        // multi
        System.out.println();
        DBObject incrementSemester = new BasicDBObject( "$inc", new BasicDBObject( "semester", 1 ) );
        collection.update( new BasicDBObject(), incrementSemester, false, true );
        printAll();
    }


    private void removeExample()
    {

        // Remove 
        collection.remove( new BasicDBObject( "name", "Suresh" ) );

        // Remove All         
        collection.remove( new BasicDBObject() );
    }


    private void dropCollectionExample()
    {
        collection.drop();
    }


    private void distinctExample()
    {
        System.out.println( collection.distinct( "branch" ) );
    }


    private void countExample()
    {
        collection.count();
        collection.count( new BasicDBObject( "semester", 4 ) );
    }


    private static class Student
    {
        private String USN;
        private String name;
        private String branch;
        private int semester;
        private String[] classes;


        /**
         * @return the uSN
         */
        public String getUSN()
        {
            return USN;
        }


        /**
         * @param uSN the uSN to set
         */
        public void setUSN( String uSN )
        {
            USN = uSN;
        }


        /**
         * @return the name
         */
        public String getName()
        {
            return name;
        }


        /**
         * @param name the name to set
         */
        public void setName( String name )
        {
            this.name = name;
        }


        /**
         * @return the branch
         */
        public String getBranch()
        {
            return branch;
        }


        /**
         * @param branch the branch to set
         */
        public void setBranch( String branch )
        {
            this.branch = branch;
        }


        /**
         * @return the semester
         */
        public int getSemester()
        {
            return semester;
        }


        /**
         * @param semester the semester to set
         */
        public void setSemester( int semester )
        {
            this.semester = semester;
        }


        /**
         * @return the classes
         */
        public String[] getClasses()
        {
            return classes;
        }


        /**
         * @param classes the classes to set
         */
        public void setClasses( String[] classes )
        {
            this.classes = classes;
        }


        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "Student [USN=" + USN + ", name=" + name + ", branch=" + branch + ", semester=" + semester + ", classes="
                + Arrays.toString( classes ) + "]";
        }


    }


    private void pojoExample() throws UnknownHostException
    {
        Student s = new Student();
        s.setUSN( "1BE14IS052" );
        s.setBranch( "ISE" );
        s.setSemester( 3 );
        s.setName( "Samarth" );
        String[] classes = { "Data Structures", "Algoritms", "Convex Optimization" };
        s.setClasses( classes );

        System.out.println( "Student: " + s );
        // TODO Convert a Pojo to a DBObject
        DBObject dbo = BasicDBObjectBuilder.start( "_id", s.getUSN() ).append( "branch", s.getBranch() )
            .append( "name", s.getName() ).append( "classes", s.getClasses() ).append( "semester", s.getSemester() ).get();
        System.out.println( "The DBObject: " + dbo );

        // TODO Insert it
        DBCollection studentCollection = new MongoClient().getDB( "TestDB" ).getCollection( "VTUStudent" );
        studentCollection.insert( dbo );


        // TODO Read it back
        DBObject o = studentCollection.findOne( new BasicDBObject( "_id", s.getUSN() ) );
        System.out.println( "Retrieved DBObject: " + o );

        Student retrieved = new Student();
        retrieved.setUSN( (String) o.get( "_id" ) );
        retrieved.setBranch( (String) o.get( "branch" ) );
        retrieved.setName( (String) o.get( "name" ) );
        retrieved.setSemester( (int) o.get( "semester" ) );
        retrieved.setClasses( (String[]) o.get( "classes" ) );

        System.out.println( "The Retrieved Student: " + retrieved );
    }


    public static void main( String[] args ) throws UnknownHostException
    {
        new Test().testAll();
    }
}
