package com.beatboxmetronome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.content.DialogInterface;
import android.os.Bundle;

public class TempoDialog extends DialogFragment
{
	public interface TempoDialogListener
	{
        public void onDialogPositiveClick(DialogFragment dialog);
    }
	
	private TempoDialogListener mListener;
	public String mTempoText;

	// Override the Fragment.onAttach() method to instantiate the TempoDialogListener
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try
		{
			mListener = (TempoDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement TempoDialogListener");
		}
	}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        builder.setView(inflater.inflate(R.layout.tempo_dialog_layout, null))
        .setMessage("Set New Tempo")
               .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface d, int id)
                   {
                	   mListener.onDialogPositiveClick(TempoDialog.this);
                   }
               })
               .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog, do nothing
                   }
               });
        return builder.create();
    }
}