package info.ginpei.cranebottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusTextView = (TextView) findViewById(R.id.textView_status);

        findViewById(R.id.button_start).setOnClickListener(view -> {
            String text = getString(R.string.start);
            setStatusText(text);
            toast(text);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setStatusText("Hello World!");
    }

    private void setStatusText(String text) {
        statusTextView.setText(text);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
