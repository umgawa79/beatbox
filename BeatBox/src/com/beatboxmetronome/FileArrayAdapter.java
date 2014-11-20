package com.beatboxmetronome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

import android.app.Activity;
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


public class FileArrayAdapter extends ArrayAdapter<Template>
{
	private Context c;
    private int id;
    private List<Template>templates;
    private List<ProgressBar>pbars;
    private ProgressBar mPBar;
    private Handler mHandler = new Handler();
    private int mProgressStatus;
    private boolean uploadInProgress = false;
    private View toDel;
    private Template toUpload;
    private boolean localList;
	
	public FileArrayAdapter(Context context, int textViewResourceId,
            List<Template> objects)
	{
        super(context, textViewResourceId, objects);
		Log.d("BeatBox", "FileArrayAdapter create!");
        c = context;
        id = textViewResourceId;
        templates = objects;
        pbars = new ArrayList<ProgressBar>();
	}
	
	public Template getTemplate(int i)
	{
		return templates.get(i);
	}
	
	public void switchMode(String s)
	{
		System.out.println("FileArray Switch Mode called with " + s);
		if (s.equals("Download")) localList = true;
		else localList = false;
		if (localList == false) System.out.println("localList set to false");
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
                        ((MainActivity)c).sendTemplateToEdit(templates.get((Integer)v.getTag()));
                        break;
                    case R.id.uploadButton :
                        System.out.println("Upload button clicked at " + v.getTag());
                        if (uploadInProgress) break; // Allow only 1 upload at a time since global variables required
                        ProgressBar pb = pbars.get((Integer)v.getTag());
                        if (pb.getVisibility()==View.VISIBLE)
                        	{
                        		// Once upload is done, allow progress bar to be hidden.
                        		pb.setVisibility(View.GONE);
                        		System.out.println("set it to gone");
                        		break;
                        	}
                        else pb.setVisibility(View.VISIBLE);
                        toUpload = templates.get((Integer)v.getTag());
                        mPBar = pb;
                        mProgressStatus = 0;
                        uploadInProgress = true;
                        new Thread(new Runnable() {
                            public void run() {
                                while (mProgressStatus < 100) {
                                    mProgressStatus+=5;
                                    try {
                                    	Thread.sleep(100);
                                    }
                                    catch(InterruptedException e) {
                                    	System.out.println("interuppted");
                                    }
                                    // Update the progress bar
                                    mHandler.post(new Runnable() {
                                        public void run() {
                                            mPBar.setProgress(mProgressStatus);
                                        }
                                    });
                                }
                                try
                        		{
                                	if (localList) {
                                		System.out.println("Uploading!");
                                		toUpload.uploadTemplate();
                                		// TODO: Need to notify edit screen to update its list...
                                	}
                                	else {
                                		System.out.println("Downloading!");
                                		toUpload.downloadTemplate();
                                		((Activity)c).runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                ((MainActivity)c).getEditFragment().updateTemplatesList();
                                            }
                                        });
                                	}
                        		}
                        		catch(IOException e)
                        		{
                        			e.printStackTrace();
                        			System.out.println("failed to upload");
                        		}
                                uploadInProgress = false;
                            }
                        }).start();
                        System.out.println("end of upload button");
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
                                ProgressBar pb = pbars.get((Integer)toDel.getTag());
                                pbars.remove(pb);
                                pbars = new ArrayList<ProgressBar>();//reset to prevent adding errors
                                notifyDataSetChanged();
                                ((MainActivity)c).getEditFragment().updateTemplatesList();
                    	    }
                    	});
                    	builder.setNegativeButton("No", null); //Do nothing on no
                    	builder.show();
                        break; 
                    }

                }
            };
            uploadInProgress = false;
            ImageButton edit = (ImageButton) v.findViewById(R.id.editButton);
            edit.setOnClickListener(mClickListener);
            edit.setTag(position);
            edit.setVisibility(View.VISIBLE);
            ImageButton upload = (ImageButton) v.findViewById(R.id.uploadButton);
            upload.setImageResource(R.drawable.ic_upload);
            upload.setOnClickListener(mClickListener);
            upload.setTag(position);
            ImageButton delete = (ImageButton) v.findViewById(R.id.deleteButton);
            delete.setOnClickListener(mClickListener);
            delete.setTag(position);
            delete.setVisibility(View.VISIBLE);
            ProgressBar pb = (ProgressBar) v.findViewById(R.id.progressBar1);
            if (localList == false) 
            {
            	System.out.println("getView localList is false");
            	upload.setImageResource(R.drawable.ic_download); // trick for now is to just reuse upload
            	edit.setVisibility(View.GONE);
            	delete.setVisibility(View.GONE);
            }
            if (pb.getVisibility()==View.VISIBLE && !pbars.contains(pb)) 
            	{
            		pb.setVisibility(View.GONE);
            	}
            if (!pbars.contains(pb)) pbars.add(pb);
        } 
        return v;
	}
}