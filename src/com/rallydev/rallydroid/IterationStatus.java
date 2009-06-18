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

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rallydev.rallydroid.dto.Iteration;
import com.rallydev.rallydroid.dto.Story;

public class IterationStatus extends RallyActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Iteration currentIteration = getCurrentIteration();
        setContentView(R.layout.artifactlist);
		
        final ArrayList<String> todoItems = new ArrayList<String>(); 
        // Create the array adapter to bind the array to the listview 
        final ArrayAdapter<String> aa; 
        aa = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, 
                                      todoItems); 
        
		List<Story> stories = getStories();
		
		if(stories.size() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(), "No stories found", Toast.LENGTH_SHORT);
			toast.show();
			this.finish();
		}
		
		for(Story story: stories) {
			todoItems.add(String.format("%s: %s", story.getFormattedID(), story.getName()));
		}
		
        ListView myListView = (ListView) findViewById(R.id.myListView);
        
        myListView.setAdapter(aa);
	}

}
