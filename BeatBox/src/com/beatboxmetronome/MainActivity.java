package com.beatboxmetronome;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import com.beatboxmetronome.TempoDialog.TempoDialogListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, LoadListFragment.OnTemplateSelectedListener, TempoDialogListener
{
	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    
    /*
     * Receives the position (maybe irrelevant) and template 
     * that was selected from load list.
     */
    public void onTemplateSelected(int position, Template t) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    	System.out.println("Just loaded template: "+t.getTemplateName());
    	    	
    	MetronomeFragment metronome = (MetronomeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
    	if(metronome != null)
    	{
    		metronome.load(t);
    	}
    	this.getActionBar().setSelectedNavigationItem(1);
    }


    /**
     * Creates the UI
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create the folders for storage
        File currentDir = getFilesDir();
		File localDir = new File(currentDir + "/local/");
		boolean success = true;
		if (!localDir.exists()) success = localDir.mkdir();
		if (!success)
		{
			Log.e("BeatBox", "Local folder could not be created!");
		}
		success = true;
		File onlineDir = new File(currentDir + "/online/");
		if (!onlineDir.exists()) success = onlineDir.mkdir();
		if (!success)
		{
			Log.e("BeatBox", "Online folder could not be created!");
		}
        
        // Save a few test templates, to be removed in the final version.
        try
		{
			Template test = new Template();
			test.testTemplate("Symphony No. 5");
			test.saveTemplate();
			test.testTemplate("1812 Overture");
			test.saveTemplate();
			test.testTemplate("Habenera");
			test.uploadTemplate();
			test.testTemplate("William Tell Overture");
			test.uploadTemplate();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			android.util.Log.e("BeatBox", "Failed to create test templates.");
		}
        
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                invalidateOptionsMenu();
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        this.getActionBar().setSelectedNavigationItem(1);
    }
    
    private String downloadItemText = "Download";
    private LoadListFragment loadFrag;
    private EditFragment editFrag;
    private EditText mSearchField;
    private ImageButton mSearchButton;
    private Button mRepoModeButton;
    
    public LoadListFragment getLoadListFragment()
    {
    	return loadFrag;
    }
    public EditFragment getEditFragment()
    {
    	return editFrag;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem loadTabItem = menu.findItem(R.id.menu_load_tab);
        mSearchField = (EditText) loadTabItem.getActionView()
        		.findViewById(R.id.search);
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                	System.out.println("Search initiated!");
        	    	String toFind = mSearchField.getText().toString();
        	    	loadFrag.onSearchRequest(toFind);
                    return true;
                }
                return false;
            }
        });
        mRepoModeButton = (Button) loadTabItem.getActionView()
        		.findViewById(R.id.repoModeButton);
        OnClickListener mClickListener = new OnClickListener() {

            public void onClick(View v) {
            	if (v.getId()==R.id.repoModeButton)
            	{
            		if (mRepoModeButton.getText().equals("Download"))
                	{
                			mRepoModeButton.setText("Local");
                			downloadItemText = "Local";
                			System.out.println("Calling switchmode local...");
                			loadFrag.switchMode("Local");
                	}
                	else
                	{
                		mRepoModeButton.setText("Download");
                		downloadItemText = "Download";
                		System.out.println("Calling switchmode download...");
                		loadFrag.switchMode("Download");
                	}
            	}
            }
        };
        mRepoModeButton.setOnClickListener(mClickListener);
        mSearchButton = (ImageButton) loadTabItem.getActionView()
        		.findViewById(R.id.searchButton);
        mRepoModeButton.setText(downloadItemText);
        mSearchButton.setOnClickListener(new OnClickListener() {
        	   @Override
        	   public void onClick(View v) {
        		    System.out.println("Search initiated!");
        	    	String toFind = mSearchField.getText().toString();
        	    	loadFrag.onSearchRequest(toFind);
        	   }
        	});
        int tab = this.getActionBar().getSelectedNavigationIndex();
        if (tab == 2) {
        	loadTabItem.setVisible(true);
        }
        else {
        	loadTabItem.setVisible(false);
        }
        return true;
    }
    
    public void sendTemplateToEdit(Template t)
    {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    	//System.out.println("Just loaded template: "+t.getTemplateName());
    	    	
    	EditFragment edit = (EditFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
    	if(edit != null)
    	{
    		edit.goToSectionList(t);
    	}
    	this.getActionBar().setSelectedNavigationItem(0);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            switch(position)
            {
    	        case 0: return editFrag = new EditFragment();
    	        case 1: return new MetronomeFragment();
    	        case 2: return loadFrag = new LoadListFragment();
    	        default: break;
            }
            return null;
        }

        
        /**
         * Returns the number of tabs
         */
        @Override
        public int getCount()
        {
            return 3;
        }

        
        /**
         * Returns the title of the tab at a given position
         */
        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale l = Locale.getDefault();
            switch (position)
            {
                case 0: return getString(R.string.title_edit).toUpperCase(l);
                case 1: return getString(R.string.title_metronome).toUpperCase(l);
                case 2: return getString(R.string.title_load).toUpperCase(l);
            }
            return null;
        }
    }


	@Override
	public void onDialogPositiveClick(DialogFragment dialog)
	{
		MetronomeFragment metronome = (MetronomeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
    	if(metronome != null)
    	{
    		EditText bpmText = (EditText) dialog.getDialog().findViewById(R.id.tempo_edit);
    		if(bpmText != null)
    		{
    			try
    			{
    				Integer bpm = Integer.valueOf(bpmText.getText().toString());
    				metronome.setTempo(bpm, 0);
    			}
    			catch(NumberFormatException e)
    			{}
    		}
    	}
	}
}
