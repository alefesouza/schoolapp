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
import android.widget.ImageView;
import aloogle.rebuapp.R;
import aloogle.rebuapp.other.CustomTextView;
import aloogle.rebuapp.other.Icons;
import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter {

	private Context context;
	private ArrayList <Icons> navDrawerItems;

	public DrawerAdapter(Context context, ArrayList <Icons> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)context
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.drawer_adapter, null);
		}

		ImageView imgIcon = (ImageView)convertView
		.findViewById(R.id.iconPicture);
		CustomTextView txtTitle = (CustomTextView)convertView.findViewById(R.id.categoryText);

		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		return convertView;
	}
}
