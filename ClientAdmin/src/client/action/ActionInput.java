package client.action;

import console.ChoiceNotifier;
import console.MenuItem;
import dto.type.in.response.LoadFileResponse;
import dto.type.in.response.Response;
import ui.input.InputHandling;

public class ActionInput implements ChoiceNotifier {
    private Response CurentResponse = null;
    private InputHandling CurrentInput = null;

    @Override
    public void Notify(Object sender) {
        CurrentInput = getInputHandling(sender);
        switch (CurrentInput) {
            case FILE_PATH_XML:
            case FILE_PATH_TXT:
                CurentResponse = new LoadFileResponse();
                CurrentInput.getInput(CurentResponse);
                break;
            case GET_GAME_ID:
                //todo: check error, create submenu for the game watching, delete on exit
        }
    }

    private InputHandling getInputHandling(Object sender) {
        MenuItem menuItem = (MenuItem) sender;
        return (InputHandling) menuItem.getItemValue();
    }

    public Response getCurentResponse() {
        return CurentResponse;
    }

    public InputHandling getCurrentInput() {
        return CurrentInput;
    }
}
