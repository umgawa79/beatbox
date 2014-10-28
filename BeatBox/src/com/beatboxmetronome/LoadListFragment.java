package com.beatboxmetronome;


import android.app.ListFragment;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This file and the entire load function is heavily based on this tutorial: 
 * http://custom-android-dn.blogspot.in/2013/01/create-simple-file-explore-in-android.html
 */

public class LoadListFragment extends ListFragment {
	
	private File currentDir; // This should be the directory we save our templates.
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		currentDir = new File("/templates/"); //TODO: Create this folder.
		fill(currentDir);
	}
	
	private void fill(File f)
	{
		File[] savedTemplates = f.listFiles();
		List<Template> templates = new ArrayList<Template>();
		try{
			for (File ff: savedTemplates)
			{
				// Here is where I will extract information from the .tt file.
				// Something like this...
				// templates.add(new Template(ff)); Passing in the file will call a constructor...
			}
		}
		catch(Exception e)
		{
			//nothing to do here
		}
		Collections.sort(templates);
	}
	
}