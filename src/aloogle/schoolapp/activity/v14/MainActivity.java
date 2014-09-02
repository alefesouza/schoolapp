package aloogle.schoolapp.activity.v14;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import aloogle.schoolapp.R;
import aloogle.schoolapp.other.Other;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Other.setText(this);
		Other.setClick(this);

		findViewById(R.id.addevent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConnectivityManager cm = (ConnectivityManager)MainActivity.this.getSystemService(Activity.CONNECTIVITY_SERVICE);
				if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
					Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
					intent.putExtra(Other.WebViewValue, 2);
					startActivity(intent);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent settings = new Intent(MainActivity.this, aloogle.schoolapp.activity.Settings.class);
			startActivity(settings);
			return true;
		case R.id.menu_share:
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharecontent));
			shareIntent.setType("text/plain");
			startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
			return true;
		case R.id.menu_about:
			Intent about = new Intent(MainActivity.this, WebViewActivity.class);
			about.putExtra(Other.WebViewValue, 4);
			startActivity(about);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		super.onResume();
		Other.setText(this);
		Other.setClick(this);
	}
}
