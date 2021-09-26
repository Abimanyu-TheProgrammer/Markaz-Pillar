package com.markaz.pillar.auth.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_menu")
@Getter
@Setter
@ToString(of = "name")
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @NotBlank
    @Size(max = 128)
    @Column(unique = true)
    private String name;
}
