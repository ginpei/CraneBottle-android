package info.ginpei.cranebottle;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuizTest {
    @Test
    public void isCorrect() throws Exception {
        Quiz quiz = new Quiz("This is a question.", "This is an answer.");
        assertFalse(quiz.isCorrect("This is an answer"));
        assertTrue(quiz.isCorrect("This is an answer."));
    }
}