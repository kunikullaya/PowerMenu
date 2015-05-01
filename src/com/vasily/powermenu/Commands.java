package com.vasily.powermenu;

public class Commands {
  public static String REBOOT = "reboot";
  public static String RECOVERY = "reboot recovery";
  public static String DOWNLOAD = "reboot download";
  public static String BOOTLOADER = "reboot bootloader";
  public static String HOTBOOT = "busybox killall system_server";
  public static String SHUTDOWN = "reboot -p";
  public static String SAFEBOOT = "setprop persist.sys.safemode 1 | reboot";
}
