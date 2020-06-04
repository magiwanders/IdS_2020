package primaprovainitinere.test;

import org.junit.*;
import static org.junit.Assert.*;
import primaprovainitinere.src.*;
import java.util.*;

public class CredentialsListTest {

  private CredentialsList emptyList;

  private String user1 = "Giorgio";
  private String psw1 = "giorgiopassword";
  private String user2 = "Piero";
  private String psw2 = "pieropassword";

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
