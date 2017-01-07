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
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import aloogle.rebuapp.R;
import aloogle.rebuapp.lib.CustomLinkMovementMethod;
import aloogle.rebuapp.other.CustomTextView;

public class CardAdapterDictionary extends BaseAdapter {

	private Context context;
	private ArrayList <String> genero;
	private ArrayList <String> termo;
	private ArrayList <String> significado;

	public CardAdapterDictionary(Context context, ArrayList <String> genero, ArrayList <String> termo, ArrayList <String> significado) {
		this.context = context;
		this.genero = genero;
		this.termo = termo;
		this.significado = significado;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return genero.size();
	}

	@Override
	public Object getItem(int position) {
		return genero.get(position);
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
					R.layout.card_dictionary, null);
		}

		CustomTextView generoetermot = (CustomTextView)convertView.findViewById(R.id.generoetermo);
		CustomTextView significadot = (CustomTextView)convertView.findViewById(R.id.significado);
		significadot.setMovementMethod(CustomLinkMovementMethod.getInstance(context));
		significadot.setLinkTextColor(Color.parseColor("#0000ff"));

		String generoetermo = "";
		if (genero.get(position).equals("") && termo.get(position).equals("")) {
			generoetermot.setVisibility(View.GONE);
		} else if (termo.get(position).equals("")) {
			generoetermo = genero.get(position);
		} else if (genero.get(position).equals("")) {
			generoetermo = termo.get(position);
		} else {
			generoetermo = genero.get(position) + " " + termo.get(position);
		}
		generoetermot.setText(generoetermo);

		if (significado.get(position).equals("")) {
			significadot.setVisibility(View.GONE);
		} else {
			significadot.setText(Html.fromHtml(significado.get(position)));
		}

		final String wtitulo = generoetermo;
		final String wdescricao = significado.get(position);
		RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.conteudo);
		relative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, aloogle.rebuapp.activity.FragmentActivity.class);
				intent.putExtra("fragment", 2);
				intent.putExtra("fromcard", true);
				intent.putExtra("fromdictionary", true);
				intent.putExtra("titulo", "");
				intent.putExtra("tipo", "0");
				intent.putExtra("wtitulo", wtitulo);
				intent.putExtra("wdescricao", wdescricao);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
