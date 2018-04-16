package postCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.BoardDBObject;
import Models.BoardLiveObject;
import org.redisson.api.RLiveObjectService;


public class RemovePostFromBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String board = gson.toJson(removePostFromBoard(message.getBoard_id(),message.getPost_id(), ArangoInstance, RLiveObjectService));
        responseJson = jsonParser.parse(board);
    }

    private BoardDBObject removePostFromBoard(String board_id, String post_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.removePostFromBoard(board_id,post_id);
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