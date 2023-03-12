package com.ahmet.recipes.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;

    private Boolean vegeterian;

    private Integer servings;

    private String instructions;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ingredient> ingredient;
}
