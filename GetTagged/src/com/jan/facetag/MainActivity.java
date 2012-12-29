package com.jan.facetag;



import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;



@TargetApi(4)
public class MainActivity extends TabActivity  {
    /** Called when the activity is first created. */
	static TabHost tabHost;
	 static Intent intentg,intentc,intentg2 ;
	 static Context context;
	 Bundle savedInstanceState;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.savedInstanceState=savedInstanceState;
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
         tabHost = getTabHost();
         View tabView = createTabView(this, "Contacts");
         intentc= new Intent().setClass(this, ContactsActivity.class)
        			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         TabSpec contacts;
         contacts=tabHost.newTabSpec("Contacts")
        		 		 .setIndicator(tabView)
        		 		 .setContent(intentc);
         context=this;
              		 			
         tabHost.addTab(contacts);
             
         intentg2= new Intent(this,myGallery.class)
  		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         TabSpec gallary;
         tabView = createTabView(this, "Gallery");
         gallary=tabHost.newTabSpec("Gallery")
 		 		 .setIndicator(tabView)
 		 		
 		 		 .setContent(intentg2);        		 			
  	tabHost.addTab(gallary);
   tabHost.setAnimation(AnimationUtils.loadAnimation(this,
            android.R.anim.fade_in));
   	tabHost.setCurrentTab(1);
  
    }
    private static View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabtext, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }

  
}