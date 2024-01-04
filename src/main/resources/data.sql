-- Users
INSERT INTO users (username) VALUES ('john_dell');


--Ingredients
INSERT INTO basic_ingredients (basic_ingredient_name) VALUES ('Pasta');
INSERT INTO basic_ingredients (basic_ingredient_name) VALUES ('Cheese');
INSERT INTO basic_ingredients (basic_ingredient_name) VALUES ('Onion');

--Recipes
INSERT INTO recipes (recipe_name) VALUES ('Pasta Carbonara');

--Inserting Recipe Ingredients
INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity, unit) VALUES
    (1, 1, 200, 'grams'),
    (1, 2, 50, 'grams');