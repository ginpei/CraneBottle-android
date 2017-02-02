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
