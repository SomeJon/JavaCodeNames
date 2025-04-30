package console;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String ItemText;
    private final Object ItemValue;
    private final ChoiceNotifier Notify;


    public String getItemText() {
        return ItemText;
    }

    public void setItemText(String i_ItemText) {
        ItemText = i_ItemText;
    }

    public Object getItemValue() {
        return ItemValue;
    }

    public MenuItem
        (String i_ItemText, Object i_ItemValue,
        ChoiceNotifier i_Notify)
    {
        ItemText = i_ItemText;
        ItemValue = i_ItemValue;
        Notify = i_Notify;
    }

    public void MenuItemChosen()
    {
        if (Notify != null)
        {
            Notify.Notify(this);
        }
    }
}
