package com.beatboxmetronome;


import android.support.v4.app.ListFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * This file and the entire load function is heavily based on this tutorial: 
 * http://custom-android-dn.blogspot.in/2013/01/create-simple-file-explore-in-android.html
 */

public class LoadListFragment extends ListFragment {
	OnTemplateSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnTemplateSelectedListener {
        public void onTemplateSelected(int position, Template t);
    }
    
    public interface OnTemplateToEditListener {
    	public void onTemplateToEdit(Template t);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTemplateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTemplateSelectedListener in Main");
        }
    }

	private File currentDir; // This should be the directory we save our templates.
    private FileArrayAdapter adapter;
    private Context c;
    private boolean localList = true;
    private String searchWords = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("BeatBox", "onCreate starting LOADLIST");
		c = getActivity();
		currentDir = c.getFilesDir();
		File localDir = new File(currentDir + "/local/");
		currentDir = localDir; // By default we populate the local list first.
		System.out.println(currentDir.getAbsolutePath());
		fill(currentDir);
		Log.d("BeatBox", "onCreate finishing");
		return;
	}
	
	@Override
	public void onResume()
	{
		Log.e("BeatBox", "Load Resume!");
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		Log.e("BeatBox", "Load Pause!");
		super.onPause();
	}
	
	public void onSearchRequest(String toFind)
	{
		//Refill the list based on templates containing the string.
		System.out.println("Search request for: " + toFind);
		searchWords = toFind;
		fill(currentDir);
	}
	
	public void onSaveRequest()
	{
		if(localList) fill(currentDir);
	}
	
	private void fill(File f)
	{
		Log.d("BeatBox", "fill starting");
		File[] savedTemplates = f.listFiles();
		List<Template> templates = new ArrayList<Template>();
		try{
			for (File ff: savedTemplates)
			{
				Template t = new Template(ff);
				if(t.getTemplateName().toLowerCase(Locale.ENGLISH).contains(searchWords.toLowerCase(Locale.ENGLISH)))
					templates.add(t); // Only what was searched for.
			}
		}
		catch(Exception e)
		{
			//nothing to do here
		}
		searchWords = ""; // Reset search
		Collections.sort(templates);
        adapter = new FileArrayAdapter(c,R.layout.fragment_load_bar,templates);
        if (localList) adapter.switchMode("Download");
        else adapter.switchMode("Local");
        this.setListAdapter(adapter); 
        Log.d("BeatBox", "fill finishing");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		if (localList)
		{
			System.out.println("Item selected at position: "+position);
			super.onListItemClick(l, v, position, id);
			Template t = adapter.getItem(position);
			onTemplateSelected(t);
			mCallback.onTemplateSelected(position, t);
		}
		else // online list. If a row is clicked what do we do?
		{
			// Maybe in a future version I would display information like description, composer, etc
		}
	}
	
	public void switchMode(String s)
	{
		System.out.println("SwitchMode Entered. String is " + s);
		if (s.equals("Local")) // We want to switch to the online list.
		{
			System.out.println("switching to " + s + " list");
			currentDir = new File(c.getFilesDir()+"/online/");
			localList = false;
			fill(currentDir);
		}
		else //s == Download, switch to the local list.
		{
			System.out.println("switching to " + s + " list");
			currentDir = new File(c.getFilesDir()+"/local/");
			localList = true;
			fill(currentDir);
		}
	}
	
	public void onTemplateSelected(Template t)
	{	// May need to delete this if I use mCallback
		System.out.println("Here's where I'd send the template to other fragments.");
	}
	
}