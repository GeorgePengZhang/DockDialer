package com.pachong.mycontacts.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.flyco.tablayout.utils.ViewFindUtils;
import com.pachong.mycontacts.R;

public class SettingsFragment extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_layout, null);
		
		ViewFindUtils.find(view, R.id.id_settings_ringtone).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_display).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_security).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_wifi).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_mobile).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_areacode).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_callsetup).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_billing).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_contactsdisplay).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_importexport).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_account).setOnClickListener(this);
		ViewFindUtils.find(view, R.id.id_settings_about).setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_settings_ringtone:
			Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
			startActivity(intent);	
			break;
		case R.id.id_settings_display:
			Intent display = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
			startActivity(display);
			break;
		case R.id.id_settings_security:
			Intent Ring = new Intent(Settings.ACTION_SECURITY_SETTINGS);
			startActivity(Ring);
			break;
		case R.id.id_settings_wifi:
			Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
			startActivity(wifiSettings);
			break;
		case R.id.id_settings_mobile:
			Intent wirelessSetting = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			startActivity(wirelessSetting);
			break;
		case R.id.id_settings_areacode:
			Intent intentare = new Intent();
			intentare.setComponent(new ComponentName("com.pachong.mycontacts", "com.pachong.mycontacts.AreaCodeActivity"));
			try{
				startActivityForResult(intentare, 0);
				startActivity(intentare);
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.id_settings_callsetup:
			Log.d("TAG", "model:"+Build.MODEL+",product:"+Build.PRODUCT);
			boolean containsMT7 = Build.MODEL.contains("MT7");
			String className = null;
			if (containsMT7) {
				className = "com.mediatek.settings.CallSettings";
			} else {
				className = "com.android.phone.CallFeaturesSetting";
			}
			
			final Intent callsetup = new Intent(Intent.ACTION_MAIN);
			callsetup.setClassName("com.android.phone", className);
			callsetup.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(callsetup);
			break;
		case R.id.id_settings_billing:
			break;
		case R.id.id_settings_contactsdisplay:
			break;
		case R.id.id_settings_importexport:
			break;
		case R.id.id_settings_account:
			Intent account = new Intent(Settings.ACTION_SYNC_SETTINGS);
			account.putExtra(Settings.EXTRA_AUTHORITIES, new String[] {
                ContactsContract.AUTHORITY
            });
			account.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(account);
			break;
		case R.id.id_settings_about:
			new AlertDialog.Builder(getActivity(), R.style.NoBorderDialog)
			.setTitle("About TelPad")
			.setIcon(android.R.drawable.ic_dialog_info)
			//.setView(new EditText(DialtactsActivity.this))
			.setMessage("TELPAD MA7 V1.0")
			.setNegativeButton("Back",  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.show();
			break;

		default:
			break;
		}
	}

}
