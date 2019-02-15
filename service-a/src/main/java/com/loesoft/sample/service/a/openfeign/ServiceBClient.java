package com.loesoft.sample.service.a.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "service-b-client", url = "http://localhost:8081")
public interface ServiceBClient {

    @RequestMapping(method = RequestMethod.GET, path = "/insecure", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> insecure();

    @RequestMapping(method = RequestMethod.GET, path = "/secure", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> secure();
}
