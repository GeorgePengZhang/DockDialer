package com.pachong.mycontacts;

import android.content.AsyncQueryHandler;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Used to create {@link CursorLoader}s to load different groups of {@link ContactTileView}s
 */
public final class ContactTileLoaderFactory {

    public final static int CONTACT_ID = 0;
    public final static int DISPLAY_NAME = 1;
    public final static int STARRED = 2;
    public final static int PHOTO_URI = 3;
    public final static int LOOKUP_KEY = 4;
    public final static int CONTACT_PRESENCE = 5;
    public final static int CONTACT_STATUS = 6;

    // Only used for StrequentPhoneOnlyLoader
    public final static int PHONE_NUMBER = 5;
    public final static int PHONE_NUMBER_TYPE = 6;
    public final static int PHONE_NUMBER_LABEL = 7;

    private static final String[] COLUMNS = new String[] {
    	ContactsContract.Contacts._ID, // ..........................................0
    	ContactsContract.Contacts.DISPLAY_NAME, // .................................1
    	ContactsContract.Contacts.STARRED, // ......................................2
    	ContactsContract.Contacts.PHOTO_URI, // ....................................3
    	ContactsContract.Contacts.LOOKUP_KEY, // ...................................4
    	ContactsContract.Contacts.CONTACT_PRESENCE, // .............................5
    	ContactsContract.Contacts.CONTACT_STATUS, // ...............................6
    };

    /**
     * Projection used for the {@link Contacts#CONTENT_STREQUENT_URI}
     * query when {@link ContactsContract#STREQUENT_PHONE_ONLY} flag
     * is set to true. The main difference is the lack of presence
     * and status data and the addition of phone number and label.
     */
    private static final String[] COLUMNS_PHONE_ONLY = new String[] {
    	ContactsContract.Contacts._ID, // ..........................................0
    	ContactsContract.Contacts.DISPLAY_NAME, // .................................1
    	ContactsContract.Contacts.STARRED, // ......................................2
    	ContactsContract.Contacts.PHOTO_URI, // ....................................3
    	ContactsContract.Contacts.LOOKUP_KEY, // ...................................4
        Phone.NUMBER, // ..........................................5
        Phone.TYPE, // ............................................6
        Phone.LABEL // ............................................7
    };

    public static void createStrequentLoader(AsyncQueryHandler handler) {
    	handler.startQuery(4, null, ContactsContract.Contacts.CONTENT_STREQUENT_URI, COLUMNS, null, null, null);
    }

    public static void createStrequentPhoneOnlyLoader(AsyncQueryHandler handler) {
        Uri uri = ContactsContract.Contacts.CONTENT_STREQUENT_URI.buildUpon()
                .appendQueryParameter("strequent_phone_only", "true").build();

        handler.startQuery(1, null, uri, COLUMNS_PHONE_ONLY, ContactsContract.Contacts.STARRED + "=?", new String[]{"1"}, null);
    }

    public static void createStarredLoader(AsyncQueryHandler handler) {
    	handler.startQuery(2, null, ContactsContract.Contacts.CONTENT_URI, COLUMNS, ContactsContract.Contacts.STARRED + "=?", new String[]{"1"}, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    public static void createFrequentLoader(AsyncQueryHandler handler) {
    	handler.startQuery(3, null, Uri.withAppendedPath(
        		ContactsContract.Contacts.CONTENT_URI, "frequent"), COLUMNS, ContactsContract.Contacts.STARRED + "=?", new String[]{"0"}, null);
    }
}