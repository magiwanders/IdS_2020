package primaprovainitinere.test;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import static org.junit.Assert.*;
import primaprovainitinere.src.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CommentTest.class,
                      DiscussionTest.class,
                      CredentialsListTest.class,
                      ServerTest.class
                     })
public class ServerTests {}
