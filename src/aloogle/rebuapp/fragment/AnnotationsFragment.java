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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.activity.MainActivity;
import aloogle.rebuapp.adapter.CardAdapterAnnotation;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;

public class AnnotationsFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view;
	ObservableListView list;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	SharedPreferences preferences;
	String annotations;
	boolean alreadyLoaded;
	ArrayList <String> jsonarray = new ArrayList <String> ();
	ArrayList <String> idarray = new ArrayList <String> ();
	ArrayList <String> tituloarray = new ArrayList <String> ();
	ArrayList <String> descricaoarray = new ArrayList <String> ();

	private SwipeRefreshLayout mSwipeLayout;

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
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		view = inflater.inflate(R.layout.fragment_main, container, false);

		alreadyLoaded = false;

		list = (ObservableListView)view.findViewById(R.id.list);

		if (!getActivity().getIntent().hasExtra("widgetpos")) {
			if (!MainActivity.home) {
				MainActivity.titulo = "Anotações";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(8, true);
				MainActivity.pos = 8;
			}

			if (Build.VERSION.SDK_INT > 10) {
				list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
			}
		} else {
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Anotações");
		}

		boolean warningAnnotation = preferences.getBoolean("warningAnnotation", false);
		if (!warningAnnotation) {
			final AlertDialog dialogannotation = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogannotation)
				.setPositiveButton("OK", null)
				.create();

			dialogannotation.setCanceledOnTouchOutside(false);

			dialogannotation.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogannotation.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("warningAnnotation", true);
							editor.commit();
							dialogannotation.dismiss();
						}
					});
				}
			});
			dialogannotation.show();
		}

		annotations = preferences.getString("annotations", "");

		if (Build.VERSION.SDK_INT >= 21) {
			progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
			if (Other.getColor2(getActivity(), 1).equals("005400")) {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF005400, 0xFF005400));
			} else {
				progressBar.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFF003061, 0xFF003061));
			}
			progressBar.setVisibility(View.GONE);
		} else {
			progressBarCompat = (ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar1);
			progressBarCompat.setBackgroundColor(Color.parseColor("#ff" + Other.getColor2(getActivity(), 1)));
			progressBarCompat.setVisibility(View.GONE);
		}

		mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeResources(R.color.primary_color, R.color.primary_color_dark, R.color.primary_color, R.color.primary_color_dark);

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		ViewGroup header = (ViewGroup)inflatere.inflate(R.layout.header2, list, false);
		header.findViewById(R.id.header2).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FragmentActivity.class);
				intent.putExtra("fragment", 4);
				startActivity(intent);
			}
		});
		CustomTextView text = (CustomTextView)header.findViewById(R.id.text);
		text.setText("Adicionar anotação");
		ViewGroup space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addHeaderView(header, null, false);
		list.addFooterView(space, null, false);

		Go();
		return view;
	}

	public void Go() {
		if (!alreadyLoaded) {
			String[]anotacoes = annotations.split("\\$\\%\\#");
			for (int i = 0; i < anotacoes.length; i++) {
				try {
					JSONObject json = new JSONObject(anotacoes[i]);
					jsonarray.add(anotacoes[i]);
					idarray.add(json.getString("id"));
					tituloarray.add(json.getString("titulo"));
					descricaoarray.add(json.getString("descricao"));
				} catch (JSONException e) {}
			}
			alreadyLoaded = true;
		}

		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new CardAdapterAnnotation(getActivity(), jsonarray, idarray, tituloarray, descricaoarray));
		swingBottomInAnimationAdapter.setAbsListView(list);

		assert swingBottomInAnimationAdapter.getViewAnimator() != null;
		swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

		list.setAdapter(swingBottomInAnimationAdapter);
		list.setOnScrollListener(AnnotationsFragment.this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			alreadyLoaded = false;
			annotations = preferences.getString("annotations", "");
			ClearAll();
			Go();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void ClearAll() {
		jsonarray.clear();
		idarray.clear();
		tituloarray.clear();
		descricaoarray.clear();
	}

	public void onRefresh() {
		alreadyLoaded = false;
		annotations = preferences.getString("annotations", "");
		ClearAll();
		Go();
		mSwipeLayout.setRefreshing(false);
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

	public void onResume() {
		if (!preferences.getString("annotations", "").equals("")) {
			alreadyLoaded = false;
			annotations = preferences.getString("annotations", "");
			ClearAll();
			Go();
		}
		super.onResume();
	}
}
