package com.thestart.pingagain.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.thestart.pingagain.data.model.Host
import com.thestart.pingagain.repository.HostListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException

class DetailViewModel(private val repository: HostListRepository): ViewModel() {

    fun observeHost(hostId: String): LiveData<Host> {
        return repository.observeHost(hostId)
    }

    fun updateHost(host: Host) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHost(host)
        }
    }

}

class DetailViewModelFactory(private val repository: HostListRepository?): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(HostListRepository::class.java)
                .newInstance(repository)
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
        fun createFactory(context: Context): DetailViewModelFactory {
            return DetailViewModelFactory(HostListRepository.getInstance(context))
        }
    }

}