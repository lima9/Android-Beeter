package edu.upc.eetac.dsa.beeter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.beeter.client.BeeterClient;
import edu.upc.eetac.dsa.beeter.client.BeeterClientException;
import edu.upc.eetac.dsa.beeter.client.entity.Sting;
import edu.upc.eetac.dsa.beeter.client.entity.StingCollection;

public class StingsListActivity extends AppCompatActivity {


    private final static String TAG = StingsListActivity.class.toString();
    private GetStingsTask mGetStingsTask = null;
    private StingCollection stings = new StingCollection();
    private StingCollectionAdapter  adapter = null;

    public class StingCollectionAdapter extends BaseAdapter {
        private StingCollection stingCollection;
        private LayoutInflater layoutInflater;

        public StingCollectionAdapter(Context context, StingCollection stingCollection){
            layoutInflater = LayoutInflater.from(context);
            this.stingCollection = stingCollection;
        }

        @Override
        public int getCount() {
            return stingCollection.getStings().size();
        }

        @Override
        public Object getItem(int position) {
            return stingCollection.getStings().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stings_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Execute AsyncTask
        mGetStingsTask = new GetStingsTask(null);
        mGetStingsTask.execute((Void) null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    class GetStingsTask extends AsyncTask<Void, Void, String> {
        private String uri;

        public GetStingsTask(String uri) {
            this.uri = uri;

        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonStingCollection = null;
            try {
                jsonStingCollection = BeeterClient.getInstance().getStings(uri);
            } catch (BeeterClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return jsonStingCollection;
        }

        @Override
        protected void onPostExecute(String jsonStingCollection) {
            Log.d(TAG, jsonStingCollection);
            StingCollection stingCollection = (new Gson()).fromJson(jsonStingCollection, StingCollection.class);
            for(Sting sting : stingCollection.getStings()){
                stings.getStings().add(stings.getStings().size(), sting);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
