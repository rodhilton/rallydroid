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

import android.view.Menu;
import android.view.MenuItem;

import com.rallydev.rallydroid.dto.Artifact;
import com.rallydev.rallydroid.dto.DomainObject;

public class MyTasks extends TaskListActivity {
	private final int MENU_OPEN = 1;
    private final int MENU_COMPLETED = 2;
    private final int MENU_ALL = 3;
    private final int MENU_REFRESH = 10;
    private int filterSelected = MENU_OPEN;

    private List<DomainObject> tasks;

    public List<DomainObject> loadData()
	{
    	if (tasks == null)
    	{
    		tasks = getHelper().getRallyConnection().listAllMyTasks();
    	}
    	
    	// add only the items that match the filter
    	List<DomainObject> ret = new ArrayList<DomainObject>();
    	for (DomainObject task: tasks)
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
    
    protected String getLine1(DomainObject artifact)
    {
    	return ((Artifact)artifact).getName();
    }
    
    protected String getLine2(DomainObject artifact)
    {
    	return getTaskStoryName((Artifact)artifact);
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
}