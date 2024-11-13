package io.okhi.okhiandroidverificationdemo.utils;

import io.okhi.android_core.models.OkHiLocation;
import io.okhi.android_core.models.OkHiUser;
public class DBAddressResponse {
  public OkHiUser user;
  public OkHiLocation location;

  DBAddressResponse(OkHiUser user,  OkHiLocation location) {
    this.user = user;
    this.location = location;
  }
}