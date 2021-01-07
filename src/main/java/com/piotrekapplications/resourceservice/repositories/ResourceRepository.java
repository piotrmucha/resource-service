package com.piotrekapplications.resourceservice.repositories;

import com.piotrekapplications.resourceservice.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    @Query("SELECT u FROM Resource u WHERE u.resourceName = ?1")
    Resource getResourceByName(String resourceName);

}
