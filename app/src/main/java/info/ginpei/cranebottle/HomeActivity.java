package info.ginpei.cranebottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    TextView statusTextView;
    TextView questionTextView;
    TextView userAnswerTextView;

    Speaker speaker;
    Microphone microphone;

    QuizManager manager;
    Quiz currentQuiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusTextView = (TextView) findViewById(R.id.textView_status);
        questionTextView = (TextView) findViewById(R.id.textView_question);
        userAnswerTextView = (TextView) findViewById(R.id.textView_userAnswer);

        findViewById(R.id.button_start).setOnClickListener(view -> {
            start();
        });

        speaker = new Speaker(this, new SpeakerListener());
        microphone = new Microphone(this, new MicrophoneListener());

        manager = new QuizManager();
        load(manager);
    }

    private void load(QuizManager manager) {
        ArrayList<Quiz> quizzes = manager.list;
        quizzes.add(new Quiz(getString(R.string.tmp_q1), "This is a pen."));
        quizzes.add(new Quiz(getString(R.string.tmp_q2), "This is a nice pen."));
        quizzes.add(new Quiz(getString(R.string.tmp_q3), "This is a nice pen which I bought yesterday."));
    }

    @Override
    protected void onResume() {
        super.onResume();

        speaker.setup();
        microphone.setup();
        setStatusText("Preparing...");
    }

    @Override
    protected void onPause() {
        super.onPause();

        speaker.shutdown();
        microphone.shutdown();
    }

    private void start() {
        setStatusText("Picking the next quiz...");

        currentQuiz = manager.next();
        String question = currentQuiz.getQuestion();
        setQuestionText(question);
        speaker.speak(question);
    }

    private void setStatusText(String text) {
        Log.d(TAG, "setStatusText: " + text);
        statusTextView.setText(text);
    }

    private void setQuestionText(String text) {
        questionTextView.setText(text);
    }

    private void setUserAnswerText(String text) {
        userAnswerTextView.setText(text);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public class SpeakerListener implements Speaker.Listener {
        @Override
        public void onProgress(int progressCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (progressCode) {
                        case Speaker.PROGRESS_READY:
                            setStatusText("Hello World!");
                            break;

                        case Speaker.PROGRESS_SPEAKING:
                            setStatusText("Speaking...");
                            break;

                        case Speaker.PROGRESS_DONE:
                            setStatusText("Preparing mic...");
                            microphone.listen();
                            break;
                    }
                }
            });
        }

        @Override
        public void onError(int errorCode) {
            // TODO
        }
    }

    private class MicrophoneListener implements Microphone.Listener {
        @Override
        public void onProgress(int progressCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (progressCode) {
                        case Microphone.PROGRESS_WAITING:
                            setStatusText("OK please speak now...");
                            break;

                        case Microphone.PROGRESS_LISTENING:
                            setStatusText("Listening...");
                            break;

                        case Microphone.PROGRESS_RECOGNIZING:
                            setStatusText("Recognizing...");
                            break;

                        case Microphone.PROGRESS_DONE:
                            setStatusText("Done.");
                            setUserAnswerText(microphone.getResult());
                            break;
                    }
                }
            });
        }

        @Override
        public void onError(int errorCode) {
            switch (errorCode) {
                case Microphone.ERROR_NOT_READY:
                    setStatusText("Error: Microphone is not ready.");
                    break;

                default:
                    setStatusText("Error: Microphone went wrong...");
            }
        }
    }
}
