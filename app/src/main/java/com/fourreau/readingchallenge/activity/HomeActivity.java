package com.fourreau.readingchallenge.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.model.Repo;
import com.fourreau.readingchallenge.service.GithubService;

import java.util.List;

import retrofit.RestAdapter;
import timber.log.Timber;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Timber.d("TOTO");

        new ListReposTask().execute("PierreFourreau");
    }

    public void afficherRepos(List<Repo> repos) {
        Toast.makeText(this,"nombre de dépots : "+repos.size(), Toast.LENGTH_SHORT).show();
        Timber.d("ttt");
        for(Repo r : repos) {
            Timber.d(""+r.getFull_name());
        }

    }

    class ListReposTask extends AsyncTask<String,Void,List<Repo>> {

        @Override
        protected List<Repo> doInBackground(String...params) {
            GithubService githubService = new RestAdapter.Builder()
                    .setEndpoint(GithubService.ENDPOINT)
                    .build()
                    .create(GithubService.class);

            String user = params[0];
            List<Repo> repoList = githubService.listRepos(user);

            return repoList;
        }

        @Override
        protected void onPostExecute(List<Repo> repos) {
            super.onPostExecute(repos);
            afficherRepos(repos);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
