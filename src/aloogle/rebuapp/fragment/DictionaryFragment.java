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
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import aloogle.rebuapp.adapter.CardAdapterDictionary;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.Other;
import aloogle.rebuapp.other.CustomTextView;

@SuppressLint("InflateParams")
public class DictionaryFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view, wnet;
	ObservableListView list;
	ArrayList <String> generoarray = new ArrayList <String> ();
	ArrayList <String> termoarray = new ArrayList <String> ();
	ArrayList <String> significadoarray = new ArrayList <String> ();
	String title, suggestion;
	boolean alreadyLoaded, fromnonet, haveFooter;
	ViewGroup space, footer, credits;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	RelativeLayout relative;

	private SwipeRefreshLayout mSwipeLayout;

	private static String url;
	private static final String TAG_DESCRICOES = "descricoes";
	private static final String TAG_GENERO = "genero";
	private static final String TAG_TERMO = "termo";
	private static final String TAG_SIGNIFICADO = "significado";

	JSONArray significado = null;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 	Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_main, container, false);

		list = (ObservableListView)view.findViewById(R.id.list);

		fromnonet = false;
		haveFooter = false;
		relative = (RelativeLayout)view.findViewById(R.id.fragment);

		if (!getActivity().getIntent().hasExtra("palavra")) {
			if (Build.VERSION.SDK_INT > 10) {
				list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
			}

			if (!MainActivity.home) {
				MainActivity.titulo = "Dicionário";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(8, true);
				MainActivity.pos = 8;
			}
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		footer = (ViewGroup)inflatere.inflate(R.layout.littlecard, list, false);
		credits = (ViewGroup)inflatere.inflate(R.layout.header2, list, false);
		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addFooterView(space, null, false);

		mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.primary_color, 		R.color.primary_color_dark, R.color.primary_color, 		R.color.primary_color_dark);

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

		if (!getActivity().getIntent().hasExtra("palavra")) {
			FloatingActionButton fabpanel = (FloatingActionButton)getActivity().findViewById(R.id.fabpanel);
			fabpanel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.openPanel(getActivity());
				}
			});
		}

		if (Build.VERSION.SDK_INT > 10) {
			final String[]from = new String[]{
				"categoryName"
			};
			final int[]to = new int[]{
				R.id.text1
			};

			mAdapter = new SimpleCursorAdapter(getActivity(), 				R.layout.simple_list_item_1, 				null, 				from, 				to, 				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}

		if (!getActivity().getIntent().hasExtra("palavra")) {
			wnet = getActivity().getLayoutInflater().inflate(R.layout.littlecard, null);
			CustomTextView warning = (CustomTextView)wnet.findViewById(R.id.text);
			warning.setText("Toque no ícone de lupa para buscar o significado de uma palavra.");
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
		} else {
			String palavra = getActivity().getIntent().getStringExtra("palavra");
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Dicionário: " + palavra);
			url = "http://apps.aloogle.net/schoolapp/rebua/json/dicionario.php?palavra=" + palavra;
			new JSONParse().execute();
		}
		return view;
	}

	private class JSONParse extends AsyncTask < String, String, JSONObject > {
		@Override
		protected void onPreExecute() {
			if (!getActivity().getIntent().hasExtra("palavra")) {
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

			if (!getActivity().getIntent().hasExtra("palavra")) {
				MainActivity.setRefreshActionButtonState(false, getActivity());
			}
			if (fromnonet) {
				relative.removeView(wnet);
				fromnonet = false;
			}
			list.setVisibility(View.VISIBLE);
			try {
				try {
					if (!getActivity().getIntent().hasExtra("palavra")) {
						if (!json.getString("nome").equals("")) {
							MainActivity.titulo = "Dicionário: " + json.getString("nome");
							((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
						}
					} else {
						((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Dicionário: " + json.getString("nome"));
					}

					significado = json.getJSONArray(TAG_DESCRICOES);

					if (!alreadyLoaded) {
						for (int i = 0; i < significado.length(); i++) {
							JSONObject c = significado.getJSONObject(i);

							String genero = c.getString(TAG_GENERO);
							String termo = c.getString(TAG_TERMO);
							String significado = c.getString(TAG_SIGNIFICADO);

							generoarray.add(genero);
							termoarray.add(termo);
							significadoarray.add(significado);
						}
						alreadyLoaded = true;
					}

					SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapterDictionary(getActivity(), generoarray, termoarray, significadoarray));
					swingBottomInAnimationAdapter.setAbsListView(list);

					assert swingBottomInAnimationAdapter.getViewAnimator() != null;
					swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

					list.setAdapter(swingBottomInAnimationAdapter);
					list.setOnScrollListener(DictionaryFragment.this);

					TextView creditos = (TextView)credits.findViewById(R.id.text);
					creditos.setTextSize(12);
					creditos.setText(json.getString("creditos"));
					final String creditslink = json.getString("creditoslink");
					credits.findViewById(R.id.header2).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(creditslink));
							activity.startActivity(intent);
						}
					});

					if (json.getString("orig").equals("")) {
						if (!json.getString("nome").equals("")) {
							list.removeFooterView(footer);
							list.removeFooterView(credits);
							list.removeFooterView(space);
							list.addFooterView(credits);
							list.addFooterView(space);
						} else {
							list.removeFooterView(footer);
							list.removeFooterView(credits);
							list.removeFooterView(space);
						}
					} else {
						list.removeFooterView(footer);
						list.removeFooterView(credits);
						list.removeFooterView(space);
						list.addFooterView(footer);
						list.addFooterView(credits);
						list.addFooterView(space);
						TextView orig = (TextView)footer.findViewById(R.id.text);
						orig.setText(Html.fromHtml(json.getString("orig")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (!getActivity().getIntent().hasExtra("palavra")) {
			inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.biblioteca_menu, menu);

			MenuItem searchItem = menu.findItem(R.id.menu_search);
			SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
			searchView.setQueryHint("Buscar palavra");

			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String s) {
					try {
						if (Other.isConnected(getActivity())) {
							MainActivity.titulo = "Dicionário: " + s;
							((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
							alreadyLoaded = false;
							list.setVisibility(View.GONE);
							url = "http://apps.aloogle.net/schoolapp/rebua/json/dicionario.php?palavra=" + URLEncoder.encode(s, "UTF-8");
							ClearAll();
							new JSONParse().execute();
							if (Build.VERSION.SDK_INT > 10) {
								suggestionarray.clear();
							}
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
										if (!MainActivity.isTouch) {
											new JSONParseSearch().execute();
										}
									}
								}
							}, 1000);
						}
					}
					return false;
				}
			});

			if (Build.VERSION.SDK_INT > 10) {
				searchView.setSuggestionsAdapter(mAdapter);
				searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
					@Override
					public boolean onSuggestionClick(int position) {
						try {
							if (Other.isConnected(getActivity())) {
								MainActivity.titulo = "Dicionário: " + suggestionarray.get(position).toString();
								((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
								alreadyLoaded = false;
								list.setVisibility(View.GONE);
								url = "http://apps.aloogle.net/schoolapp/rebua/json/dicionario.php?palavra=" + URLEncoder.encode(suggestionarray.get(position).toString(), "UTF-8");
								ClearAll();
								new JSONParse().execute();
								suggestionarray.clear();
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
								MainActivity.titulo = "Dicionário: " + suggestionarray.get(position).toString();
								((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
								alreadyLoaded = false;
								list.setVisibility(View.GONE);
								url = "http://apps.aloogle.net/schoolapp/rebua/json/dicionario.php?palavra=" + URLEncoder.encode(suggestionarray.get(position).toString(), "UTF-8");
								ClearAll();
								new JSONParse().execute();
								suggestionarray.clear();
							} else {
								Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_SHORT);
								toast.show();
							}
						} catch (UnsupportedEncodingException e) {}
						return true;
					}
				});
			}
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
	public void onScroll(AbsListView view, int firstVisibleItem, 	int visibleItemCount, int totalItemCount) {
		if (list.getChildCount() > 0 && list.getChildAt(0).getTop() == 0 && list.getFirstVisiblePosition() == 0) {
			mSwipeLayout.setEnabled(true);
		} else {
			mSwipeLayout.setEnabled(false);
		}
	}

	public void ClearAll() {
		generoarray.clear();
		termoarray.clear();
		significadoarray.clear();
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
			JSONObject json = jParser.getJSONFromUrl("http://apps.aloogle.net/schoolapp/rebua/json/dicsugestoes.php?q=" + suggestion.replace(" ", "%20"));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				try {
					if (!MainActivity.isTouch) {
						suggestionarray.clear();
						sugestoes = json.getJSONArray("sugestoes");
						for (int i = 0; i < sugestoes.length(); i++) {
							JSONObject c = sugestoes.getJSONObject(i);

							String categoria = c.getString(TAG_SUGESTAO);
							suggestionarray.add(categoria);
						}
						populateAdapter();
					}
				} catch (JSONException e) {}
			} catch (Exception e) {}
		}
	}

	private void populateAdapter() {
		if (!MainActivity.isTouch) {
			final MatrixCursor c = new MatrixCursor(new String[]{
					BaseColumns._ID, 				"categoryName"
				});
			for (int i = 0; i < suggestionarray.size(); i++) {
				if (!suggestionarray.get(i).toString().equals("")) {
					c.addRow(new Object[]{
						i, 					suggestionarray.get(i).toString()
					});
					mAdapter.changeCursor(c);
				}
			}
		}
	}
}
