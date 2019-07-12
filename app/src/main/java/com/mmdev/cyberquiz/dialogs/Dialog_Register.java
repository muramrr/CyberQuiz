//package com.mmdev.cyberquiz.dialogs;
//
//import android.app.Dialog;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.mmdev.cyberquiz.Activity_Welcome;
//import com.mmdev.cyberquiz.R;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
//
///**
// * Created by Anderson on 24.09.2017.
// */
//public class Dialog_Register extends SupportBlurDialogFragment
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
//        View Dialog_Register = inflater.inflate(R.layout.dialog_register, null);
//        builder.setView(Dialog_Register);
//        Dialog d = builder.create();
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        return d;
//    }
//
//    @Override
//    public void onStart ()
//    {
//        super.onStart();
//        final AlertDialog registrationActivity = (AlertDialog) getDialog();
//        if (registrationActivity != null)
//        {
//            final EditText mReg_name = registrationActivity.findViewById(R.id.reg_name);
//            final EditText mReg_email = registrationActivity.findViewById(R.id.reg_email);
//            final EditText mReg_pass = registrationActivity.findViewById(R.id.reg_pass);
//            Button mRegister_but = registrationActivity.findViewById(R.id.register_but) ;
//            mRegister_but.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick (View view)
//                {
//                    String mReg_name_str = mReg_name.getText().toString();
//                    String mReg_email_str = mReg_email.getText().toString();
//                    String mReg_pass_str = mReg_pass.getText().toString();
//
//                    if (TextUtils.isEmpty(mReg_name_str))
//                    {
//                        mReg_name.setError("Enter correct login");
//                        return;
//                    }
//                    if ((!mReg_email_str.contains("@")) || TextUtils.isEmpty(mReg_email_str) || (!mReg_email_str.contains(".")))
//                    {
//                        mReg_email.setError("Enter correct email");
//                        return;
//                    }
//                    if (TextUtils.isEmpty(mReg_pass_str) || (mReg_pass_str.length() < 6))
//                    {
//                        mReg_pass.setError("Enter correct password (at least 6 sym.)");
//                        return;
//                    }
//
//                    ((Activity_Welcome) getActivity()).createAccount(mReg_name_str, mReg_email_str, mReg_pass_str,true);
//                    Dialog_Register.this.getDialog().cancel();
//                }
//            });
//        }
//    }
//
//    @Override
//    protected float getDownScaleFactor ()
//    {
//        // Allow to customize the down dialog_scale factor.
//        return 5;
//    }
//    @Override
//    protected int getBlurRadius ()
//    {
//        // Allow to customize the blur radius factor.
//        return 15;
//    }
//    @Override
//    protected boolean isActionBarBlurred ()
//    {
//        // Enable main_or disable the blur effect on the action bar.
//        // Disabled by default.
//        return true;
//    }
//    @Override
//    protected boolean isDimmingEnable ()
//    {
//        // Enable main_or disable the dimming effect.
//        // Disabled by default.
//        return true;
//    }
//    @Override
//    protected boolean isRenderScriptEnable ()
//    {
//        // Enable main_or disable the use of RenderScript for blurring effect
//        // Disabled by default.
//        return true;
//    }
//
//
//}
