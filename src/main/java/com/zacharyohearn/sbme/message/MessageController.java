package com.zacharyohearn.sbme.message;

import io.restassured.path.json.JsonPath;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class MessageController {

    private MessageService messageService;


    @GetMapping("/messages")
    public ResponseEntity getMessagesForUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String dateOfBirth)
    {
        return ResponseEntity.ok(messageService.
                getMessagesForUser(firstName, lastName, dateOfBirth));
    }

    @GetMapping("/messages/search")
    public ResponseEntity searchMessagesByFirstName(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String dateOfBirth, @RequestParam String searchText) {
        return ResponseEntity.ok(messageService.
                messageSearch(firstName, lastName, dateOfBirth, searchText));
    }

    @SneakyThrows
    @PostMapping("/createnewmessage")
    public ResponseEntity createNewMessage(@RequestBody String requestBody) {
        String body =
                JsonPath.from(requestBody).getString("body");
        String firstName = JsonPath.from(requestBody).getString("firstName");
        String lastName =   JsonPath.from(requestBody).getString("lastName");
        String dateOfbirth= JsonPath.from(requestBody).getString("dateOfBirth");
        messageService.createNewMessage(body, firstName, lastName, dateOfbirth);
        return ResponseEntity
                .ok().build();
    }

    @SneakyThrows
    @PostMapping("/editmessage")
    public ResponseEntity editMessage(@RequestBody String requestBody) {
        String body =
                JsonPath.from(requestBody).getString("body");
        Integer messageId = JsonPath.from(requestBody).getInt("messageId");
        messageService.editMessageBody(messageId, body);
        return ResponseEntity
                .ok().build();
    }

}








