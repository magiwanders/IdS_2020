package test;

import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.CredentialsList;
import java.util.*;

public class CredentialsListTest {

  CredentialsList emptyList;

  String user1 = "Giorgio";
  String psw1 = "giorgiopassword";
  String user2 = "Piero";
  String psw2 = "pieropassword";

  @Before
  public void setUp() {
    emptyList = new CredentialsList(false);
  }

  @Test
  public void loginAttemptEmptyTest() {
    assertTrue(emptyList.loginAttempt(user1, psw1)); // New User
    assertFalse(emptyList.loginAttempt(user1, psw1)); // Already logged
    emptyList.logout(user1);
    assertFalse(emptyList.loginAttempt(user1, psw2)); // Wrong password
    assertTrue(emptyList.loginAttempt(user2, psw2)); // New user not empty
    assertTrue(emptyList.loginAttempt(user1, psw1)); // OldUser
    assertFalse(emptyList.loginAttempt(user2, psw2)); // Already logged
  }

}
