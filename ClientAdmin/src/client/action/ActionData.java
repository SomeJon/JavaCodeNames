package client.action;

import console.ChoiceNotifier;
import console.MenuItem;

public class ActionData implements ChoiceNotifier {
    private Action currentAction;

    @Override
    public void Notify(Object sender) {
        currentAction  = getCurrentAction(sender);

        switch (currentAction) {
            case SHOW_GAMES:
                //todo
                break;
        }
    }

    private Action getCurrentAction(Object sender) {
        return (Action) (((MenuItem) sender).getItemValue());
    }
}
