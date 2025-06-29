package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse extends AbstractEntity {
    private String title;
    private String message;
    private String type;
    private String priority;
    private Boolean read;
    private Long senderId;
    private String senderName;


}
