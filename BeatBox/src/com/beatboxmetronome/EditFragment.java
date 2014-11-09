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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
    private EditTabArrayAdapter adapter;
    private TempoSectionsAdapter tsAdapter;
    private Context c;
    private boolean templateView = true;
    
    View curView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        View fragView = inflater.inflate(R.layout.edit_layout, container, false);
        
        listView = (ListView) fragView.findViewById(R.id.templateList);
		Log.d("BeatBox", "onCreate starting");
		c = getActivity();
		currentDir = c.getFilesDir(); //TODO: fix pathing.
		System.out.println(currentDir.getAbsolutePath());
		fill(currentDir);
		Log.d("BeatBox", "onCreate finishing");
		curView = fragView;
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> a, View view, int position, long arg)
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
		});
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
        listView.setAdapter(adapter); 
        Log.d("BeatBox", "fill finishing");
	}
	
	/*
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
	*/
	
	
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
		
		ViewGroup parent = (ViewGroup) curView.getParent();
	    int index = parent.indexOfChild(curView);
	    parent.removeView(curView);
	    LayoutInflater inflater = LayoutInflater.from(c);
	    curView = inflater.inflate(R.layout.fragment_create_layout, parent, false);
	    parent.addView(curView, index);
	    
        listView = (ListView) curView.findViewById(R.id.sectionList);
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> a, View view, int position, long arg)
					{
						
						System.out.println("Item clicked in edit mode at position: " + position + ". Can you believe it?!");
					} 
			});
	    
	    
		tsAdapter = new TempoSectionsAdapter(c,R.layout.temposectionbar,sections);
		listView.setAdapter(tsAdapter);
		//maybe invalidate and redraw here, replace view
		
		tsAdapter.notifyDataSetChanged();
	}
	
	
}
    

