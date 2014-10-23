package aloogle.rebuapp.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import aloogle.rebuapp.R;

@SuppressLint("NewApi")
public class Other {
	public static final String WebViewValue = "WEB_VIEW_VALUE";

	public static void setText(Activity activity, View view, int value) {
		if (value == 0) {
			TextView classRoom = (TextView)view.findViewById(R.id.classroom);
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
			Visible(activity, view, 0);
			boolean classrepresentant = preferences.getBoolean("classRepresentant", false);
			if (classrepresentant) {
				view.findViewById(R.id.panel).setVisibility(View.VISIBLE);
			} else {
				view.findViewById(R.id.panel).setVisibility(View.GONE);
			}
		} else if (value == 1) {
			TextView clubeRoom = (TextView)view.findViewById(R.id.cluberoom);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String prefClubeRoom = preferences.getString("clubeRoom", "none");
			if (prefClubeRoom.equals("none")) {
				clubeRoom.setText("Escolha seu clube");
			} else if (prefClubeRoom.equals("jogos")) {
				clubeRoom.setText("Jogos");
			} else if (prefClubeRoom.equals("addnew")) {
				clubeRoom.setText("Adicionar novo");
			}
			Visible(activity, view, 1);
		} else if (value == 2) {
			TextView eletivaRoom = (TextView)view.findViewById(R.id.eletivaroom);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String prefEletivaRoom = preferences.getString("eletivaRoom", "none");
			if (prefEletivaRoom.equals("none")) {
				eletivaRoom.setText("Escolha sua eletiva");
			} else if (prefEletivaRoom.equals("anatomia")) {
				eletivaRoom.setText("Anatomia");
			} else if (prefEletivaRoom.equals("comunicacao")) {
				eletivaRoom.setText("Comunicação");
			} else if (prefEletivaRoom.equals("engenharia")) {
				eletivaRoom.setText("Engenharia");
			} else if (prefEletivaRoom.equals("handbol")) {
				eletivaRoom.setText("Handbol");
			} else if (prefEletivaRoom.equals("medicina")) {
				eletivaRoom.setText("Medicina");
			} else if (prefEletivaRoom.equals("jornalismo")) {
				eletivaRoom.setText("Jornalismo");
			} else if (prefEletivaRoom.equals("legislacao")) {
				eletivaRoom.setText("Legislação");
			} else if (prefEletivaRoom.equals("teatro")) {
				eletivaRoom.setText("Teatro");
			}
			Visible(activity, view, 2);
		}
	}

	public static void Visible(Activity activity, View view, int value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		if (value == 0) {
			String prefClassRoom = preferences.getString("classRoom", "none");
			if (prefClassRoom.equals("none")) {
				view.findViewById(R.id.schedules).setVisibility(View.GONE);
				view.findViewById(R.id.calendar).setVisibility(View.GONE);
				view.findViewById(R.id.panel).setVisibility(View.GONE);
			} else {
				view.findViewById(R.id.schedules).setVisibility(View.VISIBLE);
				view.findViewById(R.id.calendar).setVisibility(View.VISIBLE);
				view.findViewById(R.id.panel).setVisibility(View.VISIBLE);
			}
		} else if (value == 1) {
			String prefClubeRoom = preferences.getString("clubeRoom", "none");
			if (prefClubeRoom.equals("none")) {
				view.findViewById(R.id.clubemessages).setVisibility(View.GONE);
				view.findViewById(R.id.clubeaddnew).setVisibility(View.GONE);
			} else if (prefClubeRoom.equals("disable")) {
				view.findViewById(R.id.cluberoom).setVisibility(View.GONE);
				view.findViewById(R.id.clubemessages).setVisibility(View.GONE);
				view.findViewById(R.id.clubeaddnew).setVisibility(View.GONE);
			} else if (prefClubeRoom.equals("addnew")) {
				view.findViewById(R.id.clubeaddnew).setVisibility(View.VISIBLE);
				view.findViewById(R.id.cluberoom).setVisibility(View.GONE);
				view.findViewById(R.id.clubemessages).setVisibility(View.GONE);
			} else {
				view.findViewById(R.id.cluberoom).setVisibility(View.VISIBLE);
				view.findViewById(R.id.clubemessages).setVisibility(View.VISIBLE);
				view.findViewById(R.id.clubeaddnew).setVisibility(View.GONE);
			}
		} else if (value == 2) {
			String prefEletivaRoom = preferences.getString("eletivaRoom", "none");
			if (prefEletivaRoom.equals("none")) {
				view.findViewById(R.id.eletivamessages).setVisibility(View.GONE);
			} else {
				view.findViewById(R.id.eletivamessages).setVisibility(View.VISIBLE);
			}
		}
	}

	public static void setClick(final Activity activity, View view, int value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		if (value == 0) {
			final String classRoom = preferences.getString("classRoom", "none");
			view.findViewById(R.id.schedules).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "schedulesCache" + classRoom, 1, 0);
				}
			});

			view.findViewById(R.id.calendar).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "calendarCache" + classRoom, 1, 1);
				}
			});

			view.findViewById(R.id.panel).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "", 0, 2);
				}
			});

			view.findViewById(R.id.announcements).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "announcementsCache", 1, 3);
				}
			});
			view.findViewById(R.id.annotations).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "", 2, 4);
				}
			});
		} else if (value == 1) {
			final String clubeRoom = preferences.getString("clubeRoom", "none");
			view.findViewById(R.id.clubemessages).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "messagesCacheClube" + clubeRoom, 1, 10);
				}
			});

			view.findViewById(R.id.clubeannouncements).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "announcementsCacheClube", 1, 11);
				}
			});

			view.findViewById(R.id.clubeaddnew).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "", 0, 12);
				}
			});
		} else if (value == 2) {
			final String eletivaRoom = preferences.getString("eletivaRoom", "none");
			view.findViewById(R.id.eletivamessages).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "messagesCacheEletiva" + eletivaRoom, 1, 20);
				}
			});

			view.findViewById(R.id.eletivaannouncements).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Other.Click(activity, "announcementsCacheEletiva", 1, 21);
				}
			});
		}
	}

	public static void Click(Activity activity, String cache, int iscache, int value) {
		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		if (iscache == 0) {
			if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
				if (Build.VERSION.SDK_INT < 14) {
					Intent intent = new Intent(activity, aloogle.rebuapp.activity.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, value);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity, aloogle.rebuapp.activity.v14.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, value);
					activity.startActivity(intent);
				}
			} else {
				Toast toast = Toast.makeText(activity, activity.getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
		} else if (iscache == 1) {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
				if (Build.VERSION.SDK_INT < 14) {
					Intent intent = new Intent(activity, aloogle.rebuapp.activity.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, value);
					activity.startActivity(intent);
				} else {
					Intent intent = new Intent(activity, aloogle.rebuapp.activity.v14.WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, value);
					activity.startActivity(intent);
				}
			} else {
				boolean pageCache = preferences.getBoolean(cache, false);
				if (pageCache) {
					if (Build.VERSION.SDK_INT < 14) {
						Intent intent = new Intent(activity, aloogle.rebuapp.activity.WebViewActivity.class);
						intent.putExtra(Other.WebViewValue, value);
						activity.startActivity(intent);
					} else {
						Intent intent = new Intent(activity, aloogle.rebuapp.activity.v14.WebViewActivity.class);
						intent.putExtra(Other.WebViewValue, value);
						activity.startActivity(intent);
					}
				} else {
					Toast toast = Toast.makeText(activity, activity.getString(R.string.needinternetft), Toast.LENGTH_LONG);
					toast.show();
				}
			}
		} else {
			if (Build.VERSION.SDK_INT < 14) {
				Intent intent = new Intent(activity, aloogle.rebuapp.activity.WebViewActivity.class);
				intent.putExtra(Other.WebViewValue, value);
				activity.startActivity(intent);
			} else {
				Intent intent = new Intent(activity, aloogle.rebuapp.activity.v14.WebViewActivity.class);
				intent.putExtra(Other.WebViewValue, value);
				activity.startActivity(intent);
			}
		}
	}

	public static void ActionBarColor(Activity activity, String title) {
		if (Build.VERSION.SDK_INT >= 14) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String userColor = preferences.getString("prefAppColor", "green1");
			if (userColor.equals("green1")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff005400));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("green2")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff008002));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("green3")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff00cc00));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue1")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff003061));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue2")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff0a4e91));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue3")) {
				activity.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff0000cc));
				activity.getActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			}
		}
	}

	public static void SupportActionBarColor(ActionBarActivity activity, String title) {
		if (Build.VERSION.SDK_INT <= 14) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String userColor = preferences.getString("prefAppColor", "green1");
			if (userColor.equals("green1")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff005400));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("green2")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff008002));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("green3")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00cc00));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#fffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue1")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff003061));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue2")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff0a4e91));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			} else if (userColor.equals("blue3")) {
				activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff0000cc));
				activity.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + title + "</font>"));
			}
		}
	}
}
