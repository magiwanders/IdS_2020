package primaprovainitinere.test;

import org.junit.*;
import static org.junit.Assert.*;
import primaprovainitinere.src.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CommentTest.class,
                      DiscussionTest.class,
                      CredentialsListTest.class,
                      ServerTest.class
                     })
public class ServerTests {}
