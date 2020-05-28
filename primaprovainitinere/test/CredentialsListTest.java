package test;

import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.Comment;
import src.primaprovainitinere.Discussion;
import java.util.*;

public class CredentialsListTest {

  CredentialsList dummyUsers;
  CredentialsList emptyList;

  @Before
  public void setUp() {
    emptyList = new CredentialsList(false);
  }

  @Test
  public void emptyTest() {
    
  }

  @Test
  public void loginAttemptTest() {
    emptyList.loginAttempt("user1", "password1");
  }
}
