package com.piotrekapplications.resourceservice.entity;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResourceDto {
    private String resourceName;

    private String localization;

    private Integer howManyPeopleCanShare;

    private String categoryName;
}
