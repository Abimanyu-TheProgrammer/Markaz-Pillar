package com.markaz.pillar.markaz.controller.search;

import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import com.markaz.pillar.markaz.repository.model.Markaz;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public final class MarkazSpecs {
    public static final String[] JABODETABEK = {"jakarta", "bogor", "depok", "tanggerang", "bekasi"};

    private MarkazSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<Markaz> nameLike(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("name")),
                                "%"+name.toUpperCase()+"%"
                        );
    }

    public static Specification<Markaz> addressLike(Boolean isJabodetabek) {
        return (root, query, builder) -> {
            if(isJabodetabek == null) {
                return builder.conjunction();
            } else {
                Predicate predicate = builder.disjunction();
                for(String city : JABODETABEK) {
                    predicate = builder.or(
                            predicate,
                            builder.like(
                                    builder.upper(root.get("address")),
                                    "%"+city.toUpperCase()+"%"
                            )
                    );
                }

                return isJabodetabek ? predicate : predicate.not();
            }
        };
    }

    public static Specification<Markaz> categoryLike(MarkazDonationCategory category) {
        return (root, query, builder) ->
                category == null ?
                        builder.conjunction() :
                        builder.like(root.get("donationDetail").get("categories"), "%"+category+"%");
    }
}
