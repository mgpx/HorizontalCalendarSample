# HorizontalCalendarSample
HorizontalCalendarSample is an Android library for creating horizontal view for Calendar

# Features
- Customizable text color and size ,background color,number of days to display
- minSdkVersion 14

##Installation

Latest version of the library can be found on Maven Central.

# For Gradle users
Open your build.gradle Then, include the library as dependency:

  compile 'com.sahana.horizontalcalendarview:horizontalcalendarview:0.11'

# For Maven users
Add this dependency to your pom.xml:

<dependency>
  <groupId>com.sahana.horizontalcalendarview</groupId>
  <artifactId>horizontalcalendarview</artifactId>
  <version>0.11</version>
  <type>pom</type>
</dependency>

##Usage

Please see the /HorizontalCalendarSample-app app for a more detailed code example of how to use the library.

1.Add the HorizontalCalendar view to the layout you want to show.

<com.sahana.horizontalcalendarview.CustomHorizontalCalendar
        android:id="@+id/customHorizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="true"
        android:layout_marginTop="30dp"
        app:numOfDays="90"
        app:setLabel="@string/label" />
        
2.Configure attributes.

numOfDays - to set number of days to display in horizontal calendar (default: 60 days).

setLabel - to set your title/label to view.
