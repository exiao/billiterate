package edu.berkeley.cs160.billiterate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private Fragment agendaFragment;
	private Fragment trendingFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.agendaFragment = new AgendaFragment();
		this.trendingFragment = new TrendingFragment();
		
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
				
				
		// TEMPORARY for testing
		/*Intent i = new Intent(this, RepresentativeActivity.class);
		i.putExtra("representative", "Linda Maio");
		startActivity(i);*/
		
		/*Intent i = new Intent(this, BillInfoActivity.class);
		i.putExtra("title", "Zoning Amendments to Allow Later Hours of Operation on Telegraph Avenue; BMC Section 23E.56.060");
		startActivity(i);*/
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
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
		// When the given tab is selected, show the tab contents in the
		// container view.
		if (tab.getPosition() == 0) {
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, agendaFragment).commit();
			return;
		} else {
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, trendingFragment).commit();
			return;
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void onAgendaClick(View view) {
		String viewTag = (String) view.getTag();
		int tagNum = Integer.parseInt(viewTag);
		tagNum += 1;
		viewTag = Integer.toString(tagNum);
		View parent = findViewById(R.id.agenda_layout);
		View child = parent.findViewWithTag(viewTag);
		child.setVisibility(child.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
	}
	
	public void onBillClick(View view) {
		Intent intent = new Intent(this, BillInfoActivity.class);
		intent.putExtra("title", ((TextView) view).getText());
		startActivity(intent);
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}
	
	public static class AgendaFragment extends Fragment {
		public AgendaFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.agenda_layout, container, false);
		}
	}
	
	public static class TrendingFragment extends Fragment {
		public TrendingFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState) {
			View ret = inflater.inflate(R.layout.trending_layout, container, false);
			LoadTrendingTask task = new LoadTrendingTask();
			task.execute();
			return ret;
		}
		
		private class LoadTrendingTask extends AsyncTask<Void, Void, JSONArray> {
			/*protected void onPreExecute() {
				getView().findViewById(R.id.trending_progress).setVisibility(View.VISIBLE);
			}*/
			
			protected JSONArray doInBackground(Void...arg0) {
				String url = "http://billiterate.pythonanywhere.com/trending";
				System.err.println("URL = " + url);
				HttpResponse response;
				HttpClient client = new DefaultHttpClient();
				String responseString = "";
				
				try {
					response = client.execute(new HttpGet(url));
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						responseString = out.toString();
					} else {
						response.getEntity().getContent().close();
					}
				} catch (ClientProtocolException cpe) {
					cpe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				try {
					JSONArray messages = new JSONArray(responseString);
					return messages;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(JSONArray messageList) {
				getView().findViewById(R.id.trending_progress).setVisibility(View.GONE);
				if (messageList == null) {
					TextView err = ( (TextView) getView().findViewById(R.id.trending_one));
					err.setText("There was an error");
					err.setVisibility(View.VISIBLE);
				}
				TextView tv = null;
				for (int i = 0; i < messageList.length(); i++) {
					String title = "";
					try {
						JSONArray current = messageList.getJSONArray(i);
						title = current.getString(0);
					} catch (JSONException e ) {
						System.err.print(messageList.toString());
						e.printStackTrace();
					}
					if (i == 0) tv = (TextView) getView().findViewById(R.id.trending_one);
					if (i == 1) tv = (TextView) getView().findViewById(R.id.trending_two);
					if (i == 2) tv = (TextView) getView().findViewById(R.id.trending_three);
					if (i == 3) tv = (TextView) getView().findViewById(R.id.trending_four);
					if (i == 4) tv = (TextView) getView().findViewById(R.id.trending_five);
					tv.setText(title);
					tv.setVisibility(View.VISIBLE);
				}
			}
		}
	}

}
