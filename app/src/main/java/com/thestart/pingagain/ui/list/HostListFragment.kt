package com.thestart.pingagain.ui.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.InetAddresses
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.thestart.pingagain.R
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.databinding.FragmentHostListBinding
import com.thestart.pingagain.util.OnItemClickListener
import com.thestart.pingagain.util.PingStatus
import com.thestart.pingagain.util.Pinger
import com.thestart.pingagain.util.isOnline
import kotlinx.coroutines.*

class HostListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHostListBinding? = null
    private val binding get() = _binding!!
    private lateinit var hostListViewModel: HostListViewModel
    private lateinit var adapter: HostListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHostListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewModelFactory = HostListViewModelFactory.createFactory(requireContext())
        hostListViewModel = viewModelFactory.create(HostListViewModel::class.java)

        var hostList = listOf<Host>()

        adapter = HostListAdapter(this)
        binding.hostListRecyclerView.adapter = adapter
        binding.hostListRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        initAction()

        hostListViewModel.observeHosts().observe(viewLifecycleOwner, {
            hostList = it
            if (it.isEmpty()) {
                binding.hostListRecyclerView.visibility = View.GONE
                with(binding.noHostsTextView) {
                    visibility = View.VISIBLE
                    text = resources.getString(R.string.no_hosts)
                }
            } else {
                binding.hostListRecyclerView.visibility = View.VISIBLE
                binding.noHostsTextView.visibility = View.GONE
            }
            Log.d("ObserverTAG", "Observer Action")
            adapter.submitList(it)
        })

        //this seems ugly
        binding.hostAddressEditText.setOnEditorActionListener { v, actionId, event ->
            val hostAddress = binding.hostAddressEditText.text.toString()
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (validateHostAddressOrIP(hostAddress)) {
                        hostListViewModel.insertHost(hostAddress)
                        binding.hostAddressEditText.setText("")
                        true
                    } else {
                        return@setOnEditorActionListener false
                    }
                } else -> false
            }
        }

        binding.addBtn.setOnClickListener {
            val hostAddress = binding.hostAddressEditText.text.toString()
            if (validateHostAddressOrIP(hostAddress)) {
                hostListViewModel.insertHost(hostAddress)
                binding.hostAddressEditText.setText("")
            } else {
                return@setOnClickListener
            }
        }

        binding.pingAllHostsBtn.setOnClickListener {
            if (isOnline(requireContext())) {
                for (host in hostList) {
                    val hostInProgress = Host(
                        hostAddress =  host.hostAddress,
                        pingStatus = PingStatus.IN_PROGRESS,
                        pingValue = 0f,
                        ipAddress = host.ipAddress,
                        id = host.id
                    )
                    hostListViewModel.updateHost(hostInProgress)

                    Pinger.pingHostShort(host.hostAddress) {
                        it?.let {
                            if (it.avg == 0f) {
                                val hostComplete = Host(
                                    hostAddress = host.hostAddress,
                                    pingStatus = PingStatus.ERROR,
                                    pingValue = it.avg,
                                    ipAddress = it.ip,
                                    standardDeviation = it.stddev,
                                    minValue = it.min,
                                    maxValue = it.max,
                                    packetsSent = it.sent,
                                    packetsDropped = it.dropped,
                                    id = host.id
                                )
                                hostListViewModel.updateHost(hostComplete)
                            } else {
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
                                hostListViewModel.updateHost(hostComplete)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(host: Host) {
        val action = HostListFragmentDirections.actionHostListFragmentToDetailFragment(host.id)
        findNavController().navigate(action)
    }

    private fun validateHostAddressOrIP(hostAddress: String): Boolean {
        val regexForHostAddress = Regex("^(?!-)[A-Za-z0-9-]+([\\-\\.]{1}[a-z0-9]+)*\\.[A-Za-z]{2,6}$")
        val zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])"
        val regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255
        val regexForIPv4Validation = Regex(regex)

        if (hostAddress.isEmpty()) {
            Toast.makeText(
                requireActivity().applicationContext,
                "The host address can't be empty",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (!regexForHostAddress.matches(hostAddress) && !regexForIPv4Validation.matches(hostAddress)) {
            Toast.makeText(
                requireActivity().applicationContext,
                "The host address/IP is not valid",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        else return true
    }

    private fun initAction() {

        val callback = Callback()
        val itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(binding.hostListRecyclerView)
    }

    inner class Callback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val host = (viewHolder as HostViewHolder).getCurrentHost()
            val position = (viewHolder as HostViewHolder).adapterPosition
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

            if (prefs.getBoolean(requireContext().getString(R.string.warning_dialog_key), true)) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle(requireContext().getString(R.string.dialog_prefs_title))
                    .setMessage(requireContext().getString(R.string.delete_host_message))
                    .setPositiveButton(
                        requireContext().getString(R.string.yes),
                        DialogInterface.OnClickListener { dialog, which ->
                            hostListViewModel.deleteHost(host)
                        }
                    )
                    .setNeutralButton(
                        requireContext().getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                            adapter.notifyItemChanged(position)
                        }
                    )
                    .setNegativeButton(
                        requireContext().getString(R.string.yes_dont_show_again),
                        DialogInterface.OnClickListener { dialog, which ->
                            hostListViewModel.deleteHost(host)
                            prefs.edit().putBoolean(
                                requireContext().getString(R.string.warning_dialog_key),
                                false
                            ).apply()
                        }
                    )
                builder.create()

                builder.setOnCancelListener {
                    it.dismiss()
                    adapter.notifyItemChanged(position)
                }
                builder.show()

            } else {
                hostListViewModel.deleteHost(host)
            }
        }
    }
}