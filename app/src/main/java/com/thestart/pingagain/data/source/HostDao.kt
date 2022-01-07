package com.thestart.pingagain.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.thestart.pingagain.data.model.Host

@Entity
@Dao
interface HostDao {

    @Query("SELECT * FROM host")
    fun observeAllHosts(): LiveData<List<Host>>

    @Query("SELECT * FROM host WHERE id = :hostId")
    fun observeHost(hostId: String): LiveData<Host>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHost(host: Host)

    @Update
    fun updateHost(host: Host): Int

    @Delete
    fun deleteHost(host: Host)
}