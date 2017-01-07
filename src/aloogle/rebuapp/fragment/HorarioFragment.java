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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.MainActivity;
import aloogle.rebuapp.lib.JSONParser;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

@SuppressLint("InflateParams")
public class HorarioFragment extends Fragment {
	@SuppressWarnings("unused")
	private Activity activity;
	private View view;
	LayoutInflater celinflater;
	LinearLayout horario;
	boolean semana;
	String sala, url;

	SharedPreferences preferences;

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
		view = inflater.inflate(R.layout.horario_main, container, false);

		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		sala = preferences.getString("classRoom", "none");

		url = "http://apps.aloogle.net/schoolapp/rebua/json/horarios.php?sala=" + sala;

		semana = false;

		horario = (LinearLayout)view.findViewById(R.id.horario);

		celinflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (Other.isConnected(getActivity())) {
			if (preferences.contains("horarios" + sala)) {
				if (Other.getWeekDay(true).matches("sabado|domingo")) {
					horario.addView(Week());
				} else {
					horario.addView(Day());
				}
			} else {
				new JSONParse().execute();
				if (Build.VERSION.SDK_INT > 10) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							new JSONParse().execute();
						}
					}, 1000);
				}
				new JSONParse().execute();
			}
		} else {
			if (Other.getWeekDay(true).matches("sabado|domingo")) {
				horario.addView(Week());
			} else {
				horario.addView(Day());
			}
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
			MainActivity.setRefreshActionButtonState(false, getActivity());

			SharedPreferences.Editor editor = preferences.edit();

			editor.putString("horarios" + sala, json.toString());
			editor.commit();

			if (Other.getWeekDay(true).matches("sabado|domingo")) {
				horario.addView(Week());
			} else {
				horario.addView(Day());
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.horario_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (Other.getWeekDay(true).matches("sabado|domingo")) {
			menu.findItem(R.id.menu_toggle).setVisible(false);
		} else {
			if (getActivity().findViewById(R.id.pager).getVisibility() == View.VISIBLE) {
				menu.findItem(R.id.menu_toggle).setVisible(true);
			} else {
				menu.findItem(R.id.menu_toggle).setVisible(false);
			}

			if (semana) {
				menu.findItem(R.id.menu_toggle).setIcon(R.drawable.ic_hoje);
				menu.findItem(R.id.menu_toggle).setTitle(getString(R.string.mdia));
			} else {
				menu.findItem(R.id.menu_toggle).setIcon(R.drawable.ic_semana);
				menu.findItem(R.id.menu_toggle).setTitle(getString(R.string.msemana));
			}
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_toggle:
			if (semana) {
				semana = false;
				horario.removeAllViews();
				horario.addView(Day());
				getActivity().supportInvalidateOptionsMenu();
			} else {
				semana = true;
				horario.removeAllViews();
				horario.addView(Week());
				getActivity().supportInvalidateOptionsMenu();
			}
			return true;
		case R.id.menu_refresh:
			if (getActivity().findViewById(R.id.pager).getVisibility() == View.VISIBLE) {
				if (Other.isConnected(getActivity())) {
					semana = false;
					horario.removeAllViews();
					new JSONParse().execute();
					getActivity().supportInvalidateOptionsMenu();
				} else {
					Toast toast = Toast.makeText(getActivity(), getString(R.string.needinternet), Toast.LENGTH_LONG);
					toast.show();
				}
			}
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public View Week() {
		View alldays = null;
		if (preferences.contains("horarios" + sala)) {
			alldays = celinflater.inflate(R.layout.horario_table, null);
			try {
				ObservableScrollView scroll = (ObservableScrollView)alldays.findViewById(R.id.list);
				scroll.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				scroll.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));

				String[]horarios = getResources().getStringArray(R.array.horarios);
				String[]dias = {
					"", 				"Segunda", 				"Terça", 				"Quarta", 				"Quinta", 				"Sexta"
				};

				JSONObject materias = new JSONObject(preferences.getString("horarios" + sala, ""));
				JSONArray segunda = materias.getJSONArray("segunda");
				JSONArray terca = materias.getJSONArray("terca");
				JSONArray quarta = materias.getJSONArray("quarta");
				JSONArray quinta = materias.getJSONArray("quinta");
				JSONArray sexta = materias.getJSONArray("sexta");

				TableLayout tabela = (TableLayout)alldays.findViewById(R.id.tableLayout1);

				View celroom = celinflater.inflate(R.layout.horario_cel2, null);
				TableRow roomrow = (TableRow)celroom.findViewById(R.id.row);
				View salaview = celinflater.inflate(R.layout.horario_text, null);
				TextView sala = (TextView)salaview.findViewById(R.id.tabletext);
				sala.setText(Other.getSala(getActivity()));
				TableRow.LayoutParams params = new TableRow.LayoutParams();
				params.span = dias.length;
				roomrow.addView(salaview, 0, params);
				tabela.addView(celroom);

				View celdays = celinflater.inflate(R.layout.horario_cel2, null);
				TableRow day = (TableRow)celdays.findViewById(R.id.row);
				for (int i = 0; i < dias.length; i++) {
					View text = celinflater.inflate(R.layout.horario_text, null);
					TextView texto = (TextView)text.findViewById(R.id.tabletext);
					texto.setText(dias[i]);
					day.addView(text);
					if (i == dias.length - 1) {
						tabela.addView(celdays);
					}
				}

				for (int a = 0; a < horarios.length; a++) {
					View celview = celinflater.inflate(R.layout.horario_cel2, null);
					TableRow row = (TableRow)celview.findViewById(R.id.row);
					View hour = celinflater.inflate(R.layout.horario_text, null);
					TextView hora = (TextView)hour.findViewById(R.id.tabletext);
					hora.setText(horarios[a]);
					row.addView(hour);
					for (int i = 0; i < dias.length - 1; i++) {
						View text = celinflater.inflate(R.layout.horario_text, null);
						TextView texto = (TextView)text.findViewById(R.id.tabletext);
						switch (i) {
						case 0:
							texto.setText(segunda.getJSONObject(a).getString("materia"));
							break;
						case 1:
							texto.setText(terca.getJSONObject(a).getString("materia"));
							break;
						case 2:
							texto.setText(quarta.getJSONObject(a).getString("materia"));
							break;
						case 3:
							texto.setText(quinta.getJSONObject(a).getString("materia"));
							break;
						case 4:
							texto.setText(sexta.getJSONObject(a).getString("materia"));
							break;
						}

						row.addView(text);
						if (i == dias.length - 2) {
							tabela.addView(celview);
						}
					}
				}

				View celspace = celinflater.inflate(R.layout.horario_cel2, null);
				TableRow spacerow = (TableRow)celspace.findViewById(R.id.row);
				View spaceview = celinflater.inflate(R.layout.space, null);
				spacerow.addView(spaceview);
				tabela.addView(celspace);
			} catch (JSONException e) {}
		} else {
			alldays = celinflater.inflate(R.layout.littlecard, null);
			CustomTextView warning = (CustomTextView)alldays.findViewById(R.id.text);
			warning.setText("Você precisa estar conectado à internet pelo menos uma vez para salvar o horário desta sala.");
		}
		return alldays;
	}

	public View Day() {
		View oneday = null;
		if (preferences.contains("horarios" + sala)) {
			oneday = celinflater.inflate(R.layout.horario, null);
			try {
				ObservableScrollView scroll = (ObservableScrollView)oneday.findViewById(R.id.list);
				scroll.setScrollViewCallbacks((ObservableScrollViewCallbacks)getActivity());
				scroll.setTouchInterceptionViewGroup((ViewGroup)getActivity().findViewById(R.id.container));

				String[]horarios = getResources().getStringArray(R.array.horarios);
				JSONObject materias = new JSONObject(preferences.getString("horarios" + sala, ""));
				JSONArray materian = materias.getJSONArray(Other.getWeekDay(true));

				View celviewday = celinflater.inflate(R.layout.horario_cel, null);
				TextView horariostday = (TextView)celviewday.findViewById(R.id.horario);
				TextView materiaday = (TextView)celviewday.findViewById(R.id.materia);
				celviewday.findViewById(R.id.traco).setVisibility(View.GONE);
				horariostday.setVisibility(View.GONE);
				materiaday.setText(Other.getWeekDay(false) + " - " + Other.getSala(getActivity()));
				LinearLayout tabela = (LinearLayout)oneday.findViewById(R.id.linear);
				tabela.addView(celviewday);

				for (int i = 0; i < horarios.length; i++) {
					View celview = celinflater.inflate(R.layout.horario_cel, null);
					TextView horariost = (TextView)celview.findViewById(R.id.horario);
					TextView materia = (TextView)celview.findViewById(R.id.materia);
					horariost.setText(horarios[i]);
					materia.setText(materian.getJSONObject(i).getString("materia"));
					tabela.addView(celview);
				}

				View spaceview = celinflater.inflate(R.layout.space, null);
				tabela.addView(spaceview);
			} catch (JSONException e) {}
		} else {
			oneday = celinflater.inflate(R.layout.littlecard, null);
			CustomTextView warning = (CustomTextView)oneday.findViewById(R.id.text);
			warning.setText("Você precisa estar conectado à internet pelo menos uma vez para salvar o horário desta sala.");
		}
		return oneday;
	}

	public void onResume() {
		if (preferences.getString("classRoom", "none").equals(sala)) {}
		else {
			semana = false;
			sala = preferences.getString("classRoom", "none");
			url = "http://apps.aloogle.net/schoolapp/rebua/json/horarios.php?sala=" + sala;
			if (Other.isConnected(getActivity())) {
				if (preferences.contains("horarios" + sala)) {
					horario.removeAllViews();
					if (Other.getWeekDay(true).matches("sabado|domingo")) {
						horario.addView(Week());
					} else {
						horario.addView(Day());
					}
				} else {
					horario.removeAllViews();
					new JSONParse().execute();
				}
			} else {
				horario.removeAllViews();
				if (Other.getWeekDay(true).matches("sabado|domingo")) {
					horario.addView(Week());
				} else {
					horario.addView(Day());
				}
			}
		}
		super.onResume();
	}
}
