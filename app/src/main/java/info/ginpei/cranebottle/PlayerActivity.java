package info.ginpei.cranebottle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    public static final String TAG = "PlayerActivity";
    private QuizManager manager;

    private Speaker speaker;
    private Microphone microphone;

    private ListView questionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        manager = new QuizManager();
        load(manager);

        speaker = new Speaker(this, new SpeakerListener());
        microphone = new Microphone(this, new MicrophoneListener());

        questionListView = (ListView) findViewById(R.id.listView_questions);

        ArrayList<Quiz> quizzes = manager.list;
        Log.d(TAG, "size=" + quizzes.size());
        ArrayAdapter<Quiz> quizArrayAdapter = new ArrayAdapter<Quiz>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                quizzes
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Quiz quiz = quizzes.get(position);

                View view = super.getView(position, convertView, parent);

                ((TextView) view.findViewById(android.R.id.text1)).setText(position + quiz.getQuestion());
                Log.d(TAG, position + " " + quiz.getQuestion());
                ((TextView) view.findViewById(android.R.id.text2)).setText(quiz.getAnswer());

                return view;
            }
        };

        questionListView.setAdapter(quizArrayAdapter);
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

    private void load(QuizManager manager) {
        ArrayList<Quiz> quizzes = manager.list;
        for (int i = 0; i < 10; i++) {
            quizzes.add(new Quiz(getString(R.string.tmp_q1), "This is a pen."));
            quizzes.add(new Quiz(getString(R.string.tmp_q2), "This is a nice pen."));
            quizzes.add(new Quiz(getString(R.string.tmp_q3), "This is a nice pen which I bought yesterday."));
        }
    }

    private void setStatusText(String text) {
        Log.d(TAG, "setStatusText: " + text);
//        statusTextView.setText(text);
    }

    private void setUserAnswerText(String text) {
//        userAnswerTextView.setText(text);
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
//                            setUserAnswerText(microphone.getResult());
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
