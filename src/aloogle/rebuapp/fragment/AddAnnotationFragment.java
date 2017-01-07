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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import org.json.JSONException;
import org.json.JSONObject;
import com.gc.materialdesign.views.ButtonFlat;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.Other;

@SuppressLint("CutPasteId")
public class AddAnnotationFragment extends Fragment {
	Activity activity;
	View view;
	SharedPreferences preferences;
	String exjson, extitulo, exdescricao;
	Editor editor;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		view = inflater.inflate(R.layout.annotation_add, container, false);

		final EditText titulo = (EditText)view.findViewById(R.id.titulo);
		final EditText descricao = (EditText)view.findViewById(R.id.descricao);
		View add = view.findViewById(R.id.add);

		if (getActivity().getIntent().hasExtra("editionmode")) {
			aloogle.rebuapp.activity.FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Editar anotação");
			try {
				exjson = getActivity().getIntent().getStringExtra("json");
				JSONObject json = new JSONObject(exjson);
				extitulo = json.getString("titulo");
				exdescricao = json.getString("descricao");
				titulo.setText(extitulo);
				descricao.setText(exdescricao);
			} catch (JSONException e) {}
			if (Build.VERSION.SDK_INT >= 21) {
				Button b = (Button)view.findViewById(R.id.add);
				b.setTextColor(Color.parseColor("#" + Other.getColor(getActivity())));
				b.setText("Atualizar");
			} else {
				ButtonFlat b = (ButtonFlat)view.findViewById(R.id.add);
				b.setTextColor(Color.parseColor("#" + Other.getColor(getActivity())));
				b.setText("Atualizar");
			}
		} else {
			aloogle.rebuapp.activity.FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Adicionar anotação");
			if (Build.VERSION.SDK_INT >= 21) {
				Button b = (Button)view.findViewById(R.id.add);
				b.setText("Adicionar");
			} else {
				ButtonFlat b = (ButtonFlat)view.findViewById(R.id.add);
				b.setText("Adicionar");
			}
		}

		add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String etitulo = titulo.getText().toString().replace("\\", "\\\\").replace("\"", "\\\"");
				String edescricao = descricao.getText().toString().replace("\\", "\\\\").replace("\"", "\\\"");
				if (getActivity().getIntent().hasExtra("editionmode")) {
					String newjson = exjson.replace(extitulo.replace("\\", "\\\\").replace("\"", "\\\""), etitulo).replace(exdescricao.replace("\\", "\\\\").replace("\"", "\\\""), edescricao);
					editor.putString("annotations", preferences.getString("annotations", "").replace(exjson, newjson));
					editor.commit();
					getActivity().finish();
				} else {
					int id = preferences.getInt("lastAnnotationId", 0) + 1;
					editor.putString("annotations", "{ \"id\": \"" + String.valueOf(id) + "\", \"titulo\": \"" + etitulo + "\", \"descricao\": \"" + edescricao + "\" }$%#" + preferences.getString("annotations", ""));
					editor.commit();
					editor.putInt("lastAnnotationId", id);
					editor.commit();
					getActivity().finish();
				}
			}
		});
		return view;
	}
}
