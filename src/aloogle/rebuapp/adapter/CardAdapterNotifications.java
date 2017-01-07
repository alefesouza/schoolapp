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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.CustomTextView;

public class CardAdapterNotifications extends BaseAdapter {

	private Context context;
	@SuppressWarnings("unused")
	private ArrayList <String> id;
	private ArrayList <String> title;
	private ArrayList <String> description;
	private ArrayList <String> summary;

	public CardAdapterNotifications(Context context, ArrayList <String> id, ArrayList <String> title, ArrayList <String> description, ArrayList <String> summary) {
		this.context = context;
		this.id = id;
		this.title = title;
		this.description = description;
		this.summary = summary;
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
					R.layout.card_notification, null);
		}

		CustomTextView titulo = (CustomTextView)convertView.findViewById(R.id.titulo);
		CustomTextView descricao = (CustomTextView)convertView.findViewById(R.id.descricao);
		CustomTextView sumario = (CustomTextView)convertView.findViewById(R.id.sumario);

		titulo.setText(title.get(position));
		descricao.setText(description.get(position));
		sumario.setText(summary.get(position));

		return convertView;
	}
}
