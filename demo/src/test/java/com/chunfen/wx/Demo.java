package com.chunfen.wx;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xi.w on 2019/2/11.
 */
public class Demo {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\chenxi\\Pictures\\Screenshots");
        File dcim = new File("C:\\Users\\chenxi\\Pictures\\DCIM\\Screenshots");

        File[] files = file.listFiles();
        File[] dcimFiles = dcim.listFiles();

        Set<String> set = new HashSet<>();
        for(int i=0;i < dcimFiles.length; i++){
            set.add(dcimFiles[i].getName());
        }

        for(File f : files){
            if(set.contains(f.getName())){
                f.delete();
            }
        }

    }
}
