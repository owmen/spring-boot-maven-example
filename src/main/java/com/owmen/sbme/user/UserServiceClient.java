package com.owmen.sbme.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserServiceClient {

    @Value("${environment}")
    private String environment;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Uses the User Web Service to search all of the users and returns the one with a matching {@code firstName} and {@code lastName}
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return The found {@code User} or {@code null} if no users were found
     */
    public User getUser(String firstName, String lastName) {
        String url = getUrlForEnvironment() + "/search?firstName=" + firstName + "&lastName=" + lastName;
        return restTemplate.getForObject(url, User.class);
    }

    /**
     * Uses the User Web Service to search all of the users and returns those with a matching {@code firstName} value
     * @param firstName the value used in the search
     * @return a list of {@code User} objects
     */
    public List<User> getUsers(String firstName) {
        String url = getUrlForEnvironment() + "/search?firstName=" + firstName;
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {}).getBody();
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
