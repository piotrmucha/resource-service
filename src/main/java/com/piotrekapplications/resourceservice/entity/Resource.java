package com.piotrekapplications.resourceservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Resource {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private String resourceName;

    private String imageLink;

    private String localization;

    private Integer howManyPeopleCanShare;

    @ManyToOne
    private Category category;
}

