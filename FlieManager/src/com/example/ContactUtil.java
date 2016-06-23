package com.example;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts.Data;
import android.util.Log;

public class ContactUtil {
	
	
	
	
	
	private static final String TAG = "ContactUtil";	
	/**
	 * 查询所有联系人姓名及电话号码
	 */
	public void readContacts(Context context){
		StringBuilder sb = new StringBuilder();
		ContentResolver cr = context.getContentResolver();
		
		// select * from contacts
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, 
				null, null, null, null);
		while(cursor.moveToNext()){
			String id = cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			int iHasPhoneNum = Integer.parseInt(cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
			sb.append(name + " (");
			
			if(iHasPhoneNum > 0){
				Cursor numCursor = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
						null, 
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, 
						null, null);
				while(numCursor.moveToNext()){
					String number = numCursor.getString(
							numCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					sb.append(number + ")");
				}
				numCursor.close();
			}
			sb.append("\r\n");
		}
		cursor.close();
		
		if(!sb.toString().isEmpty()){
			Log.d(TAG, "联系人:\r\n" + sb.toString());
		}
	}
	
	
	/**
	 * 根据名字中的某一个字进行模糊查询
	 * @param key
	 */
	private void getFuzzyQueryByName(Context context,String key){
		
		StringBuilder sb = new StringBuilder();
		ContentResolver cr = context.getContentResolver();
		String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
				    ContactsContract.CommonDataKinds.Phone.NUMBER};
		Cursor cursor = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				projection, 
				ContactsContract.Contacts.DISPLAY_NAME + " like " + "'%" + key + "%'", 
				null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String number = cursor.getString(
					cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			sb.append(name + " (").append(number + ")").append("\r\n");
		}
		cursor.close();
		
		if(!sb.toString().isEmpty()){
			Log.d(TAG, "查询联系人:\r\n" + sb.toString());
		}
	}
	
	/**
	 * 根据名字中的某一个字进行模糊查询
	 * @param key
	 */
	private void getFuzzyQueryByPhoneNumber(Context context,String key){
		
		StringBuilder sb = new StringBuilder();
		ContentResolver cr = context.getContentResolver();
		String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
				    ContactsContract.CommonDataKinds.Phone.NUMBER};
		Cursor cursor = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				projection, 
				ContactsContract.Contacts.DISPLAY_NAME + " like " + "'%" + key + "%'", 
				null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(
					cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String number = cursor.getString(
					cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			sb.append(name + " (").append(number + ")").append("\r\n");
		}
		cursor.close();
		
		if(!sb.toString().isEmpty()){
			Log.d(TAG, "查询联系人:\r\n" + sb.toString());
		}
	}
	
	
	private void getPinyinByHanzi(Context context,String name){
		ContentValues values = new ContentValues();
		ContentResolver cr = context.getContentResolver();
		
		Uri rawContactUri = cr.insert(
				ContactsContract.RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		
		if(name.length() > 0){
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
			values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
			cr.insert(ContactsContract.Data.CONTENT_URI, values);
			
			String[] projection = {"sort_key"};
			String where = ContactsContract.RawContacts.CONTACT_ID + "=" + rawContactId;
			Cursor cursor = cr.query(ContactsContract.RawContacts.CONTENT_URI, projection, where, null, null);
			if(cursor != null){
				cursor.moveToFirst();
				String pinyin = cursor.getString(cursor.getColumnIndex("sort_key"));
				Log.d(TAG, pinyin);
				
				String res = "";
				for(int i = 0; i < pinyin.length(); i ++){
					String temp = pinyin.substring(i, i+1);
					if(temp.matches("[a-zA-Z ]")){
						res += temp;
					}
				}
				res = res.substring(0, res.length()-1);
				Log.d(TAG, name + " translate = \"" + res.toLowerCase(Locale.getDefault()) + "\"");
			}
		}
		
		cr.delete(ContentUris.withAppendedId(
				ContactsContract.RawContacts.CONTENT_URI, rawContactId), 
				null, null);
	}

}
