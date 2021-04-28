package com.zacharyohearn.sbme;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.zacharyohearn.sbme.message.Message;
import com.zacharyohearn.sbme.message.MessageDTO;
import com.zacharyohearn.sbme.message.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9999)
public class MessageIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void before() {
        wireMockServer.resetAll();
    }

    @Test
    public void testGetMessages() {
        Message messageForUser = Message.builder()
                .userId(1)
                .messageBody("hello world")
                .createdTimestamp(LocalDateTime.now())
                .build();

        messageRepository.save(messageForUser);

        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("firstName", equalTo("john"))
                .withQueryParam("lastName", equalTo("doe"))
                .withQueryParam("dateOfBirth", equalTo("01/01/1950"))
                .willReturn(aResponse().withHeader("content-type", "application/json").withStatus(200).withBodyFile("user.json")));

        List<MessageDTO> actual = given()
                .log().all()
                .queryParam("firstName", "john")
                .queryParam("lastName", "doe")
                .queryParam("dateOfBirth", "01/01/1950")
                .when()
                .port(port)
                .get("/messages")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", MessageDTO.class);

        assertThat(actual.size(), is(1));
    }

    @Test
    public void testSearchMessagesByFirstNameZeroResults() {
        Message messageForUser = Message.builder()
                .userId(1)
                .messageBody("hello world")
                .createdTimestamp(LocalDateTime.now())
                .build();

        messageRepository.save(messageForUser);

        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("firstName", equalTo("john"))
                .withQueryParam("lastName", equalTo("doe"))
                .withQueryParam("dateOfBirth", equalTo("01/01/1950"))
                .willReturn(aResponse().withHeader("content-type", "application/json").withStatus(200).withBodyFile("user.json")));

        List<MessageDTO> actual = given()
                .log().all()
                .queryParam("firstName", "john")
                .queryParam("lastName", "doe")
                .queryParam("dateOfBirth", "01/01/1950")
                .queryParam("searchText", "wrong text")
                .when()
                .port(port)
                .get("/messages/search")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", MessageDTO.class);

        assertThat(actual.size(), is(0));
    }

    @Test
    public void testSearchMessagesByFirstNameOneResult() {
        Message messageForUser = Message.builder()
                .userId(1)
                .messageBody("hello world")
                .createdTimestamp(LocalDateTime.now())
                .build();

        messageRepository.save(messageForUser);

        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("firstName", equalTo("john"))
                .withQueryParam("lastName", equalTo("doe"))
                .withQueryParam("dateOfBirth", equalTo("01/01/1950"))
                .willReturn(aResponse().withHeader("content-type", "application/json").withStatus(200).withBodyFile("user.json")));

        List<MessageDTO> actual = given()
                .log().all()
                .queryParam("firstName", "john")
                .queryParam("lastName", "doe")
                .queryParam("dateOfBirth", "01/01/1950")
                .queryParam("searchText", "hello")
                .when()
                .port(port)
                .get("/messages/search")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", MessageDTO.class);

        assertThat(actual.size(), is(1));
    }

    @Test
    public void testCreateNewMessage() {
        Message messageForUser = Message.builder()
                .userId(1)
                .messageBody("hello world")
                .createdTimestamp(LocalDateTime.now())
                .build();

        messageRepository.save(messageForUser);

        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("firstName", equalTo("john"))
                .withQueryParam("lastName", equalTo("doe"))
                .withQueryParam("dateOfBirth", equalTo("01/01/1950"))
                .willReturn(aResponse().withHeader("content-type", "application/json").withStatus(200).withBodyFile("user.json")));

        Map<String, String> newMessageBody = new HashMap<>();
        newMessageBody.put("firstName", "john");
        newMessageBody.put("lastName", "doe");
        newMessageBody.put("dateOfBirth", "01/01/1950");
        newMessageBody.put("body", "new message");

        given()
                .log().all()
                .body(newMessageBody)
                .when()
                .port(port)
                .post("/createnewmessage")
                .then()
                .statusCode(200);

        List<MessageDTO> actual = given()
                .log().all()
                .queryParam("firstName", "john")
                .queryParam("lastName", "doe")
                .queryParam("dateOfBirth", "01/01/1950")
                .queryParam("searchText", "new message")
                .when()
                .port(port)
                .get("/messages/search")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", MessageDTO.class);

        assertThat(actual.size(), is(1));
    }

    @Test
    public void testEditMessage() {
        Message messageForUser = Message.builder()
                .userId(1)
                .messageBody("hello world")
                .createdTimestamp(LocalDateTime.now())
                .build();

        Message savedMessage = messageRepository.save(messageForUser);
        int messageId = savedMessage.getMessageId();

        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("firstName", equalTo("john"))
                .withQueryParam("lastName", equalTo("doe"))
                .withQueryParam("dateOfBirth", equalTo("01/01/1950"))
                .willReturn(aResponse().withHeader("content-type", "application/json").withStatus(200).withBodyFile("user.json")));

        Map<String, String> newMessageBody = new HashMap<>();
        newMessageBody.put("messageId", String.valueOf(messageId));
        newMessageBody.put("body", "replacement message");

        given()
                .log().all()
                .body(newMessageBody)
                .when()
                .port(port)
                .post("/editmessage")
                .then()
                .statusCode(200);

        List<MessageDTO> actual = given()
                .log().all()
                .queryParam("firstName", "john")
                .queryParam("lastName", "doe")
                .queryParam("dateOfBirth", "01/01/1950")
                .queryParam("searchText", "replacement message")
                .when()
                .port(port)
                .get("/messages/search")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", MessageDTO.class);

        assertThat(actual.size(), is(1));
    }
}
