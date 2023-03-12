package com.ahmet.recipes.controller;

import com.ahmet.recipes.model.Recipe;
import com.ahmet.recipes.resource.SearchResource;
import com.ahmet.recipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/recipes/search")
    List<Recipe> searchRecipes(@RequestBody SearchResource searchResource) {
        return recipeService.search(searchResource);
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getById(@PathVariable(value = "id") Long id) {
        return recipeService.getById(id)
                .map(x -> new ResponseEntity<>(x, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/recipes")
    ResponseEntity<Recipe> createNewRecipe(@RequestBody Recipe newRecipe) {
        var recipe = recipeService.createRecipe(newRecipe);
        return new ResponseEntity<>(recipe, HttpStatus.ACCEPTED);
    }

    @PutMapping("/recipes/{id}")
    ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe, @PathVariable Long id) {
        var updatedRecipe = recipeService.updateRecipe(recipe, id);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{id}")
    ResponseEntity<Long> deleteRecipe(@PathVariable Long id) {
        var isRemoved = recipeService.deleteById(id);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
