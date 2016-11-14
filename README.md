# SageCollegeProject
Sage Project for Class Capstone

If you want daily builds download the SageTVPluginsDev.zip and extract into your SageTV\SageTV\ folder. Then install as normal from the UI addons from SageTV plugins section.

The zip can be found here

https://github.com/sagecollegeproject/SageCollegeProject/blob/master/ZippedProject/SageTVPluginsDev.zip


How to add/edit custom menu items. 

PROPERTIES FILE EDITING MUST BE DONE WHEN SAGETV UI IS NOT OPEN!

The main menu items are kept in one propety setting

SageCollegeProject_MainMenuItems=

The menu items are named accordingly in this property file 

MenuName;MenuType  

They are then comma seperated

MenuName1;MenuType1,MenuName2,MenuType2

The MenuTypes availabe for user editing are

PreviewGuide
Scheduled
TV
Favorites

All of these are built of phoenix VFS views and must have an associated property with them naming the VFS viewname as such

SageCollegeProject_MenuNameVFSView=

If a VFS view isn't set for a new menu item it will be defaulted. All default VFS views are from the 

SageTV\SageTV\STVs\Phoenix\vfs\s-scp-vfs.xml files. This is default you can use any view form any vfs file in that folder.

Feel free to add or remove/rename items as necessary.


