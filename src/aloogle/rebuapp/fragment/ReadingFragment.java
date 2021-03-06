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
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.activity.MainActivity;
import aloogle.rebuapp.activity.ReadingActivity;
import aloogle.rebuapp.adapter.CardAdapterReading;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.Other;
import aloogle.rebuapp.other.CustomTextView;
import android.widget.RelativeLayout;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

@SuppressLint("InflateParams")
public class ReadingFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view, wnet;
	ObservableListView list;
	ArrayList <String> livrosarray = new ArrayList <String> ();
	ArrayList <String> nomearray = new ArrayList <String> ();
	ArrayList <String> quantidadearray = new ArrayList <String> ();
	ArrayList <String> amostrasarray = new ArrayList <String> ();
	String title, suggestion;
	boolean alreadyLoaded;
	ViewGroup header, headerrecados, space;
	boolean fromnonet;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	RelativeLayout relative;

	private SwipeRefreshLayout mSwipeLayout;

	private static String url;
	private static final String TAG_CATEG = "categorias";
	private static final String TAG_CATEGORIA = "categoria";
	private static final String TAG_NOME = "nome";
	private static final String TAG_QUANTIDADE = "quantidade";
	private static final String TAG_AMOSTRAS = "amostras";

	JSONArray livros = null;
	JSONArray sugestoes = null;

	ArrayList <String> suggestionarray = new ArrayList <String> ();
	private static final String TAG_SUGESTAO = "sugestao";
	private SimpleCursorAdapter mAdapter;

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

		url = "http://apps.aloogle.net/schoolapp/rebua/json/biblioteca.php";

		list = (ObservableListView)view.findViewById(R.id.list);

		fromnonet = false;
		relative = (RelativeLayout)view.findViewById(R.id.fragment);

		if (!getActivity().getIntent().hasExtra("widgetpos")) {
			if (Build.VERSION.SDK_INT > 10) {
				list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
			}

			if (!MainActivity.home) {
				MainActivity.titulo = "Biblioteca";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(6, true);
				MainActivity.pos = 6;
			}

			FloatingActionButton fabpanel = (FloatingActionButton)getActivity().findViewById(R.id.fabpanel);
			fabpanel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.openPanel(getActivity());
				}
			});
		} else {
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Biblioteca");
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		header = (ViewGroup)inflatere.inflate(R.layout.header2, list, false);
		header.findViewById(R.id.header2).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ReadingActivity.class);
				intent.putExtra("titulo", "Todos");
				intent.putExtra("categoria", "all");
				startActivity(intent);
			}
		});
		headerrecados = (ViewGroup)inflatere.inflate(R.layout.header2, list, false);
		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addHeaderView(headerrecados, null, false);
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

		if (Build.VERSION.SDK_INT > 10) {
			final String[]from = new String[]{
				"categoryName"
			};
			final int[]to = new int[]{
				R.id.text1
			};
			mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}

		if (Other.isConnected(getActivity())) {
			new JSONParse().execute();
		} else {
			wnet = getActivity().getLayoutInflater().inflate(R.layout.littlecard, null);
			CustomTextView warning = (CustomTextView)wnet.findViewById(R.id.text);
			warning.setText("Você precisa estar conectado à internet.");
			if (Build.VERSION.SDK_INT >= 21) {
				progressBar.setVisibility(View.GONE);
			} else {
				progressBarCompat.setVisibility(View.GONE);
			}
			list.setVisibility(View.GONE);
			if (!fromnonet) {
				relative.addView(wnet);
				fromnonet = true;
			}
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
			if (fromnonet) {
				relative.removeView(wnet);
				fromnonet = false;
			}
			list.setVisibility(View.VISIBLE);
			try {
				try {
					livros = json.getJSONArray(TAG_CATEG);

					if (!alreadyLoaded) {
						for (int i = 0; i < livros.length(); i++) {
							JSONObject c = livros.getJSONObject(i);

							String categoria = c.getString(TAG_CATEGORIA);
							String nome = c.getString(TAG_NOME);
							String quantidade = c.getString(TAG_QUANTIDADE);
							String amostras = c.getString(TAG_AMOSTRAS);

							livrosarray.add(categoria);
							nomearray.add(nome);
							quantidadearray.add(quantidade);
							amostrasarray.add(amostras);
						}
						alreadyLoaded = true;
					}

					SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapterReading(getActivity(), livrosarray, nomearray, quantidadearray, amostrasarray));
					swingBottomInAnimationAdapter.setAbsListView(list);

					assert swingBottomInAnimationAdapter.getViewAnimator() != null;
					swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

					list.setAdapter(swingBottomInAnimationAdapter);
					list.setOnScrollListener(ReadingFragment.this);

					TextView total = (TextView)header.findViewById(R.id.text);
					total.setText(Html.fromHtml(json.getString("total")));

					if (!json.getString("recadosbotao").equals("")) {
						TextView recados = (TextView)headerrecados.findViewById(R.id.text);
						recados.setText(Html.fromHtml(json.getString("recadosbotao")));
						final String recadostitulo = json.getString("recadostitulo");
						final String recadosurl = json.getString("recadosurl");
						final String recadossettings = json.getString("recadossettings");
						headerrecados.findViewById(R.id.header2).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(), FragmentActivity.class);
								intent.putExtra("fragment", 6);
								intent.putExtra("titulo", recadostitulo);
								intent.putExtra("url", recadosurl);
								intent.putExtra("settings", recadossettings);
								startActivity(intent);
							}
						});
					} else {
						list.removeHeaderView(headerrecados);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu.findItem(R.id.menu_search) == null) {
			inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.biblioteca_menu, menu);
		} else {
			menu.findItem(R.id.menu_notification).setVisible(true);
		}

		MenuItem searchItem = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint("Buscar livro, autor ou editora");

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				try {
					if (Other.isConnected(getActivity())) {
						Intent intent = new Intent(getActivity(), ReadingActivity.class);
						intent.putExtra("titulo", "Busca: " + s);
						intent.putExtra("query", URLEncoder.encode(s, "UTF-8"));
						startActivity(intent);
					} else {
						Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_SHORT);
						toast.show();
					}
				} catch (UnsupportedEncodingException e) {}
				return false;
			}

			@Override
			public boolean onQueryTextChange(final String s) {
				if (Build.VERSION.SDK_INT > 10) {
					if (Other.isConnected(getActivity())) {
						suggestion = s;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if (s.equals(suggestion)) {
									new JSONParseSearch().execute();
								}
							}
						}, 1000);
					}
				}
				return false;
			}
		});

		searchView.setSuggestionsAdapter(mAdapter);

		if (Build.VERSION.SDK_INT > 10) {
			searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
				@Override
				public boolean onSuggestionClick(int position) {
					try {
						if (Other.isConnected(getActivity())) {
							Intent intent = new Intent(getActivity(), ReadingActivity.class);
							intent.putExtra("titulo", "Busca: " + suggestionarray.get(position));
							intent.putExtra("query", URLEncoder.encode(suggestionarray.get(position), "UTF-8"));
							startActivity(intent);
						} else {
							Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_SHORT);
							toast.show();
						}
					} catch (UnsupportedEncodingException e) {}
					return true;
				}

				@Override
				public boolean onSuggestionSelect(int position) {
					try {
						if (Other.isConnected(getActivity())) {
							Intent intent = new Intent(getActivity(), ReadingActivity.class);
							intent.putExtra("titulo", "Busca: " + suggestionarray.get(position));
							intent.putExtra("query", URLEncoder.encode(suggestionarray.get(position), "UTF-8"));
							startActivity(intent);
						} else {
							Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_SHORT);
							toast.show();
						}
					} catch (UnsupportedEncodingException e) {}
					return true;
				}
			});
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			if (Other.isConnected(getActivity())) {
				alreadyLoaded = false;
				list.setVisibility(View.GONE);
				ClearAll();
				new JSONParse().execute();
			} else {
				mSwipeLayout.setRefreshing(false);
				Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
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

	public void ClearAll() {
		livrosarray.clear();
		nomearray.clear();
		quantidadearray.clear();
		amostrasarray.clear();
	}

	public void onRefresh() {
		if (Other.isConnected(getActivity())) {
			alreadyLoaded = false;
			list.setVisibility(View.GONE);
			ClearAll();
			new JSONParse().execute();
		} else {
			mSwipeLayout.setRefreshing(false);
			Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
			toast.show();
		}
	}

	private class JSONParseSearch extends AsyncTask < String, String, JSONObject > {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String...args) {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl("http://apps.aloogle.net/schoolapp/rebua/json/sugestoes.php?q=" + suggestion.replace(" ", "%20"));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				try {
					suggestionarray.clear();
					sugestoes = json.getJSONArray("sugestoes");
					for (int i = 0; i < sugestoes.length(); i++) {
						JSONObject c = sugestoes.getJSONObject(i);

						String categoria = c.getString(TAG_SUGESTAO);
						suggestionarray.add(categoria);
					}
					populateAdapter();
				} catch (JSONException e) {}
			} catch (Exception e) {}
		}
	}

	private void populateAdapter() {
		final MatrixCursor c = new MatrixCursor(new String[]{
				BaseColumns._ID, 			"categoryName"
			});
		for (int i = 0; i < suggestionarray.size(); i++) {
			if (!suggestionarray.get(i).toString().equals("")) {
				c.addRow(new Object[]{
					i, 				suggestionarray.get(i).toString()
				});
			}
		}
		mAdapter.changeCursor(c);
	}
}
