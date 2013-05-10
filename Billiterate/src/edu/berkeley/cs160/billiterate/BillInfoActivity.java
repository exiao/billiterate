package edu.berkeley.cs160.billiterate;

import java.io.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class BillInfoActivity extends Activity {

	// information for this screen
	String bill_title;
	String bill_summary;
	String representative = "";
	int billId;

	// get view widgets for modification
	LinearLayout bill_view;
	TextView bill_title_textview;
	ProgressBar ratings;
	// ProgressBar down_ratings;
	// ProgressBar up_ratings;
	int likes;
	int dislikes;
	ImageButton like;
	ImageButton dislike;
	TextView summary;
	EditText commentBox;
	static String nameHint = "Name - Leave blank for Anonymous";
	static String username = "";

	ShapeDrawable pgDrawable;

	boolean liked = false;;
	boolean disliked = false;
	
	@Override
	public void onResume() {
		super.onResume();
		loadProgressBars();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_info);

		Bundle extras = this.getIntent().getExtras();
		bill_title = extras.getString("title");
		bill_summary = extras.getString("summary");
		representative = extras.getString("representative");
		billId = extras.getInt("id");

		bill_view = (LinearLayout) findViewById(R.id.bill_view);
		bill_title_textview = (TextView) findViewById(R.id.title);
		ratings = (ProgressBar) findViewById(R.id.ratings);
		like = (ImageButton) findViewById(R.id.like);
		dislike = (ImageButton) findViewById(R.id.dislike);
		summary = (TextView) findViewById(R.id.bill_summary);
		commentBox = (EditText) findViewById(R.id.comment);

		commentBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				commentClick();
			}

		});

		// display bill information
		bill_title_textview.setText(bill_title);
		summary.setText(bill_summary);

		pgDrawable = new ShapeDrawable();
		pgDrawable.getPaint().setColor(Color.CYAN);
		ClipDrawable pgBar = new ClipDrawable(pgDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
		ratings.setProgressDrawable(pgBar);
		loadProgressBars();

		load(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill_info, menu);
		return true;
	}

	public void likeBill(View v) {
		// update ratings and increment progress bar
		String id = ""+billId;
		if (liked) {
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			likes = likes - 1;
			liked = false;
			System.out.println("Undoing a like.");
			new UpdateRatingsTask().execute(id, "True", "True");
		} else {
			like.setBackgroundResource(R.drawable.thumbs_up_grn);
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			likes = likes + 1;
			liked = true;
			System.out.println("Marking a like.");
			new UpdateRatingsTask().execute(id, "True", "False");
			if (disliked) {
				dislikes = dislikes - 1;
				disliked = false;
				System.out.println("Changing from dislike to like.");
				new UpdateRatingsTask().execute(id, "False", "True");
			}
		}
	}

	public void dislikeBill(View v) {
		// update ratings and decrement progress bar
		String id = ""+billId;
		if (disliked) {
			dislike.setBackgroundResource(R.drawable.thumbs_down_blk);
			dislikes = dislikes - 1;
			disliked = false;
			System.out.println("Undoing a dislike.");
			new UpdateRatingsTask().execute(id, "False", "True");
		} else {
			dislike.setBackgroundResource(R.drawable.thumbs_down_red);
			like.setBackgroundResource(R.drawable.thumbs_up_blk);
			dislikes = dislikes + 1;
			disliked = true;
			System.out.println("Marking a dislike.");
			new UpdateRatingsTask().execute(id, "False", "False");
			if (liked) {
				likes = likes - 1;
				liked = false;
				System.out.println("Changing from like to dislike.");
				new UpdateRatingsTask().execute(id, "True", "True");
			}
		}
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
		// takes user to representative's info screen
		Intent i = new Intent(this, RepresentativeActivity.class);
		i.putExtra("representative", representative);
		i.putExtra("bill_title", bill_title);
		startActivity(i);
	}

	public void loadProgressBars() {
		LoadLikesTask task = new LoadLikesTask();
		task.execute();
		System.err.println("Finished loading progress bars");
	}

	private void commentClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Post a Comment");
		
		LinearLayout lila = new LinearLayout(this);
		lila.setOrientation(1);
		final EditText commentTextBox = new EditText(this);
		final EditText nameBox = new EditText(this);
		nameBox.setHint(nameHint);
		lila.addView(nameBox);
		lila.addView(commentTextBox);
		commentTextBox.setHint("Type Comment Here");
		builder.setView(lila);

		// Set up the buttons
		builder.setPositiveButton("Post",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String commentText = commentTextBox.getText()
								.toString();
						String name = nameBox.getText().toString();
						System.err.println(name);
						if (name.length() > 0) {
							name = nameBox.getText().toString();
							nameHint = name;
							username = name;
						} else {
							if (username.length() > 0) {
								name = username;
							} else {
								name = "Anonymous";
							}
						}
						PostTask post = new PostTask();
						post.execute(Integer.toString(billId), name,
								commentText);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								commentTextBox.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(
								commentBox.getWindowToken(), 0);
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								commentTextBox.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(
								commentBox.getWindowToken(), 0);
					}
				});

		builder.show();
	}

	private class UpdateRatingsTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String url = "http://billiterate.pythonanywhere.com/billapp/update_likes";
			HttpClient client = new DefaultHttpClient();
			try {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("id", params[0]));
				postParameters.add(new BasicNameValuePair("is_like", params[1]));
				postParameters
						.add(new BasicNameValuePair("undo", params[2]));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						postParameters);
				post.setEntity(entity);
				post.setHeader("Accept", "application/json");
				post.setHeader("Content-type", "application/json");
				System.out.println("Posting " + postParameters.toString() + " to "
						+ url);
				client.execute(post);
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
		protected void onPostExecute(Void v) {
			// loadProgressBars();
			int total = likes + dislikes;
			System.out.println("The total is: " + total);
			if (total > 0) {
				int up = (likes * 100) / total;
				ratings.setBackgroundColor(Color.RED);
				pgDrawable.getPaint().setColor(Color.GREEN);
				ratings.setProgress(up);
				System.out
						.println("============= Set ProgressBar Task =============");
				System.out
						.println("Likes: " + likes + " Dislikes: " + dislikes);
				System.out
						.println("==================================================");
			} else {
				System.out.println("The ratings bar should be reset to zero!");
				ratings.setBackgroundColor(Color.GRAY);
				ratings.setProgress(0);
			}
		}

	}

	private class LoadLikesTask extends AsyncTask<Void, Void, JSONArray> {

		protected JSONArray doInBackground(Void... arg0) {
			String url = "http://billiterate.pythonanywhere.com/billapp/bill?id="
					+ billId;
			System.out.println("URL = " + url);
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
				ratings.setBackgroundColor(Color.GRAY);
				ratings.setProgress(0);
				System.out.println("This bill has not been liked/disliked before, should display gray bar");
			} else {
				for (int i = 0; i < messageList.length(); i++) {
					try {
						JSONObject current = messageList.getJSONObject(i);
						JSONObject fields = current.getJSONObject("fields");
						likes = fields.getInt("num_likes");
						dislikes = fields.getInt("num_dislikes");
						System.out.println("Likes: " + likes + " Dislikes: " + dislikes);
					} catch (JSONException e) {
						System.err.println(messageList.toString());
						e.printStackTrace();
					}
				}
				int total = likes + dislikes;
				if (total > 0) {
					int up = (likes * 100) / total;
					ratings.setBackgroundColor(Color.RED);
					pgDrawable.getPaint().setColor(Color.GREEN);
					System.out.println("progress bar should be green and up to: "
									+ up);
					ratings.setProgress(up);
				}
			}
		}
	}

	public void postComment(View v) {
		/*
		 * TextView commentText = new TextView(this);
		 * commentText.setText(commentBox.getText());
		 * commentText.setLayoutParams(new LayoutParams(
		 * LinearLayout.LayoutParams.MATCH_PARENT,
		 * LinearLayout.LayoutParams.WRAP_CONTENT ));
		 * commentText.setBackgroundDrawable
		 * (BillInfoActivity.this.getResources()
		 * .getDrawable(R.drawable.black_border));
		 * bill_view.addView(commentText);
		 */
		String commentText = commentBox.getText().toString();
		String name = "Anonymous"; // Will eventually be populated by Facebook
									// login
		PostTask post = new PostTask();
		post.execute(Integer.toString(billId), name, commentText);
		// System.err.println("bill ID = " + Integer.toString(billId));
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		commentBox.setText("");
	}

	private class PostTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String url = "http://billiterate.pythonanywhere.com/billapp/comments";
			HttpResponse response;
			HttpClient client = new DefaultHttpClient();
			try {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("id", params[0]));
				postParameters.add(new BasicNameValuePair("name", params[1]));
				postParameters
						.add(new BasicNameValuePair("comment", params[2]));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						postParameters);
				post.setEntity(entity);
				response = client.execute(post);
				System.err.println("Posting " + postParameters.toString()
						+ " to " + url);
				HttpEntity resp = response.getEntity();
				String respStr = EntityUtils.toString(resp);
				System.err.println("Response = " + respStr);
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

		protected JSONArray doInBackground(Void... arg0) {
			String url = "http://billiterate.pythonanywhere.com/billapp/comments/?id="
					+ Integer.toString(billId);
			// System.err.println("URL = " + url);
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
				return;
			}
			LinearLayout comments = (LinearLayout) findViewById(R.id.comments);
			comments.removeAllViews();
			for (int i = messageList.length() - 1; i >= 0; i--) {
				try {
					JSONObject current = messageList.getJSONObject(i);
					JSONObject fields = current.getJSONObject("fields");

					System.err.println("ID = " + current.getString("pk")
							+ "\nName = " + fields.getString("name")
							+ "\nText = " + fields.getString("text"));

					View commentView = LayoutInflater.from(
							BillInfoActivity.this).inflate(
							R.layout.comment_layout, comments, false);
					TextView nameText = (TextView) commentView
							.findViewById(R.id.nameText);
					TextView commentText = (TextView) commentView
							.findViewById(R.id.commentText);
					TextView idText = (TextView) commentView
							.findViewById(R.id.IDText);
					nameText.setText(fields.getString("name"));
					commentText.setText(fields.getString("text"));
					idText.setText(current.getString("pk"));
					comments.addView(commentView);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}