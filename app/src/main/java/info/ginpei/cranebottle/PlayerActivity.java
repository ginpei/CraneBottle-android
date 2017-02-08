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

    private QuizStatusList quizzes;

    private Speaker speaker;
    private Microphone microphone;

    private ListView questionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        quizzes = new QuizStatusList();
        load();

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

        Log.d(TAG, "size=" + quizzes.size());
        ArrayAdapter<QuizStatus> quizArrayAdapter = new ArrayAdapter<QuizStatus>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                quizzes
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = renderListItemView(position, convertView, parent);

                return view;
            }

            @NonNull
            private View renderListItemView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                renderQuizItemView(view, position);
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

    private void load() {
        for (int i = 0; i < 10; i++) {
            quizzes.add(new Quiz(getString(R.string.tmp_q1), "This is a pen."));
            quizzes.add(new Quiz(getString(R.string.tmp_q2), "This is a nice pen."));
            quizzes.add(new Quiz(getString(R.string.tmp_q3), "This is a nice pen which I bought yesterday."));
        }

        quizzes.moveOnToNext();
        QuizStatus status1 = quizzes.get(quizzes.getCurrentPosition());
        status1.status = QuizStatus.STATUS_CORRECT;
        quizzes.moveOnToNext();
        QuizStatus status2 = quizzes.get(quizzes.getCurrentPosition());
        status2.status = QuizStatus.STATUS_INCORRECT;
        quizzes.moveOnToNext();
        QuizStatus status3 = quizzes.get(quizzes.getCurrentPosition());
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

    private void renderQuizItemView(View view, int position) {
        QuizStatus status = quizzes.get(position);
        Quiz quiz = status.getQuiz();

        String title = quiz.getQuestion();
        if (position == quizzes.getCurrentPosition()) {
            title += " (playing)";
        } else if (status.isAnswered()) {
            title += " " + (status.isCorrect() ? "(OK)" : "(NG!)");
        }

        ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        ((TextView) view.findViewById(android.R.id.text2)).setText(quiz.getAnswer());
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
