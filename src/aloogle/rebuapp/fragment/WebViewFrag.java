/*
 * Copyright (C) 2015 Alefe Souza <contato@alefesouza.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package aloogle.rebuapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.melnykov.fab.FloatingActionButton;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.other.Other;

@SuppressLint({
	"NewApi", "SetJavaScriptEnabled"
})
public class WebViewFrag extends Fragment implements ObservableScrollViewCallbacks {
	View view;
	Activity activity;

	SharedPreferences preferences;
	Editor editor;
	String iconcolor;
	ObservableWebView webView;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	ProgressBarDeterminate progressBar2;
	boolean finished;

	FloatingActionButton fabdownload, fabopen;
	long enqueue;

	private FrameLayout mTargetView;
	private FrameLayout mContentView;
	private CustomViewCallback mCustomViewCallback;
	private View mCustomView;
	private webChromeClient mClient;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 	Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		iconcolor = preferences.getString("prefIconColor", "branco");
		view = inflater.inflate(R.layout.webview, container, false);

		finished = false;

		webView = (ObservableWebView)view.findViewById(R.id.webview01);
		mClient = new webChromeClient();
		webView.setWebChromeClient(mClient);
		webView.setWebViewClient(new webViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		if (Build.VERSION.SDK_INT >= 11) {
			webView.getSettings().setDisplayZoomControls(false);
		}

		if (savedInstanceState != null) {
			webView.restoreState(savedInstanceState);
		} else {
			if (getActivity().getIntent().hasExtra("fromcard")) {
				String titulo = getActivity().getIntent().getStringExtra("wtitulo");
				String descricao = getActivity().getIntent().getStringExtra("wdescricao");

				if (getActivity().getIntent().getStringExtra("tipo").equals("0")) {
					descricao = descricao.replace("\n", "<br>");
				}

				String style = "body { background: #fafafa; } table { border: #000000 solid 1px; } td { border: #000000 solid 1px; text-align: center; padding: 5px; } hr { background: #000000; height: 1px; } img { display: block; margin-left: auto; margin-right: auto; max-width: 100%; height: auto; }";
				if (Build.VERSION.SDK_INT == 10) {
					webView.loadDataWithBaseURL(null, "<html><head><style>" + style + "</style></head><body><b>" + titulo + "</b><p>" + descricao + "</p></body></html>", "text/html", "utf-8", null);
				} else {
					webView.loadData("<html><head><style>" + style + "</style></head><body><b>" + titulo + "</b><p>" + descricao + "</p></body></html>", "text/html; charset=utf-8", "utf-8");
				}
			} else {
				webView.loadUrl(getActivity().getIntent().getStringExtra("url"));
			}
		}

		webView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), getActivity().getIntent().getStringExtra("titulo"));

		mContentView = (FrameLayout)view.findViewById(R.id.main_content);
		mTargetView = (FrameLayout)view.findViewById(R.id.target_view);
		webView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		fabdownload = (FloatingActionButton)view.findViewById(R.id.fabdownload);

		if (Build.VERSION.SDK_INT <= 10) {
			fabdownload.setShadow(false);
		}

		fabdownload.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				String[]parts = webView.getUrl().split("/");
				String fileName = parts[parts.length - 1];
				@SuppressWarnings("static-access")
				DownloadManager dm = (DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
				Request request = new Request(Uri.parse(webView.getUrl()));
				request.setTitle(fileName);
				request.setDescription("RebuApp");
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
				enqueue = dm.enqueue(request);

				BroadcastReceiver onComplete = new BroadcastReceiver() {
					public void onReceive(Context ctxt, Intent intent) {
						Toast toast = Toast.makeText(getActivity(), "Imagem salva na pasta " + Environment.DIRECTORY_DOWNLOADS, Toast.LENGTH_LONG);
						toast.show();
					}
				};

				getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			}
		});

		fabdownload.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast toast = Toast.makeText(getActivity(), "Baixar imagem", Toast.LENGTH_SHORT);
				toast.show();
				return true;
			}
		});

		fabopen = (FloatingActionButton)view.findViewById(R.id.fabopen);

		if (Build.VERSION.SDK_INT <= 10) {
			fabopen.setShadow(false);
		}

		fabopen.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (webView.getUrl().contains("facebook")) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/415176435213595"));
						startActivity(intent);
					} catch (Exception e) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(webView.getUrl()));
						startActivity(intent);
					}
				}
			}
		});

		fabopen.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				String app = null;
				if (webView.getUrl().contains("facebook")) {
					app = "Facebook";
				}
				Toast toast = Toast.makeText(getActivity(), "Abrir no aplicativo do " + app, Toast.LENGTH_SHORT);
				toast.show();
				return true;
			}
		});

		setColors();

		webView.setOnKeyListener(new OnKeyListener() {
			@SuppressWarnings("static-access")
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (event.getAction() == event.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (mCustomView != null) {
							mClient.onHideCustomView();
						} else {
							if (webView.canGoBack()) {
								webView.goBack();
							} else {
								getActivity().finish();
							}
						}
						return true;
					}
				}
				return true;
			}
		});

		webView.setScrollViewCallbacks(this);

		return view;
	}

	public void setColors() {
		progressBar2 = (ProgressBarDeterminate)view.findViewById(R.id.progressBar2);
		if (Build.VERSION.SDK_INT >= 21) {
			progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
			if (Other.getColor2(getActivity(), 1).equals("005400")) {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF005400, 0xFF005400));
			} else {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF003061, 0xFF003061));
			}
		} else {
			progressBarCompat = (ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar1);
			progressBarCompat.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
		}
		progressBar2.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
		fabopen.setColorNormal(Color.parseColor("#" + Other.getColor2(getActivity(), 0)));
		fabopen.setColorPressed(Color.parseColor("#" + Other.getColor2(getActivity(), 1)));
		fabdownload.setColorNormal(Color.parseColor("#" + Other.getColor2(getActivity(), 0)));
		fabdownload.setColorPressed(Color.parseColor("#" + Other.getColor2(getActivity(), 1)));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (!getActivity().getIntent().hasExtra("fromcard")) {
			inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.webview_menu, menu);
		} else {
			if (getActivity().getIntent().hasExtra("eventoid")) {
				MenuItem item = menu.add("Compartilhar");
				item.setIcon(R.drawable.ic_share);
				MenuItemCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
				item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						shareIntent.putExtra(Intent.EXTRA_TEXT, "http://apps.aloogle.net/web/rebuapp/evento.php?id=" + getActivity().getIntent().getStringExtra("eventoid"));
						shareIntent.setType("text/plain");
						startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
						return true;
					}
				});
			}
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (!getActivity().getIntent().hasExtra("fromcard")) {
			if (webView.canGoForward()) {
				menu.findItem(R.id.menu_forward).setEnabled(true);
			} else {
				menu.findItem(R.id.menu_forward).setEnabled(false);
			}
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_forward:
			webView.goForward();
			return true;
		case R.id.menu_reload:
			webView.reload();
			return true;
		case R.id.menu_cache:
			webView.clearCache(true);
			Toast toast = Toast.makeText(getActivity(), getString(R.string.cache2), Toast.LENGTH_LONG);
			toast.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class webViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (finished == false) {
				if (getActivity().getIntent().hasExtra("fromdictionary")) {
					Intent intent = new Intent(getActivity(), aloogle.rebuapp.activity.FragmentActivity.class);
					intent.putExtra("fragment", 5);
					String[]palavra = url.split("=");
					intent.putExtra("palavra", palavra[palavra.length - 1]);
					startActivity(intent);
				} else if (url.contains("aloogle.net") || url.contains("facebook.com") || url.contains("willianrrebua.blogspot.com") || url.contains(".jpg") || url.contains(".png") || url.contains(".gif")) {
					view.loadUrl(url);
				} else {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
				getActivity().supportInvalidateOptionsMenu();
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			String erro = "<html><head><style>body { background-color: #ffffff; }</style></head><body>Houve um erro, verifique sua conexão de internet</body></html>";
			webView.loadDataWithBaseURL("file:///android_asset/", erro, "text/html", "utf-8", null);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	public class webChromeClient extends WebChromeClient {
		public void onProgressChanged(WebView view, int progress) {
			if (finished == false) {
				progressBar2.setProgress(progress);
				progressBar2.setVisibility(View.VISIBLE);

				if (progress == 100) {
					progressBar2.setVisibility(View.GONE);
				}

				if (progress >= 50) {
					if (Build.VERSION.SDK_INT >= 21) {
						progressBar.setVisibility(View.GONE);
					} else {
						progressBarCompat.setVisibility(View.GONE);
					}
					webView.setVisibility(View.VISIBLE);

					if (webView.getUrl().contains(".jpg") || webView.getUrl().contains(".png") || webView.getUrl().contains(".gif")) {
						fabdownload.setVisibility(View.VISIBLE);
						fabopen.setVisibility(View.GONE);
					} else if (webView.getUrl().contains("facebook.com")) {
						fabdownload.setVisibility(View.GONE);
						fabopen.setVisibility(View.VISIBLE);
					} else {
						fabdownload.setVisibility(View.GONE);
						fabopen.setVisibility(View.GONE);
					}
				}
			}
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			mCustomViewCallback = callback;
			mTargetView.addView(view);
			mCustomView = view;
			mContentView.setVisibility(View.GONE);
			mTargetView.setVisibility(View.VISIBLE);
			mTargetView.bringToFront();
			((ActionBarActivity)getActivity()).getSupportActionBar().hide();
		}

		@Override
		public void onHideCustomView() {
			if (mCustomView == null)
				return;

			mCustomView.setVisibility(View.GONE);
			mTargetView.removeView(mCustomView);
			mCustomView = null;
			mTargetView.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();
			mContentView.setVisibility(View.VISIBLE);
			((ActionBarActivity)getActivity()).getSupportActionBar().show();
		}
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {}

	@Override
	public void onDownMotionEvent() {}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		if (scrollState == ScrollState.UP) {
			fabdownload.hide(true);
			fabopen.hide(true);
		} else if (scrollState == ScrollState.DOWN) {
			fabdownload.show(true);
			fabopen.show(true);
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
			(webView, (Object[])null);
		} catch (ClassNotFoundException cnfe) {}

		catch (NoSuchMethodException nsme) {}

		catch (InvocationTargetException ite) {}

		catch (IllegalAccessException iae) {}
	}

	public void onResume() {
		setColors();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		finished = true;
		super.onDestroy();
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		webView.saveState(savedInstanceState);
		super.onSaveInstanceState(savedInstanceState);
	}
}
