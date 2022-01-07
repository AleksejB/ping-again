package com.thestart.pingagain.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thestart.pingagain.util.PingStatus
import java.util.*

@Entity
data class Host(
    @ColumnInfo(name = "host_address") var hostAddress: String,
    @ColumnInfo(name = "ping_status") var pingStatus: String = PingStatus.NOT_STARTED,
    @ColumnInfo(name = "ping_value") var pingValue: Float? = -1f,
    @ColumnInfo(name = "ip_address") var ipAddress: String = "Make a ping to see",
    @ColumnInfo(name = "std") var standardDeviation: Float = 0f,
    @ColumnInfo(name = "min_value") var minValue: Float = 0f,
    @ColumnInfo(name = "max_value") var maxValue: Float = 0f,
    @ColumnInfo(name = "packets_sent") var packetsSent: Int = 0,
    @ColumnInfo(name = "packets_dropped") var packetsDropped: Int = 0,
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString()
)