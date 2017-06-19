package aspguidc.helper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Helper class which provides static methods regarding to the handling of jar files.
 */
public class JarHelper {
    /**
     * Execute the given jar file in a new process and forward the input and output of the executed process
     * to the process which called this method.
     *
     * @param jarFile jar file which is executed
     * @throws IOException if an error occurs while accessing the given file
     */
    public static void executeJarFile(File jarFile) throws IOException {
        Logger.getGlobal().info("[executing] run executable: '" + jarFile.getPath() + "'");

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarFile.getAbsolutePath());
        Process p = pb.inheritIO().start();

        try {
            Logger.getGlobal().info("[executing] executable exited with: " + p.waitFor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
