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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.rallydev.rallydroid.dto.Artifact;

public class MyTasks extends RallyActivity {
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artifactlist);
        
        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
        new AuthScope(null, 443),
        new UsernamePasswordCredentials(getUserName(), getPassword()));
        
        final ArrayList<String> taskStrings = new ArrayList<String>();
        final ArrayList<Artifact> taskRefs = new ArrayList<Artifact>();
        // Create the array adapter to bind the array to the listview 
        final ArrayAdapter<String> aa; 
        aa = new ArrayAdapter<String>(this, 
                                      android.R.layout.simple_list_item_1, 
                                      taskStrings); 

        //HttpGet httpget = new HttpGet("https://test1cluster.rallydev.com/slm/webservice/1.11/task.js?query=((Owner+=+"+getUserName()+")+and+(State+!=+Completed))&fetch=true");
        HttpGet httpget = new HttpGet("https://test1cluster.rallydev.com/slm/webservice/1.11/task.js?query=((Owner+=+"+getUserName()+")+and+(State+!=+Completed))&fetch=true&pretty=true");

        Log.d("test", "executing request" + httpget.getRequestLine());
        try {
        HttpResponse response = httpclient.execute(httpget);

        Log.d("test", response.getStatusLine().toString());
        HttpEntity entity = response.getEntity();

        InputStream is = entity.getContent();
        String blah = Util.slurp(is);

        Log.d("stuff", blah);
        

        JSONObject array = new JSONObject(blah);
        JSONObject queryResult = array.getJSONObject("QueryResult");
        JSONArray results = queryResult.getJSONArray("Results");
        for(int i = 0 ; i< results.length() ; i++) {
        Log.d("result", results.get(i).toString());
        JSONObject object = (JSONObject) results.get(i);
        taskStrings.add(object.getString("FormattedID")+": "+object.getString("Name"));
        Artifact artifact=new Artifact(object.getString("_type"), object.getString("_ref"), object.getString("FormattedID"), object.getInt("ObjectID"));
        taskRefs.add(artifact);
        //String type, String ref, String formattedID, Integer oid)
        }
        //array.
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
       
        
        ListView myListView = (ListView) findViewById(R.id.myListView);
        
        //myImageview.setImageResource(R.drawable.logo);
        
        // Create the array list of to do items 
      
        // Bind the array adapter to the listview. 
        myListView.setAdapter(aa);
        
        myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("test",arg1.toString());
				Log.d("test2",""+arg2);
				
				launchArtifactView(taskRefs.get(arg2));		
			}
        	
        });
        

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