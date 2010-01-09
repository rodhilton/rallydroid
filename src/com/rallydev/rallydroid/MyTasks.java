/*
 * Copyright 2009 Rally Software Development
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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rallydev.rallydroid.dto.Artifact;

public class MyTasks extends RallyListActivity {
	private static final int DETAIL_DIALOG=1;
	private final int MENU_OPEN = 1;
    private final int MENU_COMPLETED = 2;
    private final int MENU_ALL = 3;
    private final int MENU_REFRESH = 10;
    private int filterSelected = MENU_OPEN;

    private Artifact selectedTask;
	private List<Artifact> tasks;

    public List<Artifact> loadData()
	{
    	if (tasks == null)
    	{
    		tasks = getHelper().getRallyConnection().listAllMyTasks();
    	}
    	
    	// add only the items that match the filter
    	List<Artifact> ret = new ArrayList<Artifact>();
    	for (Artifact task: tasks)
    	{
    		String state = task.getString("State");
    		if ((filterSelected == MENU_COMPLETED && !state.equals("Completed"))
    			|| (filterSelected == MENU_OPEN && state.equals("Completed")))
    			continue;
    		
    		ret.add(task);
    	} 
    	
    	return ret;
	}
    
    protected String getActivityTitle()
	{
    	String title = "All My Tasks";
    	if (filterSelected == MENU_OPEN)
    		title = "My Defined/In-Progress Tasks";
    	else if (filterSelected == MENU_COMPLETED)
    		title = "My Completed Tasks";
    	
    	return title;
	}
    
    protected String getLine1(Artifact artifact)
    {
    	return artifact.getName();
    }
    
    protected String getLine2(Artifact artifact)
    {
    	return getTaskStoryName(artifact);
    }
	
	@Override
	protected void PostCreate()
	{
		ListView myListView = (ListView) findViewById(getListViewResId());
		myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {				
				selectedTask = getItemAt(index);
				showDialog(DETAIL_DIALOG);
			}
        	
        });
	}
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_OPEN, 0, "Open");//.setIcon(android.R.drawable.ic_menu_revert);
        menu.add(0, MENU_COMPLETED, 1, "Completed");//.setIcon(android.R.drawable.ic_menu_search);
        menu.add(0, MENU_ALL, 2, "All");//;.setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, MENU_REFRESH, 3, "Refresh");//.setIcon(android.R.drawable.);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) 
        {
	        case MENU_OPEN:
	        case MENU_COMPLETED:
	        case MENU_ALL:
	        	filterSelected = item.getItemId(); // remember the filter
	        	break;
	        case MENU_REFRESH:
	        	tasks = null; // force refresh
		        break;
        }
        refreshData();
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DETAIL_DIALOG:
				LayoutInflater li = LayoutInflater.from(this);
				View taskView = li.inflate(R.layout.view_task, null);
				
				AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
				taskDialog.setTitle("Detail View");
				taskDialog.setView(taskView);
				return taskDialog.create();
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
			case DETAIL_DIALOG:
				dialog.setTitle(selectedTask.getFormattedID());
				
	        	String description = selectedTask.getString("Description");
	        	String estimate=selectedTask.getString("Estimate");
	        	String todo=selectedTask.getString("ToDo");
	        	String actuals=selectedTask.getString("Actuals");
	        	boolean blocked=selectedTask.getBoolean("Blocked");
	        	String state = selectedTask.getString("State") + " " + (blocked ? "(BLOCKED)" : "(Not blocked)");
	        	
				((TextView)dialog.findViewById(R.id.task_nameView)).setText(selectedTask.getName());
				((TextView)dialog.findViewById(R.id.story_nameView)).setText(getTaskStoryName(selectedTask));
				((TextView)dialog.findViewById(R.id.task_descriptionView)).setText(description);
	        	((TextView)dialog.findViewById(R.id.task_stateView)).setText(state);
	        	((TextView)dialog.findViewById(R.id.task_estimateView)).setText(estimate);
	        	((TextView)dialog.findViewById(R.id.task_todoView)).setText(todo);
	        	((TextView)dialog.findViewById(R.id.task_actualView)).setText(actuals);
		}
	}
	
	private String getTaskStoryName(Artifact task)
	{
		String storyName = "";
    	try {
    		String storyJson = task.getString("WorkProduct");
        	JSONObject storyObj = new JSONObject(storyJson);
			storyName = storyObj.getString("_refObjectName");
		} catch (JSONException e) {
			Log.e("Task View", e.getMessage());
		}
		return storyName;
	}
}