package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


//Delete a holiday from the database after the user inputs the holiday ID + presses delete
public class DeleteHolidayActivity extends Activity {

    //Declare class variables so they can be used by many methods in this class
    TextView tv;                            //shows the user information from the holiday database
    Button deleteHolidayButton;
    EditText holidayIDInput;                //user input of the holiday ID



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_holiday);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);   //stops the keyboard from automatically appearing

        deleteHolidayButton = (Button)findViewById(R.id.deleteHolidayButton);
        holidayIDInput = (EditText)findViewById(R.id.holidayIDInput);
        tv = (TextView)findViewById(R.id.showDB);

        Holiday info = new Holiday (this);                              //make a new instance of the database
        info.open();                                                    //open the database
        String data = info.getDeleteDB();                               //call a method to get information from the database
        tv.setText(data);                                               //print this information to the screen
        info.close();                                                   //close the database
    }



    //when delete button is clicked, the holiday which corresponds to the inputted holiday ID is removed from the database
    public void deleteHoliday(View view){
        try{
            String SholidayRow = holidayIDInput.getText().toString();   //get String version of the user holiday ID input
            int IholidayRow = Integer.parseInt(SholidayRow);            //convert that String to an int
            Holiday info = new Holiday(this);                           //make a new instance of the database
            info.open();                                                //open the database
            info.deleteEntry(IholidayRow);                              //call a method to remove the specified holiday from the database
            info.close();                                               //close the database

        } catch (Exception e) {                                         //incase there is an error, make a dialog pop up with the error
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }
        Intent intent = new Intent("com.example.sinead.seethecapital.MainMenu");    //After the information has been added to the database, go back to the MainMenu page
        startActivity(intent);
        finish();                                                                   //finish the DeleteHolidayActivity
    }


    //Clears "Holiday ID" from the editText box so the user doesnt have to manually do it
    public void clearBox(View view){
        holidayIDInput.setText("");
    }

}

