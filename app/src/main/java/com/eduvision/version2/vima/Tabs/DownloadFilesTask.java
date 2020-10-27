package com.eduvision.version2.vima.Tabs;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.net.URL;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

    ProgressDialog progressDialog;

    @Override
    protected Long doInBackground(URL... urls) {
        Fetching.getItems();
        FetchShops.getShops();
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        // execution of result of Long time consuming operation
        progressDialog.dismiss();

    }
    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(getApplicationContext(),
                "ProgressDialog",
                "Wait for the items to load");
    }

}

