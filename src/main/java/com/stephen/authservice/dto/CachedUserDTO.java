package com.stephen.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedUserDTO implements Serializable {
  private String username;
  private String password;
  @Fetch(FetchMode.JOIN)
  private Set<String> roles;
}