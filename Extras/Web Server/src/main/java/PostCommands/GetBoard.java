package PostCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.BoardDBObject;
import Models.BoardLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.api.RLiveObjectService;


public class GetBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String board = gson.toJson(getBoard(message.getBoard_id(), ArangoInstance, RLiveObjectService));
        if(board != null)
            responseJson = jsonParser.parse(board);
    }

    private BoardLiveObject getBoard(String board_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if(boardLiveObject==null){
            BoardDBObject boardDBObject= arangoInstance.getBoard(board_id);
            if(boardDBObject!=null) {
                String message = new Gson().toJson(boardDBObject);
                Gson gson = new GsonBuilder().create();
                boardLiveObject = gson.fromJson(message, BoardLiveObject.class);
                boardLiveObject = liveObjectService.merge(boardLiveObject);
                //return categoryLiveObject;
            }
        }
        return boardLiveObject;
    }

}
