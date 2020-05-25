import java.io.*;
import java.util.*;
import java.net.*;

public class Comment
{
    private String author;
    private String comment;
    private int vote;

    public Comment(String author, int vote, String comment)
    {
        this.author = author;
        this.comment = comment;
        this.vote = vote;
    }

    synchronized public int getVote()
    {
        return vote;
    }

    synchronized public String getBody()
    {
        return comment;
    }

    synchronized public String getAuthor()
    {
        return author;
    }

}
