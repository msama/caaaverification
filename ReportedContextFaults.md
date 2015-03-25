# Introduction #

This is a list of reported context awareness faults which we have found on the web. Feel free to report more.


# Details #

Easy wifi

from http://forum.devicescape.com/index.php?topic=1035.0

  * Someone having start issues


PowerManager

From: http://www.xphonesoftware.com/pm.html#Change_history

  * Fixed brightness problems when keyguard is up (i.e. screen locked)
  * Fixed bug in, keep awake while keyboard open, which kept the device awake even if the keyboard was closed
  * Fixed race condition where the remote service could be stopped by mistake even if the monitor is enabled
  * Fixed problem with incorrect profile being applied during application launch because the service was not completely initialized


Locale

From: http://www.twofortyfouram.com/notes.html

  * Fixed a leak when the service is repeatedly enabled/disabled via the Menu
  * Fixed a bug where Time conditions could get stuck, causing performance problems and delays in triggering Time-based situations
  * Fixed a rare crash when GPS doesn't report the number of available satellites
  * Fixed a couple of bugs where location could sometimes inexplicably default to Mountain View, CA or Tulsa, OK when location could not be determined
  * Fixed regression where GPS wouldn't turn off if the Disable Locale button was pressed while GPS was on
  * Fixed a bug with Time Conditions spanning across the PM-AM boundary


Locify

From http://www.locify.com/more/changelog & http://code.google.com/p/locify/issues/

  * GPS connecting http://code.google.com/p/locify/issues/detail?id=2&can=1
  * Bluetooth doesn't reconnect http://code.google.com/p/locify/issues/detail?id=13&can=1 http://code.google.com/p/locify/issues/detail?id=14&can=1


MAEMO

  * https://bugs.maemo.org/show_bug.cgi?id=7932
  * https://bugs.maemo.org/show_bug.cgi?id=8544
  * https://bugs.maemo.org/show_bug.cgi?id=6296