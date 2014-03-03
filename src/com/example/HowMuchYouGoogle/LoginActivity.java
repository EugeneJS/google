package com.example.HowMuchYouGoogle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity
{
    WebView webview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        webview = (WebView) findViewById(R.id.wb);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.clearCache(true);
        
        //Чтобы получать уведомления об окончании загрузки страницы
        webview.setWebViewClient(new VkontakteWebViewClient());
                
        //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
        CookieSyncManager.createInstance(this);
        
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        
        String url=getUrl();
        webview.loadUrl(url);
    }
    
    class VkontakteWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            parseUrl(url);
        }
    }
    
    private void parseUrl(String url) {
        try {
            if(url==null)
                return;
            Log.i(com.example.HowMuchYouGoogle.Constants.LogTAG, "url="+url);
            if(url.startsWith(com.example.HowMuchYouGoogle.Constants.redirect_url))
            {
                if(!url.contains("error=")){
                    String[] auth=parseRedirectUrl(url);
                    Intent intent=new Intent();
                    intent.putExtra("token", auth[0]);
                    intent.putExtra("user_id", Long.parseLong(auth[1]));
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUrl()
    {
        String url="https://oauth.vk.com/authorize?client_id="+ com.example.HowMuchYouGoogle.Constants.API_ID
                +"&display=mobile&scope="+getSettings()
                +"&redirect_uri="+ URLEncoder.encode(com.example.HowMuchYouGoogle.Constants.redirect_url)
                +"&response_type=token"
                +"&v="+URLEncoder.encode(com.example.HowMuchYouGoogle.Constants.API_VERSION);
        return url;
    }

    public static String getSettings()
    {
        //http://vk.com/dev/permission
        return "";//"notify,friends,photos,audio,video,docs,status,notes,pages,wall,groups,messages,offline,notifications";
    }

    public static String[] parseRedirectUrl(String url) throws Exception {
        //url is something like http://api.vkontakte.ru/blank.html#access_token=66e8f7a266af0dd477fcd3916366b17436e66af77ac352aeb270be99df7deeb&expires_in=0&user_id=7657164
        String access_token=extractPattern(url, "access_token=(.*?)&");
        Log.i(com.example.HowMuchYouGoogle.Constants.LogTAG, "access_token=" + access_token);
        String user_id=extractPattern(url, "user_id=(\\d*)");
        Log.i(com.example.HowMuchYouGoogle.Constants.LogTAG, "user_id=" + user_id);
        if(user_id==null || user_id.length()==0 || access_token==null || access_token.length()==0)
            throw new Exception("Failed to parse redirect url "+url);
        return new String[]{access_token, user_id};
    }

    public static String extractPattern(String string, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);
        if (!m.find())
            return null;
        return m.toMatchResult().group(1);
    }
}