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

import static com.rallydev.rallydroid.Preferences.PASSWORD;
import static com.rallydev.rallydroid.Preferences.USERNAME;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.rallydev.rallydroid.dto.Iteration;
import com.rallydev.rallydroid.dto.Story;
import com.rallydev.rallydroid.dto.User;

public abstract class RallyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.containsKey("user")) {
			Log.d("blah", "restoring the user");
			user = (User) savedInstanceState.getSerializable("user");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.d("blah","saving the user");
		outState.putSerializable("user", user);
		super.onSaveInstanceState(outState);
	}

	private User user;
	
	
	protected String getUserName() {
		return getPreference(USERNAME);
	}

	protected String getPassword() {
		return getPreference(PASSWORD);
	}
	
	protected void setUserName(String newUserName) {
		setPreference(USERNAME, newUserName);
	}
	
	protected void setPassword(String newPassword) {
		setPreference(PASSWORD, newPassword);
	}
	
	private String getPreference(String key) {
		SharedPreferences mySharedPreferences = getSharedPreferences(Preferences.PREFS, Activity.MODE_PRIVATE);
		return mySharedPreferences.getString(key, "");
	}

	private void setPreference(String key, String newUserName) {
		SharedPreferences mySharedPreferences = getSharedPreferences(Preferences.PREFS, Activity.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		editor.putString(key, newUserName);
		
		editor.commit();
	}
	
	protected User getUser() {
		if(this.user != null) return user;
		
		if(getUserName() == null || getPassword() == null) {
			return null;
		}
			
        try { 
        	JSONObject adHocQuery = new JSONObject();
        	adHocQuery.put("#user", "/user");
        	adHocQuery.put("userName", "${#user.DisplayName}");
        	adHocQuery.put("subscriptionName", "${#user.Subscription.Name}");
        
        JSONObject userObject = getAdHocResult(adHocQuery);
        
        return new User(userObject.getString("userName"), userObject.getString("subscriptionName"));
        }catch(Exception e) {

        	Log.e("error",e.getMessage());
        	return null;
        }
	}
	
	protected List<Story> getStories() {
		
        try { 
        	JSONObject adHocQuery = new JSONObject();
        	adHocQuery.put("stories","/hierarchicalrequirement?query=(Iteration = ${/iteration:current})&fetch=FormattedID,Name");
        
        JSONObject storiesObject = getAdHocResult(adHocQuery).getJSONObject("stories");
        JSONArray resultsArray= storiesObject.getJSONArray("Results");
        
        List<Story> stories = new ArrayList<Story>();
        
        Log.d("size", ""+resultsArray.length());
        
        for(int i=0 ;i <resultsArray.length(); i++ )
        {
        	JSONObject object = (JSONObject) resultsArray.get(i);
        	Log.d("blah", object.getString("FormattedID"));
        	Story story = new Story(object.getString("FormattedID"), object.getString("Name"));
        	Log.d("story", story.toString());
        	stories.add(story);
        }
        
        return stories;
        //return new User(userObject.getString("userName"), userObject.getString("subscriptionName"));
        }catch(Exception e) {

        	Log.e("error",e.getMessage());
        	return Collections.emptyList();
        }
	}

	private JSONObject getAdHocResult(JSONObject adHocQuery) throws Exception {
        return getResult("https://test1cluster.rallydev.com/slm/webservice/1.11/adhoc.js?_method=POST&adHocQuery="+URLEncoder.encode(adHocQuery.toString())+"&pretty=true");
	}
	
	protected JSONObject getResult(String uri) throws Exception {
		DefaultHttpClient httpclient = getClient();
		
        HttpGet httpget = new HttpGet(uri);

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        InputStream is = entity.getContent();
        
        return new JSONObject(Util.slurp(is));
	}

	private DefaultHttpClient getClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
        new AuthScope(null, 443),
        new UsernamePasswordCredentials(getUserName(), getPassword()));
		return httpclient;
	}
	
	protected Iteration getCurrentIteration() {
		//if(this.user != null) return user;

        try { 
        JSONObject adHocQuery = new JSONObject();
        adHocQuery.put("iteration", "/iteration:current");
         
        JSONObject iterationObject = getAdHocResult(adHocQuery).getJSONObject("iteration");
        
        return new Iteration(
        		iterationObject.getString("Name"), 
        		iterationObject.getInt("ObjectID")
        		);
        }catch(Exception e) {
        	Log.e("error",e.getMessage());
        	return null;
        }
	}
	
}
