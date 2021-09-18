package com.markaz.pillar.models;

import com.markaz.pillar.validation.ValidPassword;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "registered_users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private long phoneNum;

    @NotNull
    private String address;

    @NotNull
    @ValidPassword
    private String password;
}
