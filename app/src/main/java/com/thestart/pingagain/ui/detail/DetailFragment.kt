package com.thestart.pingagain.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.thestart.pingagain.R
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.databinding.FragmentDetailBinding
import com.thestart.pingagain.util.PingStatus
import com.thestart.pingagain.util.Pinger
import com.thestart.pingagain.util.isOnline

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var host: Host
    private lateinit var hostId: String
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hostId = DetailFragmentArgs.fromBundle(requireArguments()).hostId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val detailViewModelFactory = DetailViewModelFactory.createFactory(requireContext())
        detailViewModel = detailViewModelFactory.create(DetailViewModel::class.java)

        detailViewModel.observeHost(hostId).observe(viewLifecycleOwner, {
            host = it
            binding.apply {
                hostAddressTextView.text = resources.getString(R.string.host_address_is, it.hostAddress)
                ipAddressTextView.text = resources.getString(R.string.ip_address_is, it.ipAddress)
                pingStatusTextView.text = resources.getString(R.string.status_of_ping, it.pingStatus)
                averageValueStdTextView.text = resources.getString(R.string.average_ping_value_std_is, "%.2f".format(it.pingValue), "%.2f".format(it.standardDeviation))
                minMaxValuesTextView.text = resources.getString(R.string.min_max_values_are, "%.2f".format(it.minValue), "%.2f".format(it.maxValue))
                packetsSentTextView.text = resources.getString(R.string.packets_sent_is, it.packetsSent)
                packetsLossTextView.text = resources.getString(R.string.packet_loss_is, it.packetsDropped)
            }
        })

        binding.pingThisHostShortBtn.setOnClickListener {
            if (isOnline(requireContext())) {
                val hostInProgress = Host(
                    hostAddress = host.hostAddress,
                    pingStatus = PingStatus.IN_PROGRESS,
                    pingValue = 0f,
                    ipAddress = host.ipAddress,
                    standardDeviation = 0f,
                    minValue = 0f,
                    maxValue = 0f,
                    packetsSent = 0,
                    packetsDropped = 0,
                    id = host.id
                )
                detailViewModel.updateHost(hostInProgress)

                Pinger.pingHostShort(host.hostAddress) {
                    it?.let {
                        val hostComplete = Host(
                            hostAddress = host.hostAddress,
                            pingStatus = PingStatus.COMPLETE,
                            pingValue = it.avg,
                            ipAddress = it.ip,
                            standardDeviation = it.stddev,
                            minValue = it.min,
                            maxValue = it.max,
                            packetsSent = it.sent,
                            packetsDropped = it.dropped,
                            id = host.id
                        )
                        detailViewModel.updateHost(hostComplete)
                    }
                }
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }

        binding.pingThisHostLongBtn.setOnClickListener {
            if (isOnline(requireContext())) {
                val hostInProgress = Host(
                    hostAddress = host.hostAddress,
                    pingStatus = PingStatus.IN_PROGRESS,
                    pingValue = 0f,
                    ipAddress = host.ipAddress,
                    standardDeviation = 0f,
                    minValue = 0f,
                    maxValue = 0f,
                    packetsSent = 0,
                    packetsDropped = 0,
                    id = host.id
                )
                detailViewModel.updateHost(hostInProgress)

                Pinger.pingHostLong(host.hostAddress) {
                    it?.let {
                        val hostComplete = Host(
                            hostAddress = host.hostAddress,
                            pingStatus = PingStatus.COMPLETE,
                            pingValue = it.avg,
                            ipAddress = it.ip,
                            standardDeviation = it.stddev,
                            minValue = it.min,
                            maxValue = it.max,
                            packetsSent = it.sent,
                            packetsDropped = it.dropped,
                            id = host.id
                        )
                        detailViewModel.updateHost(hostComplete)
                    }
                }
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }
}