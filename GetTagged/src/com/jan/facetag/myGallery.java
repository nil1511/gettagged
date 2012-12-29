package com.jan.facetag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class myGallery extends Activity implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory,OnTouchListener {
	private static final String SAMPLE_DB_NAME = "myGallery";
	public ArrayList<File> imags = new ArrayList<File>();
	public ImageView mIS;
	public Gallery g;
	public Button tag;
	public String[] n;
	Bitmap te,tab;
	private FaceDetector.Face[] detectedFaces;
	private int NUMBER_OF_FACES=1;
	private FaceDetector faceDetector;
	private int NUMBER_OF_FACE_DETECTED,pos;
	private float eyeDistance;
	private static float x;
	private PointF midPoint;
	private Drawable ima;
	private TextView tv;
	private SQLiteDatabase sampleDB;
	private ImageButton call,sms,rot;
	private int angle=0;
	private String na;
	private byte[] im;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        tag= (Button) findViewById(R.id.button1);
        tv = (TextView) findViewById(R.id.textView1);
        call=(ImageButton) findViewById(R.id.imageButton1);
        sms=(ImageButton) findViewById(R.id.imageButton2);
        try{
        	na=this.getIntent().getStringExtra("ContactsActivity");
        	Log.d("t",na);
        
        }catch(Exception e){
        	System.out.println(e);
        }
        try{  sampleDB =  this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " +SAMPLE_DB_NAME +" (name VARCHAR, imgPath VARCHAR);");}
                catch (SQLiteException se ) {
        	Log.e(getClass().getSimpleName(), "Could not create or Open the database");}
        if(na==null){
        File dicm  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        getImages(dicm);
         dicm = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/DCIM/100ANDRO");
        getImages(dicm);
        dicm = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/DCIM/Camera");
        getImages(dicm);}
        else{
        	Cursor c = sampleDB.rawQuery("SELECT imgPath FROM " +SAMPLE_DB_NAME +" where name = '"+na+"'", null);
        	if(c.getCount()!=0){
        		c.moveToFirst();
        		int i=0;
        		while(i<c.getCount()){
        			
        			imags.add(new File(c.getString(0)));
        			c.moveToNext();
        			i++;
        		}
        	}
        }
        rot = (ImageButton) findViewById(R.id.imageButton3);
        mIS = (ImageView) findViewById(R.id.switcher);
        mIS.setOnTouchListener((OnTouchListener) this);
        g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);
      
    }
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
    	  super.onActivityResult(reqCode, resultCode, data);
    	  switch (reqCode) {
    	    case (0) :
    	      if (resultCode == Activity.RESULT_OK) {
    	        Uri contactData = data.getData();
    	        Cursor c =  managedQuery(contactData, null, null, null, null);
    	        if (c.moveToFirst()) {
    	          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    	          tv.setText(name);
    	          try{Cursor c1 = sampleDB.rawQuery("SELECT name FROM " +SAMPLE_DB_NAME +" where imgPath = '"+imags.get(pos).getAbsolutePath()+"'", null);
             if(c1.getCount()==0){
				sampleDB.execSQL("INSERT INTO " +SAMPLE_DB_NAME +" Values ('"+name+"','"+imags.get(pos).getAbsolutePath()+"');");

             }
				else {
				 	ContentValues v = new ContentValues();
				 	v.put("name", name);
				 	sampleDB.update(SAMPLE_DB_NAME, v, "imgPath = '"+imags.get(pos).getAbsolutePath()+"'", null);
				}
            }
            	catch(SQLiteException e){System.out.println(e);}
    	        }
    	      }
    	      break;
    	  }
    }
    private void getImages(File dicm){
        n=null;
    	if(dicm.exists()){
          	 n=dicm.list(new FilenameFilter() {
   				
   				public boolean accept(File dir, String filename) {
   					return ((filename.endsWith(".jpeg"))||(filename.endsWith(".jpg"))||(filename.endsWith(".png")));
   				}
   			});
         for(int i=0;i<n.length;i++)
   		imags.add(new File(dicm+"/"+n[i]));
      }
    	
    }

    public View makeView() {
		 ImageView i = new ImageView(this);
	        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
	                LayoutParams.MATCH_PARENT));
	        return i;
	}
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id){
		pos=position;
	try{parent.setDrawingCacheEnabled(false);
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		Options options = new BitmapFactory.Options();

        options.inSampleSize=8;
        options.inPreferredConfig=Config.RGB_565;
		tab=BitmapFactory.decodeFile(imags.get(position).getAbsolutePath(),options);
		Matrix m =new Matrix();
		m.postRotate(90);
		tab = Bitmap.createBitmap(tab, 0, 0, tab.getWidth(), tab.getHeight(), m, true);
		detectedFaces=new FaceDetector.Face[NUMBER_OF_FACES];
		faceDetector=new FaceDetector(tab.getWidth(),tab.getHeight(),NUMBER_OF_FACES);
		NUMBER_OF_FACE_DETECTED=faceDetector.findFaces(tab, detectedFaces);
		tv.setText("Not Tagged");
    	call.setVisibility(8);
    	sms.setVisibility(8);
		try{Cursor c = sampleDB.rawQuery("SELECT name FROM " +SAMPLE_DB_NAME +" where imgPath = '"+imags.get(position).getAbsolutePath()+"'", null);
             if(c.getCount()!=0){c.moveToFirst();
            	tv.setText(c.getString(0));
            	String nam=c.getString(0);
            	String num = null;
            	final String[] projection = new String[]{
            		Phone.DISPLAY_NAME,
            		Phone.NUMBER
            	};
            	final Cursor phone = managedQuery(
        				Phone.CONTENT_URI,
        				projection,
        				Data.DISPLAY_NAME + "=?",
        				new String[]{String.valueOf(nam)},
        				null);
            	if(phone.moveToFirst()){
            		num=phone.getString(phone.getColumnIndex(Phone.NUMBER));
            	}
            	final String n=num;
            	call.setVisibility(0);
            	call.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						try{
							
							Intent call = new Intent(Intent.ACTION_DIAL);
							call.setData(Uri.parse("tel:"+n));
							startActivity(call);
						}
						catch(Exception e){
					         Log.e("dial", "Call failed", e);
						}
					}
				});
            	sms.setVisibility(0);
            	sms.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						try{
					  startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", n, null)));
						}
						catch(Exception e){
					         Log.e("sms", "sms failed", e);
						}
					}
				});
             	}
            	}
            	catch(SQLiteException e){System.out.println(e);}
		tag.setEnabled(false);
		if(NUMBER_OF_FACE_DETECTED>=0){
			tag.setEnabled(true);
		
		onDraw(new Canvas(tab));
			tag.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent a = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(a, 0);
				}
			});
		}
		tab.compress(Bitmap.CompressFormat.JPEG, 90, b);
		tab.recycle();
		System.gc();
		im=b.toByteArray();
		ima= new BitmapDrawable(BitmapFactory.decodeByteArray(im,0, b.size()));
		try {
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mIS.setImageDrawable(ima);
		rot.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				turn();
			}
		});		
	}catch(OutOfMemoryError e){
    	Log.d("Error",e.toString());
    } 
	}
	public void turn()
	{
	    RotateAnimation anim = new RotateAnimation(angle, angle + 90,
	            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
	    angle = (angle + 90) % 360;

	    anim.setInterpolator(new LinearInterpolator());
	    anim.setDuration(500);
	    anim.setFillEnabled(true);

	    anim.setFillAfter(true);
	    mIS.startAnimation(anim);
	}
	@SuppressLint({ "DrawAllocation", "DrawAllocation" })
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(tab, 0,0, null);
		Paint myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);
        for(int count=0;count<NUMBER_OF_FACE_DETECTED;count++)
        {
        	Face face=detectedFaces[count];
        	midPoint=new PointF();
           	face.getMidPoint(midPoint);
        	eyeDistance=face.eyesDistance();
        	RectF a = new RectF(midPoint.x-eyeDistance, midPoint.y-eyeDistance, midPoint.x+eyeDistance, midPoint.y+(1.5f*eyeDistance));
        	canvas.drawRect(a, myPaint);
        
        }
	}
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	public class ImageAdapter extends BaseAdapter{
		
		private Context mc;
		public ImageAdapter(Context c) {
			mc=c;
		}
		public int getCount() {
			return imags.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mc);

            Options options = new BitmapFactory.Options();
          
            options.inSampleSize=16;
            options.inDither=false;
            options.inPurgeable=true;
            options.inPreferredConfig=Config.ARGB_4444;
            Bitmap r = BitmapFactory.decodeFile(imags.get(position).getAbsolutePath(),options);
            te =ThumbnailUtils.extractThumbnail(r, 100, 100);
            System.gc();
            r.recycle();
        	i.setImageBitmap(te);
        	i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            i.setLayoutParams(new Gallery.LayoutParams(150, 150));
			return i;
		}
	}
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_UP:{		
			if((event.getX()-x)<40){
				try{
					if(pos>=0&&pos<imags.size()-1)
				g.setSelection(++pos, true);}
				catch(Exception e){
					System.out.println(e);
				}
			}
			else if((event.getX()-x)>40){
				try{
					Log.d("ps", pos+"");
					if(pos>0&&pos<=imags.size())
				g.setSelection(--pos, true);}
				catch(Exception e){
					System.out.println(e);
				}
			}
		}
		break;
		case MotionEvent.ACTION_DOWN:{
			x=event.getX();
			}
		break;
		}
		return true;
	}
}
