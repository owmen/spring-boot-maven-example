package com.zacharyohearn.sbme.message;

import com.zacharyohearn.sbme.user.User;
import com.zacharyohearn.sbme.user.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Message messageSearch(String firstName, String lastName, String searchText) {
        User user = null;
        try {
            user = userServiceClient.getUser(firstName, lastName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        List<Message> AllUserMessages = messageRepository.findAllByUserId(user.getUserId());
        Message foundMessage = null;
        for (int i = 0; i < AllUserMessages.size(); i++) {
            Message message = AllUserMessages.get(i);
            if (message.getMessageBody().contains(searchText)) {
                foundMessage = message;
            }
        }
        return foundMessage;
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
