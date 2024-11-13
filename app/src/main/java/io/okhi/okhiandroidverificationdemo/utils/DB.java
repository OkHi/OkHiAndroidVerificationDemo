package io.okhi.okhiandroidverificationdemo.utils;

import io.okhi.android_core.models.OkHiLocation;
import io.okhi.android_core.models.OkHiUser;

public class DB {
  private static OkHiUser user;
  private static OkHiLocation location;

  public static void saveAddress(OkHiUser user, OkHiLocation location) {
    DB.user = user;
    DB.location = location;
  }
  public static DBAddressResponse fetchAddress() {
    if (DB.user == null || DB.location == null) return null;
    return new DBAddressResponse(DB.user, DB.location);
  }
}
