package com.pachong.mycontacts;

import java.io.InputStream;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	//public static String DEV_FILE_PATH = "/sys/devices/platform/usb20_otg/usb1/1-1/1-1:1.0/dock_intf";
	public static String DEV_FILE_PATH = "/sys/devices/platform/mt_usb/musb-hdrc.0/usb1/1-1/1-1.1/1-1.1:1.0/dock_intf";
	//public static String DEV_FILE_PATH = "/sdcard/dock_intf";	
	public static String MID_CALL_STATE 		 ="com.tchip.mid_call";
	public static String MID_CALL_IN             ="com.tchip.mid.callin";
	public static String MID_CALL_IN_HOLDUP      ="com.tchip.mid.callin_holdup";
	public static String MID_CALL_IN_MISS		 ="com.tchip.mid.callin_miss";
	public static String MID_CALL_IN_OUTSIDE	 ="com.tchip.mid.callin_outside";
	public static String MID_CALL_IN_HOLDDOWN 	 ="com.tchip.mid.callin_holddown";
	public static String MID_CALL_OUT			 ="com.tchip.mid.callout";
	public static String MID_CALL_OUT_HOLDDOWN   ="com.tchip.mid.callout_holddown";
	public static String IP_CALL_STATE 			 ="com.tchip.ip_call";
	public static String MID_CALLIN_NUM 		 ="com.tchip.mid.callin_num";
	public static String MID_NOCHANGE			  ="com.tchip.mid.nochange";
		
	public static String MID_CID_NUM_UPDATE			  ="com.tchip.mid.cid_num_update";
	
	public static String AREACODE 			="02";
	public static String AREAPLACE 			="Metro Manila(02)";

	public static final String ACTION_MARAHAN_DOCK_CALL = "com.marahan.dock.action.DOCK_CALL";
	
	public static String[] phone_number = null;
	
	public static boolean onpause = false;
	public static void addCallLog(Context context, String number,  
			long duration, int type, boolean isNew) {
		 ContentValues values = new ContentValues();
		 values.put(Calls.NUMBER, number);
		 values.put(Calls.DATE, System.currentTimeMillis());
		 values.put(Calls.DURATION, duration);
		 values.put(Calls.TYPE, type);
		 values.put(Calls.NEW, isNew?1:0);		    
		 
		 Cursor cursorOriginal =
	            context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
	                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
	                new String[] {number},null);  
	        if( cursorOriginal != null && cursorOriginal.getCount() > 0) {
	        	cursorOriginal.moveToFirst();
	        	String name = cursorOriginal.getString(cursorOriginal.getColumnIndex(Phone.DISPLAY_NAME));
	        	values.put(Calls.CACHED_NAME, name);
	        	cursorOriginal.close();
	        }else{
	        	cursorOriginal.close();
	        }   	     
		 context.getContentResolver().insert(Calls.CONTENT_URI, values);	
		
	}
	
	public static void openNote(Context context){
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.socialnmobile.dictapps.notepad.color.note", "com.socialnmobile.colornote.activity.NoteList"));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			context.startActivity(intent);
		}catch(ActivityNotFoundException ex){
			ex.printStackTrace();
			Toast.makeText(context, "Can not find Zoiper app!\nHave you installed it?", Toast.LENGTH_LONG).show();
		}
	}
	
	
	
	public static void openZoip(Context context){
		Intent intent = new Intent();
		//intent.setComponent(new ComponentName("com.zoiper.android.heyu.app", "com.zoiper.android.ui.SplashScreen"));
        intent.setComponent(new ComponentName("com.pldthome.heyu", "com.pldthome.heyu.activities.registration.login.LoginActivity"));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			context.startActivity(intent);
		}catch(ActivityNotFoundException ex){
			try{
			Intent intentinstall = new Intent(Intent.ACTION_VIEW);
        	intentinstall.addCategory(Intent.CATEGORY_APP_BROWSER);
        	intentinstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intentinstall.setClassName("com.android.vending", "com.google.android.finsky.activities.MainActivity");
        	intentinstall.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.pldthome.heyu"));
        	context.startActivity(intentinstall);
			}catch(ActivityNotFoundException ek)
			{
				Toast.makeText(context, "Can not find Google Play !\nHave you installed it?", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	
	public static String getNameByNumber(Context context, String number){
		String name = null;
		
		
		String allNumber = getAllNumber(context,number);
		
		if(allNumber.equals(null))
		{
		    return null;
		}
		Cursor cursorOriginal =
            context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                new String[] {allNumber},null);  
        if( cursorOriginal != null && cursorOriginal.getCount() > 0) {
        	cursorOriginal.moveToFirst();
        	name = cursorOriginal.getString(cursorOriginal.getColumnIndex(Phone.DISPLAY_NAME));
        	cursorOriginal.close();
        }else
        {
        	cursorOriginal.close();
        }
        return name;
	}
	
	
	public static String getAllNumber(Context context, String number){
		
		if(number.equals(null) || number.equals(""))
		{
			return null;
		}
		
		String all_number = null;		
		//Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		Cursor cursorOriginal =
	            context.getContentResolver().query( 
	            		Uri.withAppendedPath(
	                            PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), new String[] {
	                            PhoneLookup._ID,
	                            PhoneLookup.NUMBER,
	                            PhoneLookup.DISPLAY_NAME,
	                            PhoneLookup.TYPE, PhoneLookup.LABEL }, null, null, null);
		
		   
		   if( cursorOriginal != null && cursorOriginal.getCount() > 0) {
				for( int i = 0; i < cursorOriginal.getCount(); i++ )  
				{  
					cursorOriginal.moveToPosition(i);  
					int nameFieldColumnIndex = cursorOriginal.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);    
					int NUmFieldColumnIndex = cursorOriginal.getColumnIndex(ContactsContract.PhoneLookup.NUMBER); 
					String name = cursorOriginal.getString(nameFieldColumnIndex);  
					Log.d("oju","name"+name);
					all_number = cursorOriginal.getString(NUmFieldColumnIndex);
					Log.d("oju","name"+number);
				}   
				
				cursorOriginal.close();
		   }else
		   {
		    if( cursorOriginal != null && cursorOriginal.getCount() > 0)     
				cursorOriginal.close();
		   }
		
	
        return all_number;
	}
	
	
		
		  public static String getLastOutgoingCall(Context context) {
	          final ContentResolver resolver = context.getContentResolver();
	          Cursor c = null;
	          try {
	              c = resolver.query(
	                  Calls.CONTENT_URI,
	                  new String[] {Calls.NUMBER},
	                  Calls.TYPE + " = " + Calls.OUTGOING_TYPE,
	                  null,
	                  Calls.DEFAULT_SORT_ORDER + " LIMIT 1");
	              if (c == null || !c.moveToFirst()) {
	                  return "";
	              }
	              return c.getString(0);
	          } finally {
	              if (c != null) c.close();
	          }
	      }
		
	
	
	
	public static void getCallDetail(final Context context, final Handler handler, final String strPhoneNumber){
		new Thread(new Runnable(){
			@Override
			public void run() {
				String name = "";
				String allNumber= "";
				
				
				try {
					// allNumber = getAllNumber(context,strPhoneNumber);
//					 
					 if(getAllNumber(context,strPhoneNumber)==null || getAllNumber(context,strPhoneNumber).length()<=0)
					{
							//
						 name = strPhoneNumber;
					}else
					{
						allNumber = getAllNumber(context,strPhoneNumber);
						name = getNameByNumber(context, allNumber);
						name = name!=null?name:strPhoneNumber;  
						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
//				name = name!=null?name:strPhoneNumber;   				
				
				Uri filterUri = Uri.parse("content://com.android.contacts/" 
		                + "data/phones/filter/" + allNumber);  
				Cursor cursorCantacts = context.getContentResolver().query(filterUri, null, null,  
				                null, null);  
				Bitmap bm = null;
				
				if (cursorCantacts!=null && cursorCantacts.getCount() > 0) { 
					cursorCantacts.moveToFirst();  
		            Long contactID = cursorCantacts.getLong(cursorCantacts  
		                    .getColumnIndex(RawContacts.CONTACT_ID));  
		            Uri uri = ContentUris.withAppendedId(  
		                    ContactsContract.Contacts.CONTENT_URI, contactID);  
		            InputStream input = ContactsContract.Contacts
		                    .openContactPhotoInputStream(context.getContentResolver(), uri);  
		            bm = BitmapFactory.decodeStream(input);   
		            cursorCantacts.close();
				}
				Log.e("liangbo","getCallDetail strPhoneNumber = " + strPhoneNumber);   
				Message msg = handler.obtainMessage(1);		
				Bundle data = new Bundle();
				data.putParcelable("bm", bm);
				data.putString("name", name);
				data.putString("p_number", strPhoneNumber);
				msg.setData(data);
				msg.sendToTarget();
			}			
		}).start();
		
	}
}
