package contactlist.spixy.android.uib.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by spixy on 17.4.2017.
 */

public class DBManager extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "MyDB.db";

    private static final String CONTACTS_TABLE_NAME = "contacts";

    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_SURNAME = "surname";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_ADDRESS = "address";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PICTURE = "picture";

    private static final String[] COLUMNS_GET_SIMPLE_CONTACT = new String[] {
            CONTACTS_COLUMN_ID,
            CONTACTS_COLUMN_NAME,
            CONTACTS_COLUMN_SURNAME };

    private static final String[] COLUMNS_GET_FULL_CONTACT = new String[] {
            CONTACTS_COLUMN_ID,
            CONTACTS_COLUMN_NAME,
            CONTACTS_COLUMN_SURNAME,
            CONTACTS_COLUMN_PHONE,
            CONTACTS_COLUMN_ADDRESS,
            CONTACTS_COLUMN_EMAIL,
            CONTACTS_COLUMN_PICTURE };

    public DBManager(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + CONTACTS_TABLE_NAME + " (" +
                CONTACTS_COLUMN_ID      + " integer primary key, " +
                CONTACTS_COLUMN_NAME    + " text, " +
                CONTACTS_COLUMN_SURNAME + " text, " +
                CONTACTS_COLUMN_PHONE   + " text, " +
                CONTACTS_COLUMN_ADDRESS + " text, " +
                CONTACTS_COLUMN_EMAIL   + " text, " +
                CONTACTS_COLUMN_PICTURE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ContactFullDTO getContact(int id)
    {
        log("getContact " + id);

        Cursor cursor = this.getReadableDatabase().query(false, CONTACTS_TABLE_NAME, COLUMNS_GET_FULL_CONTACT, CONTACTS_COLUMN_ID + " = ?",
                                                            new String[] { Integer.toString(id) }, null, null, null, null);

        if (cursor == null)
        {
            return null;
        }
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return null;
        }
        if (cursor.getCount() == 0)
        {
            return null;
        }

        ContactFullDTO dto = new ContactFullDTO(cursor);
        cursor.close();
        return dto;
    }

    public ArrayList<ContactSimpleDTO> getAllContacts()
    {
        log("getAllContacts");
        ArrayList<ContactSimpleDTO> array_list = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().query(false, CONTACTS_TABLE_NAME, COLUMNS_GET_SIMPLE_CONTACT, null, null, null, null, null, null);

        if (cursor == null)
        {
            return array_list;
        }
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return array_list;
        }

        while (!cursor.isAfterLast())
        {
            array_list.add(new ContactSimpleDTO(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return array_list;
    }

    public Boolean insertContact(String name, String surname, String phone, String email, String address, String picture)
    {
        log("insertContact");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_SURNAME, surname);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(CONTACTS_COLUMN_ADDRESS, address);
        contentValues.put(CONTACTS_COLUMN_PICTURE, picture);
        return db.insert(CONTACTS_TABLE_NAME, null, contentValues) != -1;
    }

    public Boolean updateContact(int id, String name, String surname, String phone, String email, String address, String picture)
    {
        log("updateContact " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_SURNAME, surname);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(CONTACTS_COLUMN_ADDRESS, address);
        contentValues.put(CONTACTS_COLUMN_PICTURE, picture);
        return db.update(CONTACTS_TABLE_NAME, contentValues, CONTACTS_COLUMN_ID + " = ?", new String[] { Integer.toString(id) }) > 0;
    }

    public Boolean deleteContact(int id)
    {
        log("deleteContact " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " = ?", new String[] { Integer.toString(id) }) > 0;
    }

    private static void log(String str)
    {
        Log.i("DBManager", str);
    }

    private static void error(String str)
    {
        Log.e("DBManager", str);
    }
}
