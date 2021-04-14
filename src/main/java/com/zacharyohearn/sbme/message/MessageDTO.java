package com.zacharyohearn.sbme.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private int messageId;
    private String messageBody;
    private int recipientUserId;
    private String recipientFirstName;
    private String recipientLastName;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime lastUpdatedTimestamp;
}
