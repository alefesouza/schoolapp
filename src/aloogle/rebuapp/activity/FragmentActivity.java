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
 
package aloogle.rebuapp.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import aloogle.rebuapp.R;
import aloogle.rebuapp.fragment.*;
import aloogle.rebuapp.other.Other;

@SuppressWarnings("deprecation")
public class FragmentActivity extends ActionBarActivity {
	public Toolbar mToolbar;
	PreferenceFragment settings = new SettingsFragment();
	Fragment about = new AboutFragment();
	Fragment webview = new WebViewFrag();
	Fragment licenses = new LicensesFragment();
	Fragment addannotation = new AddAnnotationFragment();
	SharedPreferences preferences;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.toolbar);

		mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		selectItem(getIntent().getIntExtra("fragment", 0));
	}

	private void selectItem(int position) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, settings);
			break;
		case 1:
			ft.replace(R.id.content_frame, about);
			break;
		case 2:
			ft.replace(R.id.content_frame, webview);
			break;
		case 3:
			ft.replace(R.id.content_frame, licenses);
			break;
		case 4:
			ft.replace(R.id.content_frame, addannotation);
			break;
		}
		ft.commit();
	}

	public static void ActionBarColor(ActionBarActivity activity, String title) {
		activity.getSupportActionBar().setTitle(title);
		activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(activity))));
		activity.findViewById(R.id.frame).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(activity))));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			FragmentActivity.this.finish();
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getIntent().getIntExtra("fragment", 0) == 2) {}
			else {
				FragmentActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
