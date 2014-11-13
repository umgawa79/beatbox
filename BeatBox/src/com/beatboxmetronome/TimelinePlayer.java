package com.beatboxmetronome;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TimelinePlayer {

	int tempoContWidth;
	int curPos;
	int offset;
	long scrollTime;
	View template;
	CountDownTimer timelineTimer;
	LinearLayout tempoContainer;
	HorizontalScrollView tempoTimeline;
	MetronomeFragment metronome;
	
	public TimelinePlayer(View v, MetronomeFragment metronome){
		this.template = v;
		this.tempoContainer = (LinearLayout) template.findViewById(R.id.tempo_container);
    	this.tempoTimeline  = (HorizontalScrollView) template.findViewById(R.id.tempo_timeline);
    	this.metronome = metronome;
	}
	
	public void play(){
		tempoContWidth = tempoContainer.getWidth();
		curPos = tempoTimeline.getScrollX();
		offset = tempoTimeline.getWidth();
		
		if(tempoContWidth - (curPos + offset) <= 0){
			tempoTimeline.scrollTo(0, 0);
			curPos = tempoTimeline.getScrollX();
		}
		
		scrollTime = (tempoContWidth - curPos - offset) * 20;
				
    	timelineTimer = new CountDownTimer(scrollTime, 1)
    	{          
    		 public void onTick(long millisLeft)
    		 {   
    			 tempoTimeline.scrollBy(1, 0);  
    		 }          

    		 public void onFinish()
    		 {
    			 tempoTimeline.smoothScrollTo((int)(tempoContWidth-(offset/2)), 0); //temporary solution to some bug.
    			 metronome.pause();    			 
    		 }      
    	}.start();
	}
	
	public void pause(){
		timelineTimer.cancel();
	}
	
	public void resume(){
		
	}
	
}
