package contactlist.spixy.android.uib.contactlist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ContactActivity extends FragmentActivity
{
    private DBManager mydb;

    private String imageFile;
    private ImageView image;
    private TextView name;
    private TextView surname;
    private TextView address;
    private TextView phone;
    private TextView email;

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
            Load(id);

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
            imageFile = null;
        }
    }

    private void Load(int id)
    {
        log("Load " + id);

        ContactFullDTO contact = mydb.getContact(id);

        if (contact == null)
            return;

        name.setText(contact.getName());
        surname.setText(contact.getSurname());
        address.setText(contact.getAddress());
        phone.setText(contact.getPhone());
        email.setText(contact.getEmail());

        imageFile = contact.getPicture();
        if (imageFile != null)
        {
            image.setImageBitmap(BitmapFactory.decodeFile(imageFile));
        }
        else
        {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.avatar_placeholder);
            image.setImageDrawable(drawable);
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
                        imageFile);
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
                        imageFile);
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

    static final int REQUEST_PERMISSION = 1;
    static final int PICK_IMAGE_REQUEST = 2;

    public void onImageClick(View v)
    {
        if (v != image)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
        else
        {
            LoadImagePicker();
        }
    }

    private void LoadImagePicker()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadImagePicker();
            } else {
                log("Permission not granted.");
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri)
    {
        grantUriPermission("contactlist.spixy.android.uib.contactlist", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(contentUri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try
            {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                image.setImageBitmap(bmp);
                imageFile = getRealPathFromURI(data.getData());
            } catch (IOException e)
            {
                error(e.getMessage());
            }
        }
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
