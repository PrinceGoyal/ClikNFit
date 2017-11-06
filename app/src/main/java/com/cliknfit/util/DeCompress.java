package com.cliknfit.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by katrina on 28/06/17.
 */

public class DeCompress {
    private String _zipFile;
    private String _location;

    public DeCompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        //_dirChecker("");
    }

    public synchronized static void unzip(String zipFile, String location) {

        try  {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(location,ze.getName());
                } else {
                    _dirChecker(location,"");
                    FileOutputStream fout = new FileOutputStream(location+ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private static void _dirChecker(String location, String dir) {
        File f = new File(location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
