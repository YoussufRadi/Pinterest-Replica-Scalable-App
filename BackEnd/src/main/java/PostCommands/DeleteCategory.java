package PostCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CategoryLiveObject;
import org.redisson.api.RLiveObjectService;

public class DeleteCategory extends ConcreteCommand {

    @Override
    protected void doCommand() {
        deleteCategory(message.getCategory_id(), ArangoInstance, RLiveObjectService);

    }

    private void deleteCategory(String category_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.deleteCategory(category_id);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject!=null){
            liveObjectService.delete(CategoryLiveObject.class,category_id);
        }
    }
}