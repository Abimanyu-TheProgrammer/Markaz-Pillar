package com.markaz.pillar.auth.repository;

import com.markaz.pillar.auth.controller.model.Activity;
import com.markaz.pillar.auth.controller.model.ActivityStatus;
import com.markaz.pillar.auth.controller.model.ActivityType;
import com.markaz.pillar.transaction.repository.UserTransactionRepository;
import com.markaz.pillar.volunteer.repository.VolunteerRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class UserActivityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private VolunteerRepository volunteerRepository;
    private UserTransactionRepository transactionRepository;

    @Autowired
    public void setTransactionRepository(UserTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setVolunteerRepository(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    //    setResultTransformer has no alternative
    private List getAllTransactionID(ActivityStatus status) {
        String whereQuery = "";
        if(status != null) {
            whereQuery = "where status = ";
            switch (status) {
                case MENUNGGU_KONFIRMASI:
                    whereQuery += "'MENUNGGU_KONFIRMASI'";
                    break;
                case DITOLAK:
                    whereQuery += "'DONASI_DITOLAK'";
                    break;
                case DITERIMA:
                    whereQuery += "'DONASI_DITERIMA'";
                    break;
            }
        }

        return entityManager.createNativeQuery(
                        "select id, created_at as createdAt, 'TRANSACTION' as type " +
                                "from user_donation " + whereQuery
                )
                .unwrap(NativeQuery.class)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("createdAt", LocalDateTimeType.INSTANCE)
                .addScalar("type", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(Activity.class))
                .list();
    }

//    setResultTransformer has no alternative
    private List getAllVolunteerID(ActivityStatus status) {
        String whereQuery = "";
        if(status != null) {
            whereQuery = "where status = ";
            switch (status) {
                case MENUNGGU_KONFIRMASI:
                    whereQuery += "'MENUNGGU_KONFIRMASI'";
                    break;
                case DITOLAK:
                    whereQuery += "'PENDAFTARAN_DITOLAK'";
                    break;
                case DITERIMA:
                    whereQuery += "'PENDAFTARAN_DITERIMA'";
                    break;
            }
        }

        return entityManager.createNativeQuery(
                        "select id, created_at as createdAt, 'VOLUNTEER' as type " +
                                "from volunteer_registration " + whereQuery
                )
                .unwrap(NativeQuery.class)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("createdAt", LocalDateTimeType.INSTANCE)
                .addScalar("type", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(Activity.class))
                .list();
    }

    public Page<Activity> fetchAllUserActivity(ActivityType type, ActivityStatus status, Pageable pageable) {
        List<Activity> registration = new ArrayList<>();
        if(type.equals(ActivityType.ALL) || type.equals(ActivityType.VOLUNTEER)) {
            registration = getAllVolunteerID(status);
        }

        List<Activity> donation = new ArrayList<>();
        if(type.equals(ActivityType.ALL) || type.equals(ActivityType.TRANSACTION)) {
            donation = getAllTransactionID(status);
        }

        List<Activity> userActivity = Stream.concat(registration.stream(), donation.stream())
                .sorted(Comparator.comparing(Activity::getCreatedAt))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        List<Activity> completedActivity = userActivity.stream()
                .skip(start)
                .limit(pageable.getPageSize())
                .map(activity -> {
                    ActivityType activityType = ActivityType.valueOf(activity.getType());
                    if(activityType.equals(ActivityType.VOLUNTEER)) {
                        activity.setData(
                                volunteerRepository.getById(activity.getId())
                                        .orElseThrow(() -> new IllegalStateException(
                                                String.format("Volunteer ID %s doesn't exist", activity.getId())
                                        ))
                        );
                    } else if(activityType.equals(ActivityType.TRANSACTION)) {
                        activity.setData(
                                transactionRepository.getById(activity.getId())
                                        .orElseThrow(() -> new IllegalStateException(
                                                String.format("Transaction ID %s doesn't exist", activity.getId())
                                        ))
                        );
                    } else {
                        throw new IllegalStateException("Unsupported function!");
                    }

                    return activity;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(completedActivity, pageable, userActivity.size());
    }
}

