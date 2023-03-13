package recipes.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.Entity.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    void deleteById(Long id);

    Recipe findRecipeById(Long id);

    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);
}