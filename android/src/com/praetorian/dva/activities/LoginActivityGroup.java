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
import android.util.Log;
import android.view.Menu;

import com.praetorian.dva.R;
import com.praetorian.dva.fragments.LoginFragment;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.fragments.RegistrationFragment;

public class LoginActivityGroup extends FragmentActivity
{

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int section)
        {
            Log.println(Log.VERBOSE, "Fragment Test", "Called with id of " + section);
            switch (section)
            {
            case 0:
                LoginFragment lf =  new LoginFragment();
                lf.setPager(mViewPager);
                return lf;
            case 1:
                RegistrationFragment rf = new RegistrationFragment();
                return rf;
            case 2:
                return new ProxyFragment();
            }
            return null;

        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
            case 0:
                return getString(LoginFragment.NAME_FRAGMENT_RESOURCE);
            case 1:
                return getString(RegistrationFragment.NAME_FRAGMENT_RESOURCE);
            case 2:
                return getString(ProxyFragment.NAME_FRAGMENT_RESOURCE);
            }
            return null;

        }

        @Override
        public int getCount()
        {
            return 3;
        }

    }

}
