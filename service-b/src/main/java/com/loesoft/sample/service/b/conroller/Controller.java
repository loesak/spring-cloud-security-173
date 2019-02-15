package com.loesoft.sample.service.b.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class Controller {

    @RequestMapping(method = RequestMethod.GET, path = "/insecure")
    public ResponseEntity<Map<String, Object>> insecure() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "B");
        data.put("endpoint", "insecure");
        data.put("principal", this.getPrincipal());

        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/secure")
    public ResponseEntity<Map<String, Object>> secure() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "B");
        data.put("endpoint", "secure");
        data.put("principal", this.getPrincipal());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private String getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (JwtAuthenticationToken.class.isInstance(authentication)) {
            JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication;
            return jwt.getToken().getClaimAsString("sub");
        }

        return authentication.getPrincipal().toString();
    }

}