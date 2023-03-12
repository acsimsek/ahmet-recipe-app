package com.ahmet.recipes.service;

import com.ahmet.recipes.resource.SearchResource;
import com.ahmet.recipes.model.Recipe;
import com.ahmet.recipes.repository.CustomRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final CustomRecipeRepository recipeRepository;

    public List<Recipe> search(SearchResource searchResource) {
        return recipeRepository.search(searchResource);
    }

    public Optional<Recipe> getById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Recipe recipe, Long id) {
        return recipeRepository.updateRecipe(recipe, id);
    }

    public boolean deleteById(Long id) {
        return recipeRepository.deleteById(id);
    }
}
