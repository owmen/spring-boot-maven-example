package com.owmen.sbme.message;

import com.owmen.sbme.user.User;
import com.owmen.sbme.user.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    public MessageRepository messageRepository;
    public UserServiceClient userServiceClient;

    public MessageService(MessageRepository messageRepository, UserServiceClient userServiceClient) {
        this.messageRepository = messageRepository;
        this.userServiceClient = userServiceClient;
    }

    public List<Message> getMessagesForUser(String firstName, String lastName) {
        User user = userServiceClient.getUser(firstName, lastName);
        List<Message> list= messageRepository.findAllByUserId(user.getUserId());


        list.sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getCreatedTimestamp().compareTo(o2.getCreatedTimestamp());
            }
        });

        return list;
    }

    public Message getFirst(String firstName, String lastName)
    {
        User user = userServiceClient.getUser(firstName, lastName);
        List<Message> list = messageRepository.findAllByUserId(user.getUserId());
        list.sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getCreatedTimestamp().compareTo(o2.getCreatedTimestamp());
            }
        });
        return list.get(0);
    }

    public List<Message> messageSearchByFirstName(String firstName) {
        List<User> users = null;
        try {
            users = userServiceClient.getUsers(firstName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        List<Message> Returnlist = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            List<Message> list = messageRepository.findAllByUserId(users.get(i).getUserId());
            for (int j = 0; j < list.size(); j++) {
                Returnlist.add(list.get(j));
            }
        }
        Collections.sort(Returnlist, (o1, o2) -> {return o1.getCreatedTimestamp().compareTo(o2.getCreatedTimestamp());});
        return Returnlist;
    }


    public void createNewMessage(String body, String firstName, String lastName) {
        User user = null;
        try {
            user = userServiceClient.getUser(firstName, lastName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message newMessage = Message.builder()
                        .userId(user.getUserId())
                .messageBody(body)
                .createdTimestamp(LocalDateTime.now())
                .build();
        messageRepository.save(newMessage);
    }
}
