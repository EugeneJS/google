package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

// Download Full Photo in Background
public class DownloadPhotoFileAsync extends AsyncTask<Object, Void, Void>
{
    private Activity activity;
    public DownloadPhotoFileAsync(Activity _activity)
    {
        activity = _activity;
    }

    String ImageUrl1 = "";
    String ImageUrl2 = "";
    String ImageUrl3 = "";
    String ImageUrl4 = "";
    ImageView image1 = null;
    ImageView image2 = null;
    ImageView image3 = null;
    ImageView image4 = null;
    Bitmap ImageFullBitmap1 = null;
    Bitmap ImageFullBitmap2 = null;
    Bitmap ImageFullBitmap3 = null;
    Bitmap ImageFullBitmap4 = null;

    protected void onPreExecute() {
        super.onPreExecute();
        //activity.showDialog(Constants.DIALOG_DOWNLOAD_PHOTO_PROGRESS);
    }

    @Override
    protected Void doInBackground(Object... params) {
        //strImageName = params[0];
        ImageUrl1 = (String) params[0];
        ImageUrl2 = (String) params[1];
        ImageUrl3 = (String) params[2];
        ImageUrl4 = (String) params[3];
        image1 = (ImageView) params[4];
        image2 = (ImageView) params[5];
        image3 = (ImageView) params[6];
        image4 = (ImageView) params[7];
        ImageFullBitmap1 = (Bitmap)Constants.loadBitmap(ImageUrl1);
        ImageFullBitmap2 = (Bitmap)Constants.loadBitmap(ImageUrl2);
        ImageFullBitmap3 = (Bitmap)Constants.loadBitmap(ImageUrl3);
        ImageFullBitmap4 = (Bitmap)Constants.loadBitmap(ImageUrl4);
        return null;
    }

    protected void onPostExecute(Void unused) {
        //showDialogPopup(strImageName,ImageFullBitmap); // When Finish Show Popup
        image1.setImageBitmap(ImageFullBitmap1);
        image2.setImageBitmap(ImageFullBitmap2);
        image3.setImageBitmap(ImageFullBitmap3);
        image4.setImageBitmap(ImageFullBitmap4);
        //activity.dismissDialog(Constants.DIALOG_DOWNLOAD_PHOTO_PROGRESS);
        //activity.removeDialog(Constants.DIALOG_DOWNLOAD_PHOTO_PROGRESS);

    }
}
