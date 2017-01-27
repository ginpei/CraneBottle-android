package info.ginpei.cranebottle;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

public class Speaker {
    public static final String MY_UTTERANCE_ID = "My Utterance ID";
    public static final Locale LOCALE_DEFAULT = Locale.US;

    public static final int PROGRESS_INIT = 0;
    public static final int PROGRESS_READY = 1;
    public static final int PROGRESS_SPEAKING = 2;
    public static final int PROGRESS_DONE = 3;
    public static final int PROGRESS_SHUTDOWN = 4;
    private static final int ERROR_NOT_READY = -1;

    Context context;
    Listener listener;
    TextToSpeech tts;

    public Speaker(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setup() {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    updateTtsSettings();
                    setOnUtteranceProgressListener();
                    fireProgress(PROGRESS_READY);
                }
            }
        });

        fireProgress(PROGRESS_INIT);
    }

    private void updateTtsSettings() {
        tts.setLanguage(LOCALE_DEFAULT);
    }

    private int setOnUtteranceProgressListener() {
        return tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                fireProgress(PROGRESS_SPEAKING);
            }

            @Override
            public void onDone(String utteranceId) {
                fireProgress(PROGRESS_DONE);
            }

            @Override
            public void onError(String utteranceId) {
                fireProgress(PROGRESS_INIT);
            }
        });
    }

    public void shutdown() {
        tts.stop();
        tts.shutdown();
        tts = null;

        fireProgress(PROGRESS_SHUTDOWN);
    }

    public void speak(String text) {
        if (tts != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, MY_UTTERANCE_ID);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            fireError(ERROR_NOT_READY);
        }
    }

    private void fireProgress(int progressCode) {
        listener.onProgress(progressCode);
    }

    private void fireError(int errorCode) {
        listener.onError(errorCode);
    }

    public static interface Listener {
        public void onProgress(int progressCode);
        public void onError(int errorCode);
    }
}
