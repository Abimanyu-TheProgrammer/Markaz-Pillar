package com.markaz.pillar.volunteer.admin.controller.search;

import com.markaz.pillar.volunteer.repository.model.RegistrationStatus;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.data.jpa.domain.Specification;

public class RegistrationSpecs {
    private RegistrationSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<VolunteerRegistration> nameLike(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("name")),
                                "%"+name.toUpperCase()+"%"
                        );
    }

    public static Specification<VolunteerRegistration> statusEqual(RegistrationStatus status) {
        return (root, query, builder) ->
                status == null ?
                        builder.conjunction() :
                        builder.equal(root.get("status"), status);
    }
}
