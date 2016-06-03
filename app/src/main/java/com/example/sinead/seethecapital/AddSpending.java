package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



//Add Spending Money item to Database_Table_Spending. Displayed as dialog box
public class AddSpending extends Activity {

    //Declare class variables so they can be used by many methods in this class
    Button saveSpendingItem, cancelSpendingItem;
    EditText enterSpending, enterAmount;    //user defined item + price input boxes
    private String destName = "";           //holds the name of the chosen destination (sent by intent extra)


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);   //displayed as a dialog box
        this.setFinishOnTouchOutside(false);                                //user must press cancel or save button to exit dialog box
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        saveSpendingItem=(Button)findViewById(R.id.saveSpendingButton);
        cancelSpendingItem =(Button)findViewById(R.id.cancButton);
        enterSpending= (EditText)findViewById(R.id.enterSpending);
        enterAmount= (EditText)findViewById(R.id.enterAmount);

        Bundle holidayData = getIntent().getExtras();                       //receive the extra information passed through intent
        destName = holidayData.getString("holidayName");                    //save extra information in class variable

    }



    //when save button is clicked, add the new information to the Spending Money table in the database
    public void addSpendingItem(View view) {
        try{
            String item = enterSpending.getText().toString();   //get String version of the user spending item input
            String amount = enterAmount.getText().toString();   //get String version of the user spending item amount input

            Holiday entry = new Holiday(AddSpending.this);      //make a new instance of the database
            entry.open();                                       //open the database
            String holID = entry.getHolidayID(destName);        //get the holiday ID of the current holiday profile
            entry.addSpending(item, amount, holID);             //add the shopping item into the Shopping list table (with holiday ID so they can be displayed seperately)
                                                                //^^see the getSpendingDB() method in Holiday.java for more information
            entry.close();                                      //close the database

        } catch (Exception e) {                                 //incase there is an error, make a dialog pop up with the error
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }
        Intent intent = new Intent(this, HolidaySpendingMoney.class);   //After the information has been added to the database, go back to the HolidaySpendingMoney page
        intent.putExtra("holidayName", destName);                       //send the extra information of the holiday name with the intent
        startActivity(intent);
        finish();                                                       //Finish the AddShopping activity
    }


    //when cancel button is clicked, go back to HolidaySpendingMoney without the database being updated
    public void backToSpending(View view){
        Intent intent = new Intent(this, HolidaySpendingMoney.class);
        intent.putExtra("holidayName", destName);
        startActivity(intent);
        finish();
    }
}



