package com.ahmet.recipes.service;

import com.ahmet.recipes.model.Recipe;
import com.ahmet.recipes.repository.CustomRecipeRepository;
import com.ahmet.recipes.resource.SearchResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private CustomRecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void testSearch() {
        var results = List.of(Recipe.builder().name("Recipe").build());
        when(recipeRepository.search(any(SearchResource.class))).thenReturn(results);

        var searchResults = recipeService.search(SearchResource.builder().build());

        verify(recipeRepository).search(any(SearchResource.class));
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        assertEquals("Recipe", searchResults.get(0).getName());
    }

    @Test
    void testGetById() {
        var recipe = Recipe.builder().name("Recipe").build();
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        var result = recipeService.getById(1L);

        verify(recipeRepository).findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Recipe", result.get().getName());
    }

    @Test
    void testCreateRecipe() {
        var recipe = Recipe.builder().name("Recipe").build();
        var createdRecipe = Recipe.builder().id(1L).name("Recipe").build();
        when(recipeRepository.save(recipe)).thenReturn(createdRecipe);

        var result = recipeService.createRecipe(recipe);

        verify(recipeRepository).save(any(Recipe.class));
        assertNotNull(result);
        assertEquals("Recipe", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void testUpdateRecipe() {
        var recipe = Recipe.builder().name("Menemen").build();
        var updatedRecipe = Recipe.builder().id(1L).name("Menemen").build();
        when(recipeRepository.updateRecipe(recipe, 1L)).thenReturn(updatedRecipe);

        var result = recipeService.updateRecipe(recipe, 1L);

        verify(recipeRepository).updateRecipe(any(Recipe.class), eq(1L));
        assertNotNull(result);
        assertEquals("Menemen", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void testDeleteById() {
        when(recipeRepository.deleteById(1L)).thenReturn(true);

        var result = recipeService.deleteById(1L);

        verify(recipeRepository).deleteById(1L);
        assertTrue(result);
    }
}
