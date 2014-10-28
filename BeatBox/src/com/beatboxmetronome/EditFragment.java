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
public class EditFragment extends Fragment
{
	public EditFragment() {}
	
	
	/**
	 * Loads the layout from XML and displays the UI
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View fragView = inflater.inflate(R.layout.edit_layout, container, false);
        return fragView;
    }
}
