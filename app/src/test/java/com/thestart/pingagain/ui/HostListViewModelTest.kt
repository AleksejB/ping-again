package com.thestart.pingagain.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HostListViewModelTest {

    val hostAddress = "www.google.co.uk"
    val invalidHostAddress = "w.invalid.address"

//    private lateinit var hostsViewModel: HostListViewModel


//    //I want a fake repo instead
//    @Before
//    fun setupViewModel() {
//        hostsViewModel = HostListViewModel(
//            ServiceLocator.provideHostListRepository(ApplicationProvider.getApplicationContext())
//        )
//    }
//
//    //Not sure if this is needed
//    @After
//    fun resetRepo() {
//        ServiceLocator.resetRepository()
//    }


    @Test
    fun pingHost_success_returnHigherThanZero() {
//        runBlockingTest {
//            //GIVEN: a view model and a host address
//            //WHEN: making a ping value
//            var it2 = 0
//            val pingValue = hostsViewModel.pingHost(hostAddress) {
//                it2 = it
//                Log.d("TESTING TAG", "$it")
//            }
            //THEN: assert that the ping was successful, so that the answer is above zero.
//            assertTrue(pingValue > 0)
    }


    @Test
    fun pingHost_failWithWrongUrl_returnInvalidUrlException() {
        //GIVEN: a view model and a host address
        //WHEN: making a ping value

        //THEN: assert that the ping failed, due to an invalid url
    }

    @Test
    fun pingHost_failWithWrongUrl_returnNoInternetErrorException() {
        //GIVEN: a view model and a host address
        //WHEN: making a ping

        //THEN: assert that ping failed due wrong url
    }

    @Test
    fun pingHost_failWithCorrectUrl_returnNoInternetErrorException() {
        //GIVEN: a view model and a host address
        //WHEN: making a ping value

        //THEN: assert that ping failed due to no internet
    }
}