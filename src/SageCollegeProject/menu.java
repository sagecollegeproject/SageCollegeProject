/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sagex.api.Configuration;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author SageCollegeProject
 */
public class menu {

    private static HashMap<String, List<IMediaResource>> allViews;
    private static final String Props = "SageCollegeProject_";
    private static final String TVDefaultView = "sagecollegeproject.allTVseasons";
    private static final String GuideDefaultView = "sagecollegeproject.currentlyairing";
    private static final String ViewPropAdder = "VFSView";
    private static final String ColorPropAdder = "Color";
    private static final String MainMenuPropAdder = "MainMenuItems";

    public static void main(String[] args) {
        Configuration.SetProperty(Props + MainMenuPropAdder, "Shows;TV,Guide;Guide,ToDo;ToDo,Settings;Settings,Search;Search,Exit;Exit");
        RenameView("Shows;TV", "TV;TV");
        System.out.println(Configuration.GetProperty(Props + MainMenuPropAdder, ""));
    }

    public static void BuildAllViews() {
        System.out.println("Building all views");
        allViews = new HashMap<String, List<IMediaResource>>();
        String[] menus = GetAllMenuItems();
        System.out.println("Total menus found =" + menus.length);
        for (int i = 0; i < menus.length; i++) {
            String cMenu = menus[i];
            String name = GetMenuName(cMenu);
            String type = GetMenuType(cMenu);
            System.out.println("Current Menu Item =" + name + " " + type);
            if (needsView(type)) {
                System.out.println("View is TV type lets build it =" + name);
                String view = GetMenuView(name, type);
                if (!view.equals("")) {
                    allViews.put(name, buildView(view));
                }
            }
        }

    }

    public static String[] GetAllMenuItems() {
        String menuItems = Configuration.GetProperty(Props + MainMenuPropAdder, "Shows;TV,Guide;Guide,ToDo;Todo,Settings;Settings,Search;Search,Exit;Exit");
        if (!menuItems.contains(",")) {
            System.out.println("Main Menu Items is not a valid property please reset value=" + menuItems);
        }
        String[] menus = menuItems.split(",");
        return menus;
    }

    public static void setColorProperty(String Name, String Value) {
        Configuration.SetProperty(Props + Name + ColorPropAdder, Value);
    }

    public static void setViewProperty(String Name, String Value) {
        Configuration.SetProperty(Props + Name + ViewPropAdder, Value);
    }

    public static void deleteMenuItem(String Menu) {
        String name = GetMenuName(Menu);
        deleteMenuViewProp(Menu);
        List<String> propsToDelete = GetProperties(name);
        for (String prop : propsToDelete) {
            deleteMenuViewProp(prop);
        }
    }

    public static void deleteMenuViewProp(String Menu) {
        String[] menu = GetAllMenuItems();
        StringBuilder newMenu = new StringBuilder();
        for (int i = 0; i < menu.length; i++) {
            if (menu[i].equals(Menu)) {
                System.out.println("View Found deleting view " + Menu);
            } else if (i == menu.length - 1) {
                newMenu.append(menu[i]);
            } else {
                newMenu.append(menu[i] + ",");
            }

        }
        System.out.println("Writing new menu to props file " + newMenu.toString());
        Configuration.SetProperty(Props + MainMenuPropAdder, newMenu.toString());
    }

    public static List<String> GetProperties(String Name) {
        List<String> props = new ArrayList<String>();
        props.add(Props + Name + ViewPropAdder);
        props.add(Props + Name + ColorPropAdder);
        return props;
    }

    public static void deleteViewProp(String Prop) {
        Configuration.RemoveProperty(Prop);
    }

    public static void RenameView(String Menu, String nMenu) {
        String name = GetMenuName(Menu);
        String nName = GetMenuName(nMenu);
        for (String prop : GetProperties(name)) {
            String cPropValue = Configuration.GetProperty(prop, "");
            Configuration.SetProperty(prop.replace(name, nName), cPropValue);
        }
        RenameMenuItem(Menu, nMenu);
    }

    public static void RenameMenuItem(String Menu, String nMenu) {
        String props = Configuration.GetProperty(Props + MainMenuPropAdder, "");
        props = props.replace(Menu, nMenu);
        Configuration.SetProperty(Props + MainMenuPropAdder, props);
    }

    public static void addMenuItem(String Menu) {
        String curMenu = Configuration.GetProperty(Props + MainMenuPropAdder, "");
        curMenu = curMenu + "," + Menu;
        Configuration.SetProperty(Props + MainMenuPropAdder, curMenu);
    }

    public static List<IMediaResource> getPreBuiltView(String view) {
        return allViews.get(view);
    }

    public static List<IMediaResource> buildView(String view) {
        return phoenix.media.GetChildren(phoenix.umb.CreateView(view));
    }

    public static boolean needsView(String type) {
        return isTypeTV(type);
    }

    public static String[] splitMenu(String Menu) {
        if (Menu.contains(";")) {
            return Menu.split(";");
        } else {
            System.out.println("Menu Item not valid returning empty for " + Menu);
            return new String[1];
        }
    }

    public static String GetMenuName(String Menu) {
        return splitMenu(Menu)[0];
    }

    public static String GetMenuType(String Menu) {
        return splitMenu(Menu)[1];
    }

    public static boolean isTypeTV(String Type) {
        return Type.equals("TV");
    }

    public static boolean isTypeGuide(String Type) {
        return Type.equals("Guide") || Type.equals("ToDo");
    }

    public static String GetMenuView(String name, String type) {

        String value = Configuration.GetProperty(Props + name + ViewPropAdder, "NotSet");
        if (doesPropertyNeedSet(value)) {

            if (isTypeTV(type)) {
                System.out.println("Property not set for view settings to default for TV view=" + name);
                Configuration.SetProperty(Props + name, TVDefaultView);
                return TVDefaultView;
            } else if (isTypeGuide(type)) {
                System.out.println("Property not set for view settings to default for Guide view=" + name);
                Configuration.SetProperty(Props + name, GuideDefaultView);
                return GuideDefaultView;
            } else {
                System.out.println("Property not set and not a default TV or Guide property=" + name);
                return "";
            }
        }
        return value;
    }

    public static Boolean doesPropertyNeedSet(String property) {
        return property.equals("NotSet");
    }
}
