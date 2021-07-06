package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ViewDialogDashboard {
    DataBaseHelper db;

    MainActivity ma;
    //RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnDisplay;
    RadioGroup radioGroup;
    EditText input_name,input_description;
    int Catg_Type;

    public ViewDialogDashboard(DataBaseHelper db,MainActivity ma)
    {
        this.ma = ma;
        this.db = db;
    }

    public void addDialog(Activity activity, String msg) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout_dashboard);

        input_name = (EditText)dialog.findViewById(R.id.input_name);
        input_description = (EditText)dialog.findViewById(R.id.input_description);

        dialog.show();

        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
        Button dialogButton = (Button) dialog.findViewById(R.id.add);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code to store data into database
                String name = input_name.getText().toString();
                String description = input_description.getText().toString();

                //Cat_type stores the type(List,Meeting,etc)
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) v.findViewById(selectedId);
                final String Catg_type_name = ((RadioButton) dialog.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                if(Catg_type_name.equals("List"))
                    Catg_Type = 1;
                else if (Catg_type_name.equals("Inventory"))
                    Catg_Type = 2;
                else if(Catg_type_name.equals("Meeting"))
                    Catg_Type = 3;
                else if(Catg_type_name.equals("Images"))
                    Catg_Type = 4;
                db.insertCategory(name,description,Catg_Type);
                dialog.dismiss();
                ma.viewCategories();
            }
        });
        Button dialogButton2 = (Button) dialog.findViewById(R.id.subtract);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    public void editDialog(Activity activity, String original_name,String original_descp) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout_dashboard2);

        input_name = (EditText)dialog.findViewById(R.id.input_name);
        input_description = (EditText)dialog.findViewById(R.id.input_description);

        dialog.show();
        input_name.setText(original_name);
        input_description.setText(original_descp);
        Button dialogButton = (Button) dialog.findViewById(R.id.edit);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code to store data into database
                String new_name = input_name.getText().toString();
                String new_description = input_description.getText().toString();
                db.editCategory(original_name,new_name,new_description);
                dialog.dismiss();
                ma.viewCategories();

            }
        });
        Button dialogButton2 = (Button) dialog.findViewById(R.id.subtract);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}

