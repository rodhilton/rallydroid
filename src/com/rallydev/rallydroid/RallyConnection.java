package com.rallydev.rallydroid;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.rallydev.rallydroid.dto.Artifact;
import com.rallydev.rallydroid.dto.Iteration;
import com.rallydev.rallydroid.dto.Story;
import com.rallydev.rallydroid.dto.User;

public class RallyConnection {
	private String userName;
	private String password;
	private User user;
	private Iteration iteration;
	public final String domain = "rally1.rallydev.com";
	public final String apiVersion = "1.15";
	
	public RallyConnection(String username, String password)
	{
		this.userName = username;
		this.password = password;
	}
	
	public User getCurrentUser() {
		if (user == null) { 
			try { 
	        	Log.i("user", "Retrieving current user");
				
	        	JSONObject adHocQuery = new JSONObject();
	        	adHocQuery.put("#user", "/user");
	        	adHocQuery.put("userName", "${#user.DisplayName}");
	        	adHocQuery.put("subscriptionName", "${#user.Subscription.Name}");
	        	
		        JSONObject userObject = getAdHocResult(adHocQuery);
		        if (userObject != null)
		        	user = new User(userObject.getString("userName"), userObject.getString("subscriptionName"));
	        }catch(Exception e) {
	        	Log.e("user", e.toString());
	        }
		}
		
        return user;
	}
	
	public List<Story> getStoriesForCurrentIteration() {
		
        try { 
        	JSONObject adHocQuery = new JSONObject();
        	adHocQuery.put("stories","/hierarchicalrequirement?query=(Iteration = ${/iteration:current})&fetch=true&pretty=true");
        
	        JSONObject storiesObject = getAdHocResult(adHocQuery).getJSONObject("stories");
	        JSONArray resultsArray = storiesObject.getJSONArray("Results");
	        
	        List<Story> stories = new ArrayList<Story>();
	        
	        Log.d("stories", "found " + resultsArray.length() + " stories");
	        
	        for(int i=0 ;i <resultsArray.length(); i++ )
	        {
	        	JSONObject object = (JSONObject) resultsArray.get(i);
	        	Story story = new Story(object);
	        	stories.add(story);
	        }
	        
	        return stories;
	        
        }catch(Exception e) {

        	Log.e("stories",e.getMessage());
        	return Collections.emptyList();
        }
	}
	
	public List<Artifact> listAllMyTasks() {
		String iterClause = "";
		Iteration it = getCurrentIteration();
		if (it != null)
			iterClause = "and (Iteration.Oid = " + it.getOid() + ")";
		return listTasks("((Owner = " + this.userName + ") " + iterClause + ")");
	}
	
	public List<Artifact> listTasks(String query) {			
	
		String url = getApiUrlBase() + "/task.js?query=" + URLEncoder.encode(query) + "&fetch=true&pretty=true";
        List<Artifact> ret = new ArrayList<Artifact>();
        try
        {
	        JSONObject queryResult = getResult(url).getJSONObject("QueryResult");
		    JSONArray results = queryResult.getJSONArray("Results");
		    for(int i = 0 ; i< results.length() ; i++) {
				JSONObject object = (JSONObject) results.get(i);
			    ret.add(new Artifact(object));
		    }
        }catch(Exception e) {
        	Log.e("task", "Error retrieving tasks " + e.toString());
        }
        return ret;
	}
	
	private String getApiUrlBase()
	{
		return "https://" + domain + "/slm/webservice/" + apiVersion;
	}

	private JSONObject getAdHocResult(JSONObject adHocQuery) throws Exception {
		String url = getApiUrlBase() 
					+ "/adhoc.js?_method=POST&adHocQuery="
					+ URLEncoder.encode(adHocQuery.toString())
					+ "&pretty=true";
		return getResult(url);
	}
	
	protected JSONObject getResult(String uri) throws Exception {
		DefaultHttpClient httpclient = getClient();
		try
		{
	        HttpGet httpget = new HttpGet(uri);
	
	        HttpResponse response = httpclient.execute(httpget);
	        StatusLine status = response.getStatusLine(); 
	        int statusCode = status.getStatusCode();
	        if (statusCode > 201)
	        {
	        	Log.e("http", "Got unexpected http status " + status.getStatusCode() + ": " + status.getReasonPhrase());
	        	return null;
	        }
	        HttpEntity entity = response.getEntity();
	
	        InputStream is = entity.getContent();
	        JSONObject obj = new JSONObject(Util.slurp(is));
        
	        return obj;
		}
		finally
		{
	        httpclient.getConnectionManager().shutdown();
		}
	}

	private DefaultHttpClient getClient() {
		DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
	        new AuthScope(null, 443),
	        new UsernamePasswordCredentials(this.userName, this.password));
		return httpclient;
	}
	
	public Iteration getCurrentIteration() {
		if (user == null) 
		{
			Log.e("iteration", "Tried to get current iteration but user was null (probably not logged in).");
			return null;
		}

		if (iteration == null) {
			try { 
		        JSONObject adHocQuery = new JSONObject();
		        adHocQuery.put("iteration", "/iteration:current");
		        JSONObject iterationObject = getAdHocResult(adHocQuery).getJSONObject("iteration");
		        
		        String name = iterationObject.getString("Name");
		        int oid = iterationObject.getInt("ObjectID");
		        
		        iteration = new Iteration(name, oid);
	        }catch(Exception e) {
	        	Log.e("iteration", e.toString());
	        }
		}
		
		return iteration;
	}
}
