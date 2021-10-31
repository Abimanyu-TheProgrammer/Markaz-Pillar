package com.markaz.pillar.donation.controller.admin.search;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import org.springframework.data.jpa.domain.Specification;

public final class DonationSpecs {
    private DonationSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<DonationDetail> nameLike(String name) {
        return (root, query, builder) ->
                name == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("name")),
                                "%"+name.toUpperCase()+"%"
                        );
    }

    public static Specification<DonationDetail> santriIdEquals(Integer id) {
        return (root, query, builder) ->
                id == null ?
                        builder.conjunction() :
                        builder.equal(root.get("santri").get("id"), id);
    }

    public static Specification<DonationDetail> markazIdEquals(Integer id) {
        return (root, query, builder) ->
                id == null ?
                        builder.conjunction() :
                        builder.equal(root.get("markaz").get("id"), id);
    }

    public static Specification<DonationDetail> uniqueIdLike(String id) {
        return (root, query, builder) ->
                id == null ?
                        builder.conjunction() :
                        builder.like(
                                builder.upper(root.get("uniqueId")),
                                "%"+id.toUpperCase()+"%"
                        );
    }
}
