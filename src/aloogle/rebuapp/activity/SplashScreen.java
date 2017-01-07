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
import aloogle.rebuapp.other.Other;

public class SplashScreen extends Activity {

	private static final int TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ParseAnalytics.trackAppOpened(getIntent());

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		final Intent intent = new Intent(SplashScreen.this, MainActivity.class);

		boolean isRoot = true;
		if (!isTaskRoot()) {
			final Intent intent2 = getIntent();
			final String intentAction = intent2.getAction();
			isRoot = intent2.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN);
		}

		Intent frag = new Intent(SplashScreen.this, FragmentActivity.class);
		frag.putExtra("fragment", 7);
		frag.putExtra("isRoot", isRoot);

		if (getIntent().hasExtra("fromnotification")) {
			Editor editor = preferences.edit();
			editor.remove("count");
			editor.commit();
			editor.remove("receivedTitles");
			editor.commit();
			intent.putExtra("fromnotification", true);
			startActivity(intent);
			SplashScreen.this.finish();
		} else if (getIntent().hasExtra("widgetpos")) {
			switch (getIntent().getIntExtra("widgetpos", 0)) {
			case 11:
				Intent blog = new Intent(this, FragmentActivity.class);
				blog.putExtra("fragment", 2);
				blog.putExtra("titulo", "Blog");
				blog.putExtra("url", "http://willianrrebua.blogspot.com");
				startActivity(blog);
				break;
			case 12:
				Intent jornal = new Intent(this, FragmentActivity.class);
				jornal.putExtra("fragment", 2);
				jornal.putExtra("titulo", "Jornal");
				jornal.putExtra("url", "http://facebook.com/REVOLUCIONARIOSREBUA");
				startActivity(jornal);
				break;
			case 15:
				Other.openPanel(this);
				break;
			default:
				frag.putExtra("widgetpos", getIntent().getIntExtra("widgetpos", 0));
				startActivity(frag);
			}
			SplashScreen.this.finish();
		} else if (!isRoot) {
			finish();
		} else {
			final boolean isFirst = preferences.getBoolean("isFirst", true);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (isFirst) {
						Intent settings = new Intent(SplashScreen.this, FragmentActivity.class);
						startActivity(settings);
					} else {
						startActivity(intent);
					}
					SplashScreen.this.finish();
				}
			}, TIME);
		}
	}
}
