package com.ahmet.recipes.repository;

import com.ahmet.recipes.resource.SearchResource;
import com.ahmet.recipes.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class CustomRecipeRepository {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public List<Recipe> search(SearchResource resource) {
        if (resource != null) {
            List<Recipe> result = null;
            Specification<Recipe> specification = null;
            if (resource.getVegeterian() != null) {
                specification = addAndSpecification(specification, isVegeterian(resource.getVegeterian()));
            }
            if (resource.getServings() != null) {
                specification = addAndSpecification(specification, servings(resource.getServings()));
            }
            if (resource.getTextSearch() != null) {
                specification = addAndSpecification(specification, instructionsLike(resource.getTextSearch()));
            }
            if (resource.getIncludedIngredients() != null) {
                for (String ingredient : resource.getIncludedIngredients()) {
                    specification = addAndSpecification(specification, includesIngredients(ingredient));
                }
            }
            if (specification == null) {
                result = recipeRepository.findAll();
            } else {
                result = recipeRepository.findAll(specification);
            }
            if (resource.getExcludedIngredients() == null) {
                return result;
            } else {
                Specification<Recipe> excludeSpecification = null;
                for (String ingredient : resource.getExcludedIngredients()) {
                    excludeSpecification = addOrSpecification(excludeSpecification, includesIngredients(ingredient));
                }
                var excludedRecipeIds = recipeRepository.findAll(excludeSpecification)
                        .stream()
                        .map(Recipe::getId)
                        .collect(Collectors.toSet());
                return result
                        .stream()
                        .filter(recipe -> !excludedRecipeIds.contains(recipe.getId()))
                        .collect(Collectors.toList());
            }
        }
        return recipeRepository.findAll();
    }

    public Recipe save(Recipe newRecipe) {
        newRecipe.setIngredient(attachIngredients(newRecipe.getIngredient()));
        return recipeRepository.save(newRecipe);
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe updateRecipe(Recipe newRecipe, Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(newRecipe.getName());
                    recipe.setServings(newRecipe.getServings());
                    recipe.setVegeterian(newRecipe.getVegeterian());
                    recipe.setInstructions(newRecipe.getInstructions());
                    recipe.setIngredient(attachIngredients(newRecipe.getIngredient()));
                    return recipeRepository.save(recipe);
                })
                .orElseGet(() -> recipeRepository.save(newRecipe));
    }

    public boolean deleteById(Long id) {
        var found = recipeRepository.findById(id);
        recipeRepository.deleteById(id);
        return found.isPresent();
    }

    private Specification<Recipe> instructionsLike(String searchTerm) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get(Recipe_.INSTRUCTIONS), "%" + searchTerm + "%");
    }

    private Specification<Recipe> isVegeterian(boolean vegeterian) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(Recipe_.VEGETERIAN), vegeterian);
    }

    private Specification<Recipe> servings(int servings) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(Recipe_.SERVINGS), servings);
    }

    private Specification<Recipe> includesIngredients(String ingredient) {
        return (root, query, criteriaBuilder) -> {
            Join<Ingredient, Recipe> recipesIngredient = root.join("ingredient");
            query.distinct(true);
            return criteriaBuilder.equal(recipesIngredient.get(Ingredient_.NAME), ingredient);
        };
    }

    private Specification<Recipe> addAndSpecification(Specification<Recipe> specification1,
                                                      Specification<Recipe> specification2) {
        if (specification1 == null) {
            return where(specification2);
        } else {
            return specification1.and(specification2);
        }
    }

    private Specification<Recipe> addOrSpecification(Specification<Recipe> specification1,
                                                      Specification<Recipe> specification2) {
        if (specification1 == null) {
            return where(specification2);
        } else {
            return specification1.or(specification2);
        }
    }

    private List<Ingredient> attachIngredients(List<Ingredient> ingredient) {
        return ingredient.stream()
                .map(i -> {
                    var ing = ingredientRepository.findByName(i.getName());
                    return ing.orElseGet(() -> ingredientRepository.save(i));
                })
                .collect(Collectors.toList());
    }
}
