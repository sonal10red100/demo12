package com.hfad.demo12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 30-05-2018.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_new3";
    private static final String TABLE_NAME = "foodi";
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM = "item";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_SP_DES = "key_sp_des";
    private static final String KEY_TOTAL_PRICE = "total_price";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DELETE FROM " + TABLE_NAME);

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ITEM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_QUANTITY + " TEXT,"
                + KEY_SP_DES + " TEXT,"
                + KEY_TOTAL_PRICE + " TEXT" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addItem(entry _entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("ssssss", "++++++++" + _entry.getItem() + " " + _entry.getPrice());

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, _entry.getItem());
        values.put(KEY_PRICE, _entry.getPrice());
        values.put(KEY_QUANTITY, _entry.getQuantity());
        values.put(KEY_SP_DES, _entry.getSp_des());
        values.put(KEY_TOTAL_PRICE, _entry.getTotal_price());
        Log.e("sp", "" + _entry.getSp_des());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        getItemsCount();
        //2nd argument is String containing nullColumnHack
        db.close();
        // Closing database connection

    }

    // code to get the single contact
    int getItem(String item, String sp) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_SP_DES, KEY_TOTAL_PRICE}, KEY_ITEM + "=?" + " AND " + KEY_SP_DES + "=?",
                new String[]{String.valueOf(item), String.valueOf(sp)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        // Log.e("id",cursor.getString(0));

        entry _entry = new entry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5));
        // return contact
        return _entry.getID();
    }

    entry getItemFromId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_SP_DES, KEY_TOTAL_PRICE}, KEY_ID + "=?" ,
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        // Log.e("id",cursor.getString(0));

        entry _entry = new entry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5));
        // return contact
        return _entry;
    }

    public entry getEntry(String item) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_SP_DES, KEY_TOTAL_PRICE}, KEY_ITEM + "=?",
                new String[]{String.valueOf(item)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        entry _entry = new entry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5));
        // return contact
        return _entry;
    }

    // code to get all contacts in a list view
    public List<entry> getAllItems() {
        List<entry> itemList = new ArrayList<entry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    entry en = new entry();

                    en.setID(Integer.parseInt(cursor.getString(0)));
                    en.setItem(cursor.getString(1));
                    en.setPrice(cursor.getString(2));
                    en.setQuantity(Integer.parseInt(cursor.getString(3)));
                    en.setSp_des(cursor.getString(4));
                    en.setTotal_price(cursor.getString(5));
                    Log.e("priceee", "++++++" + cursor.getString(2));

                    // Adding contact to list
                    itemList.add(en);
                } while (cursor.moveToNext());
            }
        }

        // return contact list
        return itemList;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    // code to update the single contact
    public String updateItem(entry _entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.e("quantity", String.valueOf(_entry.getQuantity()));
        values.put(KEY_ITEM, _entry.getItem());
        values.put(KEY_PRICE, _entry.getPrice());
        values.put(KEY_QUANTITY, _entry.getQuantity());
        values.put(KEY_SP_DES, _entry.getSp_des());
        values.put(KEY_TOTAL_PRICE, _entry.getTotal_price());
//Log.e("price",_entry.getTotal_price());

        // updating row
        db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(_entry.getID())});
        return _entry.getPrice();
    }

    // Deleting single contact
    public void deleteItem(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(Id)});
        db.close();
    }

    public void deleteItemUsingItemName(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ITEM + " = ?",
                new String[]{itemName});
        db.close();
    }

    // Getting contacts Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Log.e("Entry Count ", "++++++++++++" + String.valueOf(cursor.getCount()));
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public boolean checkIfExists(String item, String sp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_SP_DES, KEY_TOTAL_PRICE}, KEY_ITEM + "=?" + " AND " + KEY_SP_DES + "=?",
                new String[]{String.valueOf(item), String.valueOf(sp)}, null, null, null, null);

        if (cursor.getCount() == 0) {
            return false;
        } else
            return true;
    }

    public int getItemQuantity(String item, String sp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_SP_DES, KEY_TOTAL_PRICE}, KEY_ITEM + "=?" + " AND " + KEY_SP_DES + "=?",
                new String[]{String.valueOf(item), String.valueOf(sp)}, null, null, null, null);

        Log.e("qty:", "+++++" + cursor.getCount());
        if (cursor.moveToFirst())
            Log.e("qty:", "+++++" + cursor.getString(3).toString());
//cursor.close();
        return Integer.parseInt(cursor.getString(3));
    }

    public int getCartTotal() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery("SELECT SUM(KEY_TOTAL_PRICE) FROM TABLE_NAME", null);
            return cur.getInt(0);


    }

  /*  public boolean CheckForUniqueUser(String u_n)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME , new String[] { KEY_USER_NAME }, KEY_USER_NAME + "=?" , new String[]{String.valueOf(u_n)},null, null, null);
        if(cursor.getCount()==0)
        {
            cursor.close();
            return true;
        }
        cursor.close();
        return  false;
    }
*/
    /* public String getUsername(String u_n)
     {
        // String q="SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + KEY_USER_NAME + " = " + u_n + " AND " + KEY_PASSWORD + " = " + password;
         SQLiteDatabase db=this.getReadableDatabase();
         Cursor cursor = db.query(TABLE_NAME , new String[] {KEY_USER_NAME}, KEY_USER_NAME + "=?", new String[]{String.valueOf(u_n)},null, null, null);
         String u = cursor.getString(1);
         Log.e("+++++++++++id:" , String.valueOf(u));
         cursor.close();
         return u;
     }*/
    // public boolean Check
  /*  public boolean CheckIsDataAlreadyInDBorNot( String fieldValue1, String fieldValue2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[] { KEY_USER_NAME, KEY_PASSWORD},KEY_USER_NAME + "=?" + " AND " +KEY_PASSWORD + "=?", new String[]{String.valueOf(fieldValue1),String.valueOf(fieldValue2)},null, null, null);
        // int id=cursor.getInt(cursor.getColumnIndex(KEY_ID));
        //Log.e("++++++++++id: "+id);

        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
  }*/

}
