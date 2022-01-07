# PingAgain
## App Description 
This app's purpose is to showcase my ability in Kotlin. PingAgain is an app 
for testing network connectivity to different servers.
The app uses the following android libraries and classes:
- NavigationUI
- Kotlin Coroutines
- Room Database
- PreferenceFragmentCompat
- ViewModel
- RecyclerView
- Regex
- LiveData

For pinging functionality a 3rd party library was used:
- [Network Diagnosis Library by qiniu](https://github.com/qiniu/android-netdiag)

## App's functionality
The app allows the user to enter an IPv4 address or a domain address, which will be added to 
the list of the availible hosts to ping. The user can perform short pings for all the hosts
in the list at the same time, or choose between long or short ping for an individual ping by 
clicking on the host in the list. Hosts can be deleted by swiping right.

Clicking on the host in the list will bring up the "Host Info" screen, which, as forementioned above,
allows the user to perform short or long pings and shows much more information about the host.
For instance, the IP address of the host, packets lost and sent, standard deviation of the 
average ping value.

## Short Pings vs Long Pings
Short pings are pings for which the app will send 5-15 ICMP packets to the host (the nubmer can 
be changed in settings) before avergaing the ping value.

Long Pings are pings for which the app will send 50-150 ICMP packets to the host (also can be
changed in settings) before averaging the ping value.

## Screenshots & Video
https://user-images.githubusercontent.com/87714405/148594460-9b78f590-29a7-4a63-996d-4e1fa7dbbf98.mp4

<img src="https://user-images.githubusercontent.com/87714405/148593422-49bd5db3-004d-41c5-9ae7-066992a2028a.jpg" width="290" height="630"> <img src="https://user-images.githubusercontent.com/87714405/148593441-641f39d9-4bf5-4af2-bf97-4cb30223d2d7.jpg" width="290" height="630"> <img src="https://user-images.githubusercontent.com/87714405/148593451-e7a8a86a-16e1-4576-a134-e0d780409a99.jpg" width="290" height="630"> <img src="https://user-images.githubusercontent.com/87714405/148593466-5eb5acc0-3f2d-45cd-94ae-c83b258cc56c.jpg" width="290" height="630">

## License
The MIT License. [License](LICENSE)
