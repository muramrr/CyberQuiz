package com.mmdev.cyberquiz.dialogs;
//
//import android.app.Dialog;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.mmdev.cyberquiz.R;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
//import info.abdolahi.CircularMusicProgressBar;
//
///**
// * Created by Anderson on 28.09.2017.
// */
//
//public class Dialog_Profile extends SupportBlurDialogFragment
//{
//
//    @Override
//    public void onCreate (Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog (Bundle savedInstanceState)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View Dialog_Profile = inflater.inflate(R.layout.dialog_profile, null);
//        CircularMusicProgressBar mCircularMusicProgressBar=(CircularMusicProgressBar)Dialog_Profile.findViewById(R.id.album_art);
//        mCircularMusicProgressBar.setValue((float) 25);
//        //getuserinfo and update fields
//        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        //TextView acount_name=(TextView)Dialog_Profile.findViewById(R.id.account_name);
//        //acount_name.setText(user.getDisplayName());
//        //.
//        builder.setView(Dialog_Profile);
//        Dialog d = builder.create();
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        return d;
//    }
//
//    @Override
//    protected float getDownScaleFactor ()
//    {
//        // Allow to customize the down dialog_scale factor.
//        return 5;
//    }
//
//    @Override
//    protected int getBlurRadius ()
//    {
//        // Allow to customize the blur radius factor.
//        return 15;
//    }
//
//    @Override
//    protected boolean isDimmingEnable ()
//    {
//        // Enable main_or disable the dimming effect.
//        // Disabled by default.
//        return true;
//    }
//
//    @Override
//    protected boolean isRenderScriptEnable ()
//    {
//        // Enable main_or disable the use of RenderScript for blurring effect
//        // Disabled by default.
//        return true;
//    }
//
//}
