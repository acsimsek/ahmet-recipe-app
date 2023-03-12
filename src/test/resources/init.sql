CREATE TABLE recipe
(
    id           INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(250) NOT NULL,
    vegeterian   TINYINT(1) NOT NULL,
    servings     SMALLINT NOT NULL,
    instructions TEXT
);

CREATE TABLE ingredient
(
    id   INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE recipe_ingredient
(
    recipe_id     INT(11) NOT NULL,
    ingredient_id INT(11) NOT NULL,
    CONSTRAINT fk_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipe(id),
    CONSTRAINT fk_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);


INSERT INTO recipe(name, vegeterian, servings, instructions) VALUES('Soganli Menemen', 0, 4, 'chop onion chop pepper put eggs put butter cook in pan');
INSERT INTO recipe(name, vegeterian, servings, instructions) VALUES('Sogansiz Menemen', 1, 2, 'chop tomato chop pepper put eggs put olive oil cook in pan');
INSERT INTO recipe(name, vegeterian, servings, instructions) VALUES('Gulash', 0, 2, 'chop meet chop potato cook in oven');

INSERT INTO ingredient(name) VALUES('potato');
INSERT INTO ingredient(name) VALUES('tomato');
INSERT INTO ingredient(name) VALUES('onion');
INSERT INTO ingredient(name) VALUES('beef');
INSERT INTO ingredient(name) VALUES('butter');
INSERT INTO ingredient(name) VALUES('olive oil');
INSERT INTO ingredient(name) VALUES('pepper');
INSERT INTO ingredient(name) VALUES('egg');

INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(1, 2);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(1, 3);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(1, 5);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(1, 7);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(1, 8);

INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(2, 2);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(2, 7);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(2, 5);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(2, 8);

INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(3, 1);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(3, 4);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(3, 3);
INSERT INTO recipe_ingredient(recipe_id, ingredient_id) VALUES(3, 7);