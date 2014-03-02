package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
    // Download JSON in Background
    public class DownloadJSONFileAsync extends AsyncTask<String, Void, Void> {
        private Activity activity;

        public DownloadJSONFileAsync(Activity _activity){
            activity = _activity;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            activity.showDialog(Constants.DIALOG_DOWNLOAD_JSON_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];


            JSONObject data;
            try {
                data = new JSONObject(Constants.getJSONUrl(url));
                int length = data.getInt("length");
                JSONArray array = data.getJSONArray("images");
                for(int i = 0; i < array.length(); i++)
                {
                    JSONObject c = array.getJSONObject(i);
                    c.get("url");
                    Log.e("= ^_^ =>", c.toString());
                }


            } catch (JSONException e) {
                Log.e("", e.getMessage());
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            activity.dismissDialog(Constants.DIALOG_DOWNLOAD_JSON_PROGRESS);
            activity.removeDialog(Constants.DIALOG_DOWNLOAD_JSON_PROGRESS);
        }


    }