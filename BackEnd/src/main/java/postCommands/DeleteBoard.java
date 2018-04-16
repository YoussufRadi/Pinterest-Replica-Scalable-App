package postCommands;


import Database.ArangoInstance;
import Interface.ConcreteCommand;
import Models.BoardLiveObject;
import org.redisson.api.RLiveObjectService;


public class DeleteBoard extends ConcreteCommand {

    @Override
    protected void doCommand() {
        deleteBoard(message.getBoard_id(), ArangoInstance, RLiveObjectService);
    }

    private void deleteBoard(String board_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.deleteBoard(board_id);
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if(boardLiveObject!=null){
            liveObjectService.delete(BoardLiveObject.class,board_id);
        }
    }
}