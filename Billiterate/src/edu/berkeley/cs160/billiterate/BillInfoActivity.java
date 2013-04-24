package edu.berkeley.cs160.billiterate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BillInfoActivity extends Activity {
	
	// information for this screen
	String bill_title;
	
	// get view widgets for modification
	LinearLayout bill_view;
	TextView bill_title_textview;
	//ProgressBar	ratings;
	ProgressBar down_ratings;
	ProgressBar up_ratings;
	int likes;
	int dislikes;
	ImageButton like;
	ImageButton dislike;
	TextView summary;
	EditText commentBox;
	
	int billId;
	String representative = "";
	
	boolean liked = false;;
	boolean disliked = false;

	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	SimpleAdapter adapter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_info);
		
		Bundle extras = this.getIntent().getExtras();
		bill_title = extras.getString("title");
		billId = Math.abs(bill_title.hashCode());
		bill_view = (LinearLayout)findViewById(R.id.bill_view);
		bill_title_textview = (TextView)findViewById(R.id.title);
		//ratings = (ProgressBar)findViewById(R.id.ratings);
		down_ratings = (ProgressBar)findViewById(R.id.red_progress);
		up_ratings = (ProgressBar)findViewById(R.id.green_progress);
		like = (ImageButton)findViewById(R.id.like);
		dislike = (ImageButton)findViewById(R.id.dislike);
		summary = (TextView)findViewById(R.id.bill_summary);
		commentBox = (EditText)findViewById(R.id.comment);
		
		// display bill information
		bill_title_textview.setText(bill_title);
		
		//getBillSettings(bill_title);
		setSummary(bill_title);
		
		// set progress bar colors
		down_ratings.getProgressDrawable().setColorFilter(Color.RED, Mode.MULTIPLY);
		up_ratings.getProgressDrawable().setColorFilter(Color.GREEN, Mode.MULTIPLY);
		loadProgressBars();
		
		adapter = new SimpleAdapter(this, data, R.layout.comment_layout, 
					new String[] {"Name", "Comment", "ID"}, 
					new int[] {R.id.nameText, R.id.commentText, R.id.IDText});
		ListView comments = (ListView) findViewById(R.id.comments);
		comments.setAdapter(adapter);
		
		load(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill_info, menu);
		return true;
	}
	
	public void getBillSettings(String title) {
		// TODO
		// get bill information and display to screen
	}
	
	public void setSummary(String title) {
		if (title.equals("Zoning Amendments to Allow Later Hours of Operation on Telegraph Avenue; BMC Section 23E.56.060")) {
			summary.setText(R.string.late_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Amending BMC Chapter 9.32 Massage Ordinance: Exempting Chair Massage")) {
			summary.setText(R.string.massage_s);
			representative = "Darryl Moore";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for Possible Issuance After " +
				"Council Approval on April 2, 2013")) {
			summary.setText(R.string.bid_s);
			representative = "Darryl Moore";
		} else if (title.equals("Contract No. 7722D Amendment: Milliman, Inc. for Actuarial Services")) {
			summary.setText(R.string.milliman_s);
			representative = "Linda Maio";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled for " +
				"Possible Issuance After Council Approval on March 19, 2013")) {
			summary.setText(R.string.solicit_s);
			representative = "Darryl Moore";
		} else if (title.equals("Revenue Contract: County of Alameda for Shelter Plus Care Program")) {
			summary.setText(R.string.shelter_s);
			representative = "Linda Maio";
		} else if (title.equals("Classification: Assistant Human Resources Analyst")) {
			summary.setText(R.string.ahra_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Lease: Women's Daytime Drop-In Center at 2218 Acton Street")) {
			summary.setText(R.string.womens_s);
			representative = "Linda Maio";
		} else if (title.equals("Lease Amendment: International Computer Science Institute at 1947 Center Street")) {
			summary.setText(R.string.csi_s);
			representative = "Darryl Moore";
		} else if (title.equals("City Council Rules of Procedure and Order")) {
			summary.setText(R.string.rules_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on March 5, 2013")) {
			summary.setText(R.string.insure_s);
			representative = "Linda Maio";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText(R.string.taxi_s);
			representative = "Darryl Moore";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText(R.string.renew_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Option Agreement - 3135 Harper Street")) {
			summary.setText(R.string.harper_s);
			representative = "Linda Maio";
		} else if (title.equals("Appointment of City Clerk")) {
			summary.setText(R.string.clerk_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After " +
				"Council Approval on February 5, 2013")) {
			summary.setText(R.string.feb_s);
			representative = "Darryl Moore";
		} else if (title.equals("Option Agreement - 3135 Harper Street (Revised)")) {
			summary.setText(R.string.harper_rev_s);
			representative = "Linda Maio";
		} else if (title.equals("Contract: ACS State and Local Solutions for a Parking Citation Management Solution")) {
			summary.setText(R.string.acs_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Contract: AJW Construction for FY 2013 Responsive Sidewalk Project Phase 1")) {
			summary.setText(R.string.ajw_s);
			representative = "Darryl Moore";
		} else if (title.equals("Cleanup Amendment to Taxi Ordinance (BMC Chapter 9.52)")) {
			summary.setText(R.string.cleanup_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Enact Chapter 13.79, Automatically Renewing Leases")) {
			summary.setText(R.string.enact_s);
			representative = "Maxwell Andersom";
		} else if (title.equals("Contract No. 8460A Amendment: NextGen Healthcare Information " +
				"Systems Inc. for an Electronic Records System and Related Services")) {
			summary.setText(R.string.nextgen_s);
			representative = "Linda Maio";
		} else if (title.equals("Amending Waste Management Authority Joint Powers Agreement")) {
			summary.setText(R.string.waste_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Fee Assessment - State of California Self-Insurance Fund (Workers' Compensation Program)")) {
			summary.setText(R.string.fee_s);
			representative = "Darryl Moore";
		} else if (title.equals("Designate the Line of Succession for the Director of Emergency Services")) {
			summary.setText(R.string.line_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Donation of Furniture for the Dona Spring Animal Shelter by Crate and Barrel")) {
			summary.setText(R.string.dana_s);
			representative = "Maxwell Anderson";
		} else if (title.equals("Formal Bid Solicitation and Request for Proposal Scheduled For Possible Issuance After" +
				" Council Approval on January 22, 2013")) {
			summary.setText(R.string.jan_s);
			representative = "Darryl Moore";
		} else if (title.equals("Compassionate Sidewalks Background Information")) {
			summary.setText(R.string.side_s);
			representative = "Linda Maio";
		}
	}
	
	public void likeBill(View v) {
		// TODO
		// update ratings and increment progress bar
		if (liked) {
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			likes = likes - 1;
			liked = false;
		} else {
			like.setBackgroundResource(R.drawable.thumbs_up_grn);
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			likes = likes + 1;
			liked = true;
			if (disliked) {
				dislikes = dislikes - 1;
				disliked = false;
			}
		}
		setProgressBars();
	}
	
	public void dislikeBill(View v) {
		// TODO
		// update ratings and decrement progress bar
		if (disliked) {
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			dislikes = dislikes - 1;
			disliked = false;
		} else {
			dislike.setBackgroundResource(R.drawable.thumbs_down_red);
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			dislikes = dislikes + 1;
			disliked = true;
			if (liked) {
				likes = likes - 1;
				liked = false;
			}
		}
		setProgressBars();
	}
	
	public void getInfo(View v) {
		String billQuery = bill_title.replace(" ", "+");
		String search = "http://www.google.com/search?q=" + billQuery;
		Uri uri = Uri.parse(search);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void load(View v) {
		LoadTask task = new LoadTask();
		task.execute();
	}
	
	public void contact(View v) {
		// TODO
		// takes user to representative's info screen
		Intent i = new Intent(this, RepresentativeActivity.class);
		i.putExtra("representative", representative);
		startActivity(i);
	}
	
	public void setProgressBars() {
		PostLikesTask task = new PostLikesTask();
		task.execute();
		System.err.println("Finished setting progress bars");
	}
	
	public void loadProgressBars() {
		LoadLikesTask task = new LoadLikesTask();
		task.execute();
		System.err.println("Finished loading progress bars");
	}
	
	private class PostLikesTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...params) {
			String url = "http://billiterate.pythonanywhere.com/likes/" + Integer.toString(billId);
			HttpResponse response;
			HttpClient client = new DefaultHttpClient();
			try {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("likes", Integer.toString(likes)));
				postParameters.add(new BasicNameValuePair("dislikes", Integer.toString(dislikes)));
				postParameters.add(new BasicNameValuePair("trending", Integer.toString(likes + dislikes)));
				postParameters.add(new BasicNameValuePair("hash", Integer.toString(billId)));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
				post.setEntity(entity);
				System.err.println("Posting " + postParameters.toString() + "to " + url);
				response = client.execute(post);
			} catch (ClientProtocolException cpe) {
				System.err.println("*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^");
				cpe.printStackTrace();
			} catch (IOException e) {
				System.err.println("*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^");
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String arg0) {
			loadProgressBars();
		}
	}
	private class LoadLikesTask extends AsyncTask<Void, Void, JSONArray> {
		
		protected JSONArray doInBackground(Void...arg0) {
			String url = "http://billiterate.pythonanywhere.com/likes/" + Integer.toString(billId);
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
			if (messageList == null) {
				likes = 0;
				dislikes = 0;
			}
			for (int i = 0; i < messageList.length(); i++) {
				try {
					JSONArray current = messageList.getJSONArray(i);
					likes = Integer.parseInt(current.getString(0));
					dislikes = Integer.parseInt(current.getString(1));
				} catch (JSONException e ) {
					System.err.print(messageList.toString());
					e.printStackTrace();
				}
			}
			float total = likes + dislikes;
			float up = (likes * 100) / total;
			float down = (dislikes * 100) / total;
			up_ratings.setProgress( (int) up);
			down_ratings.setProgress( (int) down);
		}
	}
	
	public void postComment(View v) {
		/*TextView commentText = new TextView(this);
		commentText.setText(commentBox.getText());
		commentText.setLayoutParams(new LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT ));
		commentText.setBackgroundDrawable(BillInfoActivity.this.getResources().getDrawable(R.drawable.black_border));
		bill_view.addView(commentText);*/
		String commentText = commentBox.getText().toString();
		String name = "Anonymous"; // Will eventually be populated by Facebook login
		PostTask post = new PostTask();
		post.execute(Integer.toString(billId), name, commentText);
		//System.err.println("bill ID = " + Integer.toString(billId));
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
		
		commentBox.setText("");
	}
	
	private class PostTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...params) {
			String url = "http://billiterate.pythonanywhere.com/messages/" + Integer.toString(billId);
			HttpResponse response;
			HttpClient client = new DefaultHttpClient();
			try {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("bill", params[0]));
				postParameters.add(new BasicNameValuePair("name", params[1]));
				postParameters.add(new BasicNameValuePair("comment", params[2]));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
				post.setEntity(entity);
				response = client.execute(post);
				System.err.println("Posting " + postParameters.toString() + "to " + url);
			} catch (ClientProtocolException cpe) {
				System.err.println("*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^");
				cpe.printStackTrace();
			} catch (IOException e) {
				System.err.println("*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^");
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String arg0) {
			load(null);
		}
	}
	private class LoadTask extends AsyncTask<Void, Void, JSONArray> {
		
		protected JSONArray doInBackground(Void...arg0) {
			String url = "http://billiterate.pythonanywhere.com/messages/" + Integer.toString(billId);
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
			data.clear();
			if (messageList == null) {
				return;
			}
			for (int i = 0; i < messageList.length(); i++) {
				try {
					JSONArray current = messageList.getJSONArray(i);
					Map<String, String> listItem = new HashMap<String, String>(2);
					listItem.put("ID", current.getString(0));
					listItem.put("Name", current.getString(1));
					listItem.put("Comment", current.getString(2));
					data.add(listItem);
				} catch (JSONException e ) {
					System.err.print(data.toString());
					e.printStackTrace();
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}
