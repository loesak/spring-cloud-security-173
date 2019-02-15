package com.loesoft.sample.service.a.conroller;

import com.loesoft.sample.service.a.openfeign.ServiceBClient;
import com.loesoft.sample.service.a.openfeign.ServiceCClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    private final ServiceBClient serviceBClient;
    private final ServiceCClient serviceCClient;

    @RequestMapping(method = RequestMethod.GET, path = "/insecure-a-to-insecure-b")
    public ResponseEntity<Map<String, Object>> insecureAToInsecureB() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "A");
        data.put("endpoint", "insecure");
        data.put("principal", this.getPrincipal());
        data.put("called", this.serviceBClient.insecure());

        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/insecure-a-to-secure-b")
    public ResponseEntity<Map<String, Object>> insecureAToSecureB() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "A");
        data.put("endpoint", "insecure");
        data.put("principal", this.getPrincipal());
        data.put("called", this.serviceBClient.secure());

        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/secure-a-to-insecure-c")
    public ResponseEntity<Map<String, Object>> secureAToInsecureC() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "A");
        data.put("endpoint", "secure");
        data.put("principal", this.getPrincipal());
        data.put("called", this.serviceCClient.insecure());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/secure-a-to-secure-c")
    public ResponseEntity<Map<String, Object>> secureAToSecureC() {

        Map<String, Object> data = new HashMap<>();
        data.put("service", "A");
        data.put("endpoint", "secure");
        data.put("principal", this.getPrincipal());
        data.put("called", this.serviceCClient.secure());

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