package org.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyUser {
    private String username;
    private String lastname;
    private String firstname;
    private String phoneNumber;
    private String state;
    private String baseState;
    private long id;

}
