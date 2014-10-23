package aloogle.rebuapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import aloogle.rebuapp.fragment.TabPagerAdapter;
import aloogle.rebuapp.other.Other;
import aloogle.rebuapp.R;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class MainActivity extends ActionBarActivity {
	private ViewPager mPager;

	ActionBar mActionbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Other.SupportActionBarColor(this, getString(R.string.app_name));

		mActionbar = getSupportActionBar();

		mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mPager = (ViewPager)findViewById(R.id.pager);

		FragmentManager fm = getSupportFragmentManager();

		ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				mActionbar.setSelectedNavigationItem(position);
			}

		};

		mPager.setOnPageChangeListener(pageChangeListener);

		TabPagerAdapter fragmentPagerAdapter = new TabPagerAdapter(fm);

		mPager.setAdapter(fragmentPagerAdapter);

		mActionbar.setDisplayShowTitleEnabled(true);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mPager.setCurrentItem(tab.getPosition());

			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {}
		};

		Tab tab = mActionbar.newTab()
			.setText("Sala")
			.setTabListener(tabListener);

		mActionbar.addTab(tab);

		tab = mActionbar.newTab()
			.setText("Clube")
			.setTabListener(tabListener);

		mActionbar.addTab(tab);

		tab = mActionbar.newTab()
			.setText("Eletiva")
			.setTabListener(tabListener);

		mActionbar.addTab(tab);
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
			Intent settings = new Intent(MainActivity.this, Settings.class);
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
		Other.SupportActionBarColor(this, getString(R.string.app_name));
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
