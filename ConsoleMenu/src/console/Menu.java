package console;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Menu implements Serializable {
        private String MenuName;
        private final Menu PreviousMenu;
        private final List<MenuItem> MenuItems;
        private final ChoiceNotifier MenuChange;
        private final boolean IsMainMenu;

        public String getMenuName() {
            return MenuName;
        }

        public void setMenuName(String menuName) {
            MenuName = menuName;
        }

        public Menu getPreviousMenu() {
            return PreviousMenu;
        }

        public List<MenuItem> getMenuItems() {
            return MenuItems;
        }

        protected Menu(String i_MenuName, boolean i_IsMainMenu, ChoiceNotifier i_MenuChange) {
                MenuName = i_MenuName;
                IsMainMenu = i_IsMainMenu;
                PreviousMenu = null;
                MenuItems = new ArrayList<MenuItem>();
                MenuChange = i_MenuChange;
        }

        private Menu(String i_MenuName, boolean i_IsMainMenu, Menu i_PreviousMenu, ChoiceNotifier i_MenuChange) {
            MenuName = i_MenuName;
            IsMainMenu = i_IsMainMenu;
            PreviousMenu = i_PreviousMenu;
            MenuItems = new ArrayList<MenuItem>();
            MenuChange = i_MenuChange;
        }

        public void createMenuOption
            (String i_ItemText, Object i_ItemValue,
            ChoiceNotifier i_Notifier) {
            MenuItem newOption = new MenuItem
                (i_ItemText, i_ItemValue, i_Notifier);
            MenuItems.add (newOption);
        }

        public Menu createSubMenu(String i_SubMenuName) {
            final boolean SUB_MENU = false;

            Menu addedSubMenu = new Menu(i_SubMenuName, SUB_MENU, this, MenuChange);
            createMenuOption(i_SubMenuName, addedSubMenu, MenuChange);

            return addedSubMenu;
        }

        public Menu createSubMenuWithActions(String i_SubMenuName, NotifyList i_Notifiers) {
            final boolean SUB_MENU = false;

            Menu addedSubMenu = new Menu(i_SubMenuName, SUB_MENU, this, MenuChange);

            i_Notifiers.addNotifyBefore(MenuChange, addedSubMenu);
            createMenuOption(i_SubMenuName, addedSubMenu, i_Notifiers);

            return addedSubMenu;
        }

        public void printMenu (Integer i_DashesSize) {
            final int START_NUMBER = 1;
            int choiceNumbering = START_NUMBER;
            String endingMenuItemPrint = endingMenuItem();
            String dashes = String.join("", Collections.nCopies(i_DashesSize, "-"));
            StringBuilder menuPrint = new StringBuilder();

            menuPrint.append("\n").append(dashes).append("\n").append("**").append(MenuName).append("**\n").append(dashes).append("\n");
            for (MenuItem item : MenuItems)
            {
                menuPrint.append(choiceNumbering).append(" -> ").append(item.getItemText()).append("\n");
                choiceNumbering++;
            }

            menuPrint.append("\n0 -> ").append(endingMenuItemPrint).append("\n").append(dashes).append("\n").append("Enter your choice: ");

            System.out.print(menuPrint);
        }

        private String endingMenuItem() {
            String endingMenuItem;

            if (IsMainMenu)
            {
                endingMenuItem = "Exit";
            }
            else
            {
                endingMenuItem = "Back";
            }

            return endingMenuItem;
        }
}

