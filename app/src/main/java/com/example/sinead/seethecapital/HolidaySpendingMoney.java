package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


//This keeps track of the items the user needs to budget for while away on holidays
public class HolidaySpendingMoney extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    //Declare class variables so they can be used by many methods in this class
    Button deleteSpendingButton;
    TextView tv;
    EditText spendingIdInput;               //user item ID input
    private String destName = "";           //holds the name of the chosen destination (sent by intent extra)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_spending_money);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);   //stops the keyboard from automatically appearing

        Bundle holidayData = getIntent().getExtras();                                   //receive the extra information passed through intent
        destName = holidayData.getString("holidayName");                                //save extra information in class variable

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //sets the navigation side drawer
        navigationView.setNavigationItemSelectedListener(this);


        deleteSpendingButton = (Button)findViewById(R.id.deleteSpendingButton);
        spendingIdInput = (EditText)findViewById(R.id.spendingIdInput);
        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.spendingmoney);    //link to xml file to change to custom background
        tv = (TextView)findViewById(R.id.showSpendingDB);                               //will show the Spending Money table

        Holiday info = new Holiday (this);                                              //make an instance of the database
        info.open();                                                                    //open the database
        String data = info.getSpendingDB(destName);                                     //get the items in the Spending Money table (the ones that correspond to the selected holiday only)
        int theme = info.getThemeID(destName);                                          //get the theme ID for the selected holiday
        info.close();                                                                   //close the database
        tv.setText(data);                                                               //output the Spending Money items for the selected holiday
        switch(theme){                                                                  //use a switch case to set the custom background of the selected holiday
            case 0:
                relLayout.setBackgroundResource(R.mipmap.beach_main);
                break;
            case 1:
                relLayout.setBackgroundResource(R.mipmap.snow_main);
                break;
            case 2:
                relLayout.setBackgroundResource(R.mipmap.city_main);
                break;
            case 3:
                relLayout.setBackgroundResource(R.mipmap.country_main);
                break;
            case 4:
                relLayout.setBackgroundResource(R.mipmap.main_bg);
                break;
        }
    }




    //go to the add spending dialog when the Add Spending item button is clicked
    public void toAddSpending(View view) {
        Intent intent = new Intent("com.example.sinead.seethecapital.AddSpending");     //brings the user to a dialog box (AddSpending.java)
        intent.putExtra("holidayName", destName);                                       //attaches the extra information to be carried with the intent
        startActivity(intent);
        finish();                                                                       //finish HolidaySpendingMoney activity
    }



    //delete from the spending money list when the user inputs an item ID and clicks the delete button
    public void deleteSpending(View view){
        try{
            String deleteItem = spendingIdInput.getText().toString();                   //get the user input
            int itemID = Integer.parseInt(deleteItem);                                  //convert the String input into an int
            Holiday info = new Holiday(this);                                           //make an instance of the database
            info.open();                                                                //open the database
            info.deleteEntrySpending(itemID);                                           //delete the specific item from the database
            String data = info.getSpendingDB(destName);                                 //get an updated version of the Spending Money table
            info.close();                                                               //close the database
            tv.setText(data);                                                           //output the new version of the Shopping List

        } catch (Exception e) {                                                         //incase there is an error, show the error in a dialog box
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Uh-oh!");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }

    }


    //when the spending money question mark is clicked, show the user some information about what to do
    public void spendingDialog (View view){
        AlertDialog alertDialog = new AlertDialog.Builder(HolidaySpendingMoney.this).create();        //create a new alert dialog
        alertDialog.setTitle("Your Spending Money");                                                       //set the dialog title and text
        alertDialog.setMessage("This is where you can keep track of things to budget for while you are away (e.g. food, drink, sightseeing etc.) Simply click the Add button and fill in the details.");
        alertDialog.setIcon(R.mipmap.qmark);                                                        //set an icon for the dialog
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {                         //when the "OK" button is clicked, close the box + do nothing else
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }







    //Navigation drawer code

    //auto-generated by Android Studio
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //auto-generated by Android Studio
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.holiday_profile_page, menu);
        return true;
    }

    //auto-generated by Android Studio
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //auto-generated by Android Studio
    @SuppressWarnings("StatementWithEmptyBody")


    //This controls what happens when an item in the Navigation Drawer is clicked
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.shopping_list) {                                                             //if shopping list is clicked
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayShoppingList");     //go to HolidayShoppingList.java
            intent.putExtra("holidayName", destName);                                               //send extra information with the intent
            startActivity(intent);
            finish();                                                                               //finish HolidaySpendingMoney.java
        } else if (id == R.id.spending_money) {                                                     //do nothing if its own page is clicked

        } else if (id == R.id.bank_account) {                                                       //^^the above comments about shopping list apply to the rest of the 'else if' statements
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidaySavingStatus");
            intent.putExtra("holidayName", destName);
            startActivity(intent);
            finish();
        } else if (id == R.id.change_theme) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayChangeTheme");
            intent.putExtra("holidayName", destName);
            startActivity(intent);
            finish();
        } else if (id == R.id.holiday_profile) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayProfilePage");
            intent.putExtra("holidayName", destName);
            startActivity(intent);
            finish();
        } else if (id == R.id.main_menu) {
            Intent intent = new Intent("com.example.sinead.seethecapital.MainMenu");
            startActivity(intent);
            finish();
        } else if (id == R.id.holiday_location) {
            Intent intent = new Intent("com.example.sinead.seethecapital.HolidayLocation");
            intent.putExtra("holidayName", destName);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);                                                    //close the drawer
        return true;                                                                                //return that the action is completed
    }


}


