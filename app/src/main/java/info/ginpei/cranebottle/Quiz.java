package info.ginpei.cranebottle;

public class Quiz {
    protected String question;
    protected String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Quiz(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public boolean isCorrect(String answer) {
        return answer.equals(this.answer);
    }
}
