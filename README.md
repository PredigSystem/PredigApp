# PredigApp
![alt text](https://www.dropbox.com/s/l70jkqvriviixyu/logo_little.png?raw=1)

Predig System Android Application

PredigSystem is a mobile application that allows people calculate their blood pressure, consult all their results in accessible graphics and stores the results in a database where the doctor can access it and analyses the results of a patient. Also, the app notifies if the blood pressure is lower or higher than the normal. And finally, contains a map with the hospitals and clinics near of the user’s location.

This application is working with the [iHealth BP5](https://ihealthlabs.com/blood-pressure-monitors/wireless-blood-pressure-monitor/) device and its [opened API available on GitHub](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android).

More information about the System Requirements can be found [here](https://www.dropbox.com/s/sbn85huq75eil08/Requirements.pdf?dl=0).

Website template can be found [here](https://github.com/PredigSystem/PredigWebApp-prototype). Check out the [demo](https://predigsystem-webapp.herokuapp.com/login.html)

## Screenshots
![alt text](https://www.dropbox.com/home/BP%20App?preview=1.jpg)

![alt text](https://www.dropbox.com/s/y2sg28jwa2697qp/2.jpg?raw=1)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You only need the default tools for Android programming.


### Installing

First step is to clone the git repository on your computer:

```
git clone git@github.com:PredigSystem/PredigApp.git
```

At this moment you will be able to run it on a simulator or any device with an SDK version greater or equals than 15.

```
minSdkVersion 15
```

You can also customize whatever you want:
* Layouts (`res/layout` folder) have the vertical and horizontal design for each one.
* Java code distribution is:
```
predigsystem.udl.org.predigsystem
                |
                |-Activities
                |
                |-Database
                |
                |-Fragments
                |
                |-JavaClasses
```
* Almost all the layouts are fragments. `ActivityHome` the responsible of creating the `NavigationDrawer` and loading the fragments.

## Running the tests

There aren't tests at the moment. This feature will be implemented in a future.

### Break down into end to end tests

TODO: Explain what these tests test and why

```
Give an example
```

### And coding style tests

TODO: Explain what these tests test and why

```
Give an example
```

## Deployment

The used libraries for the project are: 

- [AndroidIcons](https://github.com/mikepenz/Android-Iconics) (For [FontAwesome](http://fontawesome.io/icons/) and [Material Icons](https://material.io/icons/))
- [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer/blob/develop/README.md)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [ScrollCalendar](https://github.com/RafalManka/ScrollCalendar)
- [BottomBar](https://github.com/roughike/BottomBar)
- [GooglePlayServices](https://developers.google.com/android/guides/setup) (For [Maps](https://developers.google.com/maps/documentation/android-api/?hl=es) and [Location](https://developer.android.com/training/location/index.html))
- [ExpandableLayout](https://github.com/AAkira/ExpandableLayout)
- [Retrofit](http://square.github.io/retrofit/)

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

TODO: We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

- [Sultan](https://github.com/sultanbeisen)
- [Didac](https://github.com/didacflorensa)
- [Pau](https://github.com/pbalaguer19)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* To all the used libraries authors.
* To all Computer Science Master teachers from University of Lleida.
* To all the Computer Science Master students for the Focus Group Session debaates.
* To all the people that helped us during the designing, developing and testing phases.
