package com.thestart.pingagain.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.thestart.pingagain.R
import com.thestart.pingagain.util.PingCounts

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager
            .getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager
            .getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences?.let {
            if (key == resources.getString(R.string.short_pings_key)) {
                PingCounts.shortPingsCount = it.getString(key, "5")!!
            }
            if (key == resources.getString(R.string.long_pings_key)) {
                PingCounts.longPingsCount = it.getString(key, "50")!!
            }
        }
    }


}