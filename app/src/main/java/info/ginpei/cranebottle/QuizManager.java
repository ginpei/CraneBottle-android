package info.ginpei.cranebottle;

import java.util.ArrayList;

public class QuizManager {

    protected ArrayList<Quiz> list;
    protected int position = 0;

    public int getPosition() {
        return position;
    }

    public QuizManager() {
        list = new ArrayList<>();
    }

    public int size() {
        return list.size();
    }

    public void load() {
        list.add(new Quiz("これはペンです。", "This is a pen."));
        list.add(new Quiz("これは良いペンです。", "This is a nice pen."));
        list.add(new Quiz("これは昨日買った、良いペンです。", "This is a nice pen which I bought yesterday."));
    }

    public Quiz next() {
        Quiz quiz;
        if (position < list.size()) {
            System.out.println(position);
            quiz = list.get(position);
            position++;
        } else {
            quiz = null;
        }
        return quiz;
    }
}
