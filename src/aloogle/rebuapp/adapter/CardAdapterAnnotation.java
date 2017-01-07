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
 
package aloogle.rebuapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import com.gc.materialdesign.views.ButtonFlat;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Other;

@SuppressLint("CutPasteId")
public class CardAdapterAnnotation extends BaseAdapter {

	private Context context;
	private ArrayList <String> json;
	private ArrayList <String> id;
	private ArrayList <String> title;
	private ArrayList <String> description;

	public CardAdapterAnnotation(Context context, ArrayList <String> json, ArrayList <String> id, ArrayList <String> title, ArrayList <String> description) {
		this.context = context;
		this.json = json;
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return title.size();
	}

	@Override
	public Object getItem(int position) {
		return title.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)context
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.card_annotation, null);
		}

		CustomTextView titulo = (CustomTextView)convertView.findViewById(R.id.titulo);
		CustomTextView descricao = (CustomTextView)convertView.findViewById(R.id.descricao);
		View editar = convertView.findViewById(R.id.edit);
		View apagar = convertView.findViewById(R.id.delete );

		titulo.setText(title.get(position));
		descricao.setText(description.get(position));
		if (Build.VERSION.SDK_INT >= 21) {
			Button e = (Button)convertView.findViewById(R.id.edit);
			Button a = (Button)convertView.findViewById(R.id.delete );

			e.setTextColor(Color.parseColor("#" + Other.getColor(((ActionBarActivity)context))));
			a.setTextColor(Color.parseColor("#" + Other.getColor(((ActionBarActivity)context))));
		} else {
			ButtonFlat e = (ButtonFlat)convertView.findViewById(R.id.edit);
			ButtonFlat a = (ButtonFlat)convertView.findViewById(R.id.delete );

			e.setTextColor(Color.parseColor("#" + Other.getColor(((ActionBarActivity)context))));
			a.setTextColor(Color.parseColor("#" + Other.getColor(((ActionBarActivity)context))));
		}

		editar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, FragmentActivity.class);
				intent.putExtra("fragment", 4);
				intent.putExtra("editionmode", true);
				intent.putExtra("json", json.get(position));
				context.startActivity(intent);
			}
		});

		apagar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("annotations", preferences.getString("annotations", "").replace(json.get(position) + "$%#", ""));
				editor.commit();
				json.remove(position);
				id.remove(position);
				title.remove(position);
				description.remove(position);
				notifyDataSetChanged();
			}
		});

		RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.conteudo);
		relative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, FragmentActivity.class);
				intent.putExtra("fragment", 2);
				intent.putExtra("fromcard", true);
				intent.putExtra("titulo", title.get(position).toString());
				intent.putExtra("tipo", "0");
				intent.putExtra("wtitulo", title.get(position).toString());
				intent.putExtra("wdescricao", description.get(position).toString());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}
