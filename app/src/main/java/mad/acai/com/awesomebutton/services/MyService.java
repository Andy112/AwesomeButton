package mad.acai.com.awesomebutton.services;

import android.os.AsyncTask;

import mad.acai.com.awesomebutton.logging.L;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Andy on 25/04/2016.
 */
public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        L.t(this, "onStartJob");
        new MyTask(this).execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.t(this, "onStopJob");
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {

        MyService myService;

        public MyTask(MyService myService) {
            this.myService = myService;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            return params[0];
        }
    }
}
