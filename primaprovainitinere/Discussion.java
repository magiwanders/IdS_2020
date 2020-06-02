import java.io.*;
import java.util.*;
import java.net.*;

public class Discussion
{
    private String author;
    private String title;
    private Vector<Comment> comments;

    private final static boolean usersCanLeaveMultipleComments = true;

    public Discussion(String author, String title)
    {
        this.author = author;
        this.title = title;
        this.comments = new Vector<Comment>();
    }

    synchronized public String getAuthor()
    {
        return author;
    }

    synchronized public String getTitle()
    {
        return title;
    }

    synchronized public int getNumberOfComments()
    {
        return comments.size();
    }

    synchronized public Comment getComment(int indexOfComment)
    {
        return comments.elementAt(indexOfComment);
    }

    synchronized public void leaveComment(String username, int vote, String comment)
    {
        comments.add(new Comment(username, vote, comment));
    }

    synchronized public float getAverageOfVotes()
    {
        float sum = 0;
        for(int i=0; i<comments.size(); i++)
        {
            sum += (float)comments.elementAt(i).getVote();
        }
        return sum/comments.size();
    }

    synchronized public String toString()
    {
        return "Voto: "
             + getAverageOfVotes()
             + " | Di: "
             + author
             + ". '"
             + title
             + "'";
    }

}
