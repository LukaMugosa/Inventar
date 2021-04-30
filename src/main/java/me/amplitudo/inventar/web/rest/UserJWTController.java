package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.domain.User;
import me.amplitudo.inventar.security.jwt.JWTFilter;
import me.amplitudo.inventar.security.jwt.TokenProvider;
import me.amplitudo.inventar.service.UserService;
import me.amplitudo.inventar.web.rest.errors.BadActionException;
import me.amplitudo.inventar.web.rest.errors.ExceptionErrors;
import me.amplitudo.inventar.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final UserService userService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        User user = userService.getUserWithAuthorities().orElseThrow(
            () -> new BadActionException(
                ExceptionErrors.USER_NOT_FOUND.getCode(),
                ExceptionErrors.USER_NOT_FOUND.getDescription()
            )
        );

        return new ResponseEntity<>(new JWTToken(jwt, user), httpHeaders, HttpStatus.OK);
    }
    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        private User user;

        JWTToken(String idToken, User user) {
            this.idToken = idToken;
            this.user = user;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        @JsonProperty("user")
        User getUser(){return user; }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        void setUser(User user) {
            this.user = user;
        }
    }
}
