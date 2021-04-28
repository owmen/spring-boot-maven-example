package com.zacharyohearn.sbme.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zacharyohearn.sbme.user.User;
import com.zacharyohearn.sbme.user.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    public MessageRepository messageRepository;
    public UserServiceClient userServiceClient;
    public ObjectMapper objectMapper;

    public MessageService(MessageRepository messageRepository, UserServiceClient userServiceClient, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

//    private Message foundMessage = new Message(); //Move this to be local to the method

    public List<MessageDTO> getMessagesForUser(String firstName, String lastName, String dateOfBirth) {
        User user = userServiceClient.getUser(firstName, lastName, dateOfBirth);
        List<Message> list= messageRepository.findAllByUserId(user.getUserId());


        list.sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getCreatedTimestamp().compareTo(o2.getCreatedTimestamp());
            }
        });

        return c(list, firstName, lastName, user.getUserId());
    }

    private List<MessageDTO> c(List<Message> list, String first, String last, int userId) {
        List<MessageDTO> retVal = new ArrayList<>();
        for (Message message: list) {
            retVal.add(MessageDTO.builder()
                    .messageId(message.getMessageId())
                    .messageBody(message.getMessageBody())
                    .createdTimeStamp(message.getCreatedTimestamp())
                    .recipientFirstName(first)
                    .recipientLastName(last)
                    .recipientUserId(userId)
                    .build());
        }
        return retVal;
    }

    private MessageDTO c(Message message, String first, String last, int user) {
        return MessageDTO.builder()
                .messageId(message.getMessageId())
                .messageBody(message.getMessageBody())
                .createdTimeStamp(message.getCreatedTimestamp())
                .recipientFirstName(first)
                .recipientLastName(last)
                .recipientUserId(user)
                .build();
    }

    public List<MessageDTO> messageSearch(String firstName, String lastName, String dateOfBirth, String searchText) {
        User user = null;
        try {
            user = userServiceClient.getUser(firstName, lastName, dateOfBirth);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        List<Message> AllUserMessages = messageRepository.findAllByUserId(user.getUserId());
        List<Message> foundMessages = new ArrayList<>();
        for (int i = 0; i < AllUserMessages.size(); i++) {
            Message message = AllUserMessages.get(i);
            if (message.getMessageBody().contains(searchText)) {
                foundMessages.add(message);
            }}

        List<MessageDTO> foundMessageDtos = new ArrayList<>();
        for(Message message : foundMessages){
            MessageDTO messageDto = c(message, firstName, lastName, user.getUserId()); //Easier to debug
            foundMessageDtos.add(messageDto);
        }
        return foundMessageDtos;
    }




public void createNewMessage(String body, String firstName, String lastName, String dateOfBirth) throws JsonProcessingException
    {
        User user = null;
        try {
            user = userServiceClient.getUser(firstName, lastName, dateOfBirth);
        } catch (Exception e) {
            ObjectMapper om = new ObjectMapper();
            log.error(om.writeValueAsString(e));
            e.printStackTrace();
        }
        Message newMessage = Message.builder()
                        .userId(user.getUserId())
                .messageBody(body)
                .createdTimestamp(LocalDateTime.now())
                .lastUpdatedTimestamp(LocalDateTime.now())
                .build();
        messageRepository.save(newMessage);
    }

    public void editMessageBody(Integer messageId, String body){
        Message message = messageRepository.getOne(messageId);
        message.setMessageBody(body);
        message.setLastUpdatedTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

}
