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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.ReadingActivity;
import aloogle.rebuapp.other.CustomTextView;

public class CardAdapterReading extends BaseAdapter {

	private Context context;
	private ArrayList <String> categoria;
	private ArrayList <String> nome;
	private ArrayList <String> quantidade;
	private ArrayList <String> amostras;

	public CardAdapterReading(Context context, ArrayList <String> categoria, ArrayList <String> nome, ArrayList <String> quantidade, ArrayList <String> amostras) {
		this.context = context;
		this.categoria = categoria;
		this.nome = nome;
		this.quantidade = quantidade;
		this.amostras = amostras;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return nome.size();
	}

	@Override
	public Object getItem(int position) {
		return nome.get(position);
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
					R.layout.card_reading, null);
		}

		CustomTextView nomet = (CustomTextView)convertView.findViewById(R.id.nome);
		CustomTextView quantidadet = (CustomTextView)convertView.findViewById(R.id.quantidade);
		CustomTextView amostrast = (CustomTextView)convertView.findViewById(R.id.amostras);

		nomet.setText(nome.get(position));
		quantidadet.setText(Html.fromHtml(quantidade.get(position)));
		amostrast.setText(Html.fromHtml(amostras.get(position)));

		RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.conteudo);
		relative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, ReadingActivity.class);
				intent.putExtra("titulo", nome.get(position).toString());
				intent.putExtra("categoria", categoria.get(position).toString());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}
