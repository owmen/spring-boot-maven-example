package com.zacharyohearn.sbme.message;

import com.zacharyohearn.sbme.user.User;
import com.zacharyohearn.sbme.user.UserServiceClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void testGetMessageForUser() {
        // ARRANGE
        User user = User.builder()
                .userId(1)
                .build();

        when(userServiceClient.getUser(any(), any(), any())).thenReturn(user);

        List<Message> messages = Collections.singletonList(Message.builder().messageId(1).build());
        when(messageRepository.findAllByUserId(anyInt())).thenReturn(messages);

        // ACT
        List<MessageDTO> actual = messageService.getMessagesForUser("", "", "");

        // ASSERT
        assertNotNull(actual);
    }

}
