package com.ahmet.recipes.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResource {
    private Boolean vegeterian;
    private Integer servings;
    private List<String> includedIngredients;
    private List<String> excludedIngredients;
    private String textSearch;
}
