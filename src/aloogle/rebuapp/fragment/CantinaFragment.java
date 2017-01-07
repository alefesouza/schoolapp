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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.melnykov.fab.FloatingActionButton;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.activity.MainActivity;
import aloogle.rebuapp.adapter.CardAdapterCartaz;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;

@SuppressLint("InflateParams")
public class CantinaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	Activity activity;
	View view;
	ObservableListView list;
	ViewGroup space, page, image, promocoes, cardapio;
	ProgressBar progressBar;
	ProgressBarCircularIndeterminate progressBarCompat;
	ArrayList <String> tituloarray = new ArrayList <String> ();
	ArrayList <String> settingsarray = new ArrayList <String> ();
	ArrayList <String> urlarray = new ArrayList <String> ();

	private SwipeRefreshLayout mSwipeLayout;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

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

		list = (ObservableListView)view.findViewById(R.id.list);

		if (!getActivity().getIntent().hasExtra("widgetpos")) {
			if (Build.VERSION.SDK_INT > 10) {
				list.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				list.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));
			}

			if (!MainActivity.home) {
				MainActivity.titulo = "Cantina";
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(MainActivity.titulo);
				MainActivity.mDrawerList.setItemChecked(7, true);
				MainActivity.pos = 7;
			}

			FloatingActionButton fabpanel = (FloatingActionButton)getActivity().findViewById(R.id.fabpanel);

			fabpanel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.openPanel(getActivity());
				}
			});
		} else {
			FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Cantina");
		}

		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		boolean warningCantina = preferences.getBoolean("warningCantina", false);
		if (!warningCantina) {
			final AlertDialog dialogcantina = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogcantina)
				.setPositiveButton("Sim", null)
				.setNegativeButton("Não", null)
				.create();

			dialogcantina.setCanceledOnTouchOutside(false);

			dialogcantina.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					editor = preferences.edit();
					Button s = dialogcantina.getButton(AlertDialog.BUTTON_POSITIVE);
					s.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningCantina", true);
							editor.commit();
							editor.putBoolean("notifCantina", true);
							editor.commit();
							((ActionBarActivity)getActivity()).supportInvalidateOptionsMenu();
							dialogcantina.dismiss();
						}
					});

					Button n = dialogcantina.getButton(AlertDialog.BUTTON_NEGATIVE);
					n.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningCantina", true);
							editor.commit();
							editor.putBoolean("notifCantina", false);
							editor.commit();
							((ActionBarActivity)getActivity()).supportInvalidateOptionsMenu();
							dialogcantina.dismiss();
						}
					});
				}
			});
			dialogcantina.show();
		}

		LayoutInflater inflatere = getActivity().getLayoutInflater();
		image = (ViewGroup)inflatere.inflate(R.layout.header_cantina, list, false);
		list.addHeaderView(image, null, false);
		page = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.header2, list, false);
		CustomTextView facebook = (CustomTextView)page.findViewById(R.id.text);
		facebook.setText("Página no Facebook");
		RelativeLayout rel = (RelativeLayout)page.findViewById(R.id.header2);
		rel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent cantina = new Intent(getActivity(), aloogle.rebuapp.activity.FragmentActivity.class);
				cantina.putExtra("fragment", 2);
				cantina.putExtra("titulo", "Cantina");
				cantina.putExtra("cantina", true);
				cantina.putExtra("url", "http://facebook.com/profile.php?id=388344088010898");
				startActivity(cantina);
			}
		});
		list.addHeaderView(page, null, false);

		cardapio = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.header2, list, false);
		CustomTextView cardap = (CustomTextView)cardapio.findViewById(R.id.text);
		cardap.setText("Cardápio");
		RelativeLayout relc = (RelativeLayout)cardapio.findViewById(R.id.header2);
		relc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FragmentActivity.class);
				intent.putExtra("fragment", 6);
				intent.putExtra("titulo", "Cardápio");
				intent.putExtra("settings", "cantinacardapio");
				intent.putExtra("url", "http://apps.aloogle.net/schoolapp/rebua/json/cantina.php?oque=cardapio");
				startActivity(intent);
			}
		});
		list.addHeaderView(cardapio, null, false);

		promocoes = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.header2, list, false);
		CustomTextView promo = (CustomTextView)promocoes.findViewById(R.id.text);
		promo.setText("Promoções");
		RelativeLayout relp = (RelativeLayout)promocoes.findViewById(R.id.header2);
		relp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FragmentActivity.class);
				intent.putExtra("fragment", 6);
				intent.putExtra("titulo", "Promoções");
				intent.putExtra("settings", "cantinapromocoes");
				intent.putExtra("url", "http://apps.aloogle.net/schoolapp/rebua/json/cantina.php?oque=promocoes");
				startActivity(intent);
			}
		});
		list.addHeaderView(promocoes, null, false);

		space = (ViewGroup)inflatere.inflate(R.layout.space, list, false);
		list.addFooterView(space, null, false);

		mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mSwipeLayout.setEnabled(false);

		if (Build.VERSION.SDK_INT >= 21) {
			progressBar = (ProgressBar)view.findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.GONE);
		} else {
			progressBarCompat = (ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBar1);
			progressBarCompat.setVisibility(View.GONE);
		}

		list.setAdapter(new CardAdapterCartaz(getActivity(), tituloarray, settingsarray, urlarray));
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu.findItem(R.id.menu_notification) == null) {
			inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.cantina_menu, menu);
		} else {
			menu.findItem(R.id.menu_notification).setVisible(true);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		boolean notif = preferences.getBoolean("notifCantina", false);
		menu.findItem(R.id.menu_notification).setChecked(notif);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_notification:
			boolean notif = preferences.getBoolean("notifCantina", false);
			if (notif) {
				editor.putBoolean("notifCantina", false);
				editor.commit();
			} else {
				editor.putBoolean("notifCantina", true);
				editor.commit();
			}
			((ActionBarActivity)getActivity()).supportInvalidateOptionsMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onRefresh() {}
}
