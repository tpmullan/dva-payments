package com.praetorian.dva.activities;
/**
 * Damn Vulnerable Application
 * 
 * @author Richard Penshorn
 * Praetorian Labs 2013
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.praetorian.dva.R;
import com.praetorian.dva.fragments.MoveMoneyFragment;
import com.praetorian.dva.fragments.MyAccountFragment;
import com.praetorian.dva.fragments.MyFriendsFragment;
import com.praetorian.dva.fragments.MyMoneyFragment;
import com.praetorian.dva.fragments.WelcomeFragment;

public class MainActivityGroup extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authed);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.mainapp);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int section) {
			switch (section) {
			case 0:
				WelcomeFragment welcomeFragment = new WelcomeFragment();
				welcomeFragment.setPager(mViewPager);
				return welcomeFragment;

			case 1:
				MyMoneyFragment myMoneyFragment = new MyMoneyFragment();
				myMoneyFragment.setPager(mViewPager);
				return myMoneyFragment;
			case 2:
				return new MoveMoneyFragment();
			case 3:
				return new MyAccountFragment();
			case 4:
				return new MyFriendsFragment();

			}
			return null;

		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(WelcomeFragment.NAME_FRAGMENT_RESOURCE);

			case 1:
				return getString(MyMoneyFragment.NAME_FRAGMENT_RESOURCE);
			case 2:
				return getString(MoveMoneyFragment.NAME_FRAGMENT_RESOURCE);
			case 3:
				return getString(MyAccountFragment.NAME_FRAGMENT_RESOURCE);
			case 4:
				return getString(MyFriendsFragment.NAME_FRAGMENT_RESOURCE);
			}
			return null;

		}

		@Override
		public int getCount() {
			return 5;
		}

	}

}