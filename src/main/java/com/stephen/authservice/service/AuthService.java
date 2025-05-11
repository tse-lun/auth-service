package com.stephen.authservice.service;

import com.stephen.authservice.domain.User;
import com.stephen.authservice.dto.AuthRequest;
import com.stephen.authservice.dto.AuthResponse;
import com.stephen.authservice.exceptions.UserAlreadyExistsException;
import com.stephen.authservice.repository.UserRepository;
import com.stephen.authservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(AuthRequest request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException("User already exists: " + request.getUsername());
    }
    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.addRole("ROLE_USER");
    userRepository.save(user);
  }

  public AuthResponse login(AuthRequest request) {
    var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
    var authentication = authenticationManager.authenticate(token);
    var authorities = authentication.getAuthorities();
    var details = (UserDetails) authentication.getPrincipal();
    var jwt = jwtUtil.generateTokenWithRoles(details.getUsername(), details.getAuthorities());
    return new AuthResponse(jwt, authorities.toString());
  }

}
