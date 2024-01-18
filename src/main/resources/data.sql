--Ingredients
-- Pasta and Grains
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Pasta', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Rice', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Gnocchi', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chow Mein noodles', 'pcs');

-- Dairy and Cheese
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cheese', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cream 18%', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cream 30%', 'grams');

-- Vegetables
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Onion', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ( 'Cucumber', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tomato', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chopped tomato(can)', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tomato passata', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Dill', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Spinach', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Coriander', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lettuce', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Paprika', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cherry tomatoes', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Peas', 'grams');

-- Nuts and Seeds
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Peanuts', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Pistachios', 'grams');

-- Protein
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lentils', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Salmon', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tuna', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chicken breast', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chicken wings', 'pcs');

-- Miscellaneous
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lime', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Yeast (fresh)', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Yeast (dry)', 'pcs');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Coconut milk(can)', 'pcs');


--Recipes
INSERT INTO recipes (recipe_name) VALUES ('Tagiatelle with Salmon');
INSERT INTO recipes (recipe_name) VALUES ('Chicken Salad');
INSERT INTO recipes (recipe_name) VALUES ('Tomato soup(thick)');
INSERT INTO recipes (recipe_name) VALUES ('Bao buns with chicken');
INSERT INTO recipes (recipe_name) VALUES ('Chicken Wings');
INSERT INTO recipes (recipe_name) VALUES ('Gnocchi in cream sauce');
INSERT INTO recipes (recipe_name) VALUES ('Curry noodles & vegetables');


--Inserting Recipe Ingredients
INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (1, 1, 200),
    (1, 14, 500),
    (1, 6, 200),
    (1, 13, 1);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (2, 25, 500),
    (2, 18, 200),
    (2, 8, 200),
    (2, 16, 1);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (3, 11, 2),
    (3, 17, 200),
    (3, 8, 200);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (4, 25, 500),
    (4, 12, 200),
    (4, 29, 1),
    (4, 10, 300),
    (4, 8, 200),
    (4, 9, 200),
    (4, 15, 1),
    (4, 20, 100);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (5, 26, 15),
    (5, 2, 300);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (6, 3, 500),
    (6, 7, 200),
    (6, 21, 100);

INSERT INTO recipe_ingredients (recipe_id,basic_ingredient_id, quantity) VALUES
    (7, 15, 1),
    (7, 17, 300),
    (7, 30, 1),
    (7, 27, 2),
    (7, 4, 1),
    (7, 19, 100);