package com.piotrekapplications.resourceservice.web;

import com.piotrekapplications.resourceservice.entity.Category;
import com.piotrekapplications.resourceservice.entity.CategoryDto;
import com.piotrekapplications.resourceservice.entity.Resource;
import com.piotrekapplications.resourceservice.entity.ResourceDto;
import com.piotrekapplications.resourceservice.repositories.CategoryRepository;
import com.piotrekapplications.resourceservice.repositories.ResourceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {
    private final CategoryRepository categoryRepository;
    private final ResourceRepository resourceRepository;

    public ResourceController(CategoryRepository categoryRepository, ResourceRepository resourceRepository) {
        this.categoryRepository = categoryRepository;
        this.resourceRepository = resourceRepository;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/category")
    public void createCategory(@RequestBody CategoryDto categoryDto) throws IOException {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        categoryRepository.save(category);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/category/image")
    public void uploadImageForCategory(@RequestParam("file") MultipartFile multipartFile, @RequestParam("categoryName")String category) throws IOException {
        String name = RandomStringUtils.randomAlphanumeric(8);
        String filePath = String.format("/home/piotr/img/%s.jpg",name);
        File file = new File(filePath);
        file.createNewFile();
        multipartFile.transferTo(file);
        Category category1 = categoryRepository.getCategoryByName(category);
        category1.setImageLink(filePath);
        categoryRepository.save(category1);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createResource(@RequestBody ResourceDto resourceDto) {
        Resource resource = new Resource();
        resource.setResourceName(resourceDto.getResourceName());
        resource.setHowManyPeopleCanShare(resourceDto.getHowManyPeopleCanShare());
        resource.setLocalization(resourceDto.getLocalization());
        resource.setReservationUntil(Timestamp.valueOf(resourceDto.getReservationUntil()));
        Category category = categoryRepository.getCategoryByName(resourceDto.getCategoryName());
        if(category == null) throw new RuntimeException("fail");
        resource.setCategory(category);
        Set<Resource> resourceSet = category.getResources();
        if (resourceSet == null) {
            resourceSet = new HashSet<>();
        }
        resourceSet.add(resource);
        category.setResources(resourceSet);
        categoryRepository.save(category);
        resourceRepository.save(resource);
    }
}
