package todo.entity;

import java.util.Date;
import java.util.Set;


public class Todo
{
    private String id;
    private String user;
    private String description;
    private Date dateCreated;
    private Date dateCompleted;
    private Set<String> tags;
    private boolean isCompleted;


    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * @param description the description to set
     */
    public void setDescription( String description )
    {
        this.description = description;
    }


    /**
     * @return the dateCreated
     */
    public Date getDateCreated()
    {
        return dateCreated;
    }


    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated( Date dateCreated )
    {
        this.dateCreated = dateCreated;
    }


    /**
     * @return the tags
     */
    public Set<String> getTags()
    {
        return tags;
    }


    /**
     * @param tags the tags to set
     */
    public void setTags( Set<String> tags )
    {
        this.tags = tags;
    }


    /**
     * @return the isCompleted
     */
    public boolean isCompleted()
    {
        return isCompleted;
    }


    /**
     * @param isCompleted the isCompleted to set
     */
    public void setCompleted( boolean isCompleted )
    {
        this.isCompleted = isCompleted;
    }


    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }


    /**
     * @param user the user to set
     */
    public void setUser( String user )
    {
        this.user = user;
    }


    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId( String id )
    {
        this.id = id;
    }


    /**
     * @return the dateCompleted
     */
    public Date getDateCompleted()
    {
        return dateCompleted;
    }


    /**
     * @param dateCompleted the dateCompleted to set
     */
    public void setDateCompleted( Date dateCompleted )
    {
        this.dateCompleted = dateCompleted;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Todo [id=" + id + ", user=" + user + ", description=" + description + ", dateCreated=" + dateCreated
            + ", tags=" + tags + ", isCompleted=" + isCompleted + "]";
    }


}
