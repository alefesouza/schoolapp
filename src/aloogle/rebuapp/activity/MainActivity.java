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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.google.android.gms.ads.*;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import aloogle.rebuapp.R;
import aloogle.rebuapp.adapter.DrawerAdapter;
import aloogle.rebuapp.fragment.*;
import aloogle.rebuapp.lib.SlidingTabLayout;
import aloogle.rebuapp.other.*;

@SuppressLint({
	"DefaultLocale", "CutPasteId", "InflateParams"
})
@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
	final Context context = this;
	public Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	@SuppressWarnings("unused")
	private CharSequence mDrawerTitle;
	private ArrayList < Icons > icons;
	private DrawerAdapter adapter2;
	private String[]categoryTitles;
	private TypedArray categoryIcons;
	SharedPreferences preferences;
	Editor editor;
	public static String titulo;
	private SalaAdapter mSalaAdapter;
	public static int pos;
	private View mToolbarView;
	private TouchInterceptionFrameLayout mInterceptionLayout;
	private ViewPager mPager;
	private int mSlop;
	private boolean mScrolled, passed, start, panelw;
	public static boolean home, isTouch;
	private ScrollState mLastScrollState;

	public static Menu optionsMenu;

	FloatingActionButton fabpanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		setContentView(R.layout.toolbar_drawer);

		mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		titulo = "Sala";
		ActionBarColor(this, titulo);

		initDrawer();

		pos = 1;
		passed = false;
		panelw = false;
		home = true;

		if (Build.VERSION.SDK_INT == 10) {
			boolean warningVersion = preferences.getBoolean("warningVersion", false);
			if (!warningVersion) {
				new dialogs().gingerBread();
			}
		}

		boolean haveAccount = preferences.getBoolean("haveAccount", false);
		if (haveAccount) {
			boolean warningPanel = preferences.getBoolean("warningPanel", false);
			if (!warningPanel) {
				if (!panelw) {
					new dialogs().panel();
					panelw = true;
				}
			}
		}

		boolean warningCard = preferences.getBoolean("warningNormalCard", false);
		if (!warningCard) {
			new dialogs().card();
		}

		ViewCompat.setElevation(findViewById(R.id.header), getResources().getDimension(R.dimen.toolbar_elevation));
		mToolbarView = findViewById(R.id.toolbar);
		mSalaAdapter = new SalaAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mSalaAdapter);

		final int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
		findViewById(R.id.pager_wrapper).setPadding(0, getActionBarSize() + tabHeight, 0, 0);

		SlidingTabLayout slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
		slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));
		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(mPager);

		ViewConfiguration vc = ViewConfiguration.get(this);
		mSlop = vc.getScaledTouchSlop();
		mInterceptionLayout = (TouchInterceptionFrameLayout)findViewById(R.id.container);
		mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);

		fabpanel = (FloatingActionButton)findViewById(R.id.fabpanel);
		fabColor();
		if (preferences.getBoolean("haveAccount", false)) {
			fabpanel.setVisibility(View.VISIBLE);
		}

		if (Build.VERSION.SDK_INT <= 10) {
			fabpanel.setShadow(false);
		}

		fabpanel.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast toast = Toast.makeText(MainActivity.this, "Painel", Toast.LENGTH_SHORT);
				toast.show();
				return true;
			}
		});

		if (getIntent().hasExtra("fromnotification")) {
			if (savedInstanceState != null) {
				pos = savedInstanceState.getInt("position");
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						selectItem(6);
					}
				}, 100);
			}
		} else {
			if (savedInstanceState != null) {
				pos = savedInstanceState.getInt("position");
			}
		}

		selectItem(pos);
	}

	public static void ActionBarColor(final ActionBarActivity activity, String title) {
		String userColor = Other.getColor(activity);
		activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + userColor)));
		activity.findViewById(R.id.frame).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + userColor)));
		activity.findViewById(R.id.header).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + userColor)));
		activity.findViewById(R.id.sliding_tabs).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + userColor)));
		activity.getSupportActionBar().setTitle(title);
	}

	public void fabColor() {
		fabpanel.setColorNormal(Color.parseColor("#" + Other.getColor2(this, 0)));
		fabpanel.setColorPressed(Color.parseColor("#" + Other.getColor2(this, 1)));
	}

	public void initDrawer() {
		mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

		mDrawerList = (ListView)findViewById(R.id.left_drawer);

		categoryTitles = getResources().getStringArray(
				R.array.navigation_main_sections);

		categoryIcons = getResources().obtainTypedArray(R.array.drawable_ids);

		icons = new ArrayList < Icons > ();

		for (int i = 0; i < categoryIcons.length(); i++) {
			icons.add(new Icons(categoryTitles[i], categoryIcons.getResourceId(i, (i + 1)*-1)));
		}

		categoryIcons.recycle();

		LayoutInflater inflater = getLayoutInflater();
		final ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, 		mDrawerList, false);
		final ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.footer, 		mDrawerList, false);
		final ViewGroup footer2 = (ViewGroup)inflater.inflate(R.layout.footer2, 		mDrawerList, false);

		mDrawerList.addHeaderView(header, null, true);
		mDrawerList.addFooterView(footer, null, true);
		mDrawerList.addFooterView(footer2, null, true);

		adapter2 = new DrawerAdapter(getApplicationContext(), icons);
		mDrawerList.setAdapter(adapter2);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(
				this, 			mDrawerLayout, 			R.string.drawer_open, 			R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};

		mDrawerToggle.setDrawerIndicatorEnabled(true);

		mDrawerToggle.syncState();

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private class dialogs {
		public void gingerBread() {
			final AlertDialog dialogversion = new AlertDialog.Builder(MainActivity.this)
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogversion)
				.setPositiveButton("OK", null)
				.create();

			dialogversion.setCanceledOnTouchOutside(false);

			dialogversion.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogversion.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningVersion", true);
							editor.commit();
							dialogversion.dismiss();
						}
					});
				}
			});
			dialogversion.show();
		}

		public void panel() {
			final AlertDialog dialogpanel = new AlertDialog.Builder(MainActivity.this)
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogpanel)
				.setPositiveButton("OK", null)
				.create();

			dialogpanel.setCanceledOnTouchOutside(false);

			dialogpanel.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogpanel.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningPanel", true);
							editor.commit();
							dialogpanel.dismiss();
						}
					});
				}
			});
			dialogpanel.show();
		}

		public void card() {
			final AlertDialog dialogcard = new AlertDialog.Builder(MainActivity.this)
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogcard)
				.setPositiveButton("OK", null)
				.create();

			dialogcard.setCanceledOnTouchOutside(false);

			dialogcard.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogcard.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningNormalCard", true);
							editor.commit();
							dialogcard.dismiss();
						}
					});
				}
			});
			dialogcard.show();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView <  ?  > parent, View view, int position, long id) {
			int posi;
			if (position == 0) {
				posi = 1;
				mDrawerList.setItemChecked(1, true);
			} else {
				posi = position;
			}

			if (posi != pos) {
				selectItem(posi);
			}
		}
	}

	public void Home() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					theHome();
				}
			}, 100);
		} else {
			theHome();
		}
	}

	public void theHome() {
		titulo = "Sala";
		passed = false;
		home = true;
		pos = 1;
		SalaFragment.setPanel(MainActivity.this);
		mDrawerList.setItemChecked(1, true);
		ActionBarColor(this, titulo);
		findViewById(R.id.sliding_tabs).setVisibility(View.VISIBLE);
		mPager.setVisibility(View.VISIBLE);
		FrameLayout content = (FrameLayout)findViewById(R.id.content_frame);
		content.setVisibility(View.GONE);
		supportInvalidateOptionsMenu();
	}

	public void ChangeFragment(String title, Fragment frag) {
		passed = true;
		if (home) {
			findViewById(R.id.sliding_tabs).setVisibility(View.GONE);
			mPager.setVisibility(View.GONE);
			FrameLayout content = (FrameLayout)findViewById(R.id.content_frame);
			content.setVisibility(View.VISIBLE);
			home = false;
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, frag);
		if (start) {
			ft.addToBackStack(null);
		} else {
			start = true;
		}
		ft.commit();
		supportInvalidateOptionsMenu();
	}

	private void selectItem(int position) {
		switch (position) {
		case 0:
			Home();
			break;
		case 1:
			Home();
			break;
		case 2:
			ChangeFragment("Clube", new ClubeFragment());
			break;
		case 3:
			ChangeFragment("Eletiva", new EletivaFragment());
			break;
		case 4:
			ChangeFragment("Comunicados", new ComunicadosFragment());
			break;
		case 5:
			ChangeFragment("Anotações", new AnnotationsFragment());
			break;
		case 6:
			ChangeFragment("Notificações", new NotificationsFragment());
			break;
		case 7:
			ChangeFragment("Biblioteca", new ReadingFragment());
			break;
		case 8:
			ChangeFragment("Dicionário", new DictionaryFragment());
			break;
		case 9:
			ChangeFragment("Cartazes", new CartazFragment());
			break;
		case 10:
			Intent blog = new Intent(MainActivity.this, FragmentActivity.class);
			blog.putExtra("fragment", 2);
			blog.putExtra("titulo", "Blog");
			blog.putExtra("url", "http://willianrrebua.blogspot.com");
			startActivity(blog);
			break;
		case 11:
			Intent jornal = new Intent(MainActivity.this, FragmentActivity.class);
			jornal.putExtra("fragment", 2);
			jornal.putExtra("titulo", "Jornal");
			jornal.putExtra("url", "http://facebook.com/REVOLUCIONARIOSREBUA");
			startActivity(jornal);
			break;
		case 12:
			Intent settings = new Intent(MainActivity.this, FragmentActivity.class);
			settings.putExtra("fragment", 0);
			startActivity(settings);
			break;
		case 13:
			Intent about = new Intent(MainActivity.this, FragmentActivity.class);
			about.putExtra("fragment", 1);
			startActivity(about);
			break;
		}

		if (position < 10) {
			pos = position;
			mDrawerList.setItemChecked(position, true);
		} else {
			mDrawerList.setItemChecked(pos, true);
		}

		setRefreshActionButtonState(false, MainActivity.this);

		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (menu.findItem(R.id.menu_search) != null) {
			if (findViewById(R.id.pager).getVisibility() == View.VISIBLE) {
				menu.findItem(R.id.menu_search).setVisible(false);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.menu_share:
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharecontent));
			shareIntent.setType("text/plain");
			startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}

	public static void setRefreshActionButtonState(final boolean refreshing, Activity activity) {
		if (optionsMenu != null && activity != null) {
			final MenuItem refreshItem = optionsMenu.findItem(R.id.menu_refresh);
			if (refreshItem != null) {
				if (refreshing) {
					LayoutInflater inflatere = activity.getLayoutInflater();
					View v = inflatere.inflate(R.layout.toolbar_progress, null);
					if (Build.VERSION.SDK_INT >= 21) {
						ProgressBar refresh = (ProgressBar)v.findViewById(R.id.progressBar1);
						refresh.getIndeterminateDrawable().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFFFFFF));
					}
					MenuItemCompat.setActionView(refreshItem, v);
				} else {
					MenuItemCompat.setActionView(refreshItem, null);
				}
			}
		}
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {}

	@Override
	public void onDownMotionEvent() {}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		if (!mScrolled) {
			adjustToolbar(scrollState);
		}
	}

	private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
		@Override
		public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
			if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
				return false;
			}

			Scrollable scrollable = getCurrentScrollable();
			if (scrollable == null) {
				mScrolled = false;
				return false;
			}

			int toolbarHeight = mToolbarView.getHeight();
			int translationY = (int)ViewHelper.getTranslationY(mInterceptionLayout);
			boolean scrollingUp = 0 < diffY;
			boolean scrollingDown = diffY < 0;
			if (scrollingUp) {
				if (translationY < 0) {
					mScrolled = true;
					mLastScrollState = ScrollState.UP;
					return true;
				}
			} else if (scrollingDown) {
				if (-toolbarHeight < translationY) {
					mScrolled = true;
					mLastScrollState = ScrollState.DOWN;
					return true;
				}
			}
			mScrolled = false;
			return false;
		}

		@Override
		public void onDownMotionEvent(MotionEvent ev) {}

		@Override
		public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
			float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -mToolbarView.getHeight(), 0);
			ViewHelper.setTranslationY(mInterceptionLayout, translationY);
			if (translationY < 0) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mInterceptionLayout.getLayoutParams();
				lp.height = (int)(-translationY + getScreenHeight());
				mInterceptionLayout.requestLayout();
			}
		}

		@Override
		public void onUpOrCancelMotionEvent(MotionEvent ev) {
			mScrolled = false;
			adjustToolbar(mLastScrollState);
		}
	};

	private Scrollable getCurrentScrollable() {
		if (Build.VERSION.SDK_INT > 10) {
			Fragment fragment = getCurrentFragment();
			if (fragment == null) {
				return null;
			}
			View view = fragment.getView();
			if (view == null) {
				return null;
			}
			return (Scrollable)view.findViewById(R.id.list);
		} else {
			return null;
		}
	}

	private void adjustToolbar(ScrollState scrollState) {
		int toolbarHeight = mToolbarView.getHeight();
		final Scrollable scrollable = getCurrentScrollable();
		if (scrollable == null) {
			return;
		}
		int scrollY = scrollable.getCurrentScrollY();
		if (scrollState == ScrollState.DOWN) {
			showToolbar();
			fabpanel.show(true);
		} else if (scrollState == ScrollState.UP) {
			if (toolbarHeight <= scrollY) {
				hideToolbar();
			} else {
				if (!passed) {
					hideToolbar();
				}
			}
			fabpanel.hide(true);
		} else if (!toolbarIsShown() && !toolbarIsHidden()) {
			hideToolbar();
			fabpanel.hide(true);
		}
	}

	private Fragment getCurrentFragment() {
		return mSalaAdapter.getItemAt(mPager.getCurrentItem());
	}

	private boolean toolbarIsShown() {
		return ViewHelper.getTranslationY(mInterceptionLayout) == 0;
	}

	private boolean toolbarIsHidden() {
		return ViewHelper.getTranslationY(mInterceptionLayout) == -mToolbarView.getHeight();
	}

	private void showToolbar() {
		animateToolbar(0);
	}

	private void hideToolbar() {
		animateToolbar(-mToolbarView.getHeight());
	}

	private void animateToolbar(final float toY) {
		float layoutTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
		if (layoutTranslationY != toY) {
			ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mInterceptionLayout), toY).setDuration(200);
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float translationY = (float)animation.getAnimatedValue();
					ViewHelper.setTranslationY(mInterceptionLayout, translationY);
					if (translationY < 0) {
						FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mInterceptionLayout.getLayoutParams();
						lp.height = (int)(-translationY + getScreenHeight());
						mInterceptionLayout.requestLayout();
					}
				}
			});
			animator.start();
		}
	}

	protected int getActionBarSize() {
		TypedValue typedValue = new TypedValue();
		int[]textSizeAttr = new int[]{
			R.attr.actionBarSize
		};
		int indexOfAttrTextSize = 0;
		TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
		int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
		a.recycle();
		return actionBarSize;
	}

	protected int getScreenHeight() {
		return findViewById(android.R.id.content).getHeight();
	}

	/**
	 *This adapter provides two types of fragments as an example.
	 *{@linkplain #createItem(int)} should be modified if you use this example for your app.
	 */
	private static class SalaAdapter extends CacheFragmentStatePagerAdapter {

		private static final String[]TITLES = new String[]{
			"Agenda", 		"Horário"
		};

		public SalaAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		protected Fragment createItem(int position) {
			Fragment f;
			final int pattern = position % 2;
			switch (pattern) {
			case 0:
				f = new SalaFragment();
				break;
			case 1:
				f = new HorarioFragment();
				break;
			default:
				f = new SalaFragment();
				break;
			}
			return f;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			if (drawerOpen) {
				mDrawerLayout.closeDrawer(mDrawerList);
				return true;
			} else {
				if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
					getSupportFragmentManager().popBackStack();
					return true;
				} else {
					if (home) {
						MainActivity.this.finish();
					}
					selectItem(1);
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			break;
		case MotionEvent.ACTION_MOVE:
			isTouch = true;
			break;
		case MotionEvent.ACTION_UP:
			isTouch = false;
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("position", pos);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		ActionBarColor(this, titulo);
		fabColor();
		boolean haveAccount = preferences.getBoolean("haveAccount", false);
		if (haveAccount) {
			fabpanel.setVisibility(View.VISIBLE);
			boolean warningPanel = preferences.getBoolean("warningPanel", false);
			if (!warningPanel) {
				if (!panelw) {
					new dialogs().panel();
					panelw = true;
				}
			}
		} else {
			fabpanel.setVisibility(View.GONE);
		}
		supportInvalidateOptionsMenu();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
