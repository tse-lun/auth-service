package com.stephen.authservice.service;

import com.stephen.authservice.domain.User;
import com.stephen.authservice.dto.CachedUserDTO;
import com.stephen.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private static final String USER_CACHE_PREFIX = "auth:user:";
  private final UserRepository userRepository;
  private final RedisTemplate<String, CachedUserDTO> redisTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String cacheKey = USER_CACHE_PREFIX + username;
    CachedUserDTO cachedUser = redisTemplate.opsForValue().get(cacheKey);

    if (cachedUser != null) {
      log.info("✅ Cache HIT for user '{}'", username);
    } else {
      log.info("❌ Cache MISS for user '{}'", username);
      User userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

      Set<String> initializedRoles = new HashSet<>(userEntity.getRoles());
      cachedUser = new CachedUserDTO(
        userEntity.getUsername(),
        userEntity.getPassword(),
        initializedRoles
      );

      redisTemplate.opsForValue().set(cacheKey, cachedUser);
    }

    return org.springframework.security.core.userdetails.User.builder()
      .username(cachedUser.getUsername())
      .password(cachedUser.getPassword())
      .authorities(cachedUser.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
        .toList())
      .build();
  }
}