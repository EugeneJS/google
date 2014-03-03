package com.example.HowMuchYouGoogle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Desmond
 * Date: 03.03.14
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public class Constants
{
    public static final int DIALOG_DOWNLOAD_JSON_PROGRESS_DISMISS = 0;
    public static final int DIALOG_DOWNLOAD_JSON_PROGRESS_REMOVE = 1;
    public static final int DIALOG_DOWNLOAD_JSON_PROGRESS_SHOW = 2;
    public static final int DIALOG_DOWNLOAD_PHOTO_PROGRESS_DISMISS = 3;
    public static final int DIALOG_DOWNLOAD_PHOTO_PROGRESS_REMOVE = 4;
    public static final int DIALOG_DOWNLOAD_PHOTO_PROGRESS_SHOW = 5;

    public static String API_ID="4220952";

    public static final String API_VERSION="5.5";
    public static final String LogTAG = "VK.Auth";
    public static String redirect_url="https://oauth.vk.com/blank.html";
    public static final int REQUEST_LOGIN=1;
    public static Account account;

    public static String accountVk(MyActivity myActivity){
        account = new Account();
        account.restore(myActivity);
        if(account.access_token == null){
            myActivity.startLoginActivity();
        }
        Log.e("", "AGAGAGG ----- " + account.user_id);
        return "http://hmug.herokuapp.com/get/question/?user_id=" + account.user_id;
    }

    /*** Get JSON Code from URL ***/
    public static String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


    /***** Get Image Resource from URL (Start) *****/
    private static final String TAG = "Image";
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
    /***** Get Image Resource from URL (End) *****/
}
