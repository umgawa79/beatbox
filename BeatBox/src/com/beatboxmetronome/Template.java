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
	private int numEntries = 0;
	private Vector<Integer> tempos;
	private Vector<Integer> measures;
	private Vector<Integer> timesigs;
	
	public Template(File f)
	{
		try {
			loadTemplate(f);
		}
		catch(IOException e) {
			Log.e("BeatBox Error","Failed to load template.");
			System.out.println("Error to load!");
		}
	}
	
	/*
	 * This function should be called when a template is chosen on the load screen.
	 * Can also be used for faking the online repository, but would need to load from a different directory.
	 */
	public void loadTemplate(File f) throws IOException
	{
		String line;
		StringTokenizer st;
		BufferedReader reader = new BufferedReader(new FileReader(f)); // TODO: Reading from correct directory?
		
		while ((line = reader.readLine()) != null)
		{
			st = new StringTokenizer(line);
		    String s1 = st.nextToken();
		    if (s1.equals("NAME")) {
		    	this.templateName = st.nextToken();
		    }
		    else if (s1.equals("TEMPO")) {
		    	this.tempos.add(Integer.valueOf(st.nextToken()));
		    }
		    else if (s1.equals("TIMESIG")) {
		    	this.timesigs.add(Integer.valueOf(st.nextToken()));
		    }
		    else if (s1.equals("MEASURES")) {
		    	this.measures.add(Integer.valueOf(st.nextToken()));
		    	this.numEntries++; // IMPORTANT: We use MEASURES to signify the end of the current tempo's info.
		    }
		}
		reader.close();
	}
	
	public void saveTemplate() throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter("/templates/"+templateName+".tt"), true);
		writer.println("NAME: " + templateName);
		for (int i = 0; i < numEntries; i++)
		{
			writer.println("TEMPO: " + tempos.elementAt(numEntries));
			writer.println("TIMESIG: " + timesigs.elementAt(numEntries));
			writer.println("MEASURES: " + measures.elementAt(numEntries));
		}
		writer.close();
	}
	
	
	
	public String getTemplateName()
	{
		return templateName;
	}
	
	public int getNumEntries()
	{
		return numEntries;
	}
	
	public Vector<Integer> getTempoArray() // Do we want to do this? Encapsulation... depends how Metronome and Edit work.
	{
		return tempos;
	}
	
	public Vector<Integer> getTimesigArray()
	{
		return timesigs;
	}
	
	public Vector<Integer> getMeasuresArray()
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
		//TODO
	}
	
	public void setTimesigsVector(Vector<Integer> newTimesigs)
	{
		//TODO
	}
	
	public void setMeasuresVector(Vector<Integer> newMeasures)
	{
		//TODO
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