package com.ahmet.recipes.repository;

import com.ahmet.recipes.CustomMySqlContainer;
import com.ahmet.recipes.resource.SearchResource;
import com.ahmet.recipes.model.Ingredient;
import com.ahmet.recipes.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Testcontainers
public class CustomRecipeRepositoryIntegrationTest {

    @Autowired
    private CustomRecipeRepository recipeRepository;

    @Container
    public static CustomMySqlContainer sqlContainer = CustomMySqlContainer.getInstance()
            .withInitScript("init.sql");

    @Test
    void test_CreateNew_FindById_DeleteById(){
        var inputRecipe = Recipe.builder()
                .vegeterian(false)
                .servings(2)
                .name("Steak")
                .ingredient(List.of(
                        new Ingredient(null, "beef"),
                        new Ingredient(null, "butter")))
                .instructions("cook steak in the cast iron pan")
                .build();

        var savedRecipe = recipeRepository.save(inputRecipe);
        var fetchedRecipe = recipeRepository.findById(savedRecipe.getId());

        assertTrue(fetchedRecipe.isPresent());
        assertFalse(fetchedRecipe.get().getVegeterian());
        assertEquals(2, fetchedRecipe.get().getServings());
        assertEquals("Steak", fetchedRecipe.get().getName());
        assertEquals("cook steak in the cast iron pan", fetchedRecipe.get().getInstructions());
        assertEquals(2, fetchedRecipe.get().getIngredient().size());
        var ingredientNames = fetchedRecipe.get().getIngredient().stream().map(Ingredient::getName).collect(Collectors.toList());
        assertTrue(ingredientNames.contains("beef"));
        assertTrue(ingredientNames.contains("butter"));

        var deleted = recipeRepository.deleteById(savedRecipe.getId());
        assertTrue(deleted);

        var fetchedAgainRecipe = recipeRepository.findById(savedRecipe.getId());
        assertFalse(fetchedAgainRecipe.isPresent());
    }

    @Test
    void test_CreateNew_Update(){
        var inputRecipe = Recipe.builder()
                .vegeterian(false)
                .servings(2)
                .name("Steak")
                .ingredient(List.of(
                        new Ingredient(null, "beef"),
                        new Ingredient(null, "butter")))
                .instructions("cook steak in the cast iron pan")
                .build();

        var savedRecipe = recipeRepository.save(inputRecipe);

        var newRecipe = Recipe.builder()
                .vegeterian(false)
                .servings(4)
                .name("Steak")
                .ingredient(List.of(
                        new Ingredient(null, "beef"),
                        new Ingredient(null, "olive oil")))
                .instructions("cook 2 steak in the cast iron pan")
                .build();

        var updatedRecipe = recipeRepository.updateRecipe(newRecipe, savedRecipe.getId());
        assertNotNull(updatedRecipe);

        assertFalse(updatedRecipe.getVegeterian());
        assertEquals(4, updatedRecipe.getServings());
        assertEquals("Steak", updatedRecipe.getName());
        assertEquals("cook 2 steak in the cast iron pan", updatedRecipe.getInstructions());
        assertEquals(2, updatedRecipe.getIngredient().size());
        var ingredientNames = updatedRecipe.getIngredient().stream().map(Ingredient::getName).collect(Collectors.toList());
        assertTrue(ingredientNames.contains("beef"));
        assertTrue(ingredientNames.contains("olive oil"));

        recipeRepository.deleteById(savedRecipe.getId());
    }

    @Test
    void testSearch_EmptySearch() {
        var resource = SearchResource.builder()
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(3, result.size());
        var recipeNames = result.stream().map(Recipe::getName).collect(Collectors.toList());
        assertTrue(recipeNames.contains("Sogansiz Menemen"));
        assertTrue(recipeNames.contains("Sogansiz Menemen"));
        assertTrue(recipeNames.contains("Gulash"));
    }

    @Test
    void testSearch_Servings() {
        var resource = SearchResource.builder()
                .servings(2)
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(2, result.size());
        var recipeNames = result.stream().map(Recipe::getName).collect(Collectors.toList());
        assertTrue(recipeNames.contains("Sogansiz Menemen"));
        assertTrue(recipeNames.contains("Gulash"));
    }

    @Test
    void testSearch_Vegeterian() {
        var resource = SearchResource.builder()
                .vegeterian(true)
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sogansiz Menemen", result.get(0).getName());
    }

    @Test
    void testSearch_Servings_Vegeterian() {
        var resource = SearchResource.builder()
                .vegeterian(false)
                .servings(4)
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Soganli Menemen", result.get(0).getName());
    }

    @Test
    void testSearch_IncludedIngredients() {
        var resource = SearchResource.builder()
                .includedIngredients(List.of("egg", "pepper"))
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(2, result.size());
        var recipeNames = result.stream().map(Recipe::getName).collect(Collectors.toList());
        assertTrue(recipeNames.contains("Sogansiz Menemen"));
        assertTrue(recipeNames.contains("Soganli Menemen"));
    }

    @Test
    void testSearch_ExcludedIngredients() {
        var resource = SearchResource.builder()
                .excludedIngredients(List.of("onion", "beef"))
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sogansiz Menemen", result.get(0).getName());
    }

    @Test
    void testSearch_IncludedIngredients_ExcludedIngredients() {
        var resource = SearchResource.builder()
                .includedIngredients(List.of("egg", "pepper"))
                .excludedIngredients(List.of("onion"))
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sogansiz Menemen", result.get(0).getName());
    }

    @Test
    void testSearch_All() {
        var resource = SearchResource.builder()
                .vegeterian(false)
                .servings(4)
                .includedIngredients(List.of("egg"))
                .excludedIngredients(List.of("potato"))
                .textSearch("chop onion")
                .build();

        List<Recipe> result = recipeRepository.search(resource);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Soganli Menemen", result.get(0).getName());
    }
}
