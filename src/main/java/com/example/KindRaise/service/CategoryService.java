package com.example.KindRaise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.KindRaise.model.Category;
import com.example.KindRaise.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity<?> addCategory(Category category){
        Map<String,Object> responseObject=new HashMap<>();
        try {
            this.categoryRepository.save(category);
            responseObject.put("message", "Category Added Successfully.");
            responseObject.put("status", true);
            return new ResponseEntity<>(responseObject, HttpStatus.CREATED);
        } catch (Exception e) {
            responseObject.put("message", "Failed to add Category." +e.getMessage());
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     public ResponseEntity<?> getAllCategories(){
        Map<String,Object> responseObject=new HashMap<>();
        try {
            List<Category> categories=this.categoryRepository.findAll();
            return new ResponseEntity<>(categories,HttpStatus.OK);
        } catch (Exception e) {
            responseObject.put("message", "Failed to get all categories." +e.getMessage());
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     public ResponseEntity<?> updateCategory(String id,Category categoryData){
        Map<String,Object> responseObject=new HashMap<>();
        try {
            
            Optional<Category> isCategoryExists=this.categoryRepository.findById(id);

            if(isCategoryExists.isEmpty()){
            responseObject.put("message", "Failed to update. Category not found");
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Category category=isCategoryExists.get();

            category.setTitle(categoryData.getTitle());
            category.setIcon(categoryData.getIcon());

            this.categoryRepository.save(category);

            responseObject.put("message", "Category Updated Successfully.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

        } catch (Exception e) {
             responseObject.put("message", "Failed to Update category." +e.getMessage());
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     public ResponseEntity<?> deleteCategory(String id){
        Map<String,Object> responseObject=new HashMap<>();
        try {
             Optional<Category> isCategoryExists=this.categoryRepository.findById(id);

            if(isCategoryExists.isEmpty()){
                responseObject.put("message", "Failed to delete. Category not found");
                responseObject.put("status", false);
                return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            this.categoryRepository.deleteById(isCategoryExists.get().getId());

            responseObject.put("message", "Category Deleted Successfully.");
            responseObject.put("status", true);

            return new ResponseEntity<>(responseObject,HttpStatus.OK);

            
        } catch (Exception e) {
             responseObject.put("message", "Failed to Delete category." +e.getMessage());
            responseObject.put("status", false);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
