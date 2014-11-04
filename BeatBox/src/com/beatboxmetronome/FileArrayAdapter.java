package com.beatboxmetronome;

import java.util.List; 
import android.content.Context;
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
                        // Remove the template from the list... maybe make a delete function for templates.
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