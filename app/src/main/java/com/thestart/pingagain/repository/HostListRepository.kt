package com.thestart.pingagain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.data.source.HostDao
import com.thestart.pingagain.data.source.HostDatabase

class HostListRepository(private val hostDao: HostDao) {

    fun insertHost(host: Host) {
        return hostDao.insertHost(host)
    }

    fun observeHosts():LiveData<List<Host>> {
        return hostDao.observeAllHosts()
    }

    fun observeHost(hostId: String): LiveData<Host> {
        return hostDao.observeHost(hostId)
    }

    fun updateHost(host: Host) {
        hostDao.updateHost(host)
    }

    fun deleteHost(host: Host) {
        hostDao.deleteHost(host)
    }

    companion object {
        @Volatile
        private var instance: HostListRepository? = null

        fun getInstance(context: Context): HostListRepository? {
            return instance ?: synchronized(HostListRepository::class.java) {
                if (instance == null) {
                    val database = HostDatabase.getDatabase(context)
                    instance = HostListRepository(database.hostsDao())
                }
                return instance
            }
        }
    }
}