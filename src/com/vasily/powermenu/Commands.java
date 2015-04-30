package com.vasily.powermenu;

public class Commands {
  public static String REBOOT = "reboot";
  public static String RECOVERY = "reboot recovery";
  public static String DOWNLOAD = "reboot download";
  public static String BOOTLOADER = "reboot bootloader";
  public static String HOTBOOT = "busybox killall system_server";
  public static String SHUTDOWN = "reboot -p";
  //https://android.googlesource.com/platform/frameworks/base/+/android-4.4_r1/services/java/com/android/server/power/ShutdownThread.java
  public static String SET_SAFEBOOT = "setprop persist.sys.safemode 1";
}
