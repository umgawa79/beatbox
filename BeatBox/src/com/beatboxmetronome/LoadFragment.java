package com.beatboxmetronome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Michael O'Sullivan
 *
 */
public class LoadFragment extends Fragment
{
	public LoadFragment() {}
	
	
	/**
	 * Loads the layout from XML and displays the UI
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View fragView = inflater.inflate(R.layout.fragment_load_bar, container, false);
        return fragView;
    }
}