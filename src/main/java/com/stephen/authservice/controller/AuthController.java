package com.stephen.authservice.controller;

import com.stephen.authservice.dto.AuthRequest;
import com.stephen.authservice.dto.AuthResponse;
import com.stephen.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody AuthRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("成功註冊新用戶");
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    var response = authService.login(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

}