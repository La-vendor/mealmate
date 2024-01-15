-- Users
INSERT INTO users (username) VALUES ('john_dell');

--Ingredients
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Pasta', 'grams');              --1
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cheese', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Onion', 'grams');              --3
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cucamber', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tomato', 'grams');             --5
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chopped tomato(can)', 'pcs');  --6
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tomato passata', 'grams');     --7
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cream 18%', 'grams');          --8
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cream 30%', 'grams');          --9
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Dill', 'pcs');                 --10

INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Spinach', 'grams');            --11
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Coriander', 'pcs');            --12
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lettuce', 'pcs');              --13
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Paprika', 'grams');            --14
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Yeast (fresh)', 'grams');
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Yeast (dry)', 'pcs');          --16
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Cherry tomatoes', 'grams');    --17
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Peanuts', 'grams');            --18
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Pistachios', 'grams');         --19
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Rice', 'grams');               --20

INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lentils', 'grams');        --21
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Gnocci', 'grams');         --22
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Salmon', 'grams');         --23
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Tuna', 'grams');           --24
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chicken breast', 'grams'); --25
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chicken wings', 'pcs');    --26
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Lime', 'pcs');             --27
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Chow Mein noodles', 'pcs');--28
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Peas', 'grams');           --29
INSERT INTO basic_ingredients (basic_ingredient_name, unit) VALUES ('Coconut milk(can)', 'pcs');--30

--Recipes
INSERT INTO recipes (recipe_name) VALUES ('Tagiatelle with Salmon');
INSERT INTO recipes (recipe_name) VALUES ('Chicken Salad');
INSERT INTO recipes (recipe_name) VALUES ('Tomato soup(thick)');
INSERT INTO recipes (recipe_name) VALUES ('Bao buns with chicken');
INSERT INTO recipes (recipe_name) VALUES ('Chicken Wings');
INSERT INTO recipes (recipe_name) VALUES ('Gnocchi in cream sauce');
INSERT INTO recipes (recipe_name) VALUES ('Curry noodles & vegetables');


--Inserting Recipe Ingredients
INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (1, 1, 200),
    (1, 11, 500),
    (1, 8, 200),
    (1, 10, 1);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (2, 25, 500),
    (2, 17, 200),
    (2, 3, 200),
    (2, 13, 1);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (3, 6, 2),
    (3, 14, 200),
    (3, 3, 200);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (4, 25, 500),
    (4, 7, 200),
    (4, 16, 1),
    (4, 5, 300),
    (4, 3, 200),
    (4, 4, 200),
    (4, 12, 1),
    (4, 18, 100);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (5, 26, 15),
    (5, 20, 300);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (6, 22, 500),
    (6, 9, 200),
    (6, 19, 100);

INSERT INTO recipe_ingredients (recipe_id, basic_ingredient_id, quantity) VALUES
    (7, 12, 1),
    (7, 14, 300),
    (7, 30, 1),
    (7, 27, 2),
    (7, 28, 1),
    (7, 29, 100);