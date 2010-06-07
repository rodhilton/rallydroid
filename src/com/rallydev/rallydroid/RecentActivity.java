/*
 * Copyright 2010 Rally Software Development
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.rallydev.rallydroid;

import static com.rallydev.rallydroid.Util.stripHTML;
import static com.rallydev.rallydroid.Util.trim;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.rallydev.rallydroid.dto.Activity;
import com.rallydev.rallydroid.dto.DomainObject;

public class RecentActivity extends RallyListActivity {
	private final int MENU_REFRESH = 10;
	private final int ITEM_MENU_DETAIL = 1;

	private final int MAX_SUMMARY_LENGTH = 120;

	private List<Activity> activities;

	@Override
	protected List<DomainObject> loadData() {
		if (activities == null) {
			activities = getHelper().getRallyConnection().getRecentActivities();
		}
		List<DomainObject> ret = new ArrayList<DomainObject>();
		ret.addAll(activities);
		return ret;
	}

	@Override
	protected String getActivityTitle() {
		return "Recent Activity";
	}

	protected String getLine1(DomainObject artifact) {
		String creationDate = formatCreationDate(artifact.getCreationDate());
		return creationDate + " by " + ((Activity) artifact).getUserName();
	}

	private String formatCreationDate(String creationDate) {
		try {
			Date date = Util.utcToDate(creationDate);
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.SHORT);
			creationDate = df.format(date);
		} catch (ParseException pe) {
			Log.e("RecentActivity", "Failed to parse date for RecentActivity "
					+ pe.getMessage());
		}
		return creationDate;
	}

	protected String getLine2(DomainObject artifact) {
		String line2 = stripHTML(((Activity) artifact).getText());
		return trim(line2, MAX_SUMMARY_LENGTH);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_REFRESH, 0, "Refresh");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_REFRESH:
			activities = null; // force refresh
			break;
		}
		refreshData();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getDetailViewResId() {
		return R.layout.view_recent_activity;
	}

	protected void PrepareDetailDialog(Dialog dialog, DomainObject selectedItem) {
		Activity selectedActivity = (Activity) selectedItem;

		String creationDate = selectedActivity.getCreationDate();
		String userName = selectedActivity.getUserName();
		String text = selectedActivity.getText();

		((TextView) dialog.findViewById(R.id.recentActivity_descriptionView))
				.setText(formatCreationDate(creationDate) + " by " + userName);
		((TextView) dialog.findViewById(R.id.recentActivity_textView))
				.setText(stripHTML(text));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, ITEM_MENU_DETAIL, 0, "Activity Detail");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int pos = info.position;

		switch (item.getItemId()) {
		case ITEM_MENU_DETAIL:
			ShowDetailForItemAt(pos);
			break;
		}
		return super.onContextItemSelected(item);
	}

}
