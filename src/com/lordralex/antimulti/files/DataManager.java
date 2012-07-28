package com.lordralex.antimulti.files;

/**
 * @version 1.0
 * @author Joshua
 */
public class DataManager {

    Manager manager;

    public DataManager(FileType typeToUse)
    {
        switch(typeToUse)
        {
            case mySQL:
                manager = new SQLManager();
                break;
            default:
                manager = new FlatFileManager();
                break;
        }
    }

}
