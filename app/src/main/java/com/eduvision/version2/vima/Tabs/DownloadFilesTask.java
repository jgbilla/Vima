package com.eduvision.version2.vima.Tabs;

import android.os.AsyncTask;

import com.eduvision.version2.vima.Spinning;

import java.net.URL;

public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
    public static Boolean dataFetched = false;
    public static int downloadProgress = 0;

    @Override
    protected Long doInBackground(URL... urls) {
        Fetching.getItems();
        FetchShops.getShops();
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        dataFetched = true;
    }
    @Override
    protected  void onProgressUpdate(Integer... progress){
        super.onProgressUpdate(progress);
        downloadProgress = progress[0];

    }
    @Override
    protected void onPreExecute() {
    }

}

