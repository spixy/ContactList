package contactlist.spixy.android.uib.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String[] COLUMNS_TO_SHOW = new String[] { CONTACTS_COLUMN_NAME, CONTACTS_COLUMN_SURNAME };
    private static final String[] COLUMNS_IMAGE_ONLY = new String[] { CONTACTS_COLUMN_PICTURE };
    private static final String[] COLUMNS_NO_IMAGE = new String[] {
            CONTACTS_COLUMN_ID,
            CONTACTS_COLUMN_NAME,
            CONTACTS_COLUMN_SURNAME,
            CONTACTS_COLUMN_ADDRESS,
            CONTACTS_COLUMN_PHONE,
            CONTACTS_COLUMN_EMAIL
    };

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
                CONTACTS_COLUMN_PICTURE + " blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ContactDTO getContact(int id)
    {
        ContactDTO dto = new ContactDTO();

        Cursor cursor = this.getReadableDatabase().query(false, CONTACTS_TABLE_NAME, COLUMNS_NO_IMAGE, CONTACTS_COLUMN_ID + " = ? ",
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

        dto.setInformation(cursor);
        cursor.close();

        cursor = this.getReadableDatabase().query(false, CONTACTS_TABLE_NAME, COLUMNS_IMAGE_ONLY, CONTACTS_COLUMN_ID + " = ? ",
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

        dto.setPicture(cursor);
        cursor.close();

        return dto;
    }

    public ArrayList<String> getAllContactsFullNames()
    {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(false, CONTACTS_TABLE_NAME, COLUMNS_TO_SHOW, null, null, null, null, null, null);

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
            array_list.add(cursor.getString(0) + " " + cursor.getString(1));
            cursor.moveToNext();
        }

        cursor.close();
        return array_list;
    }

    public Boolean insertContact(String name, String surname, String phone, String email, String address, byte[] picture)
    {
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

    public Boolean updateContact(int id, String name, String surname, String phone, String email, String address, byte[] picture)
    {
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
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " = ?", new String[] { Integer.toString(id) }) > 0;
    }
}
