// javac -cp junit-4.13.jar CommentTest.java
// java -cp junit-4.13.jar;hamcrest-core-1.3.jar;. org.junit.runner.JUnitCore CommentTest

import org.junit.*;
import primaprovainitinere.Comment;

public class CommentTest {

  private String author = "TestAuthor";
  private String comment = "This is a test comment";
  private int vote = 7;

  @Before
  public void setUp() {
    Comment comment111 = new Comment(author, vote, comment);
    Comment comment011 = new Comment("", vote, comment);
    Commetn comment101 = new Comment(author, 0, comment);
  }

  @Test
  public void getAuthorTest() {
    assertEquals("TestAuthor", comment111.getAuthor());
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
