package com.beatboxmetronome;

import java.util.List; 

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditTabArrayAdapter extends ArrayAdapter<Template>
{
	private Context c;
    private int id;
    private List<Template>templates;
    View toDel;
	
	public EditTabArrayAdapter(Context context, int textViewResourceId,
            List<Template> objects)
	{
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        templates = objects;
	}
	
	public Template getTemplate(int i)
	{
		return templates.get(i);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		System.out.println("getView start");
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        
        final Template t = templates.get(position);
        if (t != null) {
        	TextView name = (TextView) v.findViewById(R.id.song_name_button);
        	name.setText(t.getTemplateName());
        	
        	/*
            OnClickListener mClickListener = new OnClickListener() {
            	
                public void onClick(View v) {
                	
                    switch(v.getId())
                    {
                    case R.id.deleteButton :
                        System.out.println("Delete button clicked at " + v.getTag());
                        toDel = v;
                        Template templateToDelete = templates.get((Integer)toDel.getTag());
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    	builder.setTitle("Delete "+templateToDelete.getTemplateName());
                    	builder.setMessage("Are you sure?");
                    	//.setIcon(android.R.drawable.ic_dialog_alert)
                    	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    	    public void onClick(DialogInterface dialog, int which) {			      	
                    	    	//Yes button clicked, do something
                    	    	Template templateToDelete = templates.get((Integer)toDel.getTag());
                                templateToDelete.deleteTemplate();
                                templates.remove(templateToDelete);
                                notifyDataSetChanged();
                    	    }
                    	});
                    	builder.setNegativeButton("No", null); //Do nothing on no
                    	builder.show();
                        break; 
                    }

                }
            };
            ImageButton delete = (ImageButton) v.findViewById(R.id.deleteButton);
            delete.setOnClickListener(mClickListener);
            delete.setTag(position);
            */
            
        } 
        return v;
	}
}