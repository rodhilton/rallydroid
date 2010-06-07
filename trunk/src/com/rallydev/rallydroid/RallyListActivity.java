package com.rallydev.rallydroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.rallydev.rallydroid.dto.DomainObject;

public abstract class RallyListActivity extends ListActivity {
	
	static final protected String LIST_ITEM_LINE1 = "line1";
	static final protected String LIST_ITEM_LINE2 = "line2";
	
	private static final int DETAIL_DIALOG = 1;
	
	private ProgressDialog mLoadingDialog;
    private final Handler mHandler = new Handler();
    private String mErrorMsg = "";
    private List<DomainObject> items;
    private DomainObject selectedItem;
	
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
		
		PrepareItemClickHandler();
		
		PostCreate();
		
		refreshData();
		
		registerForContextMenu(getListView());
	}
	
	private void PrepareItemClickHandler()
	{
		ListView myListView = (ListView) findViewById(getListViewResId());
		myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {				
				ShowDetailForItemAt(index);
			}
	        	
	    });
	}
	
	protected void ShowDetailForItemAt(int index)
	{
		selectedItem = getItemAt(index);
		showDialog(DETAIL_DIALOG);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DETAIL_DIALOG:
				LayoutInflater li = LayoutInflater.from(this);
				View detailView = li.inflate(getDetailViewResId(), null);
				
				AlertDialog.Builder detailDialog = new AlertDialog.Builder(this);
				detailDialog.setTitle("Detail View");
				detailDialog.setView(detailView);
				return detailDialog.create();
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
			case DETAIL_DIALOG:
				PrepareDetailDialog(dialog, selectedItem);
		}
	}
	
	protected void refreshData()
	{
		mErrorMsg = "";
        
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            	items = loadData();
                mHandler.post(mUpdateResults);
            }
        };
        mLoadingDialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.loading));
    	t.start();
	}
	
	protected DomainObject getItemAt(int index)
	{
		if (items == null)
			return null;
		
		return items.get(index);
	}
	
	protected DomainObject getSelectedItem()
	{
		return selectedItem;
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
        		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        	        
    			for (DomainObject item: items)
    	    	{
    	        	Map<String, String> row = new HashMap<String, String>();
    	        	row.put(LIST_ITEM_LINE1, getLine1(item));
    	        	row.put(LIST_ITEM_LINE2, getLine2(item));
    	        	data.add(row);
    	        }
    			
        		setListAdapter(new SimpleAdapter(RallyListActivity.this, data, 
                		android.R.layout.two_line_list_item, 
                		new String[] {LIST_ITEM_LINE1, LIST_ITEM_LINE2}, 
                		new int[] {android.R.id.text1, android.R.id.text2}));
        		
        		setTitle(getActivityTitle());
        		if (data.isEmpty())
        		{
        			Toast.makeText(getApplicationContext(), R.string.no_items, Toast.LENGTH_SHORT).show();
        			//new AlertDialog.Builder(RallyListActivity.this).setMessage(R.string.no_items).show();
        		}
        	}
        	
        	if (mLoadingDialog != null && mLoadingDialog.isShowing())
    	    	mLoadingDialog.cancel();
        }
    };
    
    public void PostCreate() {}
    
    protected int getListViewResId() { return android.R.id.list; }
    
    protected int getListLayoutResId() { return android.R.layout.two_line_list_item; }
    
    protected int getDetailViewResId() { return 0; }
    
    protected void PrepareDetailDialog(Dialog dialog, DomainObject selectedItem) {}
    
    protected int getLayoutResId() { return R.layout.artifactlist; };
    
    protected abstract String getActivityTitle();

    protected abstract List<DomainObject> loadData();
    protected abstract String getLine1(DomainObject artifact);
    protected abstract String getLine2(DomainObject artifact);
}
