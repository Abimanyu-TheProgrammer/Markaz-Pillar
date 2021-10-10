package com.markaz.pillar.santri.controller.search;

import com.markaz.pillar.santri.repository.model.Santri;
import org.springframework.data.jpa.domain.Specification;

public final class SantriSpecs {
    private SantriSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<Santri> nameLike(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("name")),
                                "%"+name.toUpperCase()+"%"
                        );
    }

    public static Specification<Santri> markazLike(String markaz) {
        return (root, query, builder) ->
                markaz == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("markaz").get("name")),
                                "%"+markaz.toUpperCase()+"%"
                        );
    }
}
