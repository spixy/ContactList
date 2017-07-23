package contactlist.spixy.android.uib.contactlist;

/**
 * Created by spixy on 18.4.2017.
 */

import android.database.Cursor;
import android.util.Log;

public class ContactSimpleDTO
{
    private int id;
    private String name;
    private String surname;
    private String picture;

    public ContactSimpleDTO(Cursor cursor)
    {
        this.setId(cursor);
        this.setName(cursor);
        this.setSurname(cursor);
        this.setPicture(cursor);
    }

    private void setId(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_ID);
        id = cursor.getInt(index);
    }

    private void setName(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_NAME);
        name = cursor.getString(index);
    }

    private void setSurname(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_SURNAME);
        this.surname = cursor.getString(index);
    }

    private void setPicture(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_PICTURE);
        this.picture = cursor.getString(index);
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPicture()
    {
        return picture;
    }

    private static void error(String str)
    {
        Log.e("ContactSimpleDTO", str);
    }
}
