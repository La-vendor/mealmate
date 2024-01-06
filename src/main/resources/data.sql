-- Users
INSERT INTO users (username) VALUES ('john_dell');


--Ingredients
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Pasta', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cheese', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Onion', 'grams');

--Recipes
INSERT INTO recipes (recipe_name) VALUES ('Pasta Carbonara');

--Inserting Recipe Ingredients
INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (1, 1, 200),
    (1, 2, 50);