package com.zacharyohearn.sbme.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "messageId")
    private Integer messageId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "messageBody")
    private String messageBody;

    @Column(name = "createdTimestamp")
    private LocalDateTime createdTimestamp;

    @Column(name = "lastUpdatedTimestamp")
    private LocalDateTime lastUpdatedTimestamp;
}
