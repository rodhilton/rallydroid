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

import android.os.Bundle;

import com.rallydev.rallydroid.dto.Artifact;

public class ArtifactView extends RallyActivity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        Artifact artifact = (Artifact) extras.getSerializable("Artifact");
        
        if(artifact.getType().equals("Task")) {
        	displayTask(artifact);
        }

    }

	private void displayTask(Artifact artifact) {
		setContentView(R.layout.view_task);
		
		try {
        	/* DON'T THINK THIS IS USED ANYWHERE
        	JSONObject jsonObject = getResult(artifact.getRef()).getJSONObject("Task");
        	TextView nameView = (TextView) findViewById(R.id.task_nameView);
        	nameView.setText(String.format("%s: %s", jsonObject.getString("FormattedID"), jsonObject.getString("Name")));
        	TextView descriptionView = (TextView) findViewById(R.id.task_descriptionView);
        	String description = jsonObject.getString("Description");
        	//descriptionView.setText(Util.trim(description));
        	descriptionView.setText(description);
        	
        	String estimate=jsonObject.getString("Estimate");
        	String todo=jsonObject.getString("ToDo");
        	String state = jsonObject.getString("State");
        	
        	boolean blocked=jsonObject.getBoolean("Blocked");
        	
        	((TextView)findViewById(R.id.task_stateView)).setText(state);
        	((TextView)findViewById(R.id.task_estimateView)).setText(estimate);
        	((TextView)findViewById(R.id.task_todoView)).setText(todo);
        	
        	
        	TextView blockedView = (TextView) findViewById(R.id.task_blockedView);
        	if(blocked) {
        		blockedView.setText("BLOCKED");
        	} else {
        		blockedView.setText("Not blocked");
        	}
        		
        	*/
        	
        }catch(Exception e) {
        	
        }
	}
	
	
}
