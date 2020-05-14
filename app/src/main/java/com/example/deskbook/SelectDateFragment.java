package com.example.deskbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button btnOk;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        String month2, day2;
        if (day < 10) {
            day2 = "0" + day;
        } else {
            day2 = Integer.toString(day);
        }
        if (month < 10) {
            month2 = "0" + month;
        } else {
            month2 = Integer.toString(month);
        }
        MainFragmentActivity.bookDate = day2 + "-" + month2 + "-" + year;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        int compare = currentDate.compareTo(MainFragmentActivity.bookDate);
        if (compare < 0 || compare == 0){
            Intent I = new Intent(getActivity().getApplicationContext(), SortWorkspaceDialog.class);
            startActivity(I);
        }
        else {
            Toast.makeText(getActivity(), "Current Date exceeds booking date", Toast.LENGTH_LONG).show();
        }
    }

}

//    private int mYear, mMonth, mDay;
//    private int check;
//    DatePickerDialog datePickerDialog;
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        datePickerDialog = new DatePickerDialog(getActivity().getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year,
//                                  int monthOfYear, int dayOfMonth) {
//                String month, day, y;
//                if (dayOfMonth < 10) {
//                    day = "0" + dayOfMonth;
//                } else {
//                    day = Integer.toString(dayOfMonth);
//                }
//                if (monthOfYear < 10) {
//                    month = "0" + (monthOfYear + 1);
//                } else {
//                    month = Integer.toString(monthOfYear + 1);
//                }
//                y = Integer.toString(year);
//                MainFragmentActivity.bookDate = year + "-" + month + "-" + day;
//                check = 1;
//            }
//        }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface arg0) {
//                // do something onDismiss
//                if (check == 1) {
//                    Intent I = new Intent(getActivity().getApplicationContext(), SortWorkspaceDialog.class);
//                    startActivity(I);
//                    System.out.println("book date : " + MainFragmentActivity.bookDate);
//                }
//            }
//        });
//    }

