package com.beatboxmetronome;

import android.support.v4.app.ListFragment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
public class EditFragment extends ListFragment
{
	//ListView listView;
	//OnTemplateSelectedListener mCallback;
	private File currentDir; // This should be the directory we save our templates.
    private EditTabArrayAdapter adapter;
    private TempoSectionsAdapter tsAdapter;
    private Context c;
    private boolean templateView = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        View fragView = inflater.inflate(R.layout.edit_layout, container, false);
        
        //listView = (ListView) fragView.findViewById(R.id.list);

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
        adapter = new EditTabArrayAdapter(c,R.layout.fragment_edit_bar,templates);
        this.setListAdapter(adapter); 
        Log.d("BeatBox", "fill finishing");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		if (templateView)
		{
			System.out.println("Item selected at position: "+position);
			Template t = adapter.getItem(position);
			onTemplateSelected(t);
			templateView = false;
		}
		else
		{
			System.out.println("Item clicked in edit mode at position: " + position);
		}
	}
	
	public void onTemplateSelected(Template t)
	{	// May need to delete this if I use mCallback
		System.out.println("Here's where I'd send the template to other fragments.");
		List<Vector> sections = new ArrayList<Vector>();
		int size = t.getNumEntries();
		for (int i = 0; i < size; i++)
		{
			Vector<Integer> row = new Vector<Integer>();
			row.add(t.getTempoVector().elementAt(i));
			row.add(t.getMeasuresVector().elementAt(i));
			row.add(t.getTimesigVector().elementAt(i));
			sections.add(row);
		}
		tsAdapter = new TempoSectionsAdapter(c,R.layout.temposectionbar,sections);
		this.setListAdapter(tsAdapter);
		//maybe invalidate and redraw here, replace view
	}
	
	
}
    

