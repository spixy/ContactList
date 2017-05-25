package contactlist.spixy.android.uib.contactlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ContactActivity extends AppCompatActivity implements OnClickListener, FileDialog.FileSelectedListener
{
    private DBManager mydb;

    private ImageView image;
    private TextView name;
    private TextView surname;
    private TextView address;
    private TextView phone;
    private TextView email;

    private Bitmap GetBitmapFromImageView()
    {
        try
        {
            return ((BitmapDrawable)image.getDrawable()).getBitmap();
        }
        catch (Exception ex)
        {
            error("Empty bitmap on picture");
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mydb = new DBManager(this);

        image = (ImageView) findViewById(R.id.contact_image);
        name = (TextView) findViewById(R.id.contact_name);
        surname = (TextView) findViewById(R.id.contact_surname);
        address = (TextView) findViewById(R.id.contact_address);
        email = (TextView) findViewById(R.id.contact_email);
        phone = (TextView) findViewById(R.id.contact_phone);

        int id = getId();

        log("onCreate " + id);

        if (id > -1)
        {
            LoadContact(id);

            Button deleteButton = (Button) findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            name.setText("name");
            surname.setText("surname");
            address.setText("address");
            email.setText("email");
            phone.setText("phone");
        }
    }

    private void LoadContact(int id)
    {
        log("LoadContact " + id);

        ContactDTO contact = mydb.getContact(id);

        try
        {
            name.setText(contact.getName());
            surname.setText(contact.getSurname());
            address.setText(contact.getAddress());
            phone.setText(contact.getPhone());
            email.setText(contact.getEmail());
            image.setImageBitmap(contact.getPicture());
        }
        catch (Exception ex)
        {
            error("Cannot load contact with ID " + id + ": " + ex.getMessage());
            Toast.makeText(getApplicationContext(), "Cannot load contact with ID " + id + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    private int getId()
    {
        Bundle extras = getIntent().getExtras();

        if (extras == null)
            return -1;

        return extras.getInt("id", -1);
    }

    private void Save()
    {
        Boolean result;
        int id = getId();

        log("Save " + id);

        if (id == -1)
        {
            result = mydb.insertContact(
                        name.getText().toString(),
                        surname.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        address.getText().toString(),
                        Utility.BitmapToBytes(GetBitmapFromImageView()));
        }
        else
        {
            result = mydb.updateContact(
                        id,
                        name.getText().toString(),
                        surname.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        address.getText().toString(),
                        Utility.BitmapToBytes(GetBitmapFromImageView()));
        }

        Toast.makeText(getApplicationContext(), result ? "Success" : "An error has occurred", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void Delete()
    {
        AlertDialog ad = new AlertDialog.Builder(this)
            .setMessage(R.string.delete_contact)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {
                    Boolean result = false;
                    id = getId();

                    log("Delete " + id);

                    if (id > -1)
                    {
                        result = mydb.deleteContact(id);
                    }

                    Toast.makeText(getApplicationContext(), result ? "Success" : "An error has occurred", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {}
            })
            .create();

        ad.show();
    }

    @Override
    public void onClick(View v)
    {
        if (v != image)
            return;

        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath, ".jpg");
        fileDialog.addFileListener(this);
        fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }

    public void fileSelected(File file) {

        Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
        image.setImageBitmap(bmp);
    }

    public void SaveMenuClick()
    {
        this.Save();
    }

    public void SaveButtonClick(View view)
    {
        this.Save();
    }

    public void DeleteMenuClick()
    {
        this.Delete();
    }

    public void DeleteButtonClick(View view)
    {
        this.Delete();
    }

    private static void log(String str)
    {
        Log.i("ContactActivity", str);
    }

    private static void error(String str)
    {
        Log.e("ContactActivity", str);
    }
}
