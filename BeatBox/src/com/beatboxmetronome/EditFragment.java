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
    private int pos = -1; //I'm using this to carry position variable for onclick events for section rows, while having the buttons defined outside that onclick
    private boolean isEditingSection;
    private boolean initialViewHasBeenCreated = false;
    
    private int numSections;
    
    View curView;
    
    
	LinearLayout editSpecificSection;
	LinearLayout playSaveBar;
	Button addSectionButton;
	
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		curView = inflater.inflate(R.layout.edit_layout, container, false);

		c = getActivity();
		currentDir = c.getFilesDir(); //TODO: fix pathing.
		File localDir = new File(currentDir + "/local/");
		currentDir = localDir;
		System.out.println(currentDir.getAbsolutePath());
		
		
		return goToTemplateList();
	
	}
		
	private void fill(File f)
	{
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
	
	
    
    private View goToTemplateList()
    {
    	
    	if(initialViewHasBeenCreated)
    	{
        	System.out.println("!isCreatingView 1");
    		ViewGroup parent = (ViewGroup) curView.getParent();
        	System.out.println("!isCreatingView 2");
    	    int index = parent.indexOfChild(curView);
        	System.out.println("!isCreatingView 3");
    	    parent.removeView(curView);
    	    System.out.println("called removeView");
    	    LayoutInflater inflater = LayoutInflater.from(c);
    	    curView = inflater.inflate(R.layout.edit_layout, parent, false);
    	    parent.addView(curView, index);
    	}
    	initialViewHasBeenCreated = true;

        listView = (ListView) curView.findViewById(R.id.templateList);
        //listView.invalidateViews();
        fill(currentDir);
        
        //adapter.notifyDataSetChanged();
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> a, View view, int position, long arg)
					{

						Template t = adapter.getItem(position);
						goToSectionList(t);
				
					} 
			});
        
        Button createTemplateButton = (Button) curView.findViewById(R.id.createNewTemplateButton);
        createTemplateButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		goToSectionList(new Template());
        	}
        });
        
        return curView;       
    }
    
    private View goToSectionList(final Template t)
    {
		//System.out.println("Here's where I'd send the template to other fragments.");
		final List<Vector> sections = new ArrayList<Vector>();
		numSections = t.getNumEntries(); //Have to manually record number of sections throughout adding/deleting.
		final Vector<Integer> tempoVector = t.getTempoVector();
		final Vector<Integer> measuresVector = t.getMeasuresVector();
		final Vector<Integer> timeSigsVector = t.getTimesigVector();
		for (int i = 0; i < numSections; i++)
		{
			Vector<Integer> row = new Vector<Integer>();
			row.add(tempoVector.elementAt(i));
			row.add(measuresVector.elementAt(i));
			row.add(timeSigsVector.elementAt(i));
			sections.add(row);
		}
		
		
		ViewGroup parent = (ViewGroup) curView.getParent();
	    int index = parent.indexOfChild(curView);
	    parent.removeView(curView);
	    LayoutInflater inflater = LayoutInflater.from(c);
	    curView = inflater.inflate(R.layout.fragment_create_layout, parent, false);
	    parent.addView(curView, index);
	    
        listView = (ListView) curView.findViewById(R.id.sectionList);
        
		tsAdapter = new TempoSectionsAdapter(c,R.layout.temposectionbar,sections);
		listView.setAdapter(tsAdapter);
		
		editSpecificSection = (LinearLayout) curView.findViewById(R.id.editSpecificSection);
		playSaveBar = (LinearLayout) curView.findViewById(R.id.playSaveBar);
		addSectionButton = (Button) curView.findViewById(R.id.createNewSectionButton);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> a, View view, final int position, long arg)
					{
						if(!isEditingSection)
						{
							isEditingSection = true;
							System.out.println("Item clicked in edit mode at position: " + position + ". Can you believe it?!");
							playSaveBar.setVisibility(View.GONE);
							addSectionButton.setVisibility(View.GONE);
							pos = position;
							editSpecificSection.setVisibility(View.VISIBLE);
							
							EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
							EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
							EditText beatsPerMeasureEditText = (EditText) editSpecificSection.findViewById(R.id.editBeatsPerMeasure);
							
							bpmEditText.setText(tempoVector.elementAt(position).toString());
							measuresEditText.setText(measuresVector.elementAt(position).toString());
							beatsPerMeasureEditText.setText(timeSigsVector.elementAt(position).toString());
						}
						
						
					} 
			});
		
	    
		
		Button saveButton = (Button) curView.findViewById(R.id.saveCreateButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				t.setTemposVector(tempoVector);
				t.setMeasuresVector(measuresVector);
				t.setTimesigsVector(timeSigsVector);
				t.setNumEntries(numSections);
     
				goToSaveView(t);
			}
		});
		
		Button backButton = (Button) curView.findViewById(R.id.backCreateButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				goToTemplateList();
			}
		});
		
		Button newSectionButton = (Button) curView.findViewById(R.id.createNewSectionButton);
		newSectionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!isEditingSection)
				{
					isEditingSection = true;
					playSaveBar.setVisibility(View.GONE);
					addSectionButton.setVisibility(View.GONE);
					pos = -1;
					editSpecificSection.setVisibility(View.VISIBLE);
					
				}
			}
		});
		
				
		Button okButton = (Button) editSpecificSection.findViewById(R.id.sectionEditOK);
		okButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
					int newBPM = 0;
					String bpmText = bpmEditText.getText().toString();
					if(bpmText.length() > 0)
					{
						newBPM = Integer.parseInt(bpmText);
					}
					EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
					int newMeasures = 0;
					String measuresText = measuresEditText.getText().toString();
					if(measuresText.length() > 0)
					{
						newMeasures = Integer.parseInt(measuresText);
					}
					EditText beatsPerMeasureEditText = (EditText) editSpecificSection.findViewById(R.id.editBeatsPerMeasure);
					int newBeatsPerMeasure = 0;
					String beatsPerMeasureText = beatsPerMeasureEditText.getText().toString();
					if(beatsPerMeasureText.length() > 0)
					{
						newBeatsPerMeasure = Integer.parseInt(measuresText);
					}

					if((newBPM > 0) && (newMeasures > 0) && (newBeatsPerMeasure > 0))
					{
						if(pos < 0)
						{	
							tempoVector.add(newBPM);
							measuresVector.add(newMeasures);
							timeSigsVector.add(newBeatsPerMeasure);
								
							Vector<Integer> newSection = new Vector<Integer>();
							newSection.add(newBPM);
							newSection.add(newMeasures);
							newSection.add(newBeatsPerMeasure);
								
							sections.add(newSection);	
							numSections++;
						}
						else
						{
							tempoVector.set(pos,  newBPM);
							measuresVector.set(pos, newMeasures);
							timeSigsVector.set(pos, newBeatsPerMeasure);
							
							Vector<Integer> newSection = sections.get(pos);
							newSection.set(0, newBPM);
							newSection.set(1, newMeasures);
							newSection.set(2, newBeatsPerMeasure);
							
							sections.set(pos, newSection);
						}
							
						bpmEditText.setText("");
						measuresEditText.setText("");
						beatsPerMeasureEditText.setText("");
						
						editSpecificSection.setVisibility(View.GONE);
						playSaveBar.setVisibility(View.VISIBLE);
						addSectionButton.setVisibility(View.VISIBLE);
						isEditingSection = false;
							
						tsAdapter.notifyDataSetChanged();
					}
			}
		});
				
		
		final Button cancelButton = (Button) editSpecificSection.findViewById(R.id.sectionEditCancel);
	    cancelButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
					EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
					EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
					EditText beatsPerMeasureEditText = (EditText) editSpecificSection.findViewById(R.id.editBeatsPerMeasure);
						
					bpmEditText.setText("");
					measuresEditText.setText("");
					beatsPerMeasureEditText.setText("");
					
	                editSpecificSection.setVisibility(View.GONE);
	                isEditingSection = false;
	                playSaveBar.setVisibility(View.VISIBLE);
	                addSectionButton.setVisibility(View.VISIBLE);
	                }
	            });
	            
	    final Button deleteButton = (Button) editSpecificSection.findViewById(R.id.sectionEditDelete);
	    deleteButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    			if(pos >= 0)
	    			{
	    				sections.remove(pos);
	    				numSections--;
	    				tsAdapter.notifyDataSetChanged();
	    			}	
	    			EditText bpmEditText = (EditText) editSpecificSection.findViewById(R.id.editBPM);
	    			EditText measuresEditText = (EditText) editSpecificSection.findViewById(R.id.editMeasures);
					bpmEditText.setText("");
					measuresEditText.setText("");
					editSpecificSection.setVisibility(View.GONE);
					playSaveBar.setVisibility(View.VISIBLE);
					addSectionButton.setVisibility(View.VISIBLE);
					isEditingSection = false;

			}
		});
	    
		
		return curView;
        
        
    }
    
    private View goToSaveView(final Template t)
    {
    	ViewGroup parent = (ViewGroup) curView.getParent();
	    int index = parent.indexOfChild(curView);
	    parent.removeView(curView);
	    LayoutInflater inflater = LayoutInflater.from(c);
	    curView = inflater.inflate(R.layout.fragment_save, parent, false);
	    parent.addView(curView, index);
	    
		final EditText saveTitle = (EditText) curView.findViewById(R.id.save_edit_title);
		saveTitle.setText(t.getTemplateName());
		
		final EditText saveComposer = (EditText) curView.findViewById(R.id.save_edit_composer);
		saveComposer.setText(t.getComposer());
		
		final EditText saveDescription = (EditText) curView.findViewById(R.id.save_edit_description);
		saveDescription.setText(t.getDescription());
		
		final EditText saveCreator = (EditText) curView.findViewById(R.id.save_edit_creator);
		saveCreator.setText(t.getCreator());
		
		
		Button saveInfoButton = (Button) curView.findViewById(R.id.save_button_save);
		saveInfoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String potentialName = (saveTitle.getText().toString());
				if (potentialName == null || potentialName.equals("")) potentialName = "Untitled";
				t.setTemplateName(potentialName);
				t.setComposer(saveComposer.getText().toString());
				t.setDescription(saveDescription.getText().toString());
				t.setCreator(saveCreator.getText().toString());
				
				try {
					t.saveTemplate();
					((MainActivity) getActivity()).getLoadListFragment().onSaveRequest();
				} catch(IOException e) {
					System.out.println("Exception trying to save template");
				}
				
				goToTemplateList();
				
			}
		});
		
		Button cancelSaveInfoButton = (Button) curView.findViewById(R.id.save_button_cancel);
		cancelSaveInfoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				goToSectionList(t);
	
			}
		});
		
		return curView;
    }
    
	public void onSaveRequest()
	{
		fill(currentDir);
	}

	
	
}
    

