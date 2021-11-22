package com.markaz.pillar.volunteer.controller.search;

import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.data.jpa.domain.Specification;

public final class ProgramSpecs {
    private ProgramSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<VolunteerProgram> nameLike(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("name")),
                                "%"+name.toUpperCase()+"%"
                        );
    }
}
