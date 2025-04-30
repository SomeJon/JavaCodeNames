package console;

import java.util.ArrayList;
import java.util.List;

public class NotifyList implements ChoiceNotifier{
    List<ChoiceNotifier> NotifiersBefore;
    List<Object> NotifierValuesBefore;
    List<ChoiceNotifier> NotifiersAfter;
    List<Object> NotifierValuesAfter;


    public NotifyList(NotifyList i_Notifiers) {
        this.NotifiersBefore = i_Notifiers.NotifiersBefore;
        this.NotifierValuesBefore = i_Notifiers.NotifierValuesBefore;
        this.NotifiersAfter = i_Notifiers.NotifiersAfter;
        this.NotifierValuesAfter = i_Notifiers.NotifierValuesAfter;
    }

    public NotifyList() {
        NotifiersBefore = new ArrayList<>();
        NotifierValuesBefore = new ArrayList<>();
        NotifiersAfter = new ArrayList<>();
        NotifierValuesAfter = new ArrayList<>();
    }

    public void addNotifyBefore(ChoiceNotifier i_Notify, Object i_NotifierValue){
        NotifiersBefore.add(i_Notify);
        NotifierValuesBefore.add(i_NotifierValue);
    }

    public void addNotifyAfter(ChoiceNotifier i_Notify, Object i_NotifierValue){
        NotifiersAfter.add(i_Notify);
        NotifierValuesAfter.add(i_NotifierValue);
    }

    @Override
    public void Notify(Object sender) {
        for (int i = 0; i < NotifiersBefore.size(); i++) {
            MenuItem item = new MenuItem(((MenuItem) sender).getItemText(), NotifierValuesBefore.get(i), NotifiersBefore.get(i));

            item.MenuItemChosen();
        }

        for (int i = 0; i < NotifiersAfter.size(); i++) {
            MenuItem item = new MenuItem(((MenuItem) sender).getItemText(), NotifierValuesAfter.get(i), NotifiersAfter.get(i));

            item.MenuItemChosen();
        }
    }
}
