package com.zacharyohearn.sbme.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class UserServiceClient {

    @Value("${environment}")
    private String environment;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Calls to the User web service to search all of the users and return the one with a matching {@code firstName}, {@code lastName}, and {@code dateOfBirth}
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param dateOfBirth the date of birth of the user
     * @return The found {@code User} or {@code null} if no user was found
     */
    public User getUser(String firstName, String lastName, String dateOfBirth) {
        String url = getUrlForEnvironment() + "/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("firstName", firstName)  .queryParam("lastName", lastName).queryParam("dateOfBirth", dateOfBirth);

        return restTemplate.getForObject(builder.buildAndExpand().toUri(), User.class);
    }

    private String getUrlForEnvironment() {
        switch (environment) {
            case "production":
                return "http://userserivce.com";
            case "test":
            case "dev":
                return "http://userserivce-test.com";
            default:
            case "local":
                return "http://localhost:9999";
        }
    }
}
