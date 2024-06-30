package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Category;
import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.repository.CategoryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Retrieve all categories from the database.
     * @return a list of categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieve a category by its id.
     * @param id the id of the category to retrieve
     * @return an Optional containing the found category or empty if not found
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Add a new category to the database.
     * @param category the category to add
     */
    public void addCategory(Category category) {categoryRepository.save(category);
    }

    /**
     * Update an existing category.
     * @param category the category with updated information
     */
    public void updateCategory(@NotNull Category category) {
        Category existingCategory = categoryRepository.findById(category.getCategoryId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " +
                        category.getCategoryId() + " does not exist."));
        existingCategory.setCategoryName(category.getCategoryName());
        existingCategory.setCategoryDescription(category.getCategoryDescription());
        categoryRepository.save(existingCategory);
    }

    /**
     * Delete a category by its id.
     * @param id the id of the category to delete
     */
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category with ID " + id + " does not exist.");
        }
        categoryRepository.deleteById(id);
    }

}