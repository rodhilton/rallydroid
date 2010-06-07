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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.util.Log;
import android.widget.TextView;

import com.rallydev.rallydroid.dto.Artifact;
import com.rallydev.rallydroid.dto.DomainObject;

public abstract class TaskListActivity extends RallyListActivity {
	    
    @Override
	protected int getDetailViewResId()
	{ 
		return R.layout.view_task;
	}

	@Override
	protected void PrepareDetailDialog(Dialog dialog, DomainObject selected) {
		Artifact selectedItem = (Artifact)selected;
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
	
	protected String getTaskStoryName(Artifact task)
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