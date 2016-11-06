/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sagex.UIContext;
import sagex.api.Configuration;
import sagex.phoenix.util.SageTV;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author SageCollegeProject
 */
public class menu {

    private static HashMap<String, List<IMediaResource>> allViews;
    public static final String Props = "SageCollegeProject_";
    private static final String TVDefaultView = "sagecollegeproject.allTVseasons";
    private static final String ViewPropAdder = "VFSView";
    private static final String ColorPropAdder = "Color";
    private static final String MainMenuPropAdder = "MainMenuItems";

    public static void main(String[] args) {
        
 SetDefaultProperties("test");     
 BuildAllViews("test");
    }

    public static void BuildAllViews(String ui) {
        System.out.println("Building all views");
        allViews = new HashMap<String, List<IMediaResource>>();
        String[] menus = GetAllMenuItems(ui);
        System.out.println("Total menus found =" + menus.length);
        for (int i = 0; i < menus.length; i++) {
            String cMenu = menus[i];
            String name = GetMenuName(cMenu);
            String type = GetMenuType(cMenu);
            System.out.println("Current Menu Item =" + name + " " + type);
            if (needsView(type)) {
                System.out.println("View is needs VFS lets build it =" + name);
                String view = GetMenuView(ui,name, type);
                if (!view.equals("")) {
                    allViews.put(name, buildView(view));
                }
            }
        }

    }
    
     public static Boolean NeedsFolderView(IMediaResource show,String Type)
    {
        if(isTypePreviewGuide(Type))
        {
        return true;
        }
        else if(phoenix.umb.IsFolder(show))
        {        
         return true;
        }
        else
        {
        return false;
        }
    }

    public static String[] GetAllMenuItems(String ui) {
        String menuItems = Configuration.GetProperty(new UIContext(ui),Props + MainMenuPropAdder, "Shows;TV,Guide;Guide,ToDo;Todo,Settings;Settings,Search;Search,Exit;Exit");
        if (!menuItems.contains(",")) {
            System.out.println("Main Menu Items is not a valid property please reset value=" + menuItems);
        }
        System.out.println("Menu property ="+menuItems);
        String[] menus = menuItems.split(",");
        return menus;
    }

    public static void setColorProperty(String ui,String Name, String Value) {
        Configuration.SetProperty(new UIContext(ui),Name, Value);
        Configuration.SetProperty(new UIContext(ui),Props + Name + ColorPropAdder, Value);
    }

    public static void setViewProperty(String ui,String Name, String Value) {
        Configuration.SetProperty(new UIContext(ui),Props + Name + ViewPropAdder, Value);
    }

    public static void deleteMenuItem(String ui,String Menu) {
        String name = GetMenuName(Menu);
        deleteMenuViewProp(ui,Menu);
        List<String> propsToDelete = GetProperties(name);
        for (String prop : propsToDelete) {
            deleteMenuViewProp(ui,prop);
        }
    }

    public static void deleteMenuViewProp(String ui,String Menu) {
        String[] menu = GetAllMenuItems(ui);
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
        Configuration.SetProperty(new UIContext(ui), Props + MainMenuPropAdder, newMenu.toString());
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

    public static void RenameView(String ui,String Menu, String nMenu) {
        String name = GetMenuName(Menu);
        String nName = GetMenuName(nMenu);
        for (String prop : GetProperties(name)) {
            String cPropValue = Configuration.GetProperty(new UIContext(ui),prop, "");
            Configuration.SetProperty(new UIContext(ui), prop.replace(name, nName), cPropValue);
        }
        RenameMenuItem(ui,Menu, nMenu);
    }

    public static void RenameMenuItem(String ui,String Menu, String nMenu) {
        String props = Configuration.GetProperty(new UIContext(ui),Props + MainMenuPropAdder, "");
        props = props.replace(Menu, nMenu);
        Configuration.SetProperty(new UIContext(ui), Props + MainMenuPropAdder, props);
    }

    public static void addMenuItem(String ui,String Menu) {
        String curMenu = Configuration.GetProperty(new UIContext(ui),Props + MainMenuPropAdder, "NotSet");
        if(curMenu=="NotSet"){
            
            curMenu=Menu;}
            else{    
        curMenu = curMenu + "," + Menu;}
        Configuration.SetProperty(new UIContext(ui), Props + MainMenuPropAdder, curMenu);
    }

    public static List<IMediaResource> getPreBuiltView(String view) {
        return allViews.get(view);
    }

    public static List<IMediaResource> buildView(String view) {
        return phoenix.media.GetChildren(phoenix.umb.CreateView(view));
    }

    public static boolean needsView(String type) {
        return isTypeTV(type) || isTypeScheduled(type) || isTypeFavorites(type) || isTypePreviewGuide(type);
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
   
    public static boolean isTypeScheduled(String Type) {
        return Type.equals("Scheduled");
    }
    public static boolean isTypeFavorites(String Type) {
        return Type.equals("Favorites");
        
    }
    public static boolean isTypePreviewGuide(String Type) {
        return Type.equals("PreviewGuide");
    }
    
    

    public static boolean isTypeGuide(String Type) {
        return Type.equals("Guide") || Type.equals("ToDo");
    }

    public static String GetMenuView(String ui,String name, String type) {

        String value = Configuration.GetProperty(new UIContext(ui),Props + name + ViewPropAdder, "NotSet");
        if (doesPropertyNeedSet(value)) {

            if (isTypeTV(type)) {
                System.out.println("Property not set for view settings to default for TV view=" + name);
                Configuration.SetProperty(new UIContext(ui), Props + name, TVDefaultView);
                return TVDefaultView;
            } else if (isTypeGuide(type)) {
                System.out.println("Property not set for view settings to default for Guide view=" + name);
                Configuration.SetProperty(new UIContext(ui), Props + name, TVDefaultView);
                return TVDefaultView;
            } else {
                System.out.println("Property not set and not a default TV or Guide property=" + name);
                return "";
            }
        }
        return value;
    }
    
    public static void SetDefaultProperties(String ui)
    {
    sagex.api.Configuration.SetProperty(new UIContext(ui),Props+MainMenuPropAdder,"NotSet");
    SetDefaultView(ui,"NextUp;NextUp","cc0000","NotUsed");
    SetDefaultView(ui,"AllShows;TV","009933","sagecollegeproject.allTVseasons");
    SetDefaultView(ui,"ManageFavorites;Favorites","cc9933","sagecollegeproject.favorites");
    SetDefaultView(ui,"OnNow;PreviewGuide","666699","sagecollegeproject.currentlyairing");
    SetDefaultView(ui,"Guide;Guide","006699","NotUsed");
    SetDefaultView(ui,"Scheduled;Scheduled","990066","SageCollegeProject.scheduledrecordings");
    SetDefaultView(ui,"TVList;TV","cc0000","sagecollegeproject.allTVnoSeasons");
    SetDefaultView(ui,"Settings;Settings","009933","NotUsed");
    SetDefaultView(ui,"Search;Search","cc0000","NotUsed");
    SetDefaultView(ui,"Exit;Exit","009933","NotUsed");
    }
       
    
    public static void SetDefaultView(String UI,String name,String color,String vfsView)
    {
        String mName=GetMenuName(name);
        addMenuItem(UI,name);
        setColorProperty(UI,mName,color);
        setViewProperty(UI,mName,vfsView);
        
        
    }

    public static String GetFontSafeColor(String color)
    {
    return "#"+color;
    }
    public static Boolean doesPropertyNeedSet(String property) {
        return property.equals("NotSet");
    }
}
