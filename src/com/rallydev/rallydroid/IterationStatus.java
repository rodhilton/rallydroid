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

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.rallydev.rallydroid.dto.Artifact;
import com.rallydev.rallydroid.dto.DomainObject;
import com.rallydev.rallydroid.dto.Story;

import static com.rallydev.rallydroid.Util.stripHTML;

public class IterationStatus extends RallyListActivity {
	private final int MENU_OPEN = 1;
    private final int MENU_COMPLETED = 2;
    private final int MENU_ALL = 4;
    private final int MENU_REFRESH = 10;
    private int filterSelected = MENU_OPEN;
    
    private final int ITEM_MENU_DETAIL = 1;
    private final int ITEM_MENU_TASKS = 2;
    

    private List<Story> stories;
 
    @Override
	protected List<DomainObject> loadData()
	{
		if (stories == null)
    	{
			stories = getHelper().getRallyConnection().getStoriesForCurrentIteration();
    	}
    	
    	List<DomainObject> ret = new ArrayList<DomainObject>();
    	for (DomainObject story: stories)
    	{
    		String state = ((Story)story).getStatus();
    		if ((filterSelected == MENU_COMPLETED && !state.equals("Completed") && !state.equals("Accepted"))
    			|| (filterSelected == MENU_OPEN && (state.equals("Completed") || state.equals("Accepted"))))
        			continue;
        		
    		ret.add(story);
    	}
    	
    	return ret;
	}
	
	protected String getActivityTitle()
	{
		String title = "All Stories";
    	if (filterSelected == MENU_OPEN)
    		title = "Defined/In-Progress Stories";
    	else if (filterSelected == MENU_COMPLETED)
    		title = "Completed/Accepted Stories";
    	
    	return title;
	}
	
	protected String getLine1(DomainObject artifact)
    {
    	return ((Artifact)artifact).getName();
    }
    
    protected String getLine2(DomainObject artifact)
    {
    	return ((Artifact)artifact).getFormattedID() + " (" + ((Story)artifact).getStatus() + ")";
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
	        	stories = null; // force refresh
		        break;
        }
        refreshData();
        return super.onOptionsItemSelected(item);
    }

    @Override
	protected int getDetailViewResId()
    {
    	return R.layout.view_story;
    }

	@Override
	protected void PrepareDetailDialog(Dialog dialog, DomainObject selectedItem) {
		Story selectedStory = (Story)selectedItem;
		dialog.setTitle(selectedStory.getFormattedID());
		
    	String description = stripHTML(selectedStory.getString("Description"));
    	String planEstimate = selectedStory.getString("PlanEstimate");
    	String estimate=selectedStory.getString("TaskEstimateTotal");
    	String todo=selectedStory.getString("TaskRemainingTotal");
    	String actuals=selectedStory.getString("TaskActualTotal");
    	boolean blocked=selectedStory.getBoolean("Blocked");
    	String state = selectedStory.getStatus() + " " + (blocked ? "(BLOCKED)" : "(Not blocked)");
    	
		((TextView)dialog.findViewById(R.id.story_nameView)).setText(selectedStory.getName());
		((TextView)dialog.findViewById(R.id.story_descriptionView)).setText(description);
    	((TextView)dialog.findViewById(R.id.story_stateView)).setText(state);
    	((TextView)dialog.findViewById(R.id.plan_estimateView)).setText(planEstimate);
    	((TextView)dialog.findViewById(R.id.task_estimateView)).setText(estimate);
    	((TextView)dialog.findViewById(R.id.task_todoView)).setText(todo);
    	((TextView)dialog.findViewById(R.id.task_actualView)).setText(actuals);
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {	
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	menu.add(0, ITEM_MENU_DETAIL, 0,  "Story Detail");
    	menu.add(0, ITEM_MENU_TASKS, 0,  "Tasks");
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;
		
		switch (item.getItemId()) {
			case ITEM_MENU_DETAIL:
				ShowDetailForItemAt(pos);
				break;
			case ITEM_MENU_TASKS:
				try {
					Story story = (Story)getItemAt(pos);
					Intent intent = new Intent(this, StoryTasks.class);
					intent.putExtra(StoryTasks.STORY_OID_PARAM, story.getOid());
					startActivity(intent);
	            } catch (Exception e) {
	                Log.e("story", "Error preparing task list activity: " + e.toString());
	            }
	            break;
		}
		return super.onContextItemSelected(item);
    }

}
