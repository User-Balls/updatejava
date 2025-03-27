package mindustry.plugin;

import arc.util.Log;
import mindustry.mod.Plugin;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaUpdaterPlugin extends Plugin {

    private final String JAVA_DOWNLOAD_URL = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.2%2B8/OpenJDK17U-jdk_x64_linux_hotspot_17.0.2_8.tar.gz";
    private final String JAVA_FOLDER = "config/jdk17";
    private final String JAVA_BINARY = JAVA_FOLDER + "/jdk-17.0.2+8/bin/java";

    @Override
    public void init() {
        if (!isJava17()) {
            Log.err("Java version is too old! Downloading Java 17...");
            downloadAndExtractJava();
        } else {
            Log.info("Java 17+ is already installed.");
        }
    }

    private boolean isJava17() {
        String version = System.getProperty("java.version");
        return version.startsWith("17") || version.startsWith("18") || version.startsWith("19");
    }

    private void downloadAndExtractJava() {
        try {
            Files.createDirectories(Paths.get(JAVA_FOLDER));

            Log.info("Downloading Java 17...");
            try (InputStream in = new URL(JAVA_DOWNLOAD_URL).openStream();
                 FileOutputStream out = new FileOutputStream(JAVA_FOLDER + "/java.tar.gz")) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            Log.info("Extracting Java 17...");
            ProcessBuilder pb = new ProcessBuilder("tar", "-xvf", JAVA_FOLDER + "/java.tar.gz", "-C", JAVA_FOLDER);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();

            Log.info("Java 17 installed successfully!");

        } catch (Exception e) {
            Log.err("Failed to install Java 17!", e);
        }
    }
}
