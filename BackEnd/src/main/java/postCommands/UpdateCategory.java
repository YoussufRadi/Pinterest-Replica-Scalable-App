package postCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CategoryDBObject;
import Models.CategoryLiveObject;
import org.redisson.api.RLiveObjectService;


public class UpdateCategory extends ConcreteCommand {


    @Override
    protected void doCommand() {
        String category = gson.toJson(updateCategory(message.getCategory_id(),message.getCategory_object(), ArangoInstance, RLiveObjectService));
        if(category != null)
            responseJson = jsonParser.parse(category);
    }

    private CategoryDBObject updateCategory(String category_id, CategoryDBObject categoryDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updateCategory(category_id,categoryDBObject);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setTitle(categoryDBObject.getTitle());
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return arangoInstance.getCategory(category_id);
    }
}