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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.parse.ParseAnalytics;
import aloogle.rebuapp.R;

public class SplashScreen extends Activity {

	private static final int TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ParseAnalytics.trackAppOpened(getIntent());

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		if (getIntent().hasExtra("fromnotification")) {
			Editor editor = preferences.edit();
			editor.remove("count");
			editor.commit();
			editor.remove("receivedTitles");
			editor.commit();
		}

		if (!isTaskRoot()) {
			final Intent intent = getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		} else {
			boolean isFirst = preferences.getBoolean("isFirst", true);
			if (isFirst) {
				new Handler().postDelayed(new Runnable() {
					 @ Override
					public void run() {
						Intent intent = new Intent(SplashScreen.this, FragmentActivity.class);
						startActivity(intent);
						SplashScreen.this.finish();
					}
				}, TIME);
			} else {
				final Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				if (getIntent().hasExtra("fromnotification")) {
					intent.putExtra("fromnotification", true);
					startActivity(intent);
					SplashScreen.this.finish();
				} else if (getIntent().hasExtra("fromwidget")) {
					intent.putExtra("fromwidget", true);
					intent.putExtra("widgetpos", getIntent().getIntExtra("widgetpos", 0));
					startActivity(intent);
					SplashScreen.this.finish();
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							startActivity(intent);
							SplashScreen.this.finish();
						}
					}, TIME);
				}
			}
		}
	}
}
