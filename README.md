# spring-cloud-security-173
https://github.com/spring-cloud/spring-cloud-security/issues/173

This project is here to demonstrate the ability or inability of using the Spring Cloud Security feign capabilities with the newer Spring Security OAuth to obtain and/or relay oauth tokens when calling backend services through feign clients

There are 2 services here; A and B. Both services are secured with Spring Security 5's OAuth support and have endpoints that are both secured and insecured. Service B is a simple OAUth resource servers in that it just returns a response. Service A however is both an OAuth resource server and an OAuth client and has endpoints that call the endpoints of service B through he use of Feign.

# Assumptions

This assumes the reader knows how to obtain OAuth tokens and to make API calls using a bearer authentication token. For this sample, a throw away Okta account was created. You can obtain an access token as demonstrated below to gain access to service A only.

```
curl -X POST \
--header "accept: application/json" \
--header "content-type: application/x-www-form-urlencoded" \
--user "0oajarnb46RKOdYvK0h7:ELb_lK41XMg8BfC-Vq4LNC0I-7eqXWwLYRlGUdaD" \
--data-urlencode "grant_type=client_credentials" \
--data-urlencode "scope=doAthings doBthings" \
https://dev-383458.oktapreview.com/oauth2/ausjas48b0QEozCJL0h7/v1/token
```

#Endpoints

All endpoints return information about the call made. this includes:
* the name of the service called
* the name of the endpoint called
* the name of the principal in the security context

##Service A

Service A endpoints additionally embed in their response the response received from the call to the service B endpoints.

**http://localhost:8080/insecure-a-to-insecure-b**

As the name suggests, the endpoint is insecured and as part of generating a response also calls the insecure endpoint of service B.

**http://localhost:8080/insecure-a-to-secure-b**

This insecure endpoint calls the secured endpoint of service B in order to generate its own response

**http://localhost:8080/secure-a-to-insecure-b**

This secure endpoint of service A calls the insecure endpoint of service B to generate its response

**http://localhost:8080/secure-a-to-secure-b**

This secure endpoint of service A calls the secured endpoint of service B to generate its response

##Service B

http://localhost:8081/insecure

An insecured endpoint that returns the content described above

```
{
    "principal": <principal>,
    "endpoint": "insecure",
    "service": "B"
}
```

http://localhost:8081/secure

An secured endpoint that returns the content described above

```
{
    "principal": <principal>,
    "endpoint": "secure",
    "service": "B"
}
```

#Setup

Checkout the project and complile the source code

```
mvn compile
```

Start Service A

```
cd service-a
mvn spring-boot:run
```

Start Service B

```
cd ../service-b
mvn spring-boot:run
```

To show that feign is working as expected without security, call the insecure-a-to-insecure-b endpoint of service A

```
curl http://localhost:8080/insecure-a-to-insecure-b
```

The following is the expected response:

```
{
    "service": "A",
    "endpoint": "insecure",
    "principal": "anonymousUser",
    "called": {
        "principal": "anonymousUser",
        "endpoint": "insecure",
        "service": "B"
    }
}
```

Now to the non-expected behavior. Lets obtain an access token as described above and call the same endpoint. This is to demonstrate that OAuth2FeignRequestInterceptor should forward on the bearer received by service A to service B.

```
curl --header "Authorization: Bearer eyJraWQiOiJWeFZLc0huTW4xazBhbmlvbXd1Ung1VmJWSzVndVhveEt5c1lEemlqVUdrIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULi05d29oNC15dWptNW1JazVnQkxNdkNqdndHdTZiMDBQZHFKTEpYcHFyVlEiLCJpc3MiOiJodHRwczovL2Rldi0zODM0NTgub2t0YXByZXZpZXcuY29tL29hdXRoMi9hdXNqYXM0OGIwUUVvekNKTDBoNyIsImF1ZCI6ImFwaTovL3NwcmluZy1jbG91ZC1zZWN1cml0eS0xNzMiLCJpYXQiOjE1NTAyMDcxNDEsImV4cCI6MTU1MDIxMDc0MSwiY2lkIjoiMG9hamFybmI0NlJLT2RZdkswaDciLCJzY3AiOlsiZG9BdGhpbmdzIl0sInN1YiI6IjBvYWphcm5iNDZSS09kWXZLMGg3In0.Gsolljhm0k4kmno3ZuPmJeh1sQvCuq73lVXI56KalBzoERCqA68Htp26BkNhoGw0r8hL-exdCQ1bo1lxu1BqauQRgetvQ21Psn1rUXbyUyVfZAwDZw2Fn_U_Hgfn4NgP3oKW94C_nyAyfYmb0mOyVpUKI6j24LwB7Chbfqe4fwV_8-bO09sTOJdXgzEt3KTvIF2cAATB7--Yz4G3a_Q615NYixdUoOFsDWSfHa1XCtPKk4ACXLp69GEgTv0Hkpa34G6mync7h-fMPDeofnHCdlmRlupKq1YKe6DdeadLqSvMYFU9G_zvOHLPWS5rZvxcLb2exxNSM-gLxUEFHqYGng" \
http://localhost:8080/insecure-a-to-insecure-b
```

If OAuth2FeignRequestInterceptor was working as expected, the following would be the expected response:

```
{
    "service": "A",
    "endpoint": "insecure",
    "principal": "0oajarnb46RKOdYvK0h7",
    "called": {
        "principal": "0oajarnb46RKOdYvK0h7",
        "endpoint": "insecure",
        "service": "B"
    }
}
```

However, the following is the response:

```
{
    "service": "A",
    "endpoint": "insecure",
    "principal": "0oajarnb46RKOdYvK0h7",
    "called": {
        "principal": "anonymousUser",
        "endpoint": "insecure",
        "service": "B"
    }
}
```

This shows that the bearer token is not being relayed to the next called services.

Next lets check what happens when we call the `/insecure-a-to-secure-b` endpoint.

```
curl http://localhost:8080/insecure-a-to-secure-b
```

The expectation is that OAuth2FeignRequestInterceptor would see that there is no existing OAuth authentication and will obtain its own and pass it down to the secured service.

```
{
    "service": "A",
    "endpoint": "insecure",
    "principal": "anonymousUser",
    "called": {
        "principal": "0oajarnb46RKOdYvK0h7",
        "endpoint": "secure",
        "service": "B"
    }
}
```

However, the call overall fails because the call to service B fails as the call was not authenticated.

# Summary

None of this works because there currently is no OAuth2FeignRequestInterceptor to relay existing, or aquire new OAuth access tokens. Adding in OAuth2FeignRequestInterceptor requires:
* adding the library `org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure` which is not compatible with Spring Security 5's new OAuth support (see https://docs.spring.io/spring-security/site/docs/5.1.3.RELEASE/reference/htmlsingle/#oauth2client and https://docs.spring.io/spring-security/site/docs/5.1.3.RELEASE/reference/htmlsingle/#oauth2resourceserver)
* Adding `@EnableOAuth2Client` and `@EnableResourceServer` which requires doubling the OAuth client and resource configuration for both this and Spring Security 5's OAuth support.