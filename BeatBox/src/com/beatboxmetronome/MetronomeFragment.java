package com.beatboxmetronome;

import java.util.Vector;
import java.lang.Runnable;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/********************************************************************************************************************
 * This fragment resides in the Metronome tab of the UI. It provides the Basic and Template capabilities of the metronone.
 *******************************************************************************************************************/
public class MetronomeFragment extends Fragment implements OnClickListener
{
	private Integer mTempo;
	private enum MetronomeMode { BASIC, TEMPLATE };
	private MetronomeMode mMode;
	private MediaPlayer mMediaPlayer;
	private TempoPlayer mTempoPlayer;
	private Handler mHandler;
	private Template curTemplate;
	private TimelinePlayer mTimelinePlayer;
	
	
	/******************************************************************************************************************
	 * Creates a fragment with a default tempo of 120 bpm in the Basic mode
	 *******************************************************************************************************************/
	public MetronomeFragment()
	{
		mTempo = Integer.valueOf(120);
		mMode = MetronomeMode.BASIC;
		mTempoPlayer = null;
		mMediaPlayer = null;
		mTimelinePlayer = null;
	}
	
	
	/******************************************************************************************************************
	 * Callback when the app is resumed
	 ******************************************************************************************************************/
	@Override
	public void onResume()
	{
		mMediaPlayer = MediaPlayer.create(this.getActivity().getApplicationContext(), R.raw.snap);
		mHandler = new Handler();
		mTempoPlayer = new TempoPlayer(mMediaPlayer, mHandler);
		super.onResume();
	}
	
	
	/******************************************************************************************************************
	 * Callback when the app is paused
	 ******************************************************************************************************************/
	@Override
	public void onPause()
	{
		mMediaPlayer.release();
		mHandler = null;
		mTempoPlayer = null;
		super.onPause();
	}
	
	
	/********************************************************************************************************************
	 * Loads the layout from XML and displays the UI
	 *******************************************************************************************************************/
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
        iBtn = (ImageButton) fragView.findViewById(R.id.pause_button);
        iBtn.setOnClickListener(this);
        TextView tv = (TextView) fragView.findViewById(R.id.bpm_text);
        tv.setOnClickListener(this);
        
        setTempo(mTempo);

        return fragView;
    }

	
	/********************************************************************************************************************
	 * Callback for all button presses
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 *******************************************************************************************************************/
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
		else if(v.getId() == R.id.pause_button)
		{
			pause();
		}
		else if(v.getId() == R.id.mode_button)
		{
			switchModes();
		}
		else if(v.getId() == R.id.bpm_text)
		{
			editBpm();
		}
	}
	
	
	/********************************************************************************************************************
	 * Brings up a dialog for editing the tempo
	 *******************************************************************************************************************/
	private void editBpm()
	{
		DialogFragment dlg = new TempoDialog();
		dlg.show(getFragmentManager(), getString(R.string.tempo_dialog));
	}


	/********************************************************************************************************************
	 * Switches between the basic and template modes of the fragment.
	 *******************************************************************************************************************/
	private void switchModes()
	{
		View v = this.getView();
    	Button modeButton = (Button) v.findViewById(R.id.mode_button);
    	Button inc = (Button) v.findViewById(R.id.inc_tempo_button);
    	Button dec = (Button) v.findViewById(R.id.dec_tempo_button);
    	TextView songTitle = (TextView) v.findViewById(R.id.song_title_text);
    	View tempoTimeline = v.findViewById(R.id.tempo_timeline);
    	LinearLayout tempoTracker = (LinearLayout) v.findViewById(R.id.tempo_tracker);
    	if(modeButton != null && inc != null && dec != null && songTitle != null && tempoTimeline != null)
    	{
    		switch(mMode)
    		{
	    		case BASIC:
	    		{
	    			modeButton.setText(R.string.basic_mode_label);
	    			inc.setVisibility(View.INVISIBLE);
	    			dec.setVisibility(View.INVISIBLE);
	    			songTitle.setVisibility(View.VISIBLE);
	    			tempoTimeline.setVisibility(View.VISIBLE);
	    			tempoTracker.setVisibility(View.VISIBLE);
	    			mMode = MetronomeMode.TEMPLATE;
	    			load(curTemplate);
	    			break;
	    		}
	    		case TEMPLATE:
	    		{
	    			modeButton.setText(R.string.template_mode_label);
	    			inc.setVisibility(View.VISIBLE);
	    			dec.setVisibility(View.VISIBLE);
	    			songTitle.setVisibility(View.INVISIBLE);
	    			tempoTimeline.setVisibility(View.INVISIBLE);
	    			tempoTracker.setVisibility(View.INVISIBLE);
	    			mMode = MetronomeMode.BASIC;
	    			break;
	    		}
    		}
    	}
	}


	/********************************************************************************************************************
	 * Plays the metronome.
	 * When in the basic mode, plays an audible click at the tempo specified in the bpm field. When in template mode,
	 * starts playing the template from its beginning.
	 *******************************************************************************************************************/
	private void play()
	{
		View v = this.getView();
		ImageButton playButton = (ImageButton) v.findViewById(R.id.play_button);
		playButton.setVisibility(View.INVISIBLE);
		ImageButton pauseButton = (ImageButton) v.findViewById(R.id.pause_button);
		pauseButton.setVisibility(View.VISIBLE);
		
		LinearLayout tempoContainer = (LinearLayout) this.getView().findViewById(R.id.tempo_container);
		
		if(mMode == MetronomeMode.TEMPLATE && tempoContainer.getChildCount() > 0){
			Runnable timelinePlayer = new Runnable(){
				public void run(){
					 mTimelinePlayer.play();
				}
			};
			getActivity().runOnUiThread(timelinePlayer);
		}
		
		mTempoPlayer.setTempo(mTempo);
		mHandler.post(mTempoPlayer);
		
	}
	
	
	/********************************************************************************************************************
	 * Pauses the metronome.
	 *******************************************************************************************************************/
	public void pause()
	{
		View v = this.getView();
		ImageButton playButton = (ImageButton) v.findViewById(R.id.play_button);
		playButton.setVisibility(View.VISIBLE);
		ImageButton pauseButton = (ImageButton) v.findViewById(R.id.pause_button);
		pauseButton.setVisibility(View.INVISIBLE);
		
		mHandler.removeCallbacks(mTempoPlayer);
		
		if(((LinearLayout) v.findViewById(R.id.tempo_container)).getChildCount() > 0)
			mTimelinePlayer.pause();
	}


	/********************************************************************************************************************
	 * Decrements the tempo value.
	 *******************************************************************************************************************/
	public void decTempo()
    {
    	setTempo(--mTempo);
    }
    
    
	/********************************************************************************************************************
	 * Increments the tempo value.
	 *******************************************************************************************************************/
	public void incTempo()
    {
    	setTempo(++mTempo);
    }
	
    
	/********************************************************************************************************************
	 * Updates the tempo value displayed by the TextView widget
	 * @param bpm
	 *******************************************************************************************************************/
    public void setTempo(int bpm)
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
    
    /******************************************************************************************************************
     * Draws the tempo timeline
     * @param v The parent of the fragment widgets
     *******************************************************************************************************************/
    private void drawTempoTimeline(Template aTemplate)
    {
    	LinearLayout tempoContainer = (LinearLayout) this.getView().findViewById(R.id.tempo_container);
    	HorizontalScrollView tempoTimeline = (HorizontalScrollView) this.getView().findViewById(R.id.tempo_timeline);
    	
    	Vector<Integer> measureVector = aTemplate.getMeasuresVector();
    	Vector<Integer> timesigVector = aTemplate.getTimesigVector();
    	Vector<Integer> tempoVector = aTemplate.getTempoVector();
    	
    	//get the length of the timeline by adding all the measure * timesig
    	int bmapSize = 0;
    	for(int i=0; i < measureVector.size(); i++){
    		bmapSize += measureVector.elementAt(i) * timesigVector.elementAt(i);
    	}
    	
    	float padding = (float)(tempoTimeline.getWidth()/2.0)-5;
    	
    	bmapSize += (int)(padding*2);
    	
        Bitmap bmap = Bitmap.createBitmap(bmapSize, 70, Bitmap.Config.ARGB_8888); 
        Canvas canv = new Canvas(bmap);
        ImageView tempo = new ImageView(getActivity());
        
        float left = 0.0f;
        double blue = 0.0;
        double green = 0.0;
        Paint paddingColor = new Paint();
        paddingColor.setARGB(255, 154, 154, 154);
        
        canv.drawRect(left, 70, left+padding, 0, paddingColor);
        left += padding;
        
        for(int i = 0; i < measureVector.size(); i++)
        {
        	//Set different color depending on the tempo. The default color is blue.
        	//Faster tempo has more green which will have brighter color.
        	int tempoVal = tempoVector.elementAt(i);
        	green = Math.ceil((tempoVal/20.0)*25);
        	blue = Math.ceil((tempoVal/20.0)*25)+50;
        	
        	if(blue > 255.0){
        		blue = 255.0;
        	}
        	
            //set the color for this section
        	Paint color = new Paint();
            color.setARGB(255, 0, (int)green, (int)blue);
            
            //draw the rectangle representing this section. The width of the rectangle is proportional to numMeasures*numBeatsPermeasure
            float width = measureVector.elementAt(i) * timesigVector.elementAt(i) * 1.0f; //1 pixel per beat
            canv.drawRect(left, 70, left+width, 0, color);
            
            left += width;
        }
        
        canv.drawRect(left, 70, left+padding, 0, paddingColor);
        
        tempo.setImageBitmap(bmap);
        tempo.setScaleType(ImageView.ScaleType.FIT_START);
        tempoContainer.addView(tempo);
    	tempoTimeline.scrollTo(0, 0);
    }
    
    
    /********************************************************************************************************************
     * Resets the tempo timeline
     * @param v The parent of the fragment widgets
     *******************************************************************************************************************/
    private void resetTempoTimeline()
    {
    	LinearLayout tempoContainer = (LinearLayout) this.getView().findViewById(R.id.tempo_container);
    	tempoContainer.removeAllViews();
    }
        
    /********************************************************************************************************************
     * Loads the Template into the metronome page, updating all the fields and preparing the page to play the Template.
     *******************************************************************************************************************/
    public void load(Template aTemplate)
    {
    	curTemplate = aTemplate;
    	if(mMode == MetronomeMode.BASIC)
    		switchModes();
    	setTempo(aTemplate.getTempoVector().firstElement().intValue());
    	View v = this.getView();
    	TextView songTitle = (TextView) v.findViewById(R.id.song_title_text);
    	songTitle.setText(aTemplate.getTemplateName().toCharArray(), 0, aTemplate.getTemplateName().length());
    	this.resetTempoTimeline();
    	this.drawTempoTimeline(aTemplate);
    	mTimelinePlayer = new TimelinePlayer(v, this);
    }
}
