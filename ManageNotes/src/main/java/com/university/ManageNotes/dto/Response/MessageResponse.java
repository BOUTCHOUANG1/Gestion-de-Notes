package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
    private String status; // "SUCCESS", "ERROR", "WARNING", "INFO"
    private Object data;

    // Static factory methods
//    public static MessageResponse success(String message) {
//        return new MessageResponse(message, "SUCCESS");
//    }
//
//    public static MessageResponse error(String message) {
//        return new MessageResponse(message, "ERROR");
//    }
//
//    public static MessageResponse warning(String message) {
//        return new MessageResponse(message, "WARNING");
//    }


}
