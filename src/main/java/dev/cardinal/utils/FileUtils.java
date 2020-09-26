package dev.cardinal.utils;

import dev.cardinal.Yui;
import dev.cardinal.data.ConfigData;
import dev.cardinal.data.UserData;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileUtils {
    public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = Yui.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Yui.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }

    public String getBaseFolder() throws Exception {
        return new File(Yui.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
    }

    public void scheduleYamlDump(final String configpath, final String userdatapath) throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
                Yaml yaml = new Yaml(options);
                try {
                    yaml.dump(UserData.userData, new FileWriter(userdatapath));
                    yaml.dump(ConfigData.configMap, new FileWriter(configpath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("Saved files");

            }
        }, 0, 10000, TimeUnit.MILLISECONDS);
    }
}
