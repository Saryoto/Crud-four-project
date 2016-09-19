package ang.latihan.json;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import ang.latihan.json.Task.TaskFinishedListener;

public class Splash extends Activity implements TaskFinishedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        new Task(progressBar, this).execute("www.google.co.uk");
    }

    @Override
    public void onTaskFinished() {
        completeSplash();
    }

    private void completeSplash(){
        startApp();
        finish();
    }

    private void startApp() {
        Intent intent = new Intent(Splash.this, HalamanUtamaActivity.class);
        startActivity(intent);
    }
}
