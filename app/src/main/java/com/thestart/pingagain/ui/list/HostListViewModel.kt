package com.thestart.pingagain.ui.list

import android.content.Context
import androidx.lifecycle.*
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.repository.HostListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException

class HostListViewModel(private val hostListRepo: HostListRepository): ViewModel() {

    fun insertHost(hostAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val host = Host(hostAddress)
            hostListRepo.insertHost(host)
        }
    }

    fun observeHosts(): LiveData<List<Host>> {
        return hostListRepo.observeHosts()
    }

    fun updateHost(host: Host) {
        viewModelScope.launch(Dispatchers.IO) {
            hostListRepo.updateHost(host)
        }
    }

    fun deleteHost(host: Host) {
        viewModelScope.launch(Dispatchers.IO) {
            hostListRepo.deleteHost(host)
        }
    }
}

class HostListViewModelFactory(
    private val hostListRepo: HostListRepository?
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(HostListRepository::class.java)
                .newInstance(hostListRepo)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }

    companion object {
        fun createFactory(context: Context): HostListViewModelFactory {
            return HostListViewModelFactory(HostListRepository.getInstance(context))
        }
    }
}