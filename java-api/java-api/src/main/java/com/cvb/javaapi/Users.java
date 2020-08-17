package com.cvb.javaapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

  private int id;
  private String first_name;
  private String last_name;
  private String email;
  private String ip_address;
  private float latitude;
  private float longitude;
}