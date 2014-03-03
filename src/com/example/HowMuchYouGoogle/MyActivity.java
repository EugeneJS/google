package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MyActivity extends Activity implements View.OnClickListener {

    CustomKeyboard customKeyboard;


    ArrayList<HashMap<String, Object>> MyArrList;
    Account account = new Account();
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.main);

        startLevel(Constants.accountVk(this));

    }

    private void startLevel(String url){
        new DownloadJSONFileAsync(this).execute(url, "image");
    }

    public void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, com.example.HowMuchYouGoogle.Constants.REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.example.HowMuchYouGoogle.Constants.REQUEST_LOGIN)
        {
            if (resultCode == RESULT_OK)
            {
                //авторизовались успешно
                account.access_token=data.getStringExtra("token");
                account.user_id=data.getLongExtra("user_id", 0);
                account.save(MyActivity.this);

                new DownloadJSONFileAsync(this).execute("http://hmug.herokuapp.com/login/?id=" + account.user_id, "register");

            }
            else
            {
                startLoginActivity();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        Log.e("", "click");
        Toast.makeText(this, customKeyboard.GetCurrentText(), Toast.LENGTH_SHORT).show();

        Log.e("","keyboard - " + customKeyboard.GetCurrentText());
        account.restore(this);
        Log.e("","acc id - " + account.user_id);
        new DownloadJSONFileAsync(this).execute("http://hmug.herokuapp.com/check/question/?user_id="+ account.user_id +"&answer=" + customKeyboard.GetCurrentText(), "");
        //startLevel(Constants.accountVk(this));


        /*LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
        layout.clearAnimation();
         */
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
            case Constants.DIALOG_DOWNLOAD_JSON_PROGRESS_SHOW:
                mProgressDialog.setMessage("Downloading JSON.....");
            case Constants.DIALOG_DOWNLOAD_PHOTO_PROGRESS_SHOW:
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

    Object obj;

    public void GetImagesArray(String[] arrUrls, int length, String chars) {
        new DownloadPhotoFileAsync(this).execute(arrUrls[0],arrUrls[1],arrUrls[2],arrUrls[3],
                (ImageView) findViewById(R.id.image1x1),
                (ImageView) findViewById(R.id.image1x2),
                (ImageView) findViewById(R.id.image2x1),
                (ImageView) findViewById(R.id.image2x2));


        Log.e("","-_-Length: " + length);

        Log.e("","-_-Chars: " + chars);

        customKeyboard = new CustomKeyboard(this, length, chars.toCharArray());
        customKeyboard.SetOnNextBtnClickListner(this);

        obj = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                MyActivity activity = (MyActivity)obj;
                LinearLayout layout = (LinearLayout)activity.findViewById(R.id.layout);
                layout.addView(customKeyboard);
            }
        });

    }

    public void GetAnswerResult(int tries, int coins){
        Log.e("","Tries: "+tries);
        Log.e("","Coins: "+coins);
        final Integer t = tries;
        final Integer c = coins;
        obj = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                MyActivity activity = (MyActivity)obj;
                TextView triesCount = (TextView)activity.findViewById(R.id.tries);
                triesCount.setText("" + t);

                TextView coinsCount = (TextView)activity.findViewById(R.id.completedCount);
                coinsCount.setText("" + c);
            }
        });


    }
}
