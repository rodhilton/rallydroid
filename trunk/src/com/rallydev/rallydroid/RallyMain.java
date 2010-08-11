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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rallydev.rallydroid.dto.User;

public class RallyMain extends RallyActivity {
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem menuItem = menu.add(0, Menu.FIRST, Menu.NONE, R.string.settings_menu);
		menuItem.setIcon(android.R.drawable.ic_menu_preferences);
		menuItem.setIntent(new Intent(this, Settings.class));
		
		return true;
	}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button planningPokerButton = (Button) findViewById(R.id.planningPokerButton);
        planningPokerButton.setOnClickListener(buildActivityChangeListener(PokerSelection.class));
        
        Button myTasksButton = (Button) findViewById(R.id.myTasksButton);
        myTasksButton.setOnClickListener(buildActivityChangeListener(MyTasks.class));
        
        Button iterationStatusButton = (Button) findViewById(R.id.iterationStatusButton);
        iterationStatusButton.setOnClickListener(buildActivityChangeListener(IterationStatus.class));
        
        Button recentActivityButton = (Button) findViewById(R.id.recentActivityButton);
        recentActivityButton.setOnClickListener(buildActivityChangeListener(RecentActivity.class));
 	}
    
    public void onStart() {
        super.onStart();

        ShowHideButtons();
   }
    
    private void ShowHideButtons()
    {
    	User user = getHelper().getCurrentUser();
    	TextView statusView = (TextView) findViewById(R.id.main_statusLabel);
    	Button myTasksButton = (Button) findViewById(R.id.myTasksButton);
    	Button iterationStatusButton = (Button) findViewById(R.id.iterationStatusButton);
    	Button recentActivityButton = (Button) findViewById(R.id.recentActivityButton);
        
        if(user == null) {
        	statusView.setText("Not logged in");
        } else {
	        statusView.setText(String.format("Logged in as %s (%s)", user.getDisplayName(), user.getSubscriptionName()));
        }
        
        myTasksButton.setEnabled(user != null);
    	iterationStatusButton.setEnabled(user != null);
    	recentActivityButton.setEnabled(user != null);
    }

    private View.OnClickListener buildActivityChangeListener(final Class<? extends Activity> clazz) {
        final Activity parent = this;
    	
    	return new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(parent, clazz);
				startActivity(intent);
				ShowHideButtons();
			}
        	
        };
    }
}