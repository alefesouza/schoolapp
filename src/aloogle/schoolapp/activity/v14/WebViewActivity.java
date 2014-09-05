package aloogle.schoolapp.activity.v14;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import aloogle.schoolapp.R;
import aloogle.schoolapp.other.Other;

@SuppressLint("NewApi")
public class WebViewActivity extends Activity {

	WebView web;
	ProgressBar progressBar;

	@SuppressLint({"SetJavaScriptEnabled"})
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		progressBar = (ProgressBar)findViewById(R.id.progressBar1);

		web = (WebView)findViewById(R.id.webview01);
		web.setWebViewClient(new webViewClient());
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setSupportZoom(true);
		web.getSettings().setBuiltInZoomControls(true);
		if (Build.VERSION.SDK_INT >= 11) {
			web.getSettings().setDisplayZoomControls(false);
		}

		int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String classRoom = preferences.getString("classRoom", "none");
		if (webViewValue == 0) {
			setTitle(R.string.schedules);
			if (preferences.getBoolean("schedulesCache" + classRoom, false)) {
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			} else {
				web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			}
			web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=0&classroom=" + classRoom + "&apilevel=" + Build.VERSION.SDK_INT);
		} else if (webViewValue == 1) {
			setTitle(R.string.calendar);
			if (preferences.getBoolean("calendarCache" + classRoom, false)) {
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			} else {
				web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			}
			web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=1&classroom=" + classRoom + "&apilevel=" + Build.VERSION.SDK_INT);
		} else if (webViewValue == 2) {
			setTitle(R.string.updatecalendar);
			web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=2&apilevel=" + Build.VERSION.SDK_INT);
		} else if (webViewValue == 3) {
			setTitle(R.string.announcements);
			if (preferences.getBoolean("calendarCache" + classRoom, false)) {
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			} else {
				web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			}
			web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=3&apilevel=" + Build.VERSION.SDK_INT);
		} else if (webViewValue == 4) {
			try {
				String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
				String about = "" +
					"<h3 style=\"text-align: justify;\">RebuApp vers&atilde;o " + version + "</h3>\n" +
					"<p style=\"text-align: justify;\">RebuApp &eacute; um aplicativo para Android que cont&eacute;m todos os hor&aacute;rios e agendas da Escola Estadual Prof&ordm; Willian Rodrigues Rebu&aacute; em Carapicu&iacute;ba, S&atilde;o Paulo, Brasil.</p>\n" +
					"<h3 style=\"text-align: justify;\">Licen&ccedil;a</h3>\n" +
					"<p style=\"text-align: justify;\">Esse aplicativo foi lan&ccedil;ado sob <a href=\"http://choosealicense.com/licenses/gpl-v3?aloogleapp=openinbrowser\">licen&ccedil;a GPLv3</a> e o c&oacute;digo fonte dele est&aacute; dispon&iacute;vel no meu <a href=\"https://github.com/alefesouza/schoolapp?aloogleapp=openinbrowser\">GitHub</a>. Todo mundo esta permitido a modificar e lan&ccedil;ar esse aplicativo em seu nome, mas vai ter que liberar o c&oacute;digo fonte. E se voc&ecirc; fizer isso, por favor me d&ecirc; os devidos cr&eacute;ditos.</p>\n";
				setTitle(R.string.about);
				web.loadData(about, "text/html; charset=utf-8", "UTF-8");
			} catch (NameNotFoundException e) {}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_webview, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (getIntent().getIntExtra(Other.WebViewValue, 0) == 4) {
			menu.findItem(R.id.menu_refresh).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Activity.CONNECTIVITY_SERVICE);
		switch (item.getItemId()) {
		case android.R.id.home:
			WebViewActivity.this.finish();
			return true;
		case R.id.menu_refresh:
			if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
				progressBar.setVisibility(View.VISIBLE);
				web.setVisibility(View.GONE);
				web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
				web.reload();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}

	public class webViewClient extends WebViewClient {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WebViewActivity.this);
		String classRoom = preferences.getString("classRoom", "none");
		Editor editor = preferences.edit();
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains("?aloogleapp=openinbrowser") || url.contains("&aloogleapp=openinbrowser")) {
				String realurl = url.replace("?aloogleapp=openinbrowser", "").replace("&aloogleapp=openinbrowser", "");
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(realurl));
				startActivity(i);
			} else {
				view.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
			if (webViewValue == 0) {
				editor.putBoolean("schedulesCache" + classRoom, true);
			} else if (webViewValue == 1) {
				editor.putBoolean("calendarCache" + classRoom, true);
			} else if (webViewValue == 3) {
				editor.putBoolean("announcementsCache", true);
			}
			editor.commit();
			progressBar.setVisibility(View.GONE);
			web.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			String error = "" +
				"<p style=\"text-align: justify;\"> " + getString(R.string.erroroccured) + "</p>\n" +
				"<ul>\n" +
				"<li>Tente novamente conectado &agrave; internet.</li>\n" +
				"<li>Se voc&ecirc; viu esse aviso vaacute;rias vezes e atualizou o aplicativo recentemente, ele corrigiu o bug de apagar outras p&aacute;ginas ao atualizar uma, talvez isso tenha causado esse erro, ent&atilde;o limpe os dados do aplicativo.</li>\n" +
				"</ul>\n";
			web.loadData(error, "text/html; charset=utf-8", "UTF-8");
			int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
			if (webViewValue == 0) {
				editor.remove("schedulesCache" + classRoom);
			} else if (webViewValue == 1) {
				editor.remove("calendarCache" + classRoom);
			} else if (webViewValue == 3) {
				editor.remove("announcementsCache");
			}
			editor.commit();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			Class.forName
			("android.webkit.WebView")
			.getMethod
			("onPause", (Class[])null)
			.invoke
			(web, (Object[])null);
		} catch (ClassNotFoundException cnfe) {}

		catch (NoSuchMethodException nsme) {}

		catch (InvocationTargetException ite) {}

		catch (IllegalAccessException iae) {}
	}
}
