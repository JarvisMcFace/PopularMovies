package com.hughesdigitalimage.popularmovies.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by David on 9/24/16.
 */

public class OkHttpHelper extends AsyncTask<String, Void, String> {

    private WeakReference<OkHttpHelperCallback> weakReferenceOkHttpHelperCallback;
    private OkHttpClient client = new OkHttpClient();

    public OkHttpHelper (WeakReference<OkHttpHelperCallback> weakReferenceOkHttpHelperCallback) {
        this.weakReferenceOkHttpHelperCallback = weakReferenceOkHttpHelperCallback;
    }

    @Override
    protected String doInBackground(String... params) {

        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);

        Request request = builder.build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("OkHttpHelper", "doInBackground: ", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String jsonResults) {
        OkHttpHelperCallback okHttpHelperCallback = weakReferenceOkHttpHelperCallback.get();
        okHttpHelperCallback.performOnPostExecute(jsonResults);
    }
}
