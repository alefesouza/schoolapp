package aloogle.rebuapp.activity.v14;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.Other;

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
		web.getSettings().setDomStorageEnabled(true);
		web.getSettings().setDisplayZoomControls(false);
		int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String classRoom = preferences.getString("classRoom", "none");
		String clubeRoom = preferences.getString("clubeRoom", "none");
		String eletivaRoom = preferences.getString("eletivaRoom", "none");
		try {
			int version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			if (webViewValue == 0) {
				Other.ActionBarColor(this, getString(R.string.schedules));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=0&classroom=" + classRoom + "&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 1) {
				Other.ActionBarColor(this, getString(R.string.calendar));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=1&classroom=" + classRoom + "&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 2) {
				Other.ActionBarColor(this, getString(R.string.panel));
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=2&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 3) {
				Other.ActionBarColor(this, getString(R.string.announcements));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=3&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 4) {
				Other.ActionBarColor(this, getString(R.string.annotations));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("file:///android_asset/anotacoes.html");
			} else if (webViewValue == 5) {
				Other.ActionBarColor(this, getString(R.string.about));
				String about = "" +
					"<h3 style=\"text-align: justify;\">RebuApp vers&atilde;o " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "</h3>\n" +
					"<p style=\"text-align: justify;\">RebuApp &eacute; um aplicativo para Android que cont&eacute;m todos os hor&aacute;rios e agendas da Escola Estadual Prof&ordm; Willian Rodrigues Rebu&aacute; em Carapicu&iacute;ba, S&atilde;o Paulo, Brasil.</p>\n" +
					"<h3 style=\"text-align: justify;\">Licen&ccedil;a</h3>\n" +
					"<p style=\"text-align: justify;\">Esse aplicativo foi lan&ccedil;ado sob <a href=\"http://choosealicense.com/licenses/gpl-v3?aloogleapp=open\">licen&ccedil;a GPLv3</a> e o c&oacute;digo fonte dele est&aacute; dispon&iacute;vel no meu <a href=\"https://github.com/alefesouza/schoolapp?aloogleapp=open\">GitHub</a>. Todo mundo esta permitido a modificar e lan&ccedil;ar esse aplicativo em seu nome, mas vai ter que liberar o c&oacute;digo fonte. E se voc&ecirc; fizer isso, por favor me d&ecirc; os devidos cr&eacute;ditos.</p>\n";
				web.loadData(about, "text/html; charset=utf-8", "UTF-8");
			} else if (webViewValue == 6) {
				Other.ActionBarColor(this, getString(R.string.readingroom));
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=6&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 10) {
				Other.ActionBarColor(this, getString(R.string.messages));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=10&cluberoom=" + clubeRoom + "&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 11) {
				Other.ActionBarColor(this, getString(R.string.announcements) + " - Clubes");
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=11&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 12) {
				Other.ActionBarColor(this, getString(R.string.clubeaddnew));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=12&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 20) {
				Other.ActionBarColor(this, getString(R.string.messages));
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=20&eletivaroom=" + eletivaRoom + "&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			} else if (webViewValue == 21) {
				Other.ActionBarColor(this, getString(R.string.announcements) + " - Eletivas");
				web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=21&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
			}
		} catch (NameNotFoundException e) {}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_webview, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
		if (webViewValue == 2 || webViewValue == 6) {
			menu.findItem(R.id.menu_back).setVisible(true);
			menu.findItem(R.id.menu_forward).setVisible(true);
			menu.findItem(R.id.menu_home).setVisible(true);
		} else if (webViewValue == 4 || webViewValue == 5) {
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
				web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
				web.reload();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case R.id.menu_back:
			if (web.canGoBack()) {
				web.goBack();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.nopages), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case R.id.menu_home:
			int webViewValue = getIntent().getIntExtra(Other.WebViewValue, 0);
			try {
				int version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
				if (webViewValue == 2) {
					web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=2&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
				} else if (webViewValue == 6) {
					web.loadUrl("http://aloogle.tumblr.com/schoolapp/action?action=6&apilevel=" + Build.VERSION.SDK_INT + "&version=" + version);
				}
			} catch (NameNotFoundException e) {}
			return true;
		case R.id.menu_forward:
			if (web.canGoForward()) {
				web.goForward();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.nopages), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}

	public class webViewClient extends WebViewClient {
		ConnectivityManager cm = (ConnectivityManager)WebViewActivity.this.getSystemService(Activity.CONNECTIVITY_SERVICE);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WebViewActivity.this);
		String classRoom = preferences.getString("classRoom", "none");
		String clubeRoom = preferences.getString("clubeRoom", "none");
		String eletivaRoom = preferences.getString("eletivaRoom", "none");
		Editor editor = preferences.edit();
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains("?aloogleapp=open") || url.contains("&aloogleapp=open")) {
				String realurl = url.replace("?aloogleapp=open", "").replace("&aloogleapp=open", "");
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(realurl));
				startActivity(i);
			} else if (url.contains("?aloogleapp=reload") || url.contains("&aloogleapp=reload")) {
				if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
					progressBar.setVisibility(View.VISIBLE);
					web.setVisibility(View.GONE);
					web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
					web.reload();
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
					toast.show();
				}
			} else if (url.contains("?aloogleapp=share") || url.contains("&aloogleapp=share")) {
				String realurl = url.replace("?aloogleapp=share", "").replace("&aloogleapp=share", "");
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, realurl);
				shareIntent.setType("text/plain");
				startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
			} else if (url.contains("?aloogleapp=needinternet") || url.contains("&aloogleapp=needinternet")) {
				if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
					web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
					String realurl = url.replace("?aloogleapp=needinternet", "").replace("&aloogleapp=needinternet", "");
					web.loadUrl(realurl);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
					toast.show();
				}
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
			} else if (webViewValue == 10) {
				editor.putBoolean("messagesCacheClube" + clubeRoom, true);
			} else if (webViewValue == 11) {
				editor.putBoolean("announcementsCacheClube", true);
			} else if (webViewValue == 20) {
				editor.putBoolean("messagesCacheEletiva" + eletivaRoom, true);
			} else if (webViewValue == 21) {
				editor.putBoolean("announcementsCacheEletiva", true);
			}
			editor.commit();
			progressBar.setVisibility(View.GONE);
			web.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
			String error = "" +
				"<p style=\"text-align: justify;\"> " + getString(R.string.erroroccured) + "</p>\n" +
				"<ul>\n" +
				"<li>Tente novamente conectado &agrave; internet.</li>\n" +
				"<li>Toque em voltar e abra novamente.</li>\n" +
				"<li>Se nada funcionar, limpe os dados do aplicativo.</li>\n" +
				"</ul>\n";
			web.loadData(error, "text/html; charset=utf-8", "UTF-8");
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

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
