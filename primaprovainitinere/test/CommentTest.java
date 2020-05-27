// javac -cp .:test/junit-4.13.jar test/CommentTest.java
// java -cp ..:junit-4.13.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore CommentTest
package test;

import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.Comment;

public class CommentTest {

  private String author = "TestAuthor";
  private String comment = "This is a test comment";
  private int vote = 7;

  private Comment comment111;

  @Before
  public void setUp() {
    comment111 = new Comment(author, vote, comment);
    Comment comment011 = new Comment("", vote, comment);
    Comment comment101 = new Comment(author, 0, comment);
  }

  @Test
  public void getAuthorTest() {
    assertEquals("TestAuthor didn't work", "TestAuthor", comment111.getAuthor());
  }
/*
  @Test
  public void getVoteTest() {

  }

  @Test
  public void getCommentTest() {

  }

  @Test
  public void toStringTest() {

  }
  */
}
