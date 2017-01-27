package info.ginpei.cranebottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView statusTextView;

    Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusTextView = (TextView) findViewById(R.id.textView_status);

        findViewById(R.id.button_start).setOnClickListener(view -> {
            speaker.speak("Hello world!");
        });

        speaker = new Speaker(this, new SpeakerListener());
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

    private void setStatusText(String text) {
        statusTextView.setText(text);
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

        }
    }
}
