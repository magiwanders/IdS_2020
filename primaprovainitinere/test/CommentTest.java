// javac -cp .:test/junit-4.13.jar test/CommentTest.java
// java -cp .:test/junit-4.13.jar:test/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.CommentTest
package test;

import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.Comment;

public class CommentTest {

  private String author = "TestAuthor";
  private String comment = "This is a test comment";
  private int vote = 7;

  private Comment comment0;
  private Comment comment1;

  @Before
  public void setUp() {
    comment0 = new Comment("", 0, "");
    comment1 = new Comment(author, vote, comment);
  }

  @Test
  public void getAuthorTest() {
    assertEquals("getAuthor didn't work", author, comment1.getAuthor());
    assertEquals("getAuthor didn't work", "", comment0.getAuthor());
  }

  @Test
  public void getVoteTest() {
    assertEquals("getVote didn't work", vote, comment1.getVote());
    assertEquals("getVote didn't work", 0, comment0.getVote());
  }

  @Test
  public void getBodyTest() {
    assertEquals("getBody didn't work", comment, comment1.getBody());
    assertEquals("getBody didn't work", "", comment0.getBody());
  }

  @Test
  public void toStringTest() {
    assertEquals("toString didn't work", "[" + author + "] " + comment, comment1.toString());
    assertEquals("toString didn't work", "[]", comment0.toString());
  }

}
