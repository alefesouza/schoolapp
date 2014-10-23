package aloogle.rebuapp.activity.v14;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import aloogle.rebuapp.R;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Toast;
import aloogle.rebuapp.other.Other;
import aloogle.rebuapp.fragment.TabPagerAdapter;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	ViewPager Tab;
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Other.ActionBarColor(this, getString(R.string.app_name));

		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());

		Tab = (ViewPager)findViewById(R.id.pager);
		Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar = getActionBar();
				actionBar.setSelectedNavigationItem(position);
			}
		});
		Tab.setAdapter(TabAdapter);

		actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
				FragmentTransaction ft) {}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				Tab.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
				FragmentTransaction ft) {}

		};

		actionBar.addTab(actionBar.newTab().setText("Sala").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Clube").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Eletiva").setTabListener(tabListener));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Activity.CONNECTIVITY_SERVICE);
		switch (item.getItemId()) {
		case R.id.menu_books:
			if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
				Intent books = new Intent(MainActivity.this, WebViewActivity.class);
				books.putExtra(Other.WebViewValue, 6);
				startActivity(books);
			} else {
				Toast toast = Toast.makeText(this, getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case R.id.menu_settings:
			Intent settings = new Intent(MainActivity.this, aloogle.rebuapp.activity.Settings.class);
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
			about.putExtra(Other.WebViewValue, 5);
			startActivity(about);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		Other.ActionBarColor(this, getString(R.string.app_name));
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
