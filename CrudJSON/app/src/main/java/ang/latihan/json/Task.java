package ang.latihan.json;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class Task extends AsyncTask<String, Integer, Integer> {

    public interface TaskFinishedListener {
        void onTaskFinished();
    }

    private final ProgressBar progressBar;
    private final TaskFinishedListener finishedListener;

    public Task(ProgressBar progressBar, TaskFinishedListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        Log.i("Tutorial", "Starting task with url: " + params[0]);
        if(resourcesDontAlreadyExist()){
            downloadResources();
        }
        return 1234;
    }

    private boolean resourcesDontAlreadyExist() {
        return true;
    }


    private void downloadResources() {
        // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
        int count = 10;
        for (int i = 0; i < count; i++) {

            // Update the progress bar after every step
            int progress = (int) ((i / (float) count) * 100);
            publishProgress(progress);

            // Do some long loading things
            try { Thread.sleep(1000); } catch (InterruptedException ignore) {}
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }
}
