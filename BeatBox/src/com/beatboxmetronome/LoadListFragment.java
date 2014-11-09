package com.beatboxmetronome;


import android.support.v4.app.ListFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("BeatBox", "onCreate starting LOADLIST");
		c = getActivity();
		currentDir = c.getFilesDir(); //TODO: fix pathing.
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
	
	private void fill(File f)
	{
		try
		{
			Template test = new Template();
			test.testTemplate("Test Song Name");
			test.saveTemplate();
			test.testTemplate("Second Test Name");
			test.saveTemplate();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("failed to save");
		}
		Log.d("BeatBox", "fill starting");
		File[] savedTemplates = f.listFiles();
		List<Template> templates = new ArrayList<Template>();
		try{
			for (File ff: savedTemplates)
			{
				templates.add(new Template(ff));
			}
		}
		catch(Exception e)
		{
			//nothing to do here
		}
		Collections.sort(templates);
        adapter = new FileArrayAdapter(c,R.layout.fragment_load_bar,templates);
        this.setListAdapter(adapter); 
        Log.d("BeatBox", "fill finishing");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		System.out.println("Item selected at position: "+position);
		super.onListItemClick(l, v, position, id);
		Template t = adapter.getItem(position);
		onTemplateSelected(t);
		mCallback.onTemplateSelected(position, t);
	}
	
	public void switchMode(String s)
	{
		
	}
	
	public void onTemplateSelected(Template t)
	{	// May need to delete this if I use mCallback
		System.out.println("Here's where I'd send the template to other fragments.");
	}
	
}