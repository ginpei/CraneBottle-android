package info.ginpei.cranebottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView statusTextView;
    TextView questionTextView;

    Speaker speaker;

    QuizManager manager;
    Quiz currentQuiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusTextView = (TextView) findViewById(R.id.textView_status);
        questionTextView = (TextView) findViewById(R.id.textView_question);

        findViewById(R.id.button_start).setOnClickListener(view -> {
            start();
        });

        speaker = new Speaker(this, new SpeakerListener());

        manager = new QuizManager();
        manager.load();
    }

    @Override
    protected void onResume() {
        super.onResume();

        speaker.setup();
        setStatusText("Preparing...");
    }

    @Override
    protected void onPause() {
        super.onPause();

        speaker.shutdown();
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
}
