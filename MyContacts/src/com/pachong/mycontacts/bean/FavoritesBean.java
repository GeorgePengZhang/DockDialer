package com.pachong.mycontacts.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.pachong.mycontacts.ContactTileLoaderFactory;


/**
 * @ClassName: CallsLogBean
 * @Description: TODO
 * @author: steven zhang
 * @date: Sep 27, 2016 2:31:30 PM
 */
public class FavoritesBean implements Parcelable {

	private long id;
	private String name;
	private int starred;
	private String photouri;
	private String lookupuri;
	private String number;
	private String numbertype;
	private String numberlabel;
	
	public FavoritesBean() {
	}
	
	public FavoritesBean(long id, String name, int starred, String photouri, String lookupuri,
			String number, String numbertype, String numberlabel) {
		this.id = id;
		this.name = name;
		this.starred = starred;
		this.photouri = photouri;
		this.lookupuri = lookupuri;
		this.number = number;
		this.numbertype = numbertype;
		this.numberlabel = numberlabel;
	}

	/**
	 * @param source
	 */
	public FavoritesBean(Parcel source) {
		id = source.readLong();
		name = source.readString();
		starred = source.readInt();
		photouri = source.readString();
		lookupuri = source.readString();
		number = source.readString();
		numbertype = source.readString();
		numberlabel = source.readString();
	}
	
//	@Override
//	public boolean equals(Object o) {
//		FavoritesBean bean = (FavoritesBean)o;
//		if (TextUtils.isEmpty(this.name)) {
//			if (TextUtils.isEmpty(bean.name)) {
//				return this.number.equals(bean.number);
//			} else {
//				return false;
//			}
//		} else {
//			if (TextUtils.isEmpty(bean.name)) {
//				return false;
//			} else {
//				return this.number.equals(bean.number);
//			}
//		}
//	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStarred() {
		return starred;
	}

	public void setStarred(int starred) {
		this.starred = starred;
	}

	public String getPhotouri() {
		return photouri;
	}

	public void setPhotouri(String photouri) {
		this.photouri = photouri;
	}

	public String getLookupuri() {
		return lookupuri;
	}

	public void setLookupuri(String lookupuri) {
		this.lookupuri = lookupuri;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumbertype() {
		return numbertype;
	}

	public void setNumbertype(String numbertype) {
		this.numbertype = numbertype;
	}

	public String getNumberlabel() {
		return numberlabel;
	}

	public void setNumberlabel(String numberlabel) {
		this.numberlabel = numberlabel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id:").append(id).append(",")
		.append("name:").append(name).append(",")
		.append("starred:").append(starred).append(",")
		.append("photouri:").append(photouri).append(",")
		.append("lookupuri:").append(lookupuri).append(",")
		.append("number:").append(number).append(",")
		.append("numbertype:").append(numbertype).append(",")
		.append("numberlabel:").append(numberlabel).append(",");
		
		return builder.toString();
	}
	
	/**
	 * 将数据库中的通话记录数据转化为bean对象
	 * @param cursor
	 * @return
	 */
	public static FavoritesBean getBeanFromCursor(Cursor cursor) {
		FavoritesBean bean = null;
		
		if (cursor != null) {
			long id = cursor.getLong(ContactTileLoaderFactory.CONTACT_ID);
			String name = cursor.getString(ContactTileLoaderFactory.DISPLAY_NAME);
			int starred = cursor.getInt(ContactTileLoaderFactory.STARRED);
			String photouri = cursor.getString(ContactTileLoaderFactory.PHOTO_URI);
			String lookupuri= cursor.getString(ContactTileLoaderFactory.LOOKUP_KEY);
			String number = cursor.getString(ContactTileLoaderFactory.PHONE_NUMBER);
			String numbertype = cursor.getString(ContactTileLoaderFactory.PHONE_NUMBER_TYPE);
			String numberlabel = cursor.getString(ContactTileLoaderFactory.PHONE_NUMBER_LABEL);
			
			bean = new FavoritesBean(id, name, starred, photouri, lookupuri, number, numbertype, numberlabel);
		}
		
		return bean;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeInt(starred);
		dest.writeString(photouri);
		dest.writeString(lookupuri);
		dest.writeString(number);
		dest.writeString(numbertype);
		dest.writeString(numberlabel);
	}
	
	public static final Parcelable.Creator<FavoritesBean> CREATOR = new Parcelable.Creator<FavoritesBean>() {

		@Override
		public FavoritesBean createFromParcel(Parcel source) {
			return new FavoritesBean(source);
		}

		@Override
		public FavoritesBean[] newArray(int size) {
			return new FavoritesBean[size];
		}
	};
}
