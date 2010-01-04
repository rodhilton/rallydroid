package com.rallydev.rallydroid;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

public abstract class RallyListActivity extends ListActivity {
	
	static final protected String LIST_ITEM_LINE1 = "line1";
	static final protected String LIST_ITEM_LINE2 = "line2";
	
	private ProgressDialog mLoadingDialog;
    private final Handler mHandler = new Handler();
    private String mErrorMsg = "";
	
	private ActivityHelper helper;
	protected ActivityHelper getHelper()
	{
		if (this.helper == null)
			this.helper = new ActivityHelper(this);
		
		return this.helper;
	}

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getLayoutResId());
		
		PostCreate();
		
		refreshData();
	}
	
	protected void refreshData()
	{
		mErrorMsg = "";
        
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            	loadDataFromStore();
                mHandler.post(mUpdateResults);
            }
        };
        mLoadingDialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.loading));
    	t.start();
	}
	
	protected void setError(String errorMsg)
	{
		mErrorMsg = errorMsg;
	}
	
	// Create runnable for posting
    private final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	if (mErrorMsg != "")
        	{
        		new AlertDialog.Builder(RallyListActivity.this).setMessage(mErrorMsg).show();
        		mErrorMsg = "";
        	}
        	else
        	{
        		List<Map<String, String>> data = fillDataForDrawing();
        		
        		setListAdapter(new SimpleAdapter(RallyListActivity.this, data, 
                		android.R.layout.two_line_list_item, 
                		new String[] {LIST_ITEM_LINE1, LIST_ITEM_LINE2}, 
                		new int[] {android.R.id.text1, android.R.id.text2}));
        		
        		if (data.isEmpty())
        		{
        			new AlertDialog.Builder(RallyListActivity.this).setMessage(R.string.no_items).show();
        		}
        	}
        	
        	if (mLoadingDialog != null && mLoadingDialog.isShowing())
    	    	mLoadingDialog.cancel();
        }
    };
    
    protected final int getListViewResId() { return android.R.id.list; }
    
    protected final int getListLayoutResId() { return android.R.layout.two_line_list_item; }
    
    protected void PostCreate() {}
	
    protected int getLayoutResId() { return R.layout.artifactlist; };

    protected abstract void loadDataFromStore();
	
	protected abstract List<Map<String, String>> fillDataForDrawing();

}
