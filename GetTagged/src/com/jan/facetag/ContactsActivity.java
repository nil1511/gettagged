package com.jan.facetag;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
@SuppressLint("NewApi")
@TargetApi(5)
public class ContactsActivity extends Activity{
	static String[] name;
	static String[] photoid;
	static String sContact="",sPhotoid=null;
	static int sContactpid=0;
	static Uri uri;
	static ContentResolver cr;
	static Context context;
	private int i=0;
	public ArrayList<TextView> imags = new ArrayList<TextView>();
	ArrayList<String> al;
	public void onCreate(final Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_contacts);
	        Cursor cursor;
	        uri = ContactsContract.Contacts.CONTENT_URI;
	        context=this;
	        String[] projection = new String[] {
	    
	          
	
	                ContactsContract.Contacts.DISPLAY_NAME,
	        		ContactsContract.Contacts.PHOTO_ID
	                
	                
	        };
	        cursor=managedQuery(uri, projection, null,null,ContactsContract.Contacts.DISPLAY_NAME);
	        
	        cr= getContentResolver();
	         startManagingCursor(cursor);
	         cursor.moveToFirst();
	         
	         
	         
	        int _name = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	        int _photoid = cursor.getColumnIndex( ContactsContract.Contacts.PHOTO_ID);
	        name=new String[cursor.getCount()];
	        al= new ArrayList<String>();
	        photoid=new String[cursor.getCount()];;
	        i=0;
	       String[] name2=new String[cursor.getCount()];
	       this.getContentResolver();
	        while(i<cursor.getCount()-1){ 
	         	 
	        	 cursor.moveToNext();
	        	 name[i]=cursor.getString(_name);
	        	  photoid[i]=cursor.getString(_photoid);
	        	  name2[i]=name[i]+"\n"+photoid[i];
	        		al.add(name[i]);	
	        		
	        	 i++;
	        	 }
	       cl=(ListView)findViewById(R.id.ContactList);        
	   
	       
	     adapter a =new adapter(this,al);
	 cl.setAdapter(a);
	 
	 OnItemClickListener l=new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				sContact=name[arg2];
				sPhotoid=photoid[arg2];
				Log.d("test", sContact);
				Intent i =new Intent(ContactsActivity.this, myGallery.class);
				i.putExtra("ContactsActivity", sContact);
				startActivity(i);
				
				
		}
	};
	 cl.setOnItemClickListener(l); 

	}          
	public static Uri getURI() {
	
   if(sPhotoid==null)
	 return Uri.parse(("android.resource://com.example.facetag/" + R.drawable.ic_launcher));
   else
	   
	    return ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI,Integer.parseInt(sPhotoid));
	}


		 ListView cl;
		 TextView  tv;
		}
