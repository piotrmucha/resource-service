package com.piotrekapplications.resourceservice.repositories;

import com.piotrekapplications.resourceservice.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

}
