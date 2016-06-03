package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;


//Add holiday to the Database_Table
public class AddHolidayActivity extends Activity {

    //Declare class variables so they can be used by many methods in this class
    EditText destinationInput, flightPriceInput;        //user input boxes

    CalendarView deptCalendar, retCalendar;             //Calendars used to let user choose departure + return dates with ease
    String deptYear ="";
    String deptMonth="";
    String deptDay="";
    String retYear="";
    String retMonth="";
    String retDay="";

    private Spinner spinner;                            //Spinner used for picking the holiday theme
    private String[] themes;                            //String array used to populate the spinner
    int finalTheme;                                     //Holds the users theme choice

    Button submitButton;                                //Save all the new information



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);   //stops the keyboard from automatically appearing

        submitButton = (Button)findViewById(R.id.submitButton);
        destinationInput = (EditText)findViewById(R.id.destinationInput);
        flightPriceInput = (EditText)findViewById(R.id.flightPriceInput);

        //See Calendar methods below
        initializeDeptCalendar();
        initializeRetCalendar();


        //Theme Picker code
        themes = getResources().getStringArray(R.array.theme_list);     //see strings.xml to see the string array
        spinner = (Spinner)findViewById(R.id.theme_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, themes); //how the array will be displayed
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);        //populate the spinner with the string array

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){           //when a theme is selected, its position in the array is saved to the variable finalTheme (easy to input into database)
                    case 0:                 //if the first string is selected
                        finalTheme=0;
                        break;
                    case 1:                 //if the second string is selected
                        finalTheme=1;
                        break;
                    case 2:                 //if the third string is selected
                        finalTheme=2;
                        break;
                    case 3:                 //if the fourth string is selected
                        finalTheme=3;
                        break;
                    case 4:                 //if the fifth string is selected
                        finalTheme=4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //if nothing is selected, do nothing
                //this method will never be called but it is a necessary override method for 'OnItemSelectedListener()'
            }
        });

    }



    //Calendar code
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initializeDeptCalendar() {     //set the first calendar
        deptCalendar=(CalendarView)findViewById(R.id.departureCalendar);    //find the first calendar (departureCalendar)
        deptCalendar.setShowWeekNumber(false);                              //dont want it to show week number
        deptCalendar.setFirstDayOfWeek(2);                                  //let the first day of the week be Monday
        deptCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) { //when a date is clicked by the user
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show(); //show toast of the clicked date

                //get departure year and set it in class variable
                deptYear = ""+year;

                //get departure month and set it in class variable
                String mon = ""+(month+1);      //unsure why, but when a month was clicked it always showed as the month previous (therefore +1 to it)
                if(mon.length()==1){            //if the month chosen is 1-9, pad it with a zero beforehand
                    deptMonth= "0"+(month+1);
                }else {
                    deptMonth = "" + (month+1);
                }

                //get departure day and set it in class variable
                String day = ""+dayOfMonth;
                if(day.length()==1){            //if the day chosen is 1-9, pad it with a zero beforehand
                    deptDay= "0"+dayOfMonth;
                }else{
                    deptDay = ""+dayOfMonth;
                }
            }
        });

    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initializeRetCalendar() {      //set the second calendar (almost identical to setting the first calendar)
        retCalendar=(CalendarView)findViewById(R.id.returnCalendar);        //find the second calendar (returnCalendar)
        retCalendar.setShowWeekNumber(false);                               //dont want to show week number
        retCalendar.setFirstDayOfWeek(2);                                   //let the first day of the week be Monday
        retCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {   //when a date is clicked by the user
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show(); //show toast of the clicked date

                //get return year and set it in class variable
                retYear = ""+year;

                //get return month and set it in class variable
                String mon = ""+(month+1);      //same problem as first calendar with month+1
                if(mon.length()==1){            //if the month chosen is 1-9, pad it with a zero beforehand
                    retMonth= "0"+(month+1);
                }else{
                    retMonth = ""+(month+1);
                }

                //get return day and set it in class variable
                String day = ""+dayOfMonth;
                if(day.length()==1){            //if the day chosen is 1-9, pad it with a zero beforehand
                    retDay= "0"+dayOfMonth;
                }else{
                    retDay = ""+dayOfMonth;
                }
            }
        });

    }



    //when save button is clicked, add all the new information into the database
    public void addHoliday(View view) {
        try{                                                                //when successful
            String destination = destinationInput.getText().toString();     //get String version of the user destination input
            String flightPrice = flightPriceInput.getText().toString();     //get String version of the user travel price input
            String theme = finalTheme + "";                                 //convert finalTheme to a string (so it can be added easily to the database)

            Holiday entry = new Holiday(AddHolidayActivity.this);           //make a new instance of the database
            entry.open();                                                   //open the database
            entry.addHoliday(destination, deptDay, deptMonth, deptYear, retDay, retMonth, retYear, theme, flightPrice); //call this method to add a holiday into the database
            entry.close();                                                  //close the database

        } catch (Exception e) {                                             //incase there is an error, make a dialog box pop up with the error
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }
        Intent intent = new Intent(this, MainMenu.class);                   //After the information has been added to the database, go back to the Main Menu page
        startActivity(intent);
        finish();                                                           //finish the AddHolidayActivity
    }



    //when cancel button is clicked, go back to the Main Menu without the database being updated
    public void backToMainMenu(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }


    //when the travel price question mark is clicked, show the user some information about what to enter
    public void priceDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(AddHolidayActivity.this).create();        //create a new alert dialog
        alertDialog.setTitle("Travel Price");                                                       //set the dialog title and text
        alertDialog.setMessage("This is the amount that you still need to spend on travel. If all your travel expenses are paid off, just enter 0.");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

}
