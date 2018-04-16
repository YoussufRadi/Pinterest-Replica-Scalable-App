package postCommands;

import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.BoardDBObject;
import Models.BoardLiveObject;
import org.redisson.api.RLiveObjectService;


public class InsertPostToBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String board = gson.toJson(insertPostToBoard(message.getPost_id(),message.getBoard_id(), ArangoInstance, RLiveObjectService));
        responseJson = jsonParser.parse(board);
    }

    private BoardDBObject insertPostToBoard(String post_id, String board_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.insertPostToBoard(board_id,post_id);
        BoardDBObject boardDBObject = arangoInstance.getBoard(board_id);
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if(boardLiveObject != null){
            boardLiveObject.setName(boardDBObject.getName());
            boardLiveObject.setPosts_id(boardDBObject.getPosts_id());
            boardLiveObject.setUser_id(boardDBObject.getUser_id());
        }
        return boardDBObject;

    }
}