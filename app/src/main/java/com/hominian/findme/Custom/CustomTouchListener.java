//package com.xnet.findme.Custom;
//
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.TextView;
//
//public class CustomTouchListener implements View.OnTouchListener {
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch(MotionEvent.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                ((TextView)v).setTextColor(); //white
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                ((TextView)v).setTextColor(0xFF000000); //black
//                break;
//        }
//        return false;
//    }
//}
