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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.activity.MainActivity;
import aloogle.rebuapp.adapter.CardAdapterCartaz;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;

@SuppressLint("InflateParams")
public class CartazFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view, wnet;
	ObservableListView list;
	ArrayList <String> tituloarray = new ArrayList <String> ();
	ArrayList <String> settingsarray = new ArrayList <String> ();
	ArrayList <String> urlarray = new ArrayList <String> ();
	String title;
	ViewGroup headertime, space;
	boolean fromnonet, topanel, alreadyLoaded;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	SharedPreferences preferences;
	RelativeLayout relative;

	private SwipeRefreshLayout mSwipeLayout;

	private static String url;
	private static final String TAG_CARTAZES = "cartazes";
	private static final String TAG_TITULO = "titulo";
	private static final String TAG_SETTINGS = "settings";
	private static final String TAG_URL = "url";

	JSONArray info = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_main, container, false);

		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		url = "http://apps.aloogle.net/schoolapp/rebua/json/cartazes.php";

		list = (ObservableListView)view.findViewById(R.id.list);

		fromnonet = false;
		topanel = false;
		relative = (RelativeLayout)view.findViewById(R.id.fragment);

		if (!getActivity().getIntent().hasExtra("widgetpos")) {
			if (Build.VERSION.SDK_INT > 10) {
				list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
			}

			if (!MainActivity.home) {
				MainActivity.titulo = "Cartazes";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(10, true);
				MainActivity.pos = 10;
			}

			FloatingActionButton fabpanel = (FloatingActionButton)getActivity().findViewById(R.id.fabpanel);

			fabpanel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.openPanel(getActivity());
					topanel = true;
				}
			});
		} else {
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Cartazes");
		}

		boolean warningCartazes = preferences.getBoolean("warningCartazes", false);
		if (!warningCartazes) {
			final AlertDialog dialogcartaz = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogcartaz)
				.setPositiveButton("OK", null)
				.create();

			dialogcartaz.setCanceledOnTouchOutside(false);

			dialogcartaz.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogcartaz.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("warningCartazes", true);
							editor.commit();
							dialogcartaz.dismiss();
						}
					});
				}
			});
			dialogcartaz.show();
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addFooterView(space, null, false);

		mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.primary_color, R.color.primary_color_dark, R.color.primary_color, R.color.primary_color_dark);

		if (Build.VERSION.SDK_INT >= 21) {
			progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
			if (Other.getColor2(getActivity(), 1).equals("005400")) {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF005400, 0xFF005400));
			} else {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF003061, 0xFF003061));
			}
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBarCompat = (ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar1);
			progressBarCompat.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
			progressBarCompat.setVisibility(View.VISIBLE);
		}

		if (Other.isConnected(getActivity())) {
			offlineJSON(true);
			new JSONParse().execute();
		} else {
			offlineJSON(false);
		}
		return view;
	}

	private class JSONParse extends AsyncTask < String, String, JSONObject > {
		@Override
		protected void onPreExecute() {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					MainActivity.setRefreshActionButtonState(true, getActivity());
				}
			}, 100);
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String...args) {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url.replace(" ", "%20"));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			if (Build.VERSION.SDK_INT >= 21) {
				progressBar.setVisibility(View.GONE);
			} else {
				progressBarCompat.setVisibility(View.GONE);
			}
			mSwipeLayout.setRefreshing(false);
			MainActivity.setRefreshActionButtonState(false, getActivity());

			if (headertime != null) {
				list.removeHeaderView(headertime);
			}

			SharedPreferences.Editor editor = preferences.edit();
			Date currentDate = new Date();
			editor.putLong("lastcartazes", currentDate.getTime());
			editor.commit();
			try {
				if (preferences.contains("cartazes")) {
					if (preferences.getString("cartazes", "").equals(json.toString())) {
						if (headertime != null) {
							ClearAll();
							offlineJSON(true);
						}
					} else {
						ClearAll();
						editor.putString("cartazes", json.toString());
						editor.commit();
						alreadyLoaded = false;
						JSONConstructor(json);
					}
				} else {
					editor.putString("cartazes", json.toString());
					editor.commit();
					JSONConstructor(json);
				}
			} catch (Exception e) {}
		}
	}

	public void JSONConstructor(final JSONObject json) {
		try {
			if (fromnonet) {
				relative.removeView(wnet);
				fromnonet = false;
			}
			list.setVisibility(View.VISIBLE);

			if (!alreadyLoaded) {
				info = json.getJSONArray(TAG_CARTAZES);

				for (int i = 0; i < info.length(); i++) {
					JSONObject c = info.getJSONObject(i);

					String titulo = c.getString(TAG_TITULO);
					String settings = c.getString(TAG_SETTINGS);
					String url = c.getString(TAG_URL);

					tituloarray.add(titulo);
					settingsarray.add(settings);
					urlarray.add(url);
				}
				alreadyLoaded = true;
			}

			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapterCartaz(getActivity(), tituloarray, settingsarray, urlarray));
			swingBottomInAnimationAdapter.setAbsListView(list);

			assert swingBottomInAnimationAdapter.getViewAnimator() != null;
			swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

			list.setAdapter(swingBottomInAnimationAdapter);
			list.setOnScrollListener(CartazFragment.this);
		} catch (JSONException e) {}
	}

	public void offlineJSON(boolean haveNet) {
		if (preferences.contains("cartazes")) {
			try {
				String cartazes = preferences.getString("cartazes", "none");
				JSONObject json = new JSONObject(cartazes);

				JSONConstructor(json);

				if (!haveNet) {
					if (headertime == null) {
						headertime = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.header2, list, false);
						CustomTextView time = (CustomTextView)headertime.findViewById(R.id.text);
						time.setText("Última atualização há " + Other.compareDate(preferences.getLong("lastcartazes", 0)));
						list.addHeaderView(headertime, null, false);
					} else {
						CustomTextView time = (CustomTextView)headertime.findViewById(R.id.text);
						time.setText("Última atualização há " + Other.compareDate(preferences.getLong("lastcartazes", 0)));
					}
				}

				if (Build.VERSION.SDK_INT >= 21) {
					progressBar.setVisibility(View.GONE);
				} else {
					progressBarCompat.setVisibility(View.GONE);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			if (!haveNet) {
				if (Build.VERSION.SDK_INT >= 21) {
					progressBar.setVisibility(View.GONE);
				} else {
					progressBarCompat.setVisibility(View.GONE);
				}
				if (!fromnonet) {
					wnet = getActivity().getLayoutInflater().inflate(R.layout.littlecard, null);
					CustomTextView warning = (CustomTextView)wnet.findViewById(R.id.text);
					warning.setText("Você precisa estar conectado à internet pelo menos uma vez para salvar os cartazes.");
					list.setVisibility(View.GONE);
					relative.addView(wnet);
					fromnonet = true;
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			if (Other.isConnected(getActivity())) {
				alreadyLoaded = false;
				new JSONParse().execute();
			} else {
				mSwipeLayout.setRefreshing(false);
				Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (list.getChildCount() > 0 && list.getChildAt(0).getTop() == 0 && list.getFirstVisiblePosition() == 0) {
			mSwipeLayout.setEnabled(true);
		} else {
			mSwipeLayout.setEnabled(false);
		}
	}

	public void onRefresh() {
		if (Other.isConnected(getActivity())) {
			alreadyLoaded = false;
			new JSONParse().execute();
		} else {
			mSwipeLayout.setRefreshing(false);
			Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
			toast.show();
		}
	}

	public void ClearAll() {
		tituloarray.clear();
		settingsarray.clear();
		urlarray.clear();
	}
}
