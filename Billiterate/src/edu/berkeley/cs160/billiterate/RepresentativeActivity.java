package edu.berkeley.cs160.billiterate;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private RepresentativeUi ui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_representative);
		ui = new RepresentativeUi();
		ui.onCreateView(this.findViewById(android.R.id.content), this
				.getIntent().getExtras(), this);
	}

	public static class AsFragment extends Fragment {
		private RepresentativeUi ui;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View result = inflater.inflate(R.layout.activity_representative,
					container, false);
			ui = new RepresentativeUi();
			ui.onCreateView(result, this.getArguments(), getActivity());
			return result;
		}
	}

	private static class RepresentativeUi {
		private Representative rep;
		private ArrayList<Representative> all;
		private ArrayList<String> allNames;
		private View view;
		private Activity owner;
		private String bill_title = "";
		
		public void onCreateView(View createdView, Bundle args, Activity owner) {
			this.view = createdView;
			this.owner = owner;

			Bundle extras = args;
			String repName = "<None>";
			
			if (extras != null) {
				repName = extras.getString("representative");
				bill_title = extras.getString("bill_title");
			}
			this.all = new ArrayList<Representative>();
			this.all.add(null);
			this.all.addAll(Representative.getAll());

			this.allNames = new ArrayList<String>();
			for (Representative r : all) {
				if (r != null)
					allNames.add(r.getName());
				else
					allNames.add("<None>");
			}

			Spinner repSpinner = (Spinner) view.findViewById(R.id.repSpinner);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					owner, R.layout.spinneritem, allNames);
			adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
			repSpinner.setAdapter(adapter);
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

			if (repName == null || repName.equals("<None>"))
				repSpinner.setSelection(0);
			else {
				repSpinner.setSelection(allNames.indexOf(repName));
			}

			try {
				updateInfo();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}

		private void updateInfo() throws Throwable {
			if (rep == null) {
				view.findViewById(R.id.repPart).setVisibility(TextView.GONE);
				return;
			}

			view.findViewById(R.id.repPart).setVisibility(TextView.VISIBLE);

			TextView nameText = (TextView) view.findViewById(R.id.name);
			nameText.setText(rep.getName());

			final ProgressBar photoLoad = (ProgressBar) view
					.findViewById(R.id.photoLoading);
			photoLoad.setVisibility(ProgressBar.VISIBLE);
			final ImageView photo = (ImageView) view.findViewById(R.id.photo);
			photo.setVisibility(ImageView.INVISIBLE);
			new AsyncTask<String, Void, Void>() {

				@Override
				protected Void doInBackground(String... params) {
					try {
						URL url = new URL(rep.getImageUrl());
						InputStream content = (InputStream) url.getContent();
						final Drawable d = Drawable.createFromStream(content,
								"src");

						owner.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								photo.setImageDrawable(d);
								photo.setVisibility(ImageView.VISIBLE);
								photoLoad.setVisibility(ProgressBar.GONE);
							}
						});
					} catch (Throwable e) {
						// throw new RuntimeException(e);
					}

					return null;
				}

			}.execute(rep.getImageUrl());

			TextView summaryText = (TextView) view.findViewById(R.id.summary);
			summaryText.setText(rep.getSummary());

			TextView phoneText = (TextView) view.findViewById(R.id.phoneLabel);
			phoneText.setMovementMethod(LinkMovementMethod.getInstance());
			phoneText.setText(Html.fromHtml("Phone <a href=\"tel:"
					+ rep.getPhone() + "\">"
					+ PhoneNumberUtils.formatNumber(rep.getPhone()) + "</a>"));

			TextView emailText = (TextView) view.findViewById(R.id.emailLabel);
			emailText.setMovementMethod(LinkMovementMethod.getInstance());
			emailText.setText(Html.fromHtml("Email <a href=\"mailto:"
					+ rep.getEmail() + "?subject=" + bill_title + "\">" + rep.getEmail() + "</a>"));

			Button official = (Button) view.findViewById(R.id.seeWebsiteButton);
			official.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(rep.getWebsite()));
					owner.startActivity(i);
				}
			});
		}
	}
}
