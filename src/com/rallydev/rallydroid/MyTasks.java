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

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rallydev.rallydroid.dto.Artifact;

public class MyTasks extends RallyActivity {
    private ArrayAdapter<Artifact> aa;
	private ArrayList<Artifact> tasks;
	static final private int TASK_DIALOG=1;
	Artifact selectedTask;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artifactlist);
        
        ListView myListView = (ListView) findViewById(R.id.myListView);
        
        tasks = new ArrayList<Artifact>();
        int layoutID = android.R.layout.simple_list_item_1;
        aa = new ArrayAdapter<Artifact>(this,layoutID,tasks);
        myListView.setAdapter(aa);
  
        refreshTasks();
             
        myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {				
				selectedTask = tasks.get(index);
				showDialog(TASK_DIALOG);
			}
        	
        });
 

    }

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case TASK_DIALOG:
				LayoutInflater li = LayoutInflater.from(this);
				View taskView = li.inflate(R.layout.view_task, null);
				
				AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
				taskDialog.setTitle("View Task");
				taskDialog.setView(taskView);
				return taskDialog.create();
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
			case TASK_DIALOG:
				dialog.setTitle(selectedTask.getFormattedID());
				
				TextView nameView = (TextView) dialog.findViewById(R.id.task_nameView);

	        	nameView.setText(selectedTask.getName());
	        	TextView descriptionView = (TextView) dialog.findViewById(R.id.task_descriptionView);
	        	String description = selectedTask.getString("Description");
	        	descriptionView.setText(description);
	        	
	        	String estimate=selectedTask.getString("Estimate");
	        	String todo=selectedTask.getString("ToDo");
	        	String state = selectedTask.getString("State");
	        	
	        	boolean blocked=selectedTask.getBoolean("Blocked");
	        	
	        	((TextView)dialog.findViewById(R.id.task_stateView)).setText(state);
	        	((TextView)dialog.findViewById(R.id.task_estimateView)).setText(estimate);
	        	((TextView)dialog.findViewById(R.id.task_todoView)).setText(todo);
	        	
	        	
	        	TextView blockedView = (TextView) dialog.findViewById(R.id.task_blockedView);
	        	if(blocked) {
	        		blockedView.setText("BLOCKED");
	        	} else {
	        		blockedView.setText("Not blocked");
	        	}
		}
	}

	private void refreshTasks() {
		DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(null, 443),
                new UsernamePasswordCredentials(getUserName(), getPassword()));
       
        HttpGet httpget = new HttpGet("https://test1cluster.rallydev.com/slm/webservice/1.11/task.js?query=((Owner+=+"+getUserName()+")+and+(State+!=+Completed))&fetch=true&pretty=true");

        try {
	        HttpResponse httpResponse = httpclient.execute(httpget);
	
	        HttpEntity entity = httpResponse.getEntity();
	
	        InputStream is = entity.getContent();
	        String response=Util.slurp(is);
	        Log.d("Response", response);
	        
	        JSONObject array = new JSONObject(response);
	        JSONObject queryResult = array.getJSONObject("QueryResult");
	        JSONArray results = queryResult.getJSONArray("Results");
	        for(int i = 0 ; i< results.length() ; i++) {
		        JSONObject object = (JSONObject) results.get(i);
		        tasks.add(new Artifact(object));
	        }
	        
	        httpclient.getConnectionManager().shutdown();

        }catch(Exception e) {
        
        }
	}
	
	
	private void launchArtifactView(Artifact artifact) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ArtifactView.class);
		intent.putExtra("Artifact", artifact);
		startActivity(intent);
	}
}