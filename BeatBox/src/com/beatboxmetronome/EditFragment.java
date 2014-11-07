package com.beatboxmetronome;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.beatboxmetronome.LoadListFragment.OnTemplateSelectedListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author Michael O'Sullivan
 *
 */
public class EditFragment extends Fragment
{
	ListView listView;
	//OnTemplateSelectedListener mCallback;
	private File currentDir; // This should be the directory we save our templates.
    private FileArrayAdapter adapter;
    private Context c;

    
    /*
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
    
    */


	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        View fragView = inflater.inflate(R.layout.edit_layout, container, false);
        
        listView = (ListView) fragView.findViewById(R.id.editTemplatesList);

		Log.d("BeatBox", "onCreate starting");
		c = getActivity();
		currentDir = c.getFilesDir(); //TODO: fix pathing.
		System.out.println(currentDir.getAbsolutePath());
		fill(currentDir);
		Log.d("BeatBox", "onCreate finishing");
		
		return fragView;
	}
	
	private void fill(File f)
	{
		try
		{
			Template test = new Template();
			test.testTemplate("TestSongName");
			test.saveTemplate();
			test.testTemplate("SecondTestName");
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
        listView.setAdapter(adapter); 
        Log.d("BeatBox", "fill finishing");
	}
	/*
	public void onItemClick(ListView l, View v, int position, long id)
	{
		System.out.println("Item selected at position: "+position);
		
		Template t = adapter.getItem(position);
		onTemplateSelected(t);
		mCallback.onTemplateSelected(position, t);
	}
	
	public void onTemplateSelected(Template t)
	{	// May need to delete this if I use mCallback
		System.out.println("Here's where I'd send the template to other fragments.");
	}
	*/
	
}
    

