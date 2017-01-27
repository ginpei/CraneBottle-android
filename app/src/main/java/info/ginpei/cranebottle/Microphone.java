package info.ginpei.cranebottle;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class Microphone {

    public static final int PROGRESS_READY = 0;
    public static final int PROGRESS_WAITING = 1;
    public static final int PROGRESS_LISTENING = 2;
    public static final int PROGRESS_RECOGNIZING = 3;
    public static final int PROGRESS_DONE = 4;
    public static final int ERROR_NOT_READY = -1;

    protected Activity context;
    protected Listener listener;
    protected SpeechRecognizer recognizer;

    protected String result;

    public String getResult() {
        return result;
    }

    public Microphone(Activity context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setup() {
        getPermission();
        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        listenRecognizer();
        fireProgress(PROGRESS_READY);
    }

    private void getPermission() {
        String permission = Manifest.permission.RECORD_AUDIO;

        int permittedStatus = ContextCompat.checkSelfPermission(context, permission);
        if (permittedStatus != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                ActivityCompat.requestPermissions(context, new String[]{permission}, 1);
            }
        }
    }

    private void listenRecognizer() {
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                fireProgress(PROGRESS_WAITING);
            }

            @Override
            public void onBeginningOfSpeech() {
                fireProgress(PROGRESS_LISTENING);
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                fireProgress(PROGRESS_RECOGNIZING);
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                result = pickUpResultText(bundle);
                fireProgress(PROGRESS_DONE);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private String pickUpResultText(Bundle result) {
        ArrayList data = result.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text;
        if (data == null) {
            text = null;
        } else {
            text = (String) data.get(data.size() - 1);
        }
        return text;
    }

    public void listen() {
        if (recognizer != null) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

            recognizer.startListening(intent);
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

    public void shutdown() {
        // TODO
    }

    public static interface Listener {
        public void onProgress(int progressCode);

        public void onError(int errorCode);
    }
}
