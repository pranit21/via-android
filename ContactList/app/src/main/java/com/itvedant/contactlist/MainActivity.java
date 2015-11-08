package com.itvedant.contactlist;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<MyContacts> contacts = new ArrayList<MyContacts>();
    private ListView listView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.contact_list);
        adapter = new CustomAdapter(this, contacts);
        listView.setAdapter(adapter);

        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";

        Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, SELECTION, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        //Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        while (people.moveToNext()) {
            //if (Integer.parseInt(people.getString(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            String id = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
            int nameIndex = people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String contactName = people.getString(nameIndex);
            String contactNumber = "";
            InputStream contactPhoto;

            contactNumber = this.getPhoneNumber(id);
            contactPhoto = this.getPhotoUri(id);

            MyContacts contact = new MyContacts();
            contact.setContactName(contactName);
            contact.setContactMobile(contactNumber);
            contact.setContactPhoto(contactPhoto);

            contacts.add(contact);
            //}
        }
        people.close();

        /*
        * Notify adapter of data set changed so that data will be updated in list
        */
        adapter.notifyDataSetChanged();
    }

    private String getPhoneNumber(String id) {
        // get phone numbers
        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
        if (phone != null) {
            if(phone.moveToNext())
                return phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        } else {
            return null;
        }
        phone.close();

        return null;
    }

    private InputStream getPhotoUri(String id) {
        // get photos
        Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
        Uri photouri = Uri.withAppendedPath(personUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor photo = getContentResolver().query(
                photouri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO},
                null,
                null,
                null);
        if(photo == null) {
            return null;
        }

        try {
            if (photo.moveToNext()) {
                byte[] data = photo.getBlob(0);
                if(data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            photo.close();
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
