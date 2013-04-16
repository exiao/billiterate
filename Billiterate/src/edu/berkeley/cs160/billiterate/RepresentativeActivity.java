package edu.berkeley.cs160.billiterate;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class RepresentativeActivity extends Activity {
	private Representative rep;
	private ArrayList<Representative> all;
	private ArrayList<String> allNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_representative);

		Bundle extras = this.getIntent().getExtras();
		String repName = extras.getString("representative");
		if (repName == null)
			repName = "Linda Maio";

		this.all = Representative.getAll();
		this.allNames = new ArrayList<String>();
		for (Representative r : all) {
			if (r.getName().equals(repName)) {
				rep = r;
			}
			allNames.add(r.getName());
		}

		if (rep == null) {
			rep = all.get(0);
		}

		Spinner repSpinner = (Spinner) findViewById(R.id.repSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinneritem, allNames);
		adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
		repSpinner.setAdapter(adapter);
		repSpinner.setSelection(allNames.indexOf(rep.getName()));
		repSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				rep = all.get(position);
				try {
					updateInfo();
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		try {
			updateInfo();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private void updateInfo() throws Throwable {
		TextView nameText = (TextView) findViewById(R.id.name);
		nameText.setText(rep.getName());

		final ProgressBar photoLoad = (ProgressBar) findViewById(R.id.photoLoading);
		photoLoad.setVisibility(ProgressBar.VISIBLE);
		final ImageView photo = (ImageView) findViewById(R.id.photo);
		photo.setVisibility(ImageView.INVISIBLE);
		new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				try {
					URL url = new URL(rep.getImageUrl());
					InputStream content = (InputStream) url.getContent();
					final Drawable d = Drawable
							.createFromStream(content, "src");

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							photo.setImageDrawable(d);
							photo.setVisibility(ImageView.VISIBLE);
							photoLoad.setVisibility(ProgressBar.GONE);
						}
					});
				} catch (Throwable e) {
					//throw new RuntimeException(e);
				}

				return null;
			}

		}.execute(rep.getImageUrl());

		TextView summaryText = (TextView) findViewById(R.id.summary);
		summaryText.setText(rep.getSummary());

		TextView phoneText = (TextView) findViewById(R.id.phoneLabel);
		phoneText.setMovementMethod(LinkMovementMethod.getInstance());
		phoneText.setText(Html.fromHtml("Phone <a href=\"tel:" + rep.getPhone()
				+ "\">" + PhoneNumberUtils.formatNumber(rep.getPhone())
				+ "</a>"));

		TextView emailText = (TextView) findViewById(R.id.emailLabel);
		emailText.setMovementMethod(LinkMovementMethod.getInstance());
		emailText.setText(Html.fromHtml("Email <a href=\"mailto:"
				+ rep.getEmail() + "\">" + rep.getEmail() + "</a>"));

		Button official = (Button) findViewById(R.id.seeWebsiteButton);
		official.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(rep.getWebsite()));
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_representative, menu);
		return true;
	}

}
