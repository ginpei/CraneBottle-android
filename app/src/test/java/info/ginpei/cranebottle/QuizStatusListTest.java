package info.ginpei.cranebottle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuizStatusListTest {
    QuizStatusList list;

    @Before
    public void setUp() throws Exception {
        list = new QuizStatusList();
        list.add(new Quiz("Question 0", "Answer 0"));
        list.add(new Quiz("Question 1", "Answer 1"));
        list.add(new Quiz("Question 2", "Answer 2"));
        list.add(new Quiz("Question 3", "Answer 3"));
        list.add(new Quiz("Question 4", "Answer 4"));
    }

    @Test
    public void add() throws Exception {
        // add a Quiz instead of QuizStatus
        boolean succeeded = list.add(new Quiz("My Question", "My Answer"));
        QuizStatus status = list.get(list.size() - 1);
        Quiz quiz = status.getQuiz();
        assertTrue(succeeded);
        assertEquals("My Question", quiz.getQuestion());
        assertEquals("My Answer", quiz.getAnswer());
    }

    @Test
    public void moveOnToNext() throws Exception {
        // get the first quiz
        Quiz firstQuiz = list.moveOnToNext();
        assertEquals(0, list.currentPosition);
        assertEquals("Question 0", firstQuiz.getQuestion());
        assertEquals("Answer 0", firstQuiz.getAnswer());

        // then fetches the next
        Quiz nextQuiz = list.moveOnToNext();
        assertEquals(1, list.currentPosition);
        assertEquals("Question 1", nextQuiz.getQuestion());
        assertEquals("Answer 1", nextQuiz.getAnswer());

        // flow to the last
        list.moveOnToNext();
        list.moveOnToNext();
        list.moveOnToNext();
        assertEquals(4, list.currentPosition);

        // after fetching the last
        Quiz overQuiz = list.moveOnToNext();
        assertEquals(4, list.currentPosition);
        assertNull(overQuiz);
    }

    @Test
    public void getCurrentQuiz() throws Exception {
        // in the initial
        assertNull(list.getCurrentQuiz());

        // the first one
        list.moveOnToNext();
        Quiz firstQuiz = list.getCurrentQuiz();
        assertEquals("Question 0", firstQuiz.getQuestion());
        assertEquals("Answer 0", firstQuiz.getAnswer());
    }

    @Test
    public void getQuiz() throws Exception {
        Quiz quiz4 = list.getQuiz(4);
        assertEquals("Question 4", quiz4.getQuestion());
        assertEquals("Answer 4", quiz4.getAnswer());

        // out of bounds
        assertNull(list.getQuiz(-1));
        assertNull(list.getQuiz(5));
    }

}