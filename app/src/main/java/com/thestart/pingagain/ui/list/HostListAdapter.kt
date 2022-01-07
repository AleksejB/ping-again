package com.thestart.pingagain.ui.list

import android.media.Image
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thestart.pingagain.PingApplication
import com.thestart.pingagain.R
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.util.OnItemClickListener
import com.thestart.pingagain.util.PingStatus
import kotlin.math.roundToInt

class HostListAdapter(val itemClickListener: OnItemClickListener): ListAdapter<Host, HostViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostViewHolder {
        return HostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: HostViewHolder, position: Int) {
        val host = getItem(position)
        holder.host = host
        holder.bind(host, itemClickListener)
    }

}

class HostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    lateinit var host: Host
    val hostAddresTV = itemView.findViewById<TextView>(R.id.host_address_text_view)
    val pingValueTV = itemView.findViewById<TextView>(R.id.ping_value_text_view)
    val pingStatusDoneIV = itemView.findViewById<ImageView>(R.id.ping_status_done_image_view)
    val pingStatusPB = itemView.findViewById<ProgressBar>(R.id.ping_status_progress_bar)
    val pingStatusErrorIV = itemView.findViewById<ImageView>(R.id.ping_status_error_image_view)

    fun bind(host: Host, itemClickListener: OnItemClickListener) {
        hostAddresTV.text = makeBoldString(host.hostAddress)
        pingValueTV.text = when (host.pingValue) {
            -1f -> "--ms"
            0f -> "--ms"
            else -> "${host.pingValue?.roundToInt()}ms"
        }
        when (host.pingStatus) {
            PingStatus.COMPLETE -> {
                pingStatusErrorIV.visibility = View.GONE
                pingStatusPB.visibility = View.GONE
                pingStatusDoneIV.visibility = View.VISIBLE
            }
            PingStatus.IN_PROGRESS -> {
                pingStatusErrorIV.visibility = View.GONE
                pingStatusDoneIV.visibility = View.GONE
                pingStatusPB.visibility = View.VISIBLE
            }
            PingStatus.NOT_STARTED -> {
                pingStatusErrorIV.visibility = View.GONE
                pingStatusDoneIV.visibility = View.GONE
                pingStatusPB.visibility = View.GONE
            }
            PingStatus.ERROR -> {
                pingStatusPB.visibility = View.GONE
                pingStatusDoneIV.visibility = View.GONE
                pingStatusErrorIV.visibility = View.VISIBLE
            }
        }

        itemView.setOnClickListener {
            itemClickListener.onItemClicked(host)
        }
    }

    fun getCurrentHost(): Host = host

    private fun makeBoldString(stringToBold: String): CharSequence {
        return SpannableStringBuilder().bold { append(stringToBold) }
    }
}

class DiffCallback: DiffUtil.ItemCallback<Host>() {
    override fun areItemsTheSame(oldItem: Host, newItem: Host): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Host, newItem: Host): Boolean {
        return oldItem.pingValue == newItem.pingValue
                && oldItem.hostAddress == newItem.hostAddress
                && oldItem.pingStatus == newItem.pingStatus
    }
}