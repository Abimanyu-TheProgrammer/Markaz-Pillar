package com.markaz.pillar.models;

import com.markaz.pillar.validation.ValidPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(of = "username")
@NoArgsConstructor
@Entity
@Table(name = "registered_users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String fullName;

    @NotNull
    private long phoneNum;

    @NotBlank
    private String address;

    @NotBlank
    @ValidPassword
    private String password;
}
