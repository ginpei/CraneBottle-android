package info.ginpei.cranebottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

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
        manager.load();
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
        currentQuiz = manager.next();
        String question = currentQuiz.getQuestion();
        setQuestionText(question);
        speaker.speak(question);
    }

    private void setStatusText(String text) {
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
                            setStatusText("Now your turn.");
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
            }
        }
    }
}
