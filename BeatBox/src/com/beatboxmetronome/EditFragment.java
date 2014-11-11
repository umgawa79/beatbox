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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Button;


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
    private int pos = -1; //I'm using this to carry position variable for onclick events for section rows, while having the buttons defined outside that onclick
    
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
		File localDir = new File(currentDir + "/local/");
		currentDir = localDir;
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
						System.out.println("Inside ItemClick for list of templates at position: "+position);
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
		
        Button createTemplateButton = (Button) fragView.findViewById(R.id.createNewTemplateButton);
        createTemplateButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		onTemplateSelected(new Template());
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
	
	
	
	public void onTemplateSelected(final Template t)
	{	// May need to delete this if I use mCallback
		System.out.println("Here's where I'd send the template to other fragments.");
		final List<Vector> sections = new ArrayList<Vector>();
		int size = t.getNumEntries();
		final Vector<Integer> tempoVector = t.getTempoVector();
		final Vector<Integer> measuresVector = t.getMeasuresVector();
		for (int i = 0; i < size; i++)
		{
			Vector<Integer> row = new Vector<Integer>();
			row.add(tempoVector.elementAt(i));
			row.add(measuresVector.elementAt(i));
			row.add(t.getTimesigVector().elementAt(i));
			sections.add(row);
		}
		
		ViewGroup parent = (ViewGroup) curView.getParent();
	    int index = parent.indexOfChild(curView);
	    final View templatesView = curView;
	    parent.removeView(curView);
	    LayoutInflater inflater = LayoutInflater.from(c);
	    curView = inflater.inflate(R.layout.fragment_create_layout, parent, false);
	    parent.addView(curView, index);
	    
        listView = (ListView) curView.findViewById(R.id.sectionList);
        
		final LinearLayout editSpecificSection = (LinearLayout) curView.findViewById(R.id.editSpecificSection);
		final LinearLayout playSaveBar = (LinearLayout) curView.findViewById(R.id.playSaveBar);
    
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> a, View view, final int position, long arg)
					{
						
						System.out.println("Item clicked in edit mode at position: " + position + ". Can you believe it?!");
						playSaveBar.setVisibility(View.GONE);
						pos = position;
						editSpecificSection.setVisibility(View.VISIBLE);
						
						
					} 
			});
		
	    
	    
		tsAdapter = new TempoSectionsAdapter(c,R.layout.temposectionbar,sections);
		listView.setAdapter(tsAdapter);
		//maybe invalidate and redraw here, replace view
		
		tsAdapter.notifyDataSetChanged();
		
		Button saveButton = (Button) curView.findViewById(R.id.saveCreateButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				t.setTemposVector(tempoVector);
				t.setMeasuresVector(measuresVector);
				
				ViewGroup parent = (ViewGroup) curView.getParent();
			    int index = parent.indexOfChild(curView);
			    parent.removeView(curView);
			    LayoutInflater inflater = LayoutInflater.from(c);
			    curView = inflater.inflate(R.layout.fragment_save, parent, false);
			    
				final EditText saveTitle = (EditText) curView.findViewById(R.id.save_edit_title);
				saveTitle.setText(t.getTemplateName());
				
				final EditText saveComposer = (EditText) curView.findViewById(R.id.save_edit_composer);
				saveComposer.setText(t.getComposer());
				
				final EditText saveDescription = (EditText) curView.findViewById(R.id.save_edit_description);
				saveDescription.setText(t.getDescription());
				
				final EditText saveCreator = (EditText) curView.findViewById(R.id.save_edit_creator);
				saveCreator.setText(t.getCreator());
				
				parent.addView(curView, index);
				
				Button saveInfoButton = (Button) curView.findViewById(R.id.save_button_save);
				saveInfoButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						t.setTemplateName(saveTitle.getText().toString());
						t.setComposer(saveComposer.getText().toString());
						t.setDescription(saveDescription.getText().toString());
						t.setCreator(saveCreator.getText().toString());
						
						try {
							t.saveTemplate();
						} catch(IOException e) {
							System.out.println("Exception trying to save template");
						}
						
						ViewGroup parent = (ViewGroup) curView.getParent();
					    int index = parent.indexOfChild(curView);
					    parent.removeView(curView);
					    LayoutInflater inflater = LayoutInflater.from(c);
					    curView = templatesView;
					    parent.addView(curView, index);
					    tsAdapter.notifyDataSetChanged();
						
						
						
					}
				});
				
				
		        
				
			}
		});
		
		Button newSectionButton = (Button) curView.findViewById(R.id.createNewSectionButton);
		newSectionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playSaveBar.setVisibility(View.GONE);
				pos = -1;
				editSpecificSection.setVisibility(View.VISIBLE);
			}
		});
		
				
		Button okButton = (Button) editSpecificSection.findViewById(R.id.sectionEditOK);
		okButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
					int newBPM = Integer.parseInt(bpmEditText.getText().toString());
					EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
					int newMeasures = Integer.parseInt(measuresEditText.getText().toString());

						

					if((newBPM > 0) && (newMeasures > 0))
					{
						if(pos < 0)
						{	
							tempoVector.add(newBPM);
							measuresVector.add(newMeasures);
								
							Vector<Integer> newSection = new Vector<Integer>();
							newSection.add(newBPM);
							newSection.add(newMeasures);
							newSection.add(4);
								
							sections.add(newSection);	
						}
						else
						{
							tempoVector.set(pos,  newBPM);
							measuresVector.set(pos, newMeasures);
							
							Vector<Integer> newSection = sections.get(pos);
							newSection.set(0, newBPM);
							newSection.set(1, newMeasures);
							
							sections.set(pos, newSection);
						}
							
							bpmEditText.setText("");
							measuresEditText.setText("");
							editSpecificSection.setVisibility(View.GONE);
							playSaveBar.setVisibility(View.VISIBLE);
							
							tsAdapter.notifyDataSetChanged();
					}
			}
		});
				
		
		final Button cancelButton = (Button) editSpecificSection.findViewById(R.id.sectionEditCancel);
	    cancelButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
					EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
					EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
						
					bpmEditText.setText("");
					measuresEditText.setText("");
	                editSpecificSection.setVisibility(View.GONE);
	                playSaveBar.setVisibility(View.VISIBLE);
	                }
	            });
	            
	    final Button deleteButton = (Button) editSpecificSection.findViewById(R.id.sectionEditDelete);
	    deleteButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    			if(pos >= 0)
	    			{
	    				
	    				sections.remove(pos);
	    				tsAdapter.notifyDataSetChanged();
	            		
	    				EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
	    				EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
						bpmEditText.setText("");
						measuresEditText.setText("");
						editSpecificSection.setVisibility(View.GONE);
						playSaveBar.setVisibility(View.VISIBLE);
	            	}

			}
		});
		
		
	}
	
	
}
    

