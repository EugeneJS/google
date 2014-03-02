package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.main);

       /* TextView score = (TextView) findViewById(R.id.score);
        score.setText("Угадал: 7 | Пропустил: 1");*/

        ImageView img1x1;
        img1x1 = (ImageView) findViewById(R.id.image1x1);
        String URL1 = "http://nsp-ru-ua.com/uploads/wellness-style-international-club/beauty-and-health-wellness-style-inc.jpg";
        img1x1.setTag(URL1);
        new DownloadImagesTask().execute(img1x1);

        ImageView img1x2;
        img1x2 = (ImageView) findViewById(R.id.image1x2);
        String URL2 = "http://chaos.in.ua/sites/chaos.in.ua/files/images/16.preview.jpg";
        img1x2.setTag(URL2);
        new DownloadImagesTask().execute(img1x2);

        ImageView img2x1;
        img2x1 = (ImageView) findViewById(R.id.image2x1);
        String URL3 = "http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Julia_set_(highres_01).jpg/250px-Julia_set_(highres_01).jpg";
        img2x1.setTag(URL3);
        new DownloadImagesTask().execute(img2x1);

        ImageView img2x2;
        img2x2 = (ImageView) findViewById(R.id.image2x2);
        String URL4 = "http://img11.slando.ua/images_slandocomua/116935891_3_644x461_krasota-i-zdorove-vsego-za-4-minuty-v-den-sposob-tut-setevoy-marketing.jpg";
        img2x2.setTag(URL4);
        new DownloadImagesTask().execute(img2x2);
    }
    public void SetImagePlz(ImageView i) {
        try {
            URL newurl = new URL("http://funzoo.ru/uploads/posts/2009-09/1252395237_begemot-v-gneve001.jpg"); //Your URL String.
            Bitmap image = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            i.setImageBitmap(image);
        } catch (Exception e){
            Log.e("Error", e.getLocalizedMessage());
        }
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

            Bitmap bmp =null;
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


}

/*
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
         */