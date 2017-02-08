package info.ginpei.cranebottle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuizStatusListTest {
    QuizStatusList list;

    @Before
    public void setUp() throws Exception {
        list = new QuizStatusList();
        list.add(new QuizStatus(new Quiz("Question 0", "Answer 0")));
        list.add(new QuizStatus(new Quiz("Question 1", "Answer 1")));
        list.add(new QuizStatus(new Quiz("Question 2", "Answer 2")));
        list.add(new QuizStatus(new Quiz("Question 3", "Answer 3")));
        list.add(new QuizStatus(new Quiz("Question 4", "Answer 4")));
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

}