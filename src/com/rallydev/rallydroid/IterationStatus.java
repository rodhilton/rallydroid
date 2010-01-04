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

import com.rallydev.rallydroid.dto.Story;

public class IterationStatus extends RallyListActivity {
	
	private List<Story> stories;
    
	protected void loadDataFromStore()
	{
		stories = getHelper().getRallyConnection().getStoriesForCurrentIteration();
	}
	
	protected List<Map<String, String>> fillDataForDrawing()
    {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        
		for (Story story: stories)
    	{
        	Map<String, String> row = new HashMap<String, String>();
        	row.put(LIST_ITEM_LINE1, story.getName());
        	row.put(LIST_ITEM_LINE2, story.getFormattedID() + " (" + story.getStatus() + ")");
        	data.add(row);
        }
        
        return data;
    }
}
