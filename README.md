# CLMTE.exe
CLMTE.exe is a mobile Android application that helps users manage every aspect of vehicle ownership. It looks up detailed information about the vehicle, tracks maintenance and service history, and gets instant help from an AI assistant bot for roadside questions and automotive maintenance. If you need help keeping track of maintaining a single vehicle or multiple vehicles, CLMTE.exe will keep everything organize, accessable, and easy to use.

## Instructions for Building and Using

Steps to build and/or run the software:

1. Download and install Android
2. Download the files from GitHub
3. Download an emulator in Android Studio or hook up an Android device
4. Build the app
5. Install the app by running it on the emulator or Android device
   
Instructions for using the software:

1. On the opening screen, select the "My Garage"
2. Select the "+" buttom
3. Either select "Manual SetUp" or "VIN LookUp"
    * VIN LookUp
         * Enter your car's VIN
         * Select Find Car
         * Make sure it is the correct car details
         * Select "Continue"
         * Add a NickName and Mileage at the "Odometer"
         * Edit details as needed
         * Select "Save"
    * Manual SetUp
        * Add Car Type
        * Add Nickname
        * Add Year
        * Add Make
        * Add Model
        * Add Odometer
        * Add VIN
        * Add Drive Type
        * Add Transmission
        * Make sure everything above is filled in
        * Add Notes is optional
        * Select "Save"
4. It will bring you to your car list, and select your car details on your car's card
5. This is your car's details page. You can see all the car details
6. Want to update odometer, select "Update Odometer"
    * Enter updated mileage in the box
    * Select "Update"
7. Add Service History
    * Select "Service History"
    * Select "+" button
    * Select "Select Service"
    * Add the service you had
    * Select "Date"
    * Add date of service
    * Select "Save"
8. See Errors/Recommended maintenance
    * In your car details page, select the top right error icon button
    * Select "Fixed It" on the service you fixed
    * Select "Date"
    * Add the date
    * Select "Save"
    * If you want to clear all errors
       * Select the top right error icon button
       * Select the "New Car / Fixed All" button
9. Need help with maintenance or roadside assistance. On the home screen, select "AI Assistant"
    * Enter the question in the text box
    * Select "Send"
    * You will get a response in no time
10. Want to add a list of To-Do's? Go to the home screen
    * Select "MyWork"
    * Select the car you need to do maintenance on
    * Select the "+" button
    * Select "Title", add title
    * Select "Description", add description
    * Select "Save"
    * When completed, move the task to "DONE" by holding down the task and moving it over
    * To delete a task, drag and drop it into the red trash can
11. Settings
    * Select "Settings"
    * Select the switch next to "Dark Mode" to turn on dark mode
    * Change font size, move the slider under the "Font Size" left to right 

## Development Environment

To recreate the development environment, you need the following software and/or libraries with the specified versions:

* Android Studios
* Kotlin
* Jetpack Compose
* Firebase
* Gradle

## Useful Websites to Learn More

I found these websites useful in developing this software:

* [Learning Kotlin](https://kotlinlang.org/docs/kotlin-tour-hello-world.html#variables)
* [Learning Jetpack Compose](https://www.geeksforgeeks.org/android/android-jetpack-compose-tutorial/)
* [Android Studios](https://developer.android.com/studio)

## Future Work

The following items I plan to fix, improve, and/or add to this project in the future:

* [ ] Motion tracking for vehicles
* [ ] Add Photos folder
* [ ] Incorporate photos into logs
* [ ] Mobile notifications
* [ ] Add structure to app

