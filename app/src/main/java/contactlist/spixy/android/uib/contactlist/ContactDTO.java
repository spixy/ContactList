package contactlist.spixy.android.uib.contactlist;

/**
 * Created by spixy on 18.4.2017.
 */

import android.database.Cursor;
import android.graphics.Bitmap;

public class ContactDTO
{
    private String name;
    private String surname;
    private String address;
    private String phone;
    private String email;
    private Bitmap picture;

    public ContactDTO(Cursor cursor)
    {
        this.setName(cursor);
        this.setSurname(cursor);
        this.setAddress(cursor);
        this.setPhone(cursor);
        this.setEmail(cursor);
        this.setImage(cursor);

        cursor.close();
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

    private void setImage(Cursor cursor)
    {
        byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(DBManager.CONTACTS_COLUMN_PICTURE));
        this.picture = Utility.BytesToBitmap(byteArray);
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

    public Bitmap getPicture()
    {
        return picture;
    }
}
