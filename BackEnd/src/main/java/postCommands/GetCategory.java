package postCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.CategoryDBObject;
import Models.CategoryLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.api.RLiveObjectService;


public class GetCategory extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String category = gson.toJson(getCategory(message.getCategory_id(), ArangoInstance, RLiveObjectService));
        System.out.println(category);
        if(category != null) {
            responseJson = jsonParser.parse(category);
            //System.out.println(responseJson);
        }
    }

    private CategoryLiveObject getCategory(String category_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
//        System.out.println(categoryLiveObject);
        if(categoryLiveObject==null){
            CategoryDBObject categoryDBObject= arangoInstance.getCategory(category_id);
            if(categoryDBObject!=null) {
                String message = new Gson().toJson(categoryDBObject);
                Gson gson = new GsonBuilder().create();
                categoryLiveObject = gson.fromJson(message, CategoryLiveObject.class);
                categoryLiveObject = liveObjectService.merge(categoryLiveObject);
                //return categoryLiveObject;
            }
        }
        return categoryLiveObject;
    }

}
