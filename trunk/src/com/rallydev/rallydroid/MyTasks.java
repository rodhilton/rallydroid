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

import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.rallydev.rallydroid.dto.Artifact;

public class MyTasks extends RallyListActivity {
	private final int MENU_OPEN = 1;
    private final int MENU_COMPLETED = 2;
    private final int MENU_ALL = 3;
    private final int MENU_REFRESH = 10;
    private int filterSelected = MENU_OPEN;

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
	protected int getDetailViewResId()
	{ 
		return R.layout.view_task;
	}

	@Override
	protected void PrepareDetailDialog(Dialog dialog, Artifact selectedItem) {
		dialog.setTitle(selectedItem.getFormattedID());
		
    	String description = selectedItem.getString("Description");
    	String estimate=selectedItem.getString("Estimate");
    	String todo=selectedItem.getString("ToDo");
    	String actuals=selectedItem.getString("Actuals");
    	boolean blocked=selectedItem.getBoolean("Blocked");
    	String state = selectedItem.getString("State") + " " + (blocked ? "(BLOCKED)" : "(Not blocked)");
    	
		((TextView)dialog.findViewById(R.id.task_nameView)).setText(selectedItem.getName());
		((TextView)dialog.findViewById(R.id.story_nameView)).setText(getTaskStoryName(selectedItem));
		((TextView)dialog.findViewById(R.id.task_descriptionView)).setText(description);
    	((TextView)dialog.findViewById(R.id.task_stateView)).setText(state);
    	((TextView)dialog.findViewById(R.id.task_estimateView)).setText(estimate);
    	((TextView)dialog.findViewById(R.id.task_todoView)).setText(todo);
    	((TextView)dialog.findViewById(R.id.task_actualView)).setText(actuals);
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