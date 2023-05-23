package com.example.userservice.services.impl;

import com.example.userservice.dto.GetUserDto;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.RegisterDto;
import com.example.userservice.entities.User;
import com.example.userservice.mappers.DtoMapper;
import com.example.userservice.repositories.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final DtoMapper mapper;

    @Value("${user_service.app.jwtSecret}")
    private String jwtSecretKey;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, DtoMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + username + " not found!")
        );
    }

    public GetUserDto registerUser(RegisterDto registerDto){
        User user = mapper.convertDtoToUser(registerDto);
        if (!user.getDisplayName().isEmpty() && (user.getDisplayName().length() < 2 || user.getDisplayName().length() > 32)){
            throw new IllegalArgumentException("Display name must be between 2 and 32 characters long");
        }
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("User with email: " + user.getEmail() + " already registered");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return mapper.covertToDto(userRepository.save(user), null);
    }

    public GetUserDto verifyToken(String token){
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token);
            if (claimsJws.getBody().getExpiration().before(new Date())){
                throw new RuntimeException("Expired token!");
            }
            String email = claimsJws.getBody().getSubject();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("Unable to find user with email: " + email)
            );
            return mapper.covertToDto(user, token);
        }
        catch (JwtException | IllegalArgumentException e){

            throw new RuntimeException("Invalid or expired token!");
        }
    }

    public String generateToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 300000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }
    
    public GetUserDto getUserById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Unable to find user with id " + userId)
        );
        return mapper.covertToDto(user, null);
    }
}
