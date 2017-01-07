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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import aloogle.rebuapp.R;
import aloogle.rebuapp.activity.FragmentActivity;
import aloogle.rebuapp.activity.MainActivity;

@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment {
	@SuppressWarnings("unused")
	private Activity activity;
	SharedPreferences preferences;
	Editor editor;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		editor = preferences.edit();
		final boolean isFirst = preferences.getBoolean("isFirst", true);
		if (isFirst) {
			editor.putString("prefColor", "005400");
			editor.commit();
		}

		addPreferencesFromResource(R.xml.settings);

		FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), "Configurações");

		if (Build.VERSION.SDK_INT >= 14) {
			if (isFirst) {
				((ActionBarActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
				((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			}
		}

		Preference prefColor = findPreference("prefColor");
		prefColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						FragmentActivity.ActionBarColor(((ActionBarActivity)getActivity()), getString(R.string.settings));
					}
				}, 100);
				return true;
			}
		});

		Preference button = findPreference("buttonok");
		button.setOnPreferenceClickListener(new Preference.
			OnPreferenceClickListener() {
			@Override
			public boolean
			onPreferenceClick(Preference preference) {
				if (isFirst) {
					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
					editor.putBoolean("isFirst", false);
					editor.commit();
					getActivity().finish();
				} else {
					getActivity().finish();
				}
				return true;
			}
		});
	}
}
