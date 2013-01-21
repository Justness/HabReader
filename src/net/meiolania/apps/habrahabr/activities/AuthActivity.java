package net.meiolania.apps.habrahabr.activities;

import java.io.IOException;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends AbstractionActivity {
    public static final String MAIN_URL = "http://habrahabr.ru/";
    public static final String LOGIN_URL = "http://habrahabr.ru/login/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.auth_activity);

	showAuthPage();
    }

    private class GetUserName extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
	    // TODO: handle html from webview?
	    try {
		Preferences preferences = Preferences.getInstance(AuthActivity.this);
		
		Document document = Jsoup.connect(MAIN_URL)
					 .cookie("PHPSESSID", preferences.getPHPSessionId())
					 .cookie("hsec_id", preferences.getHSecId())
					 .get();

		Element usernameElement = document.select("a.username").first();
		preferences.setLogin(usernameElement.text());
	    } catch (IOException e) {

	    }

	    Intent intent = new Intent(AuthActivity.this, PostsActivity.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    startActivity(intent);
	    return null;
	}

    }

    private void showAuthPage() {
	WebView content = (WebView) findViewById(R.id.auth_content);

	content.setWebViewClient(new WebViewClient() {

	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		// We handle redirect to main page after auth
		if (!url.contains("/login/")) {
		    String cookies[] = CookieManager.getInstance().getCookie(LOGIN_URL).split(";");

		    Preferences preferences = Preferences.getInstance(AuthActivity.this);

		    for (String cookie : cookies) {
			String cookieNameAndValue[] = cookie.split("=");
			String cookieName = cookieNameAndValue[0].trim();
			String cookieValue = cookieNameAndValue[1].trim();
			
			if (cookieName.equals("PHPSESSID"))
			    preferences.setPHPSessionId(cookieValue);
			if (cookieName.equals("hsec_id"))
			    preferences.setHSecId(cookieValue);
		    }
		}
	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		if (!url.contains("/login/")) {
		    // @TODO: It's awful
		    new GetUserName().execute();
		}
	    }

	});
	content.getSettings().setSupportZoom(true);
	content.getSettings().setBuiltInZoomControls(true);
	content.getSettings().setJavaScriptEnabled(true);

	content.loadUrl(LOGIN_URL);
    }

    @Override
    protected OnClickListener getConnectionDialogListener() {
	return new OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		finish();
	    }
	};
    }

}