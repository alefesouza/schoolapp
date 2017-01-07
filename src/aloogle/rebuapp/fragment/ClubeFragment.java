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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.AbsListView;
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
import aloogle.rebuapp.adapter.CardAdapter;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;

@SuppressLint("InflateParams")
public class ClubeFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view, wnet;
	ObservableListView list;
	ArrayList <String> idarray = new ArrayList <String> ();
	ArrayList <String> tipoarray = new ArrayList <String> ();
	ArrayList <String> dataarray = new ArrayList <String> ();
	ArrayList <String> tituloarray = new ArrayList <String> ();
	ArrayList <String> descricaoarray = new ArrayList <String> ();
	String title, clube;
	ViewGroup header, headertime, space;
	boolean fromnonet, topanel, alreadyLoaded;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	SharedPreferences preferences;
	RelativeLayout relative;

	private SwipeRefreshLayout mSwipeLayout;

	private static String url;
	private static final String TAG_INFO = "info";
	private static final String TAG_ID = "id";
	private static final String TAG_TIPO = "tipo";
	private static final String TAG_DATA = "data";
	private static final String TAG_TITULO = "titulo";
	private static final String TAG_DESCRICAO = "descricao";

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
		clube = preferences.getString("clubeRoom", "none");

		url = "http://apps.aloogle.net/schoolapp/rebua/json/main.php?clube=" + clube;

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
				MainActivity.titulo = "Clube";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(2, true);
				MainActivity.pos = 2;
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
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Clube");
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		header = (ViewGroup)inflatere.inflate(R.layout.header2, list, false);
		CustomTextView text = (CustomTextView)header.findViewById(R.id.text);
		text.setText(Other.getClube(getActivity()));
		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addHeaderView(header, null, false);
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
			if (!getActivity().getIntent().hasExtra("widgetpos")) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						MainActivity.setRefreshActionButtonState(true, getActivity());
					}
				}, 100);
			}
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
			if (!getActivity().getIntent().hasExtra("widgetpos")) {
				MainActivity.setRefreshActionButtonState(false, getActivity());
			}

			if (headertime != null) {
				list.removeHeaderView(headertime);
			}

			SharedPreferences.Editor editor = preferences.edit();
			Date currentDate = new Date();
			editor.putLong("lastrecados" + clube, currentDate.getTime());
			editor.commit();
			try {
				if (preferences.contains("recados" + clube)) {
					if (preferences.getString("recados" + clube, "").equals(json.toString())) {
						if (headertime != null) {
							ClearAll();
							offlineJSON(true);
						}
					} else {
						ClearAll();
						editor.putString("recados" + clube, json.toString());
						editor.commit();
						alreadyLoaded = false;
						JSONConstructor(json);
					}
				} else {
					editor.putString("recados" + clube, json.toString());
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
				info = json.getJSONArray(TAG_INFO);

				for (int i = 0; i < info.length(); i++) {
					JSONObject c = info.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String tipo = c.getString(TAG_TIPO);
					String data = c.getString(TAG_DATA);
					String titulo = c.getString(TAG_TITULO);
					String descricao = c.getString(TAG_DESCRICAO);

					idarray.add(id);
					tipoarray.add(tipo);
					dataarray.add(data);
					tituloarray.add(titulo);
					descricaoarray.add(descricao);
				}
				alreadyLoaded = true;
			}

			SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapter(getActivity(), idarray, tipoarray, dataarray, tituloarray, descricaoarray, false));
			swingBottomInAnimationAdapter.setAbsListView(list);

			assert swingBottomInAnimationAdapter.getViewAnimator() != null;
			swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

			list.setAdapter(swingBottomInAnimationAdapter);
			list.setOnScrollListener(ClubeFragment.this);

			header.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						if (clube.equals("none")) {
							Intent settings = new Intent(getActivity(), aloogle.rebuapp.activity.FragmentActivity.class);
							settings.putExtra("fragment", 0);
							startActivity(settings);
						} else if (!json.getString("grupo").equals("")) {
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://group/" + json.getString("grupo")));
								startActivity(intent);
							} catch (Exception e) {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse("http://www.facebook.com/groups/" + json.getString("grupo")));
								startActivity(intent);
							}
						}
					} catch (JSONException e) {}
				}
			});
		} catch (JSONException e) {}
	}

	public void offlineJSON(boolean haveNet) {
		if (preferences.contains("recados" + clube)) {
			try {
				String recados = preferences.getString("recados" + clube, "none");
				JSONObject json = new JSONObject(recados);

				JSONConstructor(json);

				if (!haveNet) {
					if (headertime == null) {
						headertime = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.header2, list, false);
						CustomTextView time = (CustomTextView)headertime.findViewById(R.id.text);
						time.setText("Última atualização há " + Other.compareDate(preferences.getLong("lastrecados" + clube, 0)));
						list.addHeaderView(headertime, null, false);
					} else {
						CustomTextView time = (CustomTextView)headertime.findViewById(R.id.text);
						time.setText("Última atualização há " + Other.compareDate(preferences.getLong("lastrecados" + clube, 0)));
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
					warning.setText("Você precisa estar conectado à internet pelo menos uma vez para salvar os recados deste clube.");
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
		idarray.clear();
		tipoarray.clear();
		dataarray.clear();
		tituloarray.clear();
		descricaoarray.clear();
	}

	public void onResume() {
		if (!preferences.getString("clubeRoom", "none").equals(clube)) {
			ClearAll();
			clube = preferences.getString("clubeRoom", "none");
			url = "http://apps.aloogle.net/schoolapp/rebua/json/main.php?sala=" + clube;
			CustomTextView text = (CustomTextView)header.findViewById(R.id.text);
			text.setText(Other.getClube(getActivity()));
			if (Build.VERSION.SDK_INT >= 21) {
				progressBar.setVisibility(View.VISIBLE);
			} else {
				progressBarCompat.setVisibility(View.VISIBLE);
			}
			list.setVisibility(View.GONE);
			alreadyLoaded = false;
			if (Other.isConnected(getActivity())) {
				offlineJSON(true);
				new JSONParse().execute();
			} else {
				offlineJSON(false);
			}
		}

		if (topanel) {
			if (Other.isConnected(getActivity())) {
				alreadyLoaded = false;
				new JSONParse().execute();
				topanel = false;
			}
		}
		super.onResume();
	}
}
