package contactlist.spixy.android.uib.contactlist;

/**
 * Created by spixy on 18.4.2017.
 */

import android.database.Cursor;
import android.util.Log;

public class ContactFullDTO
{
    private String name;
    private int id;
    private String surname;
    private String address;
    private String phone;
    private String email;
    private String picture;

    public ContactFullDTO(Cursor cursor)
    {
        this.setId(cursor);
        this.setName(cursor);
        this.setSurname(cursor);
        this.setAddress(cursor);
        this.setPhone(cursor);
        this.setEmail(cursor);
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

    private void setAddress(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_ADDRESS);
        this.address = cursor.getString(index);
    }

    private void setEmail(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_EMAIL);
        this.email = cursor.getString(index);
    }

    private void setPhone(Cursor cursor)
    {
        int index = cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_PHONE);
        this.phone = cursor.getString(index);
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

    public String getAddress()
    {
        return address;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPicture()
    {
        return picture;
    }

    private static void error(String str)
    {
        Log.e("ContacdDTO", str);
    }
}
