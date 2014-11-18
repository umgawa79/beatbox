package com.beatboxmetronome;

import java.util.List; 
import java.util.Vector;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TempoSectionsAdapter extends ArrayAdapter<Vector<Integer>>
{
	private Context c;
    private int id;
    private List<Vector<Integer>>sections;
	
	public TempoSectionsAdapter(Context context, int textViewResourceId, List<Vector<Integer>> objects)
	{
        super(context, textViewResourceId, objects);
		Log.e("BeatBox", "loadList onCreate!");
        c = context;
        id = textViewResourceId;
        sections = objects;
	}
	
	public Vector<Integer> getTemplate(int i)
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
		int beats = (Integer)row.elementAt(2);
		TextView tempoView = (TextView) v.findViewById(R.id.tempo);
		tempoView.setText(tempo + " bpm");
		TextView measuresView = (TextView) v.findViewById(R.id.measures);
		measuresView.setText(measures + " measures");
		TextView numBeats = (TextView) v.findViewById(R.id.num_beats);
		numBeats.setText(beats + " beats/measures");
		return v;
	}
}