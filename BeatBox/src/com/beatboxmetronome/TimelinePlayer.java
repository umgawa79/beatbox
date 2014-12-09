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
	Timeline tempoTimeline;
	MetronomeFragment metronome;
	
	public TimelinePlayer(View v, MetronomeFragment metronome){
		this.template = v;
		this.tempoContainer = (LinearLayout) template.findViewById(R.id.tempo_container);
    	this.tempoTimeline  = (Timeline) template.findViewById(R.id.tempo_timeline);
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
		
		scrollTime = (tempoContWidth - curPos - offset) * 114;
		
    	timelineTimer = new CountDownTimer(scrollTime, 100)
    	{          
    		 public void onTick(long millisLeft)
    		 {   
    			 tempoTimeline.smoothScrollBy(1, 100);
    		 }          

    		 public void onFinish()
    		 {
    			 metronome.pause();    			 
    		 }      
    	}.start();
	}
	
	public void pause(){
		if(timelineTimer != null)
			timelineTimer.cancel();
	}
	
}
