package com.beatboxmetronome;

import java.util.Vector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class Timeline extends HorizontalScrollView{

	private OnScrollViewListener scrollListener;
	private Vector<Integer> tempoVector;
	private Vector<Integer> measureVector;
	private Vector<Integer> timesigVector;
	private Vector<Integer> sectionVector;
	
	public Timeline(Context context) {
		super(context);
	}
	
	public Timeline(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public Timeline(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}
	
	public void setOnScrollViewListener(OnScrollViewListener l, View v, Vector<Integer> tempo, Vector<Integer> measure, Vector<Integer> timesig){
		this.scrollListener = l;
		this.tempoVector = tempo;
		this.measureVector = measure;
		this.timesigVector = timesig;
		setSectionVector(v, tempo, measure, timesig);
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt){
		scrollListener.onScrollChanged(this, l, t, oldl, oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public void scrollHandler(View v, TempoPlayer tempoP, int l){
		Timeline tline = (Timeline) v.findViewById(R.id.tempo_timeline);
		int padding = tline.getWidth()/2;
	
		int pos = findTempoPos(l+padding);
		
		int bpm = tempoVector.elementAt(pos);
		
		TextView bpmText = (TextView) v.findViewById(R.id.bpm_text);
		String bpmString = new String();
		bpmString = bpm + " BPM";
		bpmText.setText(bpmString);
	
		tempoP.setTempo(bpm);
	}
	
	private int findTempoPos(int l){
		for(int i = 0; i < sectionVector.size(); i++){
			if(i == 0 && sectionVector.elementAt(i) >= l)
				return i;
			else{
				if(i != 0 && l > sectionVector.elementAt(i-1) && l <= sectionVector.elementAt(i))
					return i;
			}
		}
		return sectionVector.size()-1;
	}
	
	private void setSectionVector(View v, Vector<Integer> tempo, Vector<Integer> measure, Vector<Integer> timesig){
		sectionVector = new Vector<Integer>();
		Timeline tline = (Timeline) v.findViewById(R.id.tempo_timeline);
		int padding = tline.getWidth()/2;
		
		Integer section = 0;
		
		for(int i=0; i<measure.size(); i++){
			if(i == 0){
				section += (padding + measure.elementAt(i) * timesig.elementAt(i));
				sectionVector.add(section);
			}else{
				section += measure.elementAt(i) * timesig.elementAt(i);
				sectionVector.add(section);
			}
		}
	}
	
}
