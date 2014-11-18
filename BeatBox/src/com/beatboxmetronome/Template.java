package com.beatboxmetronome;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;

public class Template implements Comparable<Template> {
	
	private String templateName;
	private String description;
	private String composer;
	private String creator;
	private String endField = "18675421857205847205756"; // TODO: Think of a much better way to handle this.
	private int numEntries = 0;
	private Vector<Integer> tempos; //The sections of the template, each with its own tempo 
	private Vector<Integer> measures; //The number of measures in section n
	private Vector<Integer> timesigs; //The number of beats per measure in section n
	private String localFileDir = "/data/data/com.beatboxmetronome/files/local/";
	private String onlineFileDir = "/data/data/com.beatboxmetronome/files/online/";
	private String ext = ".tt";
	
	public Template(File f)
	{
		Log.d("BeatBox", "Template constructor starting");
		try {
			loadTemplate(f);
		}
		catch(IOException e) {
			Log.e("BeatBox Error","Failed to load template.");
			System.out.println("Error to load! at " + f);
		}
	}
	
	public Template()
	{
		initVectors();
	}
	
	public void testTemplate(String songTitle) // Used to test loading and saving by LoadListFragment.
	{
		System.out.println("Making default template");
		numEntries=3;
		templateName = songTitle;
		description = "Placeholder description.";
		composer = "Unknown";
		creator = "Anonymous";
		initVectors();
		//Random rand = new Random();
		tempos.add(144); // TODO get rid of these after testing.
		tempos.add(120);
		tempos.add(88);
		measures.add(4);
		measures.add(6);
		measures.add(8);
		timesigs.add(4);
		timesigs.add(3);
		timesigs.add(2);
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
	{	//TODO: Save and load description, composer, creator
		initVectors();
		String line;
		StringTokenizer st;
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		while ((line = reader.readLine()) != null)
		{
			st = new StringTokenizer(line);
		    String s1 = st.nextToken();
		    if (s1.equals("NAME:")) {
		    	s1 = st.nextToken();
		    	templateName = s1; // The template name is the only required field.
		    	while(st.hasMoreTokens())
		    	{
		    		s1 = st.nextToken();
		    		if (!s1.equals(endField)) templateName = templateName+" "+s1;
		    	}
		    	System.out.println("name: " + templateName);
		    }
		    else if (s1.equals("CREATOR:")) {
		    	s1 = st.nextToken();
		    	if (!s1.equals(endField)) creator = s1;
		    	else {
		    		creator = "Anonymous";
		    	}
		    	while(st.hasMoreTokens())
		    	{
		    		s1 = st.nextToken();
		    		if(!s1.equals(endField)) creator = creator+" "+s1;
		    	}
		    	System.out.println("creator: " + creator);
		    }
		    else if (s1.equals("COMPOSER:")) {
		    	s1 = st.nextToken();
		    	if (!s1.equals(endField)) composer = s1;
		    	else {
		    		composer = "Unknown";
		    	}
		    	while(st.hasMoreTokens())
		    	{
		    		s1 = st.nextToken();
		    		if (!s1.equals(endField)) composer = composer+" "+s1;
		    	}
		    	System.out.println("composer: " + composer);
		    }
		    else if (s1.equals("DESCRIPTION:")) {
		    	s1 = st.nextToken();
		    	if (!s1.equals(endField)) description = s1;
		    	else {
		    		description = "No description available.";
		    	}
		    	while(st.hasMoreTokens())
		    	{
		    		s1 = st.nextToken();
		    		if (!s1.equals(endField)) description = description+" "+s1;
		    	}
		    	System.out.println("description: " + description);
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
				FileWriter(localFileDir+templateName+ext), true);//TODO temp hardcode
		writer.println("NAME: " + templateName + " " + endField);
		writer.println("CREATOR: " + creator + " " + endField);
		writer.println("DESCRIPTION: " + description + " " + endField);
		writer.println("COMPOSER: " + composer + " " + endField);
		for (int i = 0; i < numEntries; i++)
		{
			writer.println("TEMPO: " + tempos.elementAt(i));
			writer.println("TIMESIG: " + timesigs.elementAt(i));
			writer.println("MEASURES: " + measures.elementAt(i));
		}
		writer.close();
	}
	
	public void uploadTemplate() throws IOException
	{
		PrintWriter writer = new PrintWriter(new
				FileWriter(onlineFileDir+templateName+ext), true);//TODO temp hardcode
		writer.println("NAME: " + templateName + " " + endField);
		writer.println("CREATOR: " + creator + " " + endField);
		writer.println("DESCRIPTION: " + description + " " + endField);
		writer.println("COMPOSER: " + composer + " " + endField);
		for (int i = 0; i < numEntries; i++)
		{
			writer.println("TEMPO: " + tempos.elementAt(i));
			writer.println("TIMESIG: " + timesigs.elementAt(i));
			writer.println("MEASURES: " + measures.elementAt(i));
		}
		writer.close();
	}
	
	public void downloadTemplate() throws IOException
	{
		PrintWriter writer = new PrintWriter(new
				FileWriter(localFileDir+templateName+ext), true);//TODO temp hardcode
		writer.println("NAME: " + templateName + " " + endField);
		writer.println("CREATOR: " + creator + " " + endField);
		writer.println("DESCRIPTION: " + description + " " + endField);
		writer.println("COMPOSER: " + composer + " " + endField);
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
		File f = new File(localFileDir+templateName+ext);
		if(f.exists())
			Log.d("BeatBox", "File "+f.getAbsolutePath()+" exists");
		if(f.delete())
			Log.d("BeatBox", "Deleted successfully");
		else
			Log.e("BeatBox", "Failed to delete template file!"); 
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