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
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.other.CustomTextView;

@SuppressLint("SetJavaScriptEnabled")
public class CardAdapter extends BaseAdapter {

	private Context context;
	private ArrayList <String> id;
	private ArrayList <String> type;
	private ArrayList <String> date;
	private ArrayList <String> title;
	private ArrayList <String> description;
	boolean webEx;

	public CardAdapter(Context context, ArrayList <String> id, ArrayList <String> type, ArrayList <String> date, ArrayList <String> title, ArrayList <String> description, boolean webEx) {
		this.context = context;
		this.id = id;
		this.date = date;
		this.type = type;
		this.title = title;
		this.webEx = webEx;
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
					R.layout.card, null);
		}

		CustomTextView titulo = (CustomTextView)convertView.findViewById(R.id.titulo);
		CustomTextView descricao = (CustomTextView)convertView.findViewById(R.id.descricao);
		WebView descricaowebview = (WebView)convertView.findViewById(R.id.descricaowebview);
		descricaowebview.getSettings().setDefaultTextEncodingName("utf-8");
		descricaowebview.getSettings().setJavaScriptEnabled(true);

		String tvtitulo = "";
		if (date.get(position).equals("") && title.get(position).equals("")) {
			titulo.setVisibility(View.GONE);
		} else if (date.get(position).equals("")) {
			tvtitulo = title.get(position);
		} else {
			tvtitulo = date.get(position) + " - " + title.get(position);
		}
		titulo.setText(tvtitulo);

		if (description.get(position).equals("")) {
			descricao.setVisibility(View.GONE);
			descricaowebview.setVisibility(View.GONE);
		} else {
			if (type.get(position).equals("0")) {
				descricaowebview.setVisibility(View.GONE);
				descricao.setText(description.get(position));
			} else {
				if (webEx) {
					titulo.setText(tvtitulo + " - Toque aqui para expandir");
					descricao.setVisibility(View.GONE);
					descricaowebview.setVisibility(View.GONE);
				} else {
					descricao.setVisibility(View.GONE);
					descricaowebview.clearCache(true);
					String style = "body { background: #fafafa; -webkit-user-select: none; user-select: none; word-wrap: break-word; } table { border: #000000 solid 1px; } td { border: #000000 solid 1px; text-align: center; padding: 5px; } hr { background: #000000; height: 1px; }";
					if (Build.VERSION.SDK_INT == 10) {
						descricaowebview.loadDataWithBaseURL(null, "<html><head><style>" + style + "</style></head><body>" + description.get(position) + "</body></html>", "text/html", "utf-8", null);
					} else {
						descricaowebview.loadData("<html><head><style>" + style + "</style></head><body>" + description.get(position) + "</body></html>", "text/html; charset=utf-8", "utf-8");
					}
					descricaowebview.setVisibility(View.VISIBLE);
				}
			}
		}

		final String webviewtitulo = tvtitulo;
		RelativeLayout relative = (RelativeLayout)convertView.findViewById(R.id.conteudo);
		relative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, FragmentActivity.class);
				intent.putExtra("fragment", 2);
				intent.putExtra("fromcard", true);
				intent.putExtra("titulo", title.get(position).toString());
				intent.putExtra("tipo", type.get(position).toString());
				intent.putExtra("eventoid", id.get(position).toString());
				intent.putExtra("wtitulo", webviewtitulo);
				intent.putExtra("wdescricao", description.get(position).toString());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}
