package com.example.sinead.seethecapital;
//Commented by Sinead O'Rourke


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.AdapterView;
import java.util.LinkedList;

/**
 * Created by Sinéad on 22/12/2015.
 */
//THE DATABASE
public class Holiday {

    //General Database variables
    private static final String DATABASE_NAME = "Holiday";
    private static final int DATABASE_VERSION = 1;
    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    //Holiday table variables/"columns"
    private static final String DATABASE_TABLE = "holidayTable";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DESTINATION = "_destination";
    public static final String KEY_DEPARTURE_DAY = "_departureDay";
    public static final String KEY_DEPARTURE_MONTH = "_departureMonth";
    public static final String KEY_DEPARTURE_YEAR = "_departureYear";
    public static final String KEY_RETURN_DAY = "_returnDay";
    public static final String KEY_RETURN_MONTH= "_returnMonth";
    public static final String KEY_RETURN_YEAR= "_returnYear";
    public static final String KEY_FLIGHT = "_flight";
    public static final String KEY_THEME = "_theme";

    //Bank table variable/"column"
    private static final String DATABASE_BANK = "bankTable";
    public static final String KEY_BANK_TOTAL = "_total";

    //Shopping List table variables/"columns"
    private static final String DATABASE_TABLE_SHOPPING = "shoppingTable";
    public static final String KEY_ROWID_SHOP = "_id";
    public static final String KEY_ITEM_SHOP = "_item";
    public static final String KEY_PRICE_SHOP = "_price";
    public static final String KEY_SHOP_HOLIDAY = "_holidayId";

    //Spending Money table variables/"columns"
    private static final String DATABASE_TABLE_SPENDING = "spendingTable";
    public static final String KEY_ROWID_SPEND = "_idSpend";
    public static final String KEY_ITEM_SPEND = "_itemSpend";
    public static final String KEY_AMOUNT_SPEND = "_amountSpend";
    public static final String KEY_SPEND_HOLIDAY = "_spendHolidayId";





    private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +                     //SQL CREATE statement for Holiday table
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_DESTINATION + " VARCHAR(15) NOT NULL, " +
                    KEY_DEPARTURE_DAY + " INTEGER NOT NULL, " +
                    KEY_DEPARTURE_MONTH + " INTEGER NOT NULL, " +
                    KEY_DEPARTURE_YEAR + " INTEGER NOT NULL, " +
                    KEY_RETURN_DAY + " INTEGER NOT NULL, " +
                    KEY_RETURN_MONTH + " INTEGER NOT NULL, " +
                    KEY_RETURN_YEAR + " INTEGER NOT NULL, " +
                    KEY_THEME + " INTEGER NOT NULL, " +
                    KEY_FLIGHT + " INTEGER NOT NULL" + "); ");

            db.execSQL(" CREATE TABLE " + DATABASE_BANK + " ("+                     //SQL CREATE statement for Bank table
                    KEY_BANK_TOTAL + " INTEGER NOT NULL PRIMARY KEY);");

            db.execSQL("INSERT INTO " + DATABASE_BANK + " (" + KEY_BANK_TOTAL + ") VALUES (" + 0 + ");");   //SQL INSERT statement for Bank table (set to 0 initially)

            db.execSQL(" CREATE TABLE " + DATABASE_TABLE_SHOPPING + " (" +          //SQL CREATE statement for Shopping List table
                    KEY_ROWID_SHOP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_ITEM_SHOP + " VARCHAR(20) NOT NULL, " +
                    KEY_PRICE_SHOP + " INTEGER NOT NULL, " +
                    KEY_SHOP_HOLIDAY + " INTEGER NOT NULL); ");

            db.execSQL(" CREATE TABLE " + DATABASE_TABLE_SPENDING + " (" +          //SQL CREATE statement for Spending Money table
                    KEY_ROWID_SPEND + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_ITEM_SPEND + " VARCHAR(20) NOT NULL, " +
                    KEY_AMOUNT_SPEND + " INTEGER NOT NULL, " +
                    KEY_SPEND_HOLIDAY + " INTEGER NOT NULL); ");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }





                            //General Database methods

    //constructor
    public Holiday(Context c){
        ourContext=c;
    }

    public Holiday(AdapterView.OnItemClickListener onItemClickListener) {
        ourContext= (Context) onItemClickListener;
    }

    //open the database
    public Holiday open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close(){
        ourHelper.close();
    }








                            //Holiday methods

    //Take in a destination name and return the ID of that holiday
    public String getHolidayID(String holidayName)   {
        String[] columns = new String[]{KEY_ROWID, KEY_DESTINATION};        //String array of the two columns/variables needed
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DESTINATION + " LIKE '%" + holidayName +"%'", null, null, null, null); //similar to a SELECT query in SQL [WHERE key_destination LIKE '%holidayname%';]
        if( c!=null){                                                       //as long as the cursor is pointing at a row it will keep searching
            c.moveToFirst();                                                //cursor goes to the first row in the result set (although there should only be one row in result set)
            String id = c.getString(0);                                     //cursor gets the String version of the element in position 0 of the array (in this case, KEY_ROWID)
            return id;                                                      //return the ID
        }
        return null;                                                        //return null if a holiday is not found
    }


    //Takes in Strings and adds this information as a new holiday into the holiday table (method called in AddHolidayActivity.java)
    public long addHoliday(String destination, String departureDay, String departureMonth, String departureYear, String returnDay, String returnMonth, String returnYear, String theme, String flight){
        ContentValues cv = new ContentValues();                             //ContentValues allows you to insert many elements into the one row at the same time
        cv.put(KEY_DESTINATION, destination);                               //add destination into the ContentValues container (see more inputs below)
        cv.put(KEY_DEPARTURE_DAY, departureDay);
        cv.put(KEY_DEPARTURE_MONTH, departureMonth);
        cv.put(KEY_DEPARTURE_YEAR, departureYear);
        cv.put(KEY_RETURN_DAY, returnDay);
        cv.put(KEY_RETURN_MONTH, returnMonth);
        cv.put(KEY_RETURN_YEAR, returnYear);
        cv.put(KEY_THEME, theme);
        cv.put(KEY_FLIGHT, flight);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);                //SQL INSERT statement to put everything from the ContentValues into the Holiday table
    }


    //Takes in a holiday ID and deletes the holiday that corresponds with it
    public void deleteEntry(int LRow1) throws SQLException {
        ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + " = " + LRow1, null);    //SQL DELETE statement [DELETE FROM DATABASE_TABLE WHERE key_ROWID = LRow1;]
        ourDatabase.delete(DATABASE_TABLE_SHOPPING, KEY_SHOP_HOLIDAY + " = " + LRow1, null);  //Delete any items from the Shopping List that correspond with this holiday ID
        ourDatabase.delete(DATABASE_TABLE_SPENDING, KEY_SPEND_HOLIDAY + " = " + LRow1, null); //Delete any items from Spending Money that correspond with this holiday ID
    }


    //Get the String version of the Holiday table (method called in DeleteHolidayActvity.java)
    public String getDeleteDB() {
        String[] columns = new String[]{KEY_ROWID, KEY_DESTINATION, KEY_DEPARTURE_DAY, KEY_DEPARTURE_MONTH, KEY_DEPARTURE_YEAR, KEY_RETURN_DAY, KEY_RETURN_MONTH, KEY_RETURN_YEAR}; //String array of the variables needed
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null); //Cursor will go through the whole table (no condition)
        String result = "ID:  Holiday: " + "\n";                                             //Print out on top of holiday table

        int myRow = c.getColumnIndex(KEY_ROWID);                                             //get the column indexes for all the variables
        int myDestination = c.getColumnIndex(KEY_DESTINATION);
        int myDepartureDay = c.getColumnIndex(KEY_DEPARTURE_DAY);
        int myDepartureMonth = c.getColumnIndex(KEY_DEPARTURE_MONTH);
        int myDepartureYear = c.getColumnIndex(KEY_DEPARTURE_YEAR);
        int myReturnDay = c.getColumnIndex(KEY_RETURN_DAY);
        int myReturnMonth= c.getColumnIndex(KEY_RETURN_MONTH);
        int myReturnYear = c.getColumnIndex(KEY_RETURN_YEAR);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){                            //for loop (cursor = 0; cursor <= last element; cursor++)
            result = result + c.getString(myRow) + "   " + c.getString(myDestination) + "                    " + c.getString(myDepartureDay)+"/"+c.getString(myDepartureMonth)+"/"+c.getString(myDepartureYear)+"  -  "
                    + c.getString(myReturnDay)+"/"+c.getString(myReturnMonth)+"/"+c.getString(myReturnYear) + "\n";     //Print the String version of the holiday on individual lines
        }
        return result;                                                                      //When the loop is finished, return the output of all the holidays
    }



    //Get a String array version of all the destinations in the holiday table (method called in MainMenu.java)
    public String [] getDestination() {
        String[]columns = new String[]{KEY_DESTINATION};                                        //String array of the variable needed
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);    //Cursor will go through the whole table (no condition)
        LinkedList<String> result = new LinkedList<>();                                         //Declare new Linked List
        int myDestination = c.getColumnIndex(KEY_DESTINATION);                                  //get the column index of the holiday name

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){                                //for loop (cursor = 0; cursor <= last element; cursor++)
            result.add(c.getString(myDestination));                                             //with each loop, add a destination name on to the Linked List (better than array - unsure of how many holidays the user will input)
        }
        return result.toArray(new String[result.size()]);                                       //convert the Linked List into an array and return it
    }


    //Take in a destination name and return the String version of the departure date for that holiday (method called in HolidayProfile.java)
    public String getDepatureDate(String holidayName){
        String[] columns = new String[]{KEY_DEPARTURE_DAY, KEY_DEPARTURE_MONTH, KEY_DEPARTURE_YEAR, KEY_ROWID}; //String array of the variables needed
        String holID = getHolidayID(holidayName);                                                               //get the holiday ID of the parameter holidayName
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns,KEY_ROWID + " = " + holID, null, null, null, null ); //similar to a SELECT query in SQL [WHERE key_rowid LIKE '%holID%';]
        int myDeptDay = c.getColumnIndex(KEY_DEPARTURE_DAY);                                                    //get column indexes for all the variables needed
        int myDeptMon = c.getColumnIndex(KEY_DEPARTURE_MONTH);
        int myDeptYear = c.getColumnIndex(KEY_DEPARTURE_YEAR);
        String Ddate="";                                                                                        //String to hold the result from the cursor
        if(c!=null){
            c.moveToFirst();                                                                                    //cursor goes to the first row in the result set
            Ddate = c.getString(myDeptDay)+"/"+c.getString(myDeptMon)+"/"+c.getString(myDeptYear);              //cursor gets the String version of all the departure date parts
        }
        return Ddate;                                                                                           //return the String result
    }


    //Take in a destination name and return the String version of the travel price (method called below in getTotalHolidayAmount() method)
    public String getTravelPrice(String holidayName){
        String[] columns = new String[]{KEY_FLIGHT, KEY_ROWID};                                                 //String array of the variables needed
        String holID = getHolidayID(holidayName);                                                               //get the holiday ID of the parameter holidayName
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " = " + holID, null, null, null, null);//similar to a SELECT query in SQL [WHERE key_rowid LIKE '%holID%';]
        String price = "";
        if(c!=null){
            c.moveToFirst();                                                                                     //cursor goes to the first row in the result set
            price = c.getString(0);                                                                              //cursor gets the String version of the first element in the string array (in this case key_flight)
        }
        return price;                                                                                            //return the String result
    }


    //Take in a destination name and return the int version of the themeID for that holiday (method called in 5 java classes, e.g. HolidayProfile.java)
    public int getThemeID(String destination){
        String[] columns = new String[]{KEY_DESTINATION, KEY_THEME};                                            //String array of the variables needed
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DESTINATION + " LIKE '%" + destination +"%'", null, null, null, null); //similar to a SELECT query in SQL [WHERE key_destination LIKE '%destination%';]
        if( c!=null){
            c.moveToFirst();                                                                                    //cursor goes to the first row in the result set
            String id = c.getString(1);                                                                         //cursor gets the String version of the second element in the array (in this case, KEY_THEME)
            int themeID = Integer.parseInt(id);                                                                 //convert String to int
            return themeID;                                                                                     //return the theme ID
        }
        return -1;                                                                                              //otherwise return -1 if there was a problem
    }


    //Takes in a destination name and int. This method changes the current themeID to a newThemeID if the user changes their mind (method called in HolidayChangeTheme.java)
    public void updateTheme(int newTheme, String holidayName){
        String newThemeStr = newTheme + "";                                                                                                                         //convert the int to a string
        ourDatabase.execSQL("UPDATE " + DATABASE_TABLE + " SET " + KEY_THEME + " = " + newThemeStr + " WHERE " + KEY_DESTINATION + " LIKE '%"+holidayName+"%';");   //SQL UPDATE statement
    }











                            //Bank Account methods

    //Take in an int and update the current amount in the bank table (method called in BankActivity.java)
    public void addBankAccount(int amount){
        String[] columns = new String[]{KEY_BANK_TOTAL};                                    //String array of the variable needed
        Cursor c = ourDatabase.query(DATABASE_BANK, columns, null, null, null, null, null); //Cursor will go through the whole table (no condition)
        String total="";                                                                    //will hold the result from the cursor
        if( c!=null){
            c.moveToFirst();                                                                //cursor goes to the first row in the result set (although there should only be one row in result set)
            total = c.getString(0);                                                         //cursor gets the String version of the first element in the array
        }

        int current = Integer.parseInt(total);                                              //convert the current amount in the bank table to an int
        current = current+amount;                                                           //add on the new amount to the old amount
        ourDatabase.execSQL("UPDATE " + DATABASE_BANK + " SET " + KEY_BANK_TOTAL + " = " + current + ";"); //SQL UPDATE statement
    }


    //Take in an int and update the current amount in the bank table (method called in BankActivity.java)
    public void deleteBankAccount(int amount){
        String[] columns = new String[]{KEY_BANK_TOTAL};                                    //String array of the variable needed
        Cursor c = ourDatabase.query(DATABASE_BANK, columns, null, null, null, null, null); //Cursor will go through the whole table (no condition)
        String total="";                                                                    //will hold the result from the cursor
        if( c!=null){
            c.moveToFirst();                                                                //cursor goes to the first row in the result set (although there should only be one row in result set)
            total = c.getString(0);                                                         //cursor gets the String version of the first element in the array
        }
        int current = Integer.parseInt(total);                                              //convert the current amount in the bank table to an int
        current = current-amount;                                                           //subtract the new amount from the old amount
        ourDatabase.execSQL("UPDATE " + DATABASE_BANK + " SET " + KEY_BANK_TOTAL + " = " + current + ";"); //SQL UPDATE statement

    }


    //Get a String version of the current amount in the bank table
    public String getAmount(){
        String[] columns = new String[]{KEY_BANK_TOTAL};                                    //String array of the variable needed
        Cursor c = ourDatabase.query(DATABASE_BANK, columns, null, null, null, null, null); //Cursor will go through the whole table (no condition)

        if( c!=null){
            c.moveToFirst();                                                                //cursor goes to the first row in the result set
            String total = c.getString(0);                                                  //cursor gets the String version of the first element in the array
            return total;                                                                   //return the cursor's result
        }

        return null;                                                                        //if there was a problem, return null
    }









                                //Shopping List methods

    //Take in 3 Strings and add a new shopping item to the Shopping List (method called in AddShopping.java)
    public long addShopping(String item, String price, String holidayID){
        ContentValues cv = new ContentValues();                         //ContentValues allows you to insert many elements into the one row at the same time
        cv.put(KEY_ITEM_SHOP, item);                                    //add shopping item into the ContentValues container (see more inputs below)
        cv.put(KEY_PRICE_SHOP, price);
        cv.put(KEY_SHOP_HOLIDAY, holidayID);
        return ourDatabase.insert(DATABASE_TABLE_SHOPPING, null, cv);   //SQL INSERT statement to put everything from the ContentValues into the Shopping List table
    }


    //Takes in a shopping item ID and deletes the shopping item that corresponds with it (method called in HolidayShoppingList.java)
    public void deleteEntryShopping(int LRow1) throws SQLException{
        ourDatabase.delete(DATABASE_TABLE_SHOPPING, KEY_ROWID_SHOP + " = " + LRow1, null); //SQL DELETE statement
    }


    //Takes in a destination name and returns all the shopping items that correspond to that holiday (method called in HolidayShoppingList.java)
    public String getShoppingDB(String name) {
        String[] columns = new String[]{KEY_ROWID_SHOP, KEY_ITEM_SHOP, KEY_PRICE_SHOP, KEY_SHOP_HOLIDAY};   //String array of the variables needed
        String holId = getHolidayID(name);                                                                  //get holiday ID
        Cursor c = ourDatabase.query(DATABASE_TABLE_SHOPPING, columns, KEY_SHOP_HOLIDAY + " = " + holId, null, null, null, null); //similar to a SELECT query in SQL [WHERE key_shop_holiday LIKE '%holId%';]
        String result = "ID:  Item: " + "\n";                                                               //Print out before Shopping List

        int myRow = c.getColumnIndex(KEY_ROWID_SHOP);                                                       //get column indexes for all the variables needed
        int myItem = c.getColumnIndex(KEY_ITEM_SHOP);
        int myPrice = c.getColumnIndex(KEY_PRICE_SHOP);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){                                            //for loop (cursor = 0; cursor <= last element; cursor++)
            result = result + c.getString(myRow)+ "    " + c.getString(myItem) + "      €" + c.getString(myPrice) + "\n"; //cursor gets the String version of all the variables needed
        }
        return result;                                                                                      //return the cursor's result
    }










                                //Spending Money methods

    //Take in 3 Strings and add a new spending money item to the Soending Money table (method called in AddSpending.java)
    public long addSpending(String item, String amount, String holidayID){
        ContentValues cv = new ContentValues();                             //ContentValues allows you to insert many elements into the one row at the same time
        cv.put(KEY_ITEM_SPEND, item);                                       //add spending item into the ContentValues container (see more inputs below)
        cv.put(KEY_AMOUNT_SPEND, amount);
        cv.put(KEY_SPEND_HOLIDAY, holidayID);
        return ourDatabase.insert(DATABASE_TABLE_SPENDING, null, cv);       //SQL INSERT statement to put everything from the ContentValues into the Spending Money table
    }


    //Takes in a spending money item ID and deletes the spending money item that corresponds with it (method called in HolidaySpendingMoney.java)
    public void deleteEntrySpending(int LRow1) throws SQLException{
        ourDatabase.delete(DATABASE_TABLE_SPENDING, KEY_ROWID_SPEND + " = " + LRow1, null);     //SQL DELETE statement
    }


    //Takes in a destination name and returns all the spending money items that correspond to that holiday (method called in HolidaySpendingMoney.java)
    public String getSpendingDB(String name) {
        String[] columns = new String[]{KEY_ROWID_SPEND, KEY_ITEM_SPEND, KEY_AMOUNT_SPEND, KEY_SPEND_HOLIDAY};  //String array of the variables needed
        String holId = getHolidayID(name);                                                                      //get holiday ID
        Cursor c = ourDatabase.query(DATABASE_TABLE_SPENDING, columns, KEY_SPEND_HOLIDAY + " = " + holId, null, null, null, null); //similar to a SELECT query in SQL [WHERE key_spend_holiday LIKE '%holId%';]
        String result = "ID:  Item: " + "\n";                                                                   //Print out before Shopping List

        int myRow = c.getColumnIndex(KEY_ROWID_SPEND);                                                          //get column indexes for all the variables needed
        int myItem = c.getColumnIndex(KEY_ITEM_SPEND);
        int myPrice = c.getColumnIndex(KEY_AMOUNT_SPEND);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){                                                //for loop (cursor = 0; cursor <= last element; cursor++)
            result = result + c.getString(myRow)+ "    " + c.getString(myItem) + "      €" + c.getString(myPrice) + "\n"; //cursor gets the String version of all the variables needed
        }
        return result;                                                                                          //return the cursor's result
    }











    //Get the total cost (from Shopping list + Spending money) for a particular holiday
    public String getTotalHolidayAmount(String holidayName){
        String[] columnShop = new String[]{KEY_PRICE_SHOP, KEY_SHOP_HOLIDAY};               //String array of the variables needed
        String holId = getHolidayID(holidayName);                                           //Get holidayID
        Cursor c = ourDatabase.query(DATABASE_TABLE_SHOPPING, columnShop, KEY_SHOP_HOLIDAY + " = " + holId, null, null, null, null); //similar to a SELECT query in SQL [WHERE key_shop_holiday LIKE '%holId%';]
        int myPrice = c.getColumnIndex(KEY_PRICE_SHOP);                                     //get column index of the variable needed

        int priceNum=0;
        int totalPrice=0;                                                                   //will hold the total cost of all items put together
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){                             //for loop (cursor = 0; cursor <= last element; cursor++)
            String price = c.getString(myPrice);                                            //cursor gets the string version of the price
            priceNum = Integer.parseInt(price);                                             //convert price to an int
            totalPrice = totalPrice+priceNum;                                               //add all the prices together (Shopping List)
        }

        String[] columnSpend = new String[]{KEY_AMOUNT_SPEND, KEY_SPEND_HOLIDAY};           //String array of the variables needed
        Cursor C = ourDatabase.query(DATABASE_TABLE_SPENDING, columnSpend, KEY_SPEND_HOLIDAY + " = " + holId, null, null, null, null); //similar to a SELECT query in SQL [WHERE key_spend_holiday LIKE '%holId%';]
        int myAmount = C.getColumnIndex(KEY_AMOUNT_SPEND);                                  //get column index of the variable needed

        int amountNum=0;
        for(C.moveToFirst(); !C.isAfterLast(); C.moveToNext()){                             //for loop (cursor = 0; cursor <= last element; cursor++)
            String amount = C.getString(myAmount);                                          //cursor gets the string version of the amount
            amountNum = Integer.parseInt(amount);                                           //convert amount to an int
            totalPrice = totalPrice+amountNum;                                              //add together (Shopping List + Spending Money)
        }

        String travel = getTravelPrice(holidayName);                                        //get the travel price (method above)
        int travelPrice = Integer.parseInt(travel);                                         //convert to int
        totalPrice = totalPrice+travelPrice;                                                //add together (Shopping List, Spending Money + travel price)

        if(totalPrice==0){                                                                  //If there is nothing in the Shopping List or Spending Budget just return N/A
            String result = "N/A";
            return result;
        }else{                                                                              //otherwise return a String version of the total price
            String result = ""+totalPrice;
            return result;
        }

    }

}

