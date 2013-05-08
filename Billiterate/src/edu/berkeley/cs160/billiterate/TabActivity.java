package edu.berkeley.cs160.billiterate;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabActivity extends FragmentActivity implements
		ActionBar.TabListener {

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
		setContentView(R.layout.activity_tutorial);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		actionBar.addTab(actionBar.newTab()
				.setText("Start")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Agenda")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Trending")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Representatives")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText("Bills")
				.setTabListener(this));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
		public Fragment getItem(int position) {
			Log.d("GET ITEM", "GET ITEM CALLED FOR POSITION " + position);
			// getItem is called to instantiate the fragment for the given page.
			
			switch(position) {
			case 0: 
				return new StartFragment();
			case 1: 
				return new AgendaFragment();
			case 2:
				return new TrendingFragment();
			case 3:
				return new RepresentativesFragment();
			case 4:
				return new BillsFragment();
			default:
				return null;

			}
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, that simply
	 * displays an image.
	 */
	public static class StartFragment extends Fragment {

		public StartFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView;
				rootView = inflater.inflate(R.layout.fragment_tutorial_start,
						container, false);
			return rootView;
		}
		
	}
	
	public static class AgendaFragment extends Fragment {

		public AgendaFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView;
				rootView = inflater.inflate(R.layout.fragment_tutorial_agenda,
						container, false);
			return rootView;
		}
		
	}
	
	public static class TrendingFragment extends Fragment {

		public TrendingFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {			
			View rootView;
				rootView = inflater.inflate(R.layout.fragment_tutorial_trending,
						container, false);
			return rootView;
		}
	}
	
	public static class RepresentativesFragment extends Fragment {

		public RepresentativesFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView;
				rootView = inflater.inflate(R.layout.fragment_tutorial_representatives,
						container, false);
			return rootView;
		}
	}
	
	public static class BillsFragment extends Fragment {

		public BillsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView;
				rootView = inflater.inflate(R.layout.fragment_tutorial_bills,
						container, false);
			return rootView;
		}
	}
	
	 public void closeTutorial(View view) {
		  Intent intent = new Intent(this, MainActivity.class);
		  startActivity(intent);
		  finish();
	  }

}
