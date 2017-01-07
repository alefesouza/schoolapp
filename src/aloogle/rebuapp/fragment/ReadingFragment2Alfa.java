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
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import aloogle.rebuapp.R;
import aloogle.rebuapp.adapter.CardAdapterReading2;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.other.Other;

@SuppressLint("InflateParams")
public class ReadingFragment2Alfa extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view;
	ObservableListView list;
	ArrayList <String> obraarray = new ArrayList <String> ();
	ArrayList <String> autorarray = new ArrayList <String> ();
	ArrayList <String> categoriaaarray = new ArrayList <String> ();
	ArrayList <String> editoraarray = new ArrayList <String> ();
	ArrayList <String> quantidadearray = new ArrayList <String> ();
	int more, total, page, mLastFirstVisibleItem;
	boolean ismore, block, passed, nomore, limite, isall;
	String title;
	ViewGroup footer3, footer4, footer5, space;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	private SwipeRefreshLayout mSwipeLayout;

	private static String url;
	private static final String TAG_BOOKS = "livros";
	private static final String TAG_OBRA = "obra";
	private static final String TAG_AUTOR = "autor";
	private static final String TAG_CATEGORIA = "categoria";
	private static final String TAG_EDITORA = "editora";
	private static final String TAG_QUANTIDADE = "quantidade";

	JSONArray livros = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_reading, container, false);

		if (!getArguments().getString("categoria").equals("")) {
			url = "http://apps.aloogle.net/schoolapp/rebua/json/livros.php?categoria=" + getArguments().getString("categoria") + "&alfabetica=true";
		} else {
			url = "http://apps.aloogle.net/schoolapp/rebua/json/busca.php?q=" + getArguments().getString("query") + "&alfabetica=true";
		}

		if (getArguments().getString("categoria").equals("all") || !getArguments().getString("query").equals("")) {
			isall = true;
		} else {
			isall = false;
		}

		list = (ObservableListView)view.findViewById(R.id.list);
		if (Build.VERSION.SDK_INT > 10) {
			list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
			list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
		}

		if (Build.VERSION.SDK_INT >= 21) {
			ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
			if (Other.getColor2(getActivity(), 1).equals("005400")) {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF005400, 0xFF005400));
			} else {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF003061, 0xFF003061));
			}
		} else {
			ProgressBarCircularIndeterminate progressBarCompat = (ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar1);
			progressBarCompat.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		footer3 = (ViewGroup)inflatere.inflate(R.layout.footer3, list, false);
		if (Build.VERSION.SDK_INT >= 21) {
			ProgressBar progressBar = (ProgressBar)footer3.findViewById(R.id.progressBar1);
			if (Other.getColor2(getActivity(), 1).equals("005400")) {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF005400, 0xFF005400));
			} else {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF003061, 0xFF003061));
			}
		} else {
			ProgressBarCircularIndeterminate progressBarCompat = (ProgressBarCircularIndeterminate)footer3.findViewById(R.id.progressBar1);
			progressBarCompat.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
		}
		footer4 = (ViewGroup)inflatere.inflate(R.layout.no_more, list, false);
		footer5 = (ViewGroup)inflatere.inflate(R.layout.load_more, list, false);
		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addFooterView(footer3, null, false);
		list.addFooterView(space, null, false);

		more = 0;
		page = 0;
		total = 25;
		ismore = false;
		block = false;
		passed = false;

		mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.primary_color,
			R.color.primary_color_dark, R.color.primary_color,
			R.color.primary_color_dark);

		if (Other.isConnected(getActivity())) {
			new JSONParse().execute();
		}

		return view;
	}

	private class JSONParse extends AsyncTask <String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String...args) {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			mSwipeLayout.setRefreshing(false);
			try {
				try {
					if (ismore) {
						if (passed == false) {
							more += livros.length();
						}
					}
					block = false;
					passed = false;
					livros = json.getJSONArray(TAG_BOOKS);

					total = livros.length();
					limite = json.getBoolean("limite");

					if (limite) {
						list.removeFooterView(footer3);
						list.removeFooterView(space);
						list.addFooterView(footer4, null, false);
						list.addFooterView(space, null, false);
						nomore = true;
					}

					for (int i = 0; i < livros.length(); i++) {
						JSONObject c = livros.getJSONObject(i);

						String obra = c.getString(TAG_OBRA);
						String autor = c.getString(TAG_AUTOR);
						String categoria = null;
						if (getArguments().getString("categoria").equals("all") || !getArguments().getString("query").equals("")) {
							categoria = c.getString(TAG_CATEGORIA);
						} else {
							categoria = "";
						}
						String editora = c.getString(TAG_EDITORA);
						String quantidade = c.getString(TAG_QUANTIDADE);

						obraarray.add(obra);
						autorarray.add(autor);
						categoriaaarray.add(categoria);
						editoraarray.add(editora);
						quantidadearray.add(quantidade);
					}

					SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapterReading2(getActivity(), obraarray, autorarray, categoriaaarray, editoraarray, quantidadearray, isall));
					swingBottomInAnimationAdapter.setAbsListView(list);

					assert swingBottomInAnimationAdapter.getViewAnimator() != null;
					swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

					list.setAdapter(swingBottomInAnimationAdapter);
					list.setOnScrollListener(ReadingFragment2Alfa.this);
					list.setSelection(more);

					page += 1;

					if (!getArguments().getString("categoria").equals("")) {
						url = "http://apps.aloogle.net/schoolapp/rebua/json/livros.php?categoria=" + getArguments().getString("categoria") + "&alfabetica=true&page=" + String.valueOf(page);
					}

					list.setVisibility(View.VISIBLE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (list.getLastVisiblePosition() == list.getAdapter().getCount() - 1 && list.getChildAt(list.getChildCount() - 1).getBottom() <= list.getHeight()) {
			if (limite) {}
			else {
				if (block == false) {
					if (Other.isConnected(getActivity())) {
						ismore = true;
						new JSONParse().execute();
						block = true;
					} else {
						Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
						toast.show();
						list.removeFooterView(footer3);
						list.removeFooterView(space);
						footer5.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (Other.isConnected(getActivity())) {
									list.removeFooterView(footer5);
									list.removeFooterView(space);
									list.addFooterView(footer3);
									list.addFooterView(space);
									ismore = true;
									new JSONParse().execute();
								} else {
									Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
									toast.show();
								}
							}
						});
						list.addFooterView(footer5);
						list.addFooterView(space);
						block = true;
					}
				}
			}
		}

		if (list.getChildCount() > 0 && list.getChildAt(0).getTop() == 0 && list.getFirstVisiblePosition() == 0) {
			mSwipeLayout.setEnabled(true);
		} else {
			mSwipeLayout.setEnabled(false);
		}
	}

	public void onRefresh() {
		if (Other.isConnected(getActivity())) {
			list.setVisibility(View.GONE);
			obraarray.clear();
			autorarray.clear();
			categoriaaarray.clear();
			editoraarray.clear();
			quantidadearray.clear();
			if (!getArguments().getString("categoria").equals("")) {
				url = "http://apps.aloogle.net/schoolapp/rebua/json/livros.php?categoria=" + getArguments().getString("categoria") + "&alfabetica=true";
			} else {
				url = "http://apps.aloogle.net/schoolapp/rebua/json/busca.php?q=" + getArguments().getString("query") + "&alfabetica=true";
			}
			more = 0;
			ismore = false;
			block = true;
			if (nomore) {
				list.removeFooterView(footer4);
				list.removeFooterView(space);
				list.addFooterView(footer3, null, false);
				list.addFooterView(space, null, false);
			}
			new JSONParse().execute();
		} else {
			mSwipeLayout.setRefreshing(false);
			Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
