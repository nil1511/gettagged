package com.jan.facetag;


import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class adapter extends BaseAdapter {
 
    private Activity activity;
    ArrayList<String> al;
    private static LayoutInflater inflater=null;
    public ImageView imageVeiw; 
 
    public adapter(Activity a,ArrayList<String> names) {
        activity = a;
       al=names;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     
    }
    public int getCount() {
        return al.size();
    }
 
    public Object getItem(int position) {
        return al.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @TargetApi(5)
	public View getView(int position, View convertView, ViewGroup parent) {
    	
        View vi=convertView;

        if(convertView==null)
          vi = inflater.inflate(R.layout.conlist, null);
        
         TextView _name = (TextView)vi.findViewById(R.id.name); 
             _name.setText(al.get(position));
             imageVeiw=(ImageView)vi.findViewById(R.id.list_image);
           
         Uri uri;
       
       
         if(ContactsActivity.photoid[position]==null)
        	 uri= Uri.parse(("android.resource://com.example.facetag/" + R.drawable.user));
           else
        	          	 uri=  ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI,Integer.parseInt(ContactsActivity.photoid[position]));
        imageVeiw.setImageURI(uri);
         
    
        return vi;
        
    }
}