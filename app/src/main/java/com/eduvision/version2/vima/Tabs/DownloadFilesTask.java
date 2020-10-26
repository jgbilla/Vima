package com.eduvision.version2.vima.Tabs;

import android.os.AsyncTask;

import java.net.URL;

public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

    @Override
    protected Long doInBackground(URL... urls) {
        Fetching.getItems();
        FetchShops.getShops();
        return null;
    }
}
