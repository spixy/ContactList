package contactlist.spixy.android.uib.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by spixy on 17.4.2017.
 */

public class MainActivity extends Activity
{
    private DBManager mydb;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) this.findViewById(R.id.listView);

        mydb = new DBManager(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.Load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    private void Load()
    {
        ArrayList<String> contactNames = mydb.getAllContactsFullNames();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, contactNames);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                log("onItemClick, " + position + ", " + id);
                launchContactActivity(position + 1);
            }
        });
    }

    public void CreateNewButtonClick()
    {
        log("CreateNewButtonClick");
        launchContactActivity(-1);
    }

    public void CreateNewButtonClick(View button)
    {
        log("CreateNewButtonClick");
        launchContactActivity(-1);
    }

    private void launchContactActivity(int contactId)
    {
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", contactId);

        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
        intent.putExtras(dataBundle);

        startActivity(intent);
    }

    private static void log(String str)
    {
        Log.i("MainActivity", str);
    }
}
