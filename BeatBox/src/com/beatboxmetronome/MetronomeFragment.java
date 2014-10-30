package com.beatboxmetronome;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Michael O'Sullivan
 * 
 * This fragment resides in the Metronome tab of the UI. It provides the basic and template capabilities of the mentronone.
 */
public class MetronomeFragment extends Fragment implements OnClickListener
{
	private Integer mTempo;
	private enum MetronomeMode { BASIC, TEMPLATE };
	private MetronomeMode mMode;
	private MediaPlayer mMediaPlayer;
	
	public MetronomeFragment()
	{
		mTempo = new Integer(120);
		mMode = MetronomeMode.BASIC;
	}
	
	@Override
	public void onResume()
	{
		mMediaPlayer = MediaPlayer.create(this.getActivity().getApplicationContext(), R.raw.snap);
		super.onResume();
	}
	
	
	@Override
	public void onPause()
	{
		mMediaPlayer.release();
		super.onPause();
	}
	
	
	/**
	 * Loads the layout from XML and displays the UI
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View fragView = inflater.inflate(R.layout.metronome_layout, container, false);

        Button btn = (Button) fragView.findViewById(R.id.inc_tempo_button);
        btn.setOnClickListener(this);
        btn = (Button) fragView.findViewById(R.id.dec_tempo_button);
        btn.setOnClickListener(this);
        btn = (Button) fragView.findViewById(R.id.mode_button);
        btn.setOnClickListener(this);
        ImageButton iBtn = (ImageButton) fragView.findViewById(R.id.play_button);
        iBtn.setOnClickListener(this);
        
        setTempo(mTempo);

        return fragView;
    }

	/**
	 * Callback for all button presses
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.inc_tempo_button)
		{
			incTempo();
		}
		else if(v.getId() == R.id.dec_tempo_button)
		{
			decTempo();
		}
		else if(v.getId() == R.id.play_button)
		{
			play();
		}
		else if(v.getId() == R.id.mode_button)
		{
			switchModes();
		}
	}
	
	
	/**
	 * Switches between the basic and template modes of the fragment.
	 */
	private void switchModes()
	{
		View v = this.getView();
    	Button modeButton = (Button) v.findViewById(R.id.mode_button);
    	Button inc = (Button) v.findViewById(R.id.inc_tempo_button);
    	Button dec = (Button) v.findViewById(R.id.dec_tempo_button);
    	TextView songTitle = (TextView) v.findViewById(R.id.song_title_text);
    	if(modeButton != null && inc != null && dec != null && songTitle != null)
    	{
    		switch(mMode)
    		{
	    		case BASIC:
	    		{
	    			modeButton.setText(R.string.basic_mode_label);
	    			inc.setVisibility(View.INVISIBLE);
	    			dec.setVisibility(View.INVISIBLE);
	    			songTitle.setVisibility(View.VISIBLE);
	    			mMode = MetronomeMode.TEMPLATE;
	    			break;
	    		}
	    		case TEMPLATE:
	    		{
	    			modeButton.setText(R.string.template_mode_label);
	    			inc.setVisibility(View.VISIBLE);
	    			dec.setVisibility(View.VISIBLE);
	    			songTitle.setVisibility(View.INVISIBLE);
	    			mMode = MetronomeMode.BASIC;
	    			break;
	    		}
    		}
    	}
	}


	/**
	 * Plays the metronome.
	 * When in the basic mode, plays an audible click at the tempo specified in the bpm field. When in template mode,
	 * starts playing the template from its beginning.
	 */
	private void play()
	{
		if(mMediaPlayer != null)
		{
			for(int i = 0; i < 5; i++)
			{
				try
				{
					mMediaPlayer.start();
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * Decrements the tempo value.
	 */
	public void decTempo()
    {
    	setTempo(--mTempo);
    }
    
    
	/**
	 * Increments the tempo value.
	 */
	public void incTempo()
    {
    	setTempo(++mTempo);
    }
	
    
	/**
	 * Updates the tempo value displayed by the TextView widget
	 * @param bpm
	 */
    private void setTempo(int bpm)
    {
    	View v = this.getView();
    	if(v != null)
    	{
    		TextView bpmText = (TextView) v.findViewById(R.id.bpm_text);
    		if(bpmText != null)
    		{
    			mTempo = bpm;
    			String text = new String();
    			text += mTempo.toString() + " BPM";
    			bpmText.setText(text);
    		}
    	}
    }

}
