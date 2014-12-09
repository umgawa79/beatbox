package com.beatboxmetronome;

import android.media.MediaPlayer;
import android.os.Handler;

public class TempoPlayer implements Runnable
{
	private Integer mTempo;
	private MediaPlayer mMediaPlayer;
	private Handler mHandler;
	private Boolean shouldContinue;

	public TempoPlayer(MediaPlayer mp, Handler h)
	{
		mMediaPlayer = mp;
		mHandler = h;
		shouldContinue = false;
	}
	
	public synchronized void setTempo(Integer tempo)
	{
		mTempo = tempo;
	}
	
	
	public synchronized void stop()
	{
		shouldContinue = false;
	}

	public boolean getContinue()
	{
		return shouldContinue;
	}
	

	public void run()
	{
		shouldContinue = true;
		if(mMediaPlayer != null)
		{
			long period = Math.round(60000.0 / mTempo); //number of milliseconds per beat
			mMediaPlayer.setVolume(0.75f, 0.75f);
			mMediaPlayer.start();
			if(shouldContinue)
			{
				mHandler.postDelayed(this, period);
			}
		}
	}
}