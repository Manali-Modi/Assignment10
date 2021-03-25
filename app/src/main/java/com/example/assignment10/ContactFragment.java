package com.example.assignment10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment {

    ArrayList<String> contactName;
    ArrayList<String> mobileNumber;
    View view;

    public static final int R_CODE = 111;

    ListView lstContact;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==R_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            contactName = getAllContacts();
            loadContacts();
        }
    }

    private void loadContacts() {
        lstContact.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,contactName));
        lstContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),mobileNumber.get(i),Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<String> getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = getContext().getContentResolver();
        Cursor cName = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cName!=null){
            while (cName.moveToNext()){
                String id = cName.getString(cName.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cName.getString(cName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                nameList.add(name);

                String hasPhone = cName.getString(cName.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(hasPhone.equals("1")){
                    Cursor cPhone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,null,null);
                    if(cPhone.moveToFirst()){
                        String number = cPhone.getString(cPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mobileNumber.add(number);
                    }
                    cPhone.close();
                }
                else {
                    mobileNumber.add("No Number");
                }
            }
            cName.close();
        }
        return nameList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        lstContact = view.findViewById(R.id.lst_contact);

        mobileNumber = new ArrayList<>();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},R_CODE);
        }
        else {
            contactName = getAllContacts();
            loadContacts();
        }
        return view;
    }
}