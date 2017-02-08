package info.ginpei.cranebottle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuizManagerTest {
    QuizManager manager;

    @Before
    public void setUp() throws Exception {
        manager = new QuizManager();
//        manager.load();
    }

    @Test
    public void next() throws Exception {
        Quiz quiz;
        assertEquals(0, manager.getPosition());
        assertEquals(3, manager.size());

        quiz = manager.next();
        assertNotNull(quiz);
        assertEquals(1, manager.getPosition());

        quiz = manager.next();
        assertNotNull(quiz);
        assertEquals(2, manager.getPosition());

        quiz = manager.next();
        assertNotNull(quiz);
        assertEquals(3, manager.getPosition());

        quiz = manager.next();
        assertNull(quiz);
        assertEquals(3, manager.getPosition());
    }
}