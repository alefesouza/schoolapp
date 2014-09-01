package aloogle.schoolapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import aloogle.schoolapp.R;

public class Settings extends PreferenceActivity {

	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.settings);
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (Build.VERSION.SDK_INT >= 14) {
			boolean isFirst = preferences.getBoolean("isFirst", true);
			if (isFirst) {}
			else {
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		addPreferencesFromResource(R.xml.settings);

		Preference button = findPreference("buttonok");
		button.setOnPreferenceClickListener(new Preference.
			OnPreferenceClickListener() {
			@Override
			public boolean
			onPreferenceClick(Preference preference) {
				boolean isFirst = preferences.getBoolean("isFirst", true);
				if (isFirst) {
					if (Build.VERSION.SDK_INT < 14) {
						Intent intent = new Intent(Settings.this, MainActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(Settings.this, aloogle.schoolapp.activity.v14.MainActivity.class);
						startActivity(intent);
					}

					Editor editor = preferences.edit();
					editor.putBoolean("isFirst", false);
					editor.commit();
					Settings.this.finish();
				} else {
					Settings.this.finish();
				}
				return true;
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Settings.this.finish();
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}
}
