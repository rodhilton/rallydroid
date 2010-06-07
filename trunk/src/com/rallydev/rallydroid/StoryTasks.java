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

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rallydev.rallydroid.dto.Artifact;
import com.rallydev.rallydroid.dto.DomainObject;

public class StoryTasks extends TaskListActivity {
	private final int MENU_REFRESH = 10;
    private int storyOid;
    private List<DomainObject> tasks;
    public static final String STORY_OID_PARAM = "StoryOid";
    
    public void PostCreate() {
        Bundle extras = getIntent().getExtras();
        try
        {
        	storyOid = extras.getInt(STORY_OID_PARAM);
        	if (storyOid == 0)
        	{
        		throw new IllegalArgumentException("StoryOid extra not found.");
        	}
        }
        catch (Exception e)
        {
        	Log.e("tasks", "Error retrieving story for task activity: " + e.toString());
        	new AlertDialog.Builder(this).setMessage("Could not retrieve story info.").show();
        	finish();
        }
    }

    public List<DomainObject> loadData()
	{
    	if (tasks == null)
    	{
    		tasks = getHelper().getRallyConnection().listTasksByStory(storyOid);
    	}
    	
    	return tasks;
	}
    
    protected String getActivityTitle()
	{
    	return "Story Tasks";
	}
    
    protected String getLine1(DomainObject artifact)
    {
    	return ((Artifact)artifact).getName();
    }
    
    protected String getLine2(DomainObject artifact)
    {
    	return ((Artifact)artifact).getFormattedID() + " (" + artifact.getString("State") + ")";
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_REFRESH, 3, "Refresh");//.setIcon(android.R.drawable.);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) 
        {
	        case MENU_REFRESH:
	        	tasks = null; // force refresh
		        break;
        }
        refreshData();
        return super.onOptionsItemSelected(item);
    }
}