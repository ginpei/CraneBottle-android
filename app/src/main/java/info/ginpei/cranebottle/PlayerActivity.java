package info.ginpei.cranebottle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    public static final String TAG = "G#PlayerActivity";
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

        findViewById(R.id.toggleButton_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton button = (ToggleButton) view;
                boolean checked = button.isChecked();
                if (checked) {
                    play();
                } else {
                    pause();
                }
            }
        });

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
        Log.d(TAG, "onResume()");
        super.onResume();

        speaker.setup();
        microphone.setup();
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

    private void play() {
        speaker.speak("Hello world!", () -> {
            runOnUiThread(() -> {
                Log.d(TAG, "Called back from speaker!");
                microphone.listen(() -> {
                    Log.d(TAG, "Called back from mic and the result is: " + microphone.result);
                });
            });
        });
    }

    private void pause() {
        Log.d(TAG, "pause");
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
                            Log.d(TAG, "speaker:ready");
                            break;

                        case Speaker.PROGRESS_SPEAKING:
                            Log.d(TAG, "speaker:speaking");
                            break;

                        case Speaker.PROGRESS_DONE:
                            Log.d(TAG, "speaker:done");
                            break;
                    }
                }
            });
        }

        @Override
        public void onError(int errorCode) {
            switch (errorCode) {
                case Speaker.ERROR_NOT_READY:
                    Log.d(TAG, "speaker:error: Not ready.");
                    break;

                default:
                    Log.d(TAG, "speaker:error: other: " + errorCode);
            }
        }
    }

    private class MicrophoneListener implements Microphone.Listener {
        @Override
        public void onProgress(int progressCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (progressCode) {
                        case Microphone.PROGRESS_READY:
                            Log.d(TAG, "mic:ready");
                            break;

                        case Microphone.PROGRESS_WAITING:
                            Log.d(TAG, "mic:waiting");
                            break;

                        case Microphone.PROGRESS_LISTENING:
                            Log.d(TAG, "mic:listening");
                            break;

                        case Microphone.PROGRESS_RECOGNIZING:
                            Log.d(TAG, "mic:recognizing");
                            break;

                        case Microphone.PROGRESS_DONE:
                            Log.d(TAG, "mic:done");
                            break;
                    }
                }
            });
        }

        @Override
        public void onError(int errorCode) {
            switch (errorCode) {
                case Microphone.ERROR_NOT_READY:
                    Log.d(TAG, "mic:error: Not ready.");
                    break;

                default:
                    Log.d(TAG, "mic:error: other: " + errorCode);
            }
        }
    }
}
