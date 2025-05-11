package com.stephen.authservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(name = "roles", nullable = false)
  private String rolesAsString;

  // Convert the comma-separated string into a Set of roles
  public Set<String> getRoles() {
    return new HashSet<>(Arrays.asList(rolesAsString.split(",")));
  }

  // Convert a Set of roles into a comma-separated string for storage
  public void setRoles(Set<String> roles) {
    this.rolesAsString = String.join(",", roles);
  }

  public void addRole(String role) {
    Set<String> roles = getRoles();
    roles.add(role);
    setRoles(roles);
  }


}
