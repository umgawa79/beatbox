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

public class FileArrayAdapter extends ArrayAdapter<Template>
{
	private Context c;
    private int id;
    private List<Template>templates;
    View toDel;
	
	public FileArrayAdapter(Context context, int textViewResourceId,
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
            OnClickListener mClickListener = new OnClickListener() {

                public void onClick(View v) {
                    switch(v.getId())
                    {
                    case R.id.editButton :
                        System.out.println("Edit button clicked at " + v.getTag());
                        // can send templates<position> to edit screen here.
                        break;
                    case R.id.uploadButton :
                        System.out.println("Upload button clicked at " + v.getTag());
                        // Begin the upload process here...
                        break;  
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
            ImageButton edit = (ImageButton) v.findViewById(R.id.editButton);
            edit.setOnClickListener(mClickListener);
            edit.setTag(position);
            ImageButton upload = (ImageButton) v.findViewById(R.id.uploadButton);
            upload.setOnClickListener(mClickListener);
            upload.setTag(position);
            ImageButton delete = (ImageButton) v.findViewById(R.id.deleteButton);
            delete.setOnClickListener(mClickListener);
            delete.setTag(position);
        } 
        return v;
	}
}