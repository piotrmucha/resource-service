package com.piotrekapplications.resourceservice.repositories;

import com.piotrekapplications.resourceservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT u FROM Category u WHERE u.categoryName = ?1")
    Category getCategoryByName(String categoryName);
}
