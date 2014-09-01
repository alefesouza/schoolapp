package aloogle.schoolapp.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import aloogle.schoolapp.R;

public class Other {
	public static final String WebViewValue = "WEB_VIEW_VALUE";

	public static void setText(Activity activity) {
		TextView classRoom = (TextView)activity.findViewById(R.id.classroom);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String prefClassRoom = preferences.getString("classRoom", "none");
		if (prefClassRoom.equals("none")) {
			classRoom.setText("Escolha sua sala");
		} else if (prefClassRoom.equals("1a")) {
			classRoom.setText("1°A");
		} else if (prefClassRoom.equals("1b")) {
			classRoom.setText("1°B");
		} else if (prefClassRoom.equals("1c")) {
			classRoom.setText("1°C");
		} else if (prefClassRoom.equals("1d")) {
			classRoom.setText("1°D");
		} else if (prefClassRoom.equals("2a")) {
			classRoom.setText("2°A");
		} else if (prefClassRoom.equals("2b")) {
			classRoom.setText("2°B");
		} else if (prefClassRoom.equals("2c")) {
			classRoom.setText("2°C");
		} else if (prefClassRoom.equals("3a")) {
			classRoom.setText("3°A");
		} else if (prefClassRoom.equals("3b")) {
			classRoom.setText("3°B");
		} else if (prefClassRoom.equals("3c")) {
			classRoom.setText("3°C");
		}
		Visible(activity);
		boolean classrepresentant = preferences.getBoolean("classRepresentant", false);
		if (classrepresentant) {
			activity.findViewById(R.id.addevent).setVisibility(View.VISIBLE);
		} else {
			activity.findViewById(R.id.addevent).setVisibility(View.GONE);
		}
	}

	public static void Visible(Activity activity) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String prefClassRoom = preferences.getString("classRoom", "none");
		if (prefClassRoom.equals("none")) {
			activity.findViewById(R.id.schedules).setVisibility(View.GONE);
			activity.findViewById(R.id.calendar).setVisibility(View.GONE);
			activity.findViewById(R.id.addevent).setVisibility(View.GONE);
		} else {
			activity.findViewById(R.id.schedules).setVisibility(View.VISIBLE);
			activity.findViewById(R.id.calendar).setVisibility(View.VISIBLE);
			activity.findViewById(R.id.addevent).setVisibility(View.VISIBLE);
		}
	}

	public static void setClick(final Activity activity) {
		final String classRoom = PreferenceManager.getDefaultSharedPreferences(activity).getString("classRoom", "none");
		activity.findViewById(R.id.schedules).setOnClickListener(new View.OnClickListener() {
			 @ Override
			public void onClick(View v) {
				Other.Click(activity, "schedulesCache" + classRoom, 0);
			}
		});

		activity.findViewById(R.id.calendar).setOnClickListener(new View.OnClickListener() {
			 @ Override
			public void onClick(View v) {
				Other.Click(activity, "calendarCache" + classRoom, 1);
			}
		});
	}

	public static void Click(Activity activity, String cache, int number) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
			if (Build.VERSION.SDK_INT < 14) {
				Intent intent = new Intent(activity, aloogle.schoolapp.activity.WebViewActivity.class);
				intent.putExtra(Other.WebViewValue, number);
				activity.startActivity(intent);
			} else {
				Intent intent = new Intent(activity, aloogle.schoolapp.activity.v14.WebViewActivity.class);
				intent.putExtra(Other.WebViewValue, number);
				activity.startActivity(intent);
			}
		} else {
			boolean pageCache = preferences.getBoolean(cache, false);
			if (pageCache) {
				if (Build.VERSION.SDK_INT < 14) {
					Intent intent = new Intent(activity, aloogle.schoolapp.activity.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, number);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity, aloogle.schoolapp.activity.v14.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, number);
					activity.startActivity(intent);
				}
			} else {
				Toast toast = Toast.makeText(activity, activity.getString(R.string.needinternetft), Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}
}
