package net.dohaw.play.landclaiming;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<File> getFilesInConfig(final File folder){
        List<File> files = new ArrayList<>();
        if(folder.isDirectory()){
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    files.add(fileEntry);
                }
            }
        }else{
            return null;
        }
        return files;
    }

}
