package com.ahmet.recipes.controller;

import com.ahmet.recipes.model.Recipe;
import com.ahmet.recipes.resource.SearchResource;
import com.ahmet.recipes.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class RecipeControllerIntegrationTest {

    @Mock
    private RecipeService recipeService;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        var recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .build();
    }

    @Test
    void test_Search() throws Exception {
        var results = List.of(Recipe.builder().name("Menemen").build());
        when(recipeService.search(any(SearchResource.class))).thenReturn(results);
        var searchResource = SearchResource.builder().build();

        mockMvc.perform(post("/recipes/search")
                        .content(creaSearchResourceJson(searchResource))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Menemen")));
    }

    @Test
    void test_GetById_Found() throws Exception {
        var recipe = Recipe.builder()
                .name("Menemen")
                .build();
        when(recipeService.getById(1L)).thenReturn(Optional.of(recipe));

        mockMvc.perform(get("/recipes/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Menemen")));
    }

    @Test
    void test_GetById_NotFound() throws Exception {
        when(recipeService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/recipes/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_CreateRecipe() throws Exception {
        var requestRecipe = Recipe.builder()
                .name("Menemen")
                .build();
        var createdRecipe = Recipe.builder()
                .id(1L)
                .name("Menemen")
                .build();
        when(recipeService.createRecipe(any(Recipe.class))).thenReturn(createdRecipe);

        mockMvc.perform(post("/recipes")
                        .content(creaRecipeJson(requestRecipe))
                        .contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("Menemen")));
    }

    @Test
    void test_UpdateRecipe() throws Exception {
        var requestRecipe = Recipe.builder()
                .name("Updated")
                .build();
        var createdRecipe = Recipe.builder()
                .id(1L)
                .name("Updated")
                .build();
        when(recipeService.updateRecipe(any(Recipe.class), eq(1L))).thenReturn(createdRecipe);

        mockMvc.perform(put("/recipes/{id}", "1")
                        .content(creaRecipeJson(requestRecipe))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated")));
    }

    @Test
    void test_Deleted() throws Exception {
        when(recipeService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/recipes/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }

    @Test
    void test_NotDeleted() throws Exception {
        when(recipeService.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/recipes/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    private String creaRecipeJson(Recipe recipe) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(recipe);
    }

    private String creaSearchResourceJson(SearchResource searchResource) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(searchResource);
    }
}
