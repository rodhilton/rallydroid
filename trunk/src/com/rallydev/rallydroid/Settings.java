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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.rallydev.rallydroid.Preferences.*;

public class Settings extends RallyActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
		final EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
		final EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        
        usernameEdit.setText(getUserName());
        passwordEdit.setText(getPassword());
        
        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			
				setUserName(usernameEdit.getText().toString());
				setPassword(passwordEdit.getText().toString());
				
				closeSettings();
			}
        });
	}
    
    private void closeSettings() {
  
    	Toast toast = Toast.makeText(getApplicationContext(), "Settings saved.", Toast.LENGTH_SHORT);
    	toast.show();
    	this.finish();
    }
}
