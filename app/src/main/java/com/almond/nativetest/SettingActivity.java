package com.almond.nativetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        Preference pref1 = (Preference)findPreference("version");
        pref1.setOnPreferenceClickListener(this);

        Preference pref2 = (Preference)findPreference("versioncheck");
        pref2.setOnPreferenceClickListener(this);

        Preference box1 = (Preference)findPreference("Setting_Push");
        box1.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        // #Version
        if(preference.getKey().toString().equals("version"))
        {
            // Get Version
            SharedPreferences pref = getSharedPreferences("Setting", 0);
            String Version = getResources().getString(R.string.Version);
            Toast.makeText(this, Version, Toast.LENGTH_SHORT).show();
        }

        // #Check Version
        else if(preference.getKey().toString().equals("versioncheck"))
        {
            // Compare Version
            /*try {
                HttpPost httpPost = new HttpPost(mContext, "VersionCheck");
                String ServerVersion = httpPost.execute().get();

                if(!ServerVersion.equals(getResources().getString(R.string.Version)))
                    Toast.makeText(getApplicationContext(), "새 버전이 출시되었습니다. 버전을 업데이트 해주세요", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "이 앱은 가장 최신 버전입니다.", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "최신 버전 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT);
            }*/
        }

        return false;
    }
}
