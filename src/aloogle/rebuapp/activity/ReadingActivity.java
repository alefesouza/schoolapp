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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.google.android.gms.ads.*;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import aloogle.rebuapp.R;
import aloogle.rebuapp.fragment.ReadingFragment2;
import aloogle.rebuapp.fragment.ReadingFragment2Alfa;
import aloogle.rebuapp.lib.SlidingTabLayout;
import aloogle.rebuapp.other.Other;

@SuppressLint({
	"DefaultLocale", "CutPasteId"
})
public class ReadingActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
	final Context context = this;
	public Toolbar mToolbar;
	SharedPreferences preferences;
	Editor editor;
	String titulo;
	public static String categoria, query;
	FragmentStatePagerAdapter TabAdapter;

	private NavigationAdapter mPagerAdapter;
	private View mToolbarView;
	private TouchInterceptionFrameLayout mInterceptionLayout;
	private ViewPager mPager;
	private int mSlop;
	private boolean mScrolled;
	private ScrollState mLastScrollState;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		setContentView(R.layout.reading);

		mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		titulo = getIntent().getStringExtra("titulo");

		FragmentActivity.ActionBarColor(this, titulo);
		findViewById(R.id.headert).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(this))));
		findViewById(R.id.sliding_tabs).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + Other.getColor(this))));

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (getIntent().hasExtra("categoria")) {
			categoria = getIntent().getStringExtra("categoria");
			query = "";
		} else {
			categoria = "";
			query = getIntent().getStringExtra("query");
		}

		initNotification();

		ViewCompat.setElevation(findViewById(R.id.headert), getResources().getDimension(R.dimen.toolbar_elevation));
		mToolbarView = findViewById(R.id.toolbar);
		mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);

		final int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
		findViewById(R.id.pager_wrapper).setPadding(0, getActionBarSize() + tabHeight, 0, 0);

		SlidingTabLayout slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(mPager);

		ViewConfiguration vc = ViewConfiguration.get(this);
		mSlop = vc.getScaledTouchSlop();
		mInterceptionLayout = (TouchInterceptionFrameLayout)findViewById(R.id.container);
		mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
	}

	public void initNotification() {
		boolean warningCard = preferences.getBoolean("warningReadingCard", false);
		if (!warningCard) {
			final AlertDialog dialogreading = new AlertDialog.Builder(ReadingActivity.this)
				.setTitle(R.string.app_name)
				.setMessage(R.string.dialogreading)
				.setPositiveButton("OK", null)
				.create();

			dialogreading.setCanceledOnTouchOutside(false);

			dialogreading.setOnShowListener(new
				DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialogreading.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new
						View.OnClickListener() {
						@Override
						public void onClick(View view) {
							editor.putBoolean("warningReadingCard", true);
							editor.commit();
							dialogreading.dismiss();
						}
					});
				}
			});
			dialogreading.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			ReadingActivity.this.finish();
			return true;
		default:
			return
			super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ReadingActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
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
		} else if (scrollState == ScrollState.UP) {
			if (toolbarHeight <= scrollY) {
				hideToolbar();
			} else {
				hideToolbar();
			}
		} else if (!toolbarIsShown() && !toolbarIsHidden()) {
			hideToolbar();
		}
	}

	private Fragment getCurrentFragment() {
		return mPagerAdapter.getItemAt(mPager.getCurrentItem());
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

	private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

		private static final String[]TITLES = new String[]{
			"Recentes", 		"Alfabética"
		};

		public NavigationAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		protected Fragment createItem(int position) {
			Fragment f;
			final int pattern = position % 2;
			switch (pattern) {
			case 0:
				Bundle bundle = new Bundle();
				bundle.putString("categoria", categoria);
				bundle.putString("query", query);
				Fragment recentes = new ReadingFragment2();
				recentes.setArguments(bundle);
				f = recentes;
				break;
			case 1:
				Bundle bundlea = new Bundle();
				bundlea.putString("categoria", categoria);
				bundlea.putString("query", query);
				Fragment alfabetica = new ReadingFragment2Alfa();
				alfabetica.setArguments(bundlea);
				f = alfabetica;
				break;
			default:
				Bundle bundler = new Bundle();
				bundler.putString("categoria", categoria);
				bundler.putString("query", query);
				Fragment recentesd = new ReadingFragment2();
				recentesd.setArguments(bundler);
				f = recentesd;
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
	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		supportInvalidateOptionsMenu();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
