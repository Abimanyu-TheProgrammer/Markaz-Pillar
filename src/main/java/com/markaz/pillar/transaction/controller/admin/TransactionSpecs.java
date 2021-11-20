package com.markaz.pillar.transaction.controller.admin;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecs {
    private TransactionSpecs() {
        throw new IllegalStateException("Utility Class");
    }

    public static Specification<UserTransaction> nameOrUniqueIdLike(String s) {
        return (root, query, builder) ->
                s == null ?
                        builder.conjunction() :
                        builder.or(
                                builder.like(
                                        builder.upper(root.get("user").get("username")),
                                        "%"+s.toUpperCase()+"%"
                                ),
                                builder.like(
                                        builder.upper(root.get("trxId")),
                                        "%"+s.toUpperCase()+"%"
                                )
                        );
    }

    public static Specification<UserTransaction> donationEqual(DonationDetail donationDetail) {
        return (root, query, builder) ->
                donationDetail == null ?
                        builder.conjunction() :
                        builder.equal(root.get("donationDetail"), donationDetail);
    }
}
