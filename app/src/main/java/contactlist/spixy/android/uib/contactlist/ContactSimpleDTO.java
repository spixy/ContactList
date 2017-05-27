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

    public ContactSimpleDTO(Cursor cursor)
    {
        this.setId(cursor);
        this.setName(cursor);
        this.setSurname(cursor);
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

    private static void error(String str)
    {
        Log.e("ContacdDTO", str);
    }
}
