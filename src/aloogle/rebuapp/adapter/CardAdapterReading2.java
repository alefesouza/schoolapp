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
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import java.util.ArrayList;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.CustomTextView;

public class CardAdapterReading2 extends BaseAdapter {

	private Context context;
	private ArrayList <String> obra;
	private ArrayList <String> autor;
	private ArrayList <String> categoria;
	private ArrayList <String> editora;
	private ArrayList <String> quantidade;
	boolean isall;

	public CardAdapterReading2(Context context, ArrayList <String> obra, ArrayList <String> autor, ArrayList <String> categoria, ArrayList <String> editora, ArrayList <String> quantidade, boolean isall) {
		this.context = context;
		this.obra = obra;
		this.autor = autor;
		this.categoria = categoria;
		this.editora = editora;
		this.quantidade = quantidade;
		this.isall = isall;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return obra.size();
	}

	@Override
	public Object getItem(int position) {
		return obra.get(position);
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
					R.layout.card_reading2, null);
		}

		CustomTextView obrat = (CustomTextView)convertView.findViewById(R.id.obra);
		CustomTextView autort = (CustomTextView)convertView.findViewById(R.id.autor);
		CustomTextView categoriat = (CustomTextView)convertView.findViewById(R.id.categoria);
		CustomTextView editorat = (CustomTextView)convertView.findViewById(R.id.editora);
		CustomTextView quantidadet = (CustomTextView)convertView.findViewById(R.id.quantidade);

		if (isall) {
			categoriat.setVisibility(View.VISIBLE);
		}

		obrat.setText(obra.get(position));
		autort.setText(Html.fromHtml("<b>Autor:</b> " + autor.get(position)));
		categoriat.setText(Html.fromHtml("<b>Categoria:</b> " + categoria.get(position)));
		editorat.setText(Html.fromHtml("<b>Editora:</b> " + editora.get(position)));
		quantidadet.setText(Html.fromHtml("<b>Quantidade:</b> " + quantidade.get(position)));

		LinearLayout conteudo = (LinearLayout)convertView.findViewById(R.id.conteudo);
		conteudo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://google.com.br/search?q=" + obra.get(position) + " " + autor.get(position)));
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}
