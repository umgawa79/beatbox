package com.beatboxmetronome;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;

public class Template implements Comparable<Template> {
	
	private String templateName;
	private String description;
	private String composer;
	private String creator;
	private int numEntries = 0;
	private Vector<Integer> tempos;
	private Vector<Integer> measures;
	private Vector<Integer> timesigs;
	
	public Template(File f)
	{
		Log.d("BeatBox", "Template constructor starting");
		try {
			loadTemplate(f);
		}
		catch(IOException e) {
			Log.e("BeatBox Error","Failed to load template.");
			System.out.println("Error to load!");
		}
	}
	
	public Template()
	{
		initVectors();
	}
	
	public void testTemplate() // Used to test loading and saving by LoadListFragment.
	{
		System.out.println("Making default template");
		numEntries=2;
		templateName = "TestSongName";
		initVectors();
		tempos.add(144); // TODO get rid of these after testing.
		tempos.add(120);
		measures.add(4);
		measures.add(5);
		timesigs.add(3);
		timesigs.add(8);
		try {
			saveTemplate();
			templateName = "SecondTestName";
			saveTemplate();
			}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("failed to save");
		}
	}
	
	private void initVectors()
	{
		tempos = new Vector<Integer>();
		timesigs = new Vector<Integer>();
		measures = new Vector<Integer>();
	}
	
	/*
	 * This function should be called when a template is chosen on the load screen.
	 * Can also be used for faking the online repository, but would need to load from a different directory.
	 */
	public void loadTemplate(File f) throws IOException
	{
		initVectors();
		String line;
		StringTokenizer st;
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		while ((line = reader.readLine()) != null)
		{
			st = new StringTokenizer(line);
		    String s1 = st.nextToken();
		    if (s1.equals("NAME:")) {
		    	this.templateName = st.nextToken();
		    	System.out.println("name " + templateName);
		    }
		    else if (s1.equals("TEMPO:")) {
		    	this.tempos.add(Integer.valueOf(st.nextToken()));
		    }
		    else if (s1.equals("TIMESIG:")) {
		    	this.timesigs.add(Integer.valueOf(st.nextToken()));
		    }
		    else if (s1.equals("MEASURES:")) {
		    	this.measures.add(Integer.valueOf(st.nextToken()));
		    	this.numEntries++; // IMPORTANT: We use MEASURES to signify the end of the current tempo's info.
		    }
		}
		reader.close();
	}
	
	public void saveTemplate() throws IOException
	{
		PrintWriter writer = new PrintWriter(new
				FileWriter("/data/data/com.beatboxmetronome/files/"+templateName+".tt"), true);//TODO temp hardcode
		writer.println("NAME: " + templateName);
		for (int i = 0; i < numEntries; i++)
		{
			writer.println("TEMPO: " + tempos.elementAt(i));
			writer.println("TIMESIG: " + timesigs.elementAt(i));
			writer.println("MEASURES: " + measures.elementAt(i));
		}
		writer.close();
	}
	
	public void deleteTemplate()
	{
		// Remove the saved file.
	}
	
	
	
	public String getTemplateName()
	{
		return templateName;
	}
	
	public int getNumEntries()
	{
		return numEntries;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getComposer()
	{
		return composer;
	}
	
	public String getCreator()
	{
		return creator;
	}
	
	public Vector<Integer> getTempoVector()
	{
		return tempos;
	}
	
	public Vector<Integer> getTimesigVector()
	{
		return timesigs;
	}
	
	public Vector<Integer> getMeasuresVector()
	{
		return measures;
	}
	
	public void setTemplateName(String s)
	{
		this.templateName = s;
	}
	
	public void setNumEntries(int n)
	{
		this.numEntries = n;
	}
	
	public void setTemposVector(Vector<Integer> newTempos)
	{
		tempos = newTempos;
	}
	
	public void setTimesigsVector(Vector<Integer> newTimesigs)
	{
		timesigs = newTimesigs;
	}
	
	public void setMeasuresVector(Vector<Integer> newMeasures)
	{
		measures = newMeasures;
	}
	
	public void setDescription(String s)
	{
		description = s;
	}
	
	public void setComposer(String s)
	{
		composer = s;
	}
	
	public void setCreator(String s)
	{
		creator = s;
	}
	
	public int compareTo(Template other) // For sorting alphabetically by name on load screen
	{
		if(this.templateName != null) {
            return this.templateName.toLowerCase(Locale.ENGLISH).compareTo(other.getTemplateName().toLowerCase(Locale.ENGLISH));
		}
    else
            throw new IllegalArgumentException();
	}
	
}