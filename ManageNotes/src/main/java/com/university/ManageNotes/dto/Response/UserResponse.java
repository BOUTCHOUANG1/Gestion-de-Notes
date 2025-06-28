package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import com.university.ManageNotes.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends AbstractEntity {


    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Role role;

    private Boolean active;


}
