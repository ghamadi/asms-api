package com.mycompany.asms.api;

import com.mycompany.asms.dao.UserDAO;
import com.mycompany.asms.model.AuthenticationRequest;
import com.mycompany.asms.model.AuthenticationResponse;
import com.mycompany.asms.security.ASMSUserDetailsService;
import com.mycompany.asms.utils.JwtUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@RestController
@CrossOrigin
public class WebController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ASMSUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final int refreshTokenLifespan = 52 * 604800; //in seconds = 52 weeks
    private final int jwtLifespan = 1800000; //in milliseconds = 30 minutes

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        return sendAuthenticationResponse(userDetails);
    }

    private ResponseEntity<?> sendAuthenticationResponse(UserDetails userDetails) {
        final String jwt = jwtUtil.generateToken(userDetails);
        int maxAge = 60 * 30;

        AuthenticationResponse authResponse = new AuthenticationResponse(jwt, jwtLifespan);

        HttpHeaders headers = new HttpHeaders();
        String cookie = String.format("refresh_token=%s; Max-Age=%d; HttpOnly", generateRefreshToken(userDetails), refreshTokenLifespan);
        headers.add("Set-Cookie", cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(authResponse);
    }

    private String generateRefreshToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        String newToken = Base64.encodeBase64String(String.format("%s::%d", username, jwtLifespan).getBytes());
        new UserDAO(jdbcTemplate).updateRefreshToken(newToken, username);
        return newToken;
    }

    @GetMapping("/current_user")
    public String getCurrentUser(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring("bearer_".length());
        return jwtUtil.extractUsername(jwt);
    }


    @GetMapping("/refresh_token")
    public ResponseEntity<?> checkRefreshToken(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken) {

        HttpHeaders headers = new HttpHeaders();
        if (refreshToken.isBlank()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");

        UserDAO userDAO = new UserDAO(jdbcTemplate);
        String[] decodedToken = new String(Base64.decodeBase64(refreshToken)).split("::");
        String username = decodedToken[0];

        long expTime = Long.parseLong(decodedToken[1]);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDAO.getRefreshToken(username).equals(refreshToken) && expTime < System.currentTimeMillis())
            return sendAuthenticationResponse(userDetails);
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
    }

    @GetMapping("/exit")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String jwt) {
        int recordsUpdated = new UserDAO(jdbcTemplate).updateRefreshToken(null, jwtUtil.extractUsername(jwt.substring("bearer_".length())));
        String cookie = "refresh_token=; Max-Age=0; SameSite=None; Secure=true; HttpOnly";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie);

        return recordsUpdated == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("username doesn't exist")
                : ResponseEntity.status(HttpStatus.OK)
                .headers(headers).build();
    }
}
