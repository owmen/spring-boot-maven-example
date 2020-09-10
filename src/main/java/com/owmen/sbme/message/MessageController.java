package com.owmen.sbme.message;

import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity getMessagesForUser(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok(messageService.getMessagesForUser(firstName, lastName));
    }

    @GetMapping("/messages/first")
    public ResponseEntity getFirstMessage(@RequestParam String firstName, @RequestParam String lastName, @RequestParam int age) {
        return ResponseEntity.ok(messageService.getFirst(firstName, lastName));
    }

    @GetMapping("/messages/search")
    public ResponseEntity searchMessagesByFirstName(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String searchText) {
        return ResponseEntity.ok(messageService.messageSearch(firstName, lastName, searchText));
    }

    @PostMapping("/createnewmessage")
    public ResponseEntity createNewMessage(@RequestBody String requestBody) {
        String body = JsonPath.from(requestBody).getString("body");
        String firstName = JsonPath.from(requestBody).getString("firstName");
        String lastName = JsonPath.from(requestBody).getString("lastName");
        messageService.createNewMessage(body, firstName, lastName);
        return ResponseEntity.ok().build();
    }
}
