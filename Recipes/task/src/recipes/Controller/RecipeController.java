package recipes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.Entity.CountClass;
import recipes.Entity.Recipe;
import recipes.Repository.RecipeRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@Validated
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;
    CountClass id = new CountClass();

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        if (recipeRepository.findRecipeById(id) != null)
            return new ResponseEntity<>(recipeRepository.findRecipeById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if (name == null && category == null || name != null && category != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (name == null)
            return new ResponseEntity<>(recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category), HttpStatus.OK);
        else
            return new ResponseEntity<>(recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name), HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        if (recipeRepository.findRecipeById(id) != null) {
            Recipe updateRecipe = recipeRepository.findRecipeById(id);
            updateRecipe.setCategory(recipe.getCategory());
            updateRecipe.setDescription(recipe.getDescription());
            updateRecipe.setName(recipe.getName());
            updateRecipe.setDirections(recipe.getDirections());
            updateRecipe.setIngredients(recipe.getIngredients());
            updateRecipe.setDate(LocalDateTime.now());
            return new ResponseEntity<>(recipeRepository.save(updateRecipe), HttpStatus.NO_CONTENT);
        } else if (recipeRepository.findRecipeById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> add(@Valid @RequestBody Recipe recipe) {
        recipeRepository.save(recipe);
        return new ResponseEntity<>(new CountClass(recipe.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (recipeRepository.findRecipeById(id) != null) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}