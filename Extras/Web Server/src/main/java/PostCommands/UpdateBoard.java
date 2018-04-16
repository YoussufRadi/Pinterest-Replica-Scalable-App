package PostCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.BoardDBObject;
import Models.BoardLiveObject;
import org.json.JSONObject;
import org.redisson.api.RLiveObjectService;


public class UpdateBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        String s  = "";
        if(message.getBoard_id() == null){
            s+= "board_id is missing";
        }
        if(message.getBoard_object()==null){
            s+= "board_object is missing";
        }
        if(s.isEmpty()) {
            String board = gson.toJson(update_board(message.getBoard_id(), message.getBoard_object(), ArangoInstance, RLiveObjectService));
            responseJson = jsonParser.parse(board);
        }else {
            System.out.println(s);
            JSONObject err = new JSONObject();
            err.put("error",s);
            responseJson = jsonParser.parse(err.toString());
        }
    }

    private BoardDBObject update_board(String board_id, BoardDBObject boardDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updateBoard(board_id,boardDBObject);
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if (boardLiveObject!= null){
            boardLiveObject.setName(boardDBObject.getName());
            boardLiveObject.setPosts_id(boardDBObject.getPosts_id());
            boardLiveObject.setUser_id(boardDBObject.getUser_id());
        }
        return arangoInstance.getBoard(board_id);
    }
}