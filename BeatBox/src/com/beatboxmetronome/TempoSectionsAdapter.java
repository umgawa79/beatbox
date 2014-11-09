package com.beatboxmetronome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


public class TempoSectionsAdapter extends ArrayAdapter<Vector>
{
	private Context c;
    private int id;
    private List<Vector>sections;
	
	public TempoSectionsAdapter(Context context, int textViewResourceId,
            List<Vector> objects)
	{
        super(context, textViewResourceId, objects);
		Log.e("BeatBox", "loadList onCreate!");
        c = context;
        id = textViewResourceId;
        sections = objects;
	}
	
	public Vector getTemplate(int i)
	{
		// Returns the section/row.
		return sections.get(i);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
		
		Vector<Integer> row = getTemplate(position);
		int tempo = (Integer)row.elementAt(0);
		int measures = (Integer)row.elementAt(1);
		int timesig = (Integer)row.elementAt(2);
		TextView tempoView = (TextView) v.findViewById(R.id.tempo);
		tempoView.setText(tempo + " bpm");
		TextView measuresView = (TextView) v.findViewById(R.id.measures);
		measuresView.setText(measures + " measures");
		return v;
	}
}