package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MyActivity extends Activity  {
    /**
     * Called when the activity is first created.
     */
    private static String url = "http://hmug.herokuapp.com/get/question/";


    ArrayList<HashMap<String, Object>> MyArrList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.main);

        new DownloadPhotoFileAsync(this).execute("http://ain.ua/wp-content/uploads/2012/04/KPI-e1333958712758.png",
                "http://ain.ua/wp-content/uploads/2012/04/KPI-e1333958712758.png",
                "http://ain.ua/wp-content/uploads/2012/04/KPI-e1333958712758.png",
                "http://ain.ua/wp-content/uploads/2012/04/KPI-e1333958712758.png",
                (ImageView) findViewById(R.id.image1x1),
                (ImageView) findViewById(R.id.image1x2),
                (ImageView) findViewById(R.id.image2x1),
                (ImageView) findViewById(R.id.image2x2));
        new DownloadJSONFileAsync(this).execute(url);
    }

    class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp = null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){
                Log.e("", e.toString())   ;
                e.printStackTrace();
            }
            return bmp;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        switch (id) {
            case Constants.DIALOG_DOWNLOAD_JSON_PROGRESS:
                mProgressDialog.setMessage("Downloading JSON.....");
            case Constants.DIALOG_DOWNLOAD_PHOTO_PROGRESS:
                mProgressDialog.setMessage("Downloading PHOTO.....");
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        return mProgressDialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void ShowImg()
    {}
}
