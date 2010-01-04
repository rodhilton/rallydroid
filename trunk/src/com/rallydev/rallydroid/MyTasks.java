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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rallydev.rallydroid.dto.Artifact;

public class MyTasks extends RallyListActivity {
    private List<Artifact> tasks;
    private Artifact selectedTask;

    public void loadDataFromStore()
	{
		tasks = getHelper().getRallyConnection().getMyTasks();
	}
	
	protected List<Map<String, String>> fillDataForDrawing()
    {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        
		for (Artifact task: tasks)
    	{
        	Map<String, String> row = new HashMap<String, String>();
        	row.put(LIST_ITEM_LINE1, task.getName());
        	row.put(LIST_ITEM_LINE2, getTaskStoryName(task));
        	data.add(row);
        }
		
		return data;
    }
    
    static final private int DETAIL_DIALOG=1;
	
	@Override
	protected void PostCreate()
	{
		ListView myListView = (ListView) findViewById(getListViewResId());
		myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {				
				selectedTask = tasks.get(index);
				showDialog(DETAIL_DIALOG);
			}
        	
        });
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