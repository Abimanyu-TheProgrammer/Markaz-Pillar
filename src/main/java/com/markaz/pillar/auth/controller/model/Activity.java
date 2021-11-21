package com.markaz.pillar.auth.controller.model;

import com.markaz.pillar.transaction.repository.model.UserTransaction;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class Activity {
    public Activity(@Valid VolunteerRegistration obj) {
        this.id = obj.getId();
        this.createdAt = obj.getCreatedAt();
        this.type = ActivityType.VOLUNTEER.toString();
        this.data = obj;
    }

    public Activity(@Valid UserTransaction obj) {
        this.id = obj.getId();
        this.createdAt = obj.getCreatedAt();
        this.type = ActivityType.TRANSACTION.toString();
        this.data = obj;
    }

    public Activity(int id, LocalDateTime createdAt, String type) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
    }

    @NotNull
    private Integer id;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String type;

    private Object data;
}
