package PostCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CategoryDBObject;
import Models.CategoryLiveObject;
import org.redisson.api.RLiveObjectService;


public class RemovePostFromCategory extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String category = gson.toJson(removePostFromCategory(message.getPost_id(),message.getCategory_id(), ArangoInstance, RLiveObjectService));
        responseJson = jsonParser.parse(category);
    }

    private CategoryDBObject removePostFromCategory(String post_id, String category_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.removePostToCategory(category_id,post_id);
        CategoryDBObject categoryDBObject = arangoInstance.getCategory(category_id);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return categoryDBObject;
    }

}