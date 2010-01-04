package com.rallydev.rallydroid;

import static com.rallydev.rallydroid.Preferences.PASSWORD;
import static com.rallydev.rallydroid.Preferences.USERNAME;

import com.rallydev.rallydroid.dto.User;

import android.app.Activity;
import android.content.SharedPreferences;

public class ActivityHelper {
	private Activity activity;
	private User user;
	private static RallyConnection conn;
	
	public ActivityHelper(Activity activity)
	{
		this.activity = activity;
	}
	private String getPreference(String key) {
		SharedPreferences mySharedPreferences = activity.getSharedPreferences(Preferences.PREFS, Activity.MODE_PRIVATE);
		return mySharedPreferences.getString(key, "");
	}
	
	protected User getCurrentUser()
	{
		if (user != null)
			return user;
		
		RallyConnection conn = getRallyConnection();
		if (conn != null)
		{
			return conn.getCurrentUser();
		}
		return null;
	}
	
	public void resetCredentials()
	{
		conn = null;
	}

	public RallyConnection getRallyConnection()
	{
		if (conn == null)
		{
			String username = getUserName();
			String password = getPassword();
			
			if(username == null || password == null) {
				return null;
			}
			
			conn = new RallyConnection(username, password);
		}
		
		return conn;
	}
	
	public String getUserName() {
		return getPreference(USERNAME);
	}

	public String getPassword() {
		return getPreference(PASSWORD);
	}
}
