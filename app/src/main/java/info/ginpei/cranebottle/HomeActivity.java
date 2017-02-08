package info.ginpei.cranebottle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    TextView statusTextView;
    TextView userAnswerTextView;

    Quiz currentQuiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.button_start).setOnClickListener(view -> {
            start();
        });
    }

    private void start() {
        startActivity(new Intent(this, PlayerActivity.class));
    }
}
