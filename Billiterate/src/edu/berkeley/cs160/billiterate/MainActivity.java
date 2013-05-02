package edu.berkeley.cs160.billiterate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
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
	private Fragment representativeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.agendaFragment = new AgendaFragment();
		this.trendingFragment = new TrendingFragment();
		this.representativeFragment = new RepresentativeActivity.AsFragment();

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));

		// TEMPORARY for testing
		/*
		 * Intent i = new Intent(this, RepresentativeActivity.class);
		 * i.putExtra("representative", "Linda Maio"); startActivity(i);
		 */

		/*
		 * Intent i = new Intent(this, BillInfoActivity.class);
		 * i.putExtra("title",
		 * "Zoning Amendments to Allow Later Hours of Operation on Telegraph Avenue; BMC Section 23E.56.060"
		 * ); startActivity(i);
		 */
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
		} else if (tab.getPosition() == 1) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, trendingFragment).commit();
			return;
		} else if (tab.getPosition() == 2) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, representativeFragment).commit();
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
		child.setVisibility(child.getVisibility() == View.GONE ? View.VISIBLE
				: View.GONE);
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
	}

	public static class AgendaFragment extends Fragment {
		
		ArrayList<Meeting> meetingsList = new ArrayList<Meeting>();
		LinearLayout ll;
		
		public AgendaFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View agenda = inflater.inflate(R.layout.agenda_layout, container, false);
			ll = (LinearLayout)agenda.findViewById(R.id.agenda_layout);
			LoadAgendaTask load_agenda = new LoadAgendaTask();
			load_agenda.execute();
			System.err.println("executed loading agenda");
			return agenda;
		}
		
		public void setAgendaView(LinearLayout ll) {

			OnClickListener clickAgenda = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int viewTag = (Integer)v.getTag();
					int tagNum = viewTag + 1;
					//viewTag = Integer.toString(tagNum);
					View parent = getView().findViewById(R.id.agenda_layout);
					//View child = parent.findViewWithTag(viewTag);
					View child = parent.findViewWithTag(tagNum);
					child.setVisibility(child.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
				}
				
			};
			System.err.println("somethings wrong with clickAgenda");
			int i = 1;
			String date = "";
			System.err.println("There should be two meetings listed, and there is actually: " + meetingsList.size());
			for (Meeting mt : meetingsList) {
				if (!(mt.date.equals(date))) {
					System.err.println("setting agenda there is a new date!");
					TextView dateHeading = new TextView(this.getActivity());
					dateHeading.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					dateHeading.setPadding(10, 5, 10, 5);
					dateHeading.setBackgroundResource(R.drawable.border);
					//dateHeading.setBackground(this.getResources().getDrawable(R.drawable.border));
					dateHeading.setTag(i);
					dateHeading.setText(mt.date);
					dateHeading.setTextSize(25);
					date = mt.date;
					i++;
					ll.addView(dateHeading);
					ll.invalidate();
					System.err.println("invalidating the view!");
				}
				TextView mtHeading = new TextView(this.getActivity());
				mtHeading.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				mtHeading.setPadding(10, 5, 10, 5);
				mtHeading.setBackgroundResource(R.drawable.border);
				//mtHeading.setBackground(this.getResources().getDrawable(R.drawable.border));
				mtHeading.setTag(i);
				mtHeading.setText(mt.type + " " + mt.time);
				mtHeading.setTextSize(25);
				mtHeading.setClickable(true);
				mtHeading.setOnClickListener(clickAgenda);
				i++;
				ll.addView(mtHeading);
				LinearLayout billsList = new LinearLayout(this.getActivity());
				billsList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				billsList.setPadding(10, 5, 10, 5);
				billsList.setOrientation(LinearLayout.VERTICAL);
				billsList.setTag(i);
				billsList.setVisibility(LinearLayout.GONE);
				TextView loc = new TextView(this.getActivity());
				loc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				loc.setPadding(20, 5, 20, 5);
				loc.setBackgroundResource(R.drawable.border);
				//loc.setBackground(this.getResources().getDrawable(R.drawable.border));
				loc.setGravity(Gravity.CENTER_VERTICAL);
				loc.setText(mt.location);
				loc.setTextSize(20);
				billsList.addView(loc);
				LoadBillsTask load_bills = new LoadBillsTask(this.getActivity().getApplicationContext(), billsList);
				load_bills.execute(mt.pk);
				ll.addView(billsList);
				i++;
			}
		}
		
		/**
		 * Takes a string of format year-month-day and returns string
		 * of format month day, year
		 * @param date
		 * @return
		 */
		public String convertDate(String date) {
			String[] date_sections = date.split("-");
			String day = date_sections[2];
			String year = date_sections[0];
			String month;
			switch (Integer.parseInt(date_sections[1], 10)) {
			case 1:
				month = "January";
				break;
			case 2:
				month = "February";
				break;
			case 3:
				month = "March";
				break;
			case 4:
				month = "April";
				break;
			case 5:
				month = "May";
				break;
			case 6:
				month = "June";
				break;
			case 7:
				month = "July";
				break;
			case 8:
				month = "August";
				break;
			case 9:
				month = "September";
				break;
			case 10:
				month = "October";
				break;
			case 11:
				month = "November";
				break;
			case 12:
				month = "December";
				break;
			default:
				month = "";
			}
			System.err.println("The month is " + month + " for the value of " + date_sections[1]);
			return month + " " + day + ", " + year;
		}
		
		private class LoadAgendaTask extends AsyncTask<Void, Void, JSONArray> {

			@Override
			protected JSONArray doInBackground(Void... arg0) {
				String url = "http://billiterate.pythonanywhere.com/billapp/agendas";
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
				// TODO
				if (messageList == null) {
					System.err.println("something went wrong with getting agendas");
				} else {
					for (int i = 0; i < messageList.length(); i++) {
						int pk = 0;
						String date = "";
						String time = "";
						String type = "";
						String location = "";
						try {
							JSONObject current = messageList.getJSONObject(i);
							pk = current.getInt("pk");
							//pk = current.getInt(2);
							//JSONArray fields = (JSONArray)current.getJSONArray(1);
							JSONObject fields = current.getJSONObject("fields");
							String date_time = fields.getString("date_time");
							date = date_time.split(" ")[0];
							date = convertDate(date);
							time = date_time.split(" ")[1];
							type = fields.getString("type");
							location = fields.getString("location");
						} catch (JSONException e) {
							System.err.println(messageList.toString());
							e.printStackTrace();
						}
						Meeting mt = new Meeting(pk, date, time, type, location);
						System.err.println("Adding new meeting to list");
						meetingsList.add(mt);
						System.err.println("After adding meeting to list, there are " + meetingsList.size() + " meetings!");
					}
					setAgendaView(ll);
				}
				
			}
			
		}
		
		private class LoadBillsTask extends AsyncTask<Integer, Void, JSONArray> {
			
			Context context;
			LinearLayout bills_layout;

			
			public LoadBillsTask(Context context, LinearLayout ll) {
				this.context = context;
				this.bills_layout = ll;
			}
			
			private class BillClickListener implements OnClickListener {
				
				Context context;
				String title;
				String summary;
				String rep;
				
				public BillClickListener(Context context, String title, String summary, String rep) {
					this.context = context;
					this.title = title;
					this.summary = summary;
					this.rep = rep;
				}

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(this.context, BillInfoActivity.class);
					i.putExtra("title", this.title);
					i.putExtra("summary", this.summary);
					i.putExtra("representative", this.rep);
					startActivity(i);
				}
				
			}

			@Override
			protected JSONArray doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				String url = "http://billiterate.pythonanywhere.com/billapp/agenda_bills?id=" + params[0];
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
				// TODO
				if (messageList == null) {
					System.err.println("something went wrong with getting the bills");
				} else {
					for (int i = 0; i < messageList.length(); i++) {
						String title = "";
						String summary = "";
						String representative = "";
						try {
							JSONObject current = messageList.getJSONObject(i);
							JSONObject fields = current.getJSONObject("fields");
							title = fields.getString("title");
							summary = fields.getString("summary");
							representative = fields.getString("representative");
						} catch (JSONException e) {
							System.err.print(messageList.toString());
							e.printStackTrace();
						}
						TextView bill = new TextView(this.context);
						bill.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
						bill.setPadding(20, 5, 20, 5);
						bill.setBackgroundResource(R.drawable.border);
						//bill.setBackground(this.context.getResources().getDrawable(R.drawable.border));
						bill.setGravity(Gravity.CENTER_VERTICAL);
						bill.setText(title);
						bill.setTextSize(18);
						bill.setClickable(true);
						bill.setOnClickListener(new BillClickListener(this.context, title, summary, representative));
						bills_layout.addView(bill);
						View bar = new View(this.context);
						bar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
						bar.setBackgroundColor(Color.DKGRAY);
						bills_layout.addView(bar);
					}
				}
			}
			
		}
	}

	public static class TrendingFragment extends Fragment {
		public TrendingFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View ret = inflater.inflate(R.layout.trending_layout, container,
					false);
			LoadTrendingTask task = new LoadTrendingTask();
			task.execute();
			return ret;
		}

		private class LoadTrendingTask extends AsyncTask<Void, Void, JSONArray> {
			/*
			 * protected void onPreExecute() {
			 * getView().findViewById(R.id.trending_progress
			 * ).setVisibility(View.VISIBLE); }
			 */

			protected JSONArray doInBackground(Void... arg0) {
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
				getView().findViewById(R.id.trending_progress).setVisibility(
						View.GONE);
				if (messageList == null) {
					TextView err = ((TextView) getView().findViewById(
							R.id.trending_one));
					err.setText("There was an error");
					err.setVisibility(View.VISIBLE);
					return;
				}
				TextView tv = null;
				for (int i = 0; i < messageList.length(); i++) {
					String title = "";
					try {
						JSONArray current = messageList.getJSONArray(i);
						title = current.getString(0);
					} catch (JSONException e) {
						System.err.print(messageList.toString());
						e.printStackTrace();
					}
					if (i == 0)
						tv = (TextView) getView().findViewById(
								R.id.trending_one);
					if (i == 1)
						tv = (TextView) getView().findViewById(
								R.id.trending_two);
					if (i == 2)
						tv = (TextView) getView().findViewById(
								R.id.trending_three);
					if (i == 3)
						tv = (TextView) getView().findViewById(
								R.id.trending_four);
					if (i == 4)
						tv = (TextView) getView().findViewById(
								R.id.trending_five);
					tv.setText(title);
					tv.setVisibility(View.VISIBLE);
				}
			}
		}
	}

}
