-//File: AppConfig.java
  package edu.ccrm.config;

import java.nio.file.Paths;

public class AppConfig {
  private static AppConfig instance;
  private final String dataDirectory;

  private AppConfig() {
    //private constructor for singleton
    this.dataDirectory=
      paths.get("data").toAbsolutePath().toString();
  }

  public static AppConfig getInstance() {
    if(instance == null) {
      instance = new AppConfig();
    }
    return instance;
  }
  public String getDataDirectory() {
    return dataDirectory() {
    }
}
  
