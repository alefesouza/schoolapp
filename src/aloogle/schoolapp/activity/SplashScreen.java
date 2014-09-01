package aloogle.schoolapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import aloogle.schoolapp.R;

public class SplashScreen extends Activity {

	private static final int TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isFirst = preferences.getBoolean("isFirst", true);
		if (isFirst) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(SplashScreen.this, Settings.class);
					startActivity(intent);
					SplashScreen.this.finish();
				}
			}, TIME);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (Build.VERSION.SDK_INT < 14) {
						Intent intent = new Intent(SplashScreen.this, MainActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(SplashScreen.this, aloogle.schoolapp.activity.v14.MainActivity.class);
						startActivity(intent);
					}
					SplashScreen.this.finish();
				}
			}, TIME);
		}
	}
}
