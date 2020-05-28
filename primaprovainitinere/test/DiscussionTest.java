package test;

import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.Comment;
import src.primaprovainitinere.Discussion;
import java.util.*;

public class DiscussionTest {

  String author1 = "Giovanni";
  String author2 = "Marco";
  String title1 = "Notizia generica prima.";
  String title2 = "Articolo generico secondo.";

  Discussion discussion1;
  Discussion discussion2;

  @Before
  public void setUp() {
    discussion1 = new Discussion(author1, title1);
    discussion2 = new Discussion(author2, title2);
  }

  @Test
  public void getAuthorTest() {
    assertEquals("getAuthor() failed", author1, discussion1.getAuthor());
    assertEquals("getAuthor() failed", author2, discussion2.getAuthor());
  }

  @Test
  public void getTitleTest() {
    assertEquals("getTitle() failed", title1, discussion1.getTitle());
    assertEquals("getTitle() failed", title2, discussion2.getTitle());
  }

  @Test
  public void zeroCommentsTest() {
    assertEquals("Discussion1 failed zero comments test.", 0, discussion1.getNumberOfComments());
    assertEquals(discussion1.getNumberOfComments(), discussion2.getNumberOfComments());
    try {
      discussion1.getComment(-1);
      fail("getComment() failed throwing ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      assertNotNull(e);
    }
    try {
      discussion1.getComment(0);
      fail("getComment() failed throwing ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void commentsTest() {
    Comment comment1 = new Comment("Samuele", 5, "Commento di Samuele.");
    Comment comment2 = new Comment("Gianni", 6, "Gianni ha commmentato.");

    discussion1.leaveComment("Samuele", 5, "Commento di Samuele.");
    assertEquals("First commment on discussion1", 1, discussion1.getNumberOfComments());

    try {
      discussion1.getComment(0);
    } catch (ArrayIndexOutOfBoundsException e) {
      fail("getComment() threw an unjustified ArrayIndexOutOfBoundsException");
    }
    try {
      discussion1.getComment(1);
      fail("getComment() failed throwing ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      assertNotNull(e);
    }

    discussion1.leaveComment("Gianni", 6, "Gianni ha commmentato.");
    assertEquals("Second commment on discussion1", 2, discussion1.getNumberOfComments());

    discussion2.leaveComment(discussion1.getComment(1).getAuthor(), discussion1.getComment(1).getVote(), discussion1.getComment(1).getBody());
    discussion2.leaveComment(discussion1.getComment(0).getAuthor(), discussion1.getComment(0).getVote(), discussion1.getComment(0).getBody());

    assertEquals("Comment got corrupted", comment1.toString(), discussion1.getComment(0).toString());
    assertEquals("Comment got corrupted", comment1.toString(), discussion2.getComment(1).toString());
    assertEquals("Comment got corrupted", discussion2.getComment(0).toString(), discussion1.getComment(1).toString());

  }

}
