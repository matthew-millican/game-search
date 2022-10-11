package functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class used to store the result of a given search
 *
 */
public class Writer {


    private static final char COLON = ':';


    /**
     * Builds the string to be stored in a file, seperated by colons
     * @param num
     * @param time
     * @param nodes
     * @param solution
     * @return
     */
    public static String format(int num, double time, int nodes, boolean solution) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(num);
        stringBuilder.append(COLON);
        stringBuilder.append(time);
        stringBuilder.append(COLON);
        stringBuilder.append(nodes);
        stringBuilder.append(COLON);
        stringBuilder.append(solution);

        return stringBuilder.toString();


    }


    /**
     * Stores a result which is given a specified sub-folder to be stored in.
     */
    public void run(String message, String part, String variant, int subDir) {

        File dir = new File(".." + File.separator + "results" + File.separator + part + File.separator + variant + File.separator + String.valueOf(subDir));


        if (dir.exists() || dir.mkdir()) {


            String fileName = dir + File.separator + timeStamp();

            File file = new File(fileName);

            if (!file.exists()) {
                try {
                    FileWriter fw = new FileWriter(file);
                    fw.write(message);
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    System.err.println("IOException --> write(): " + e.getMessage());
                }
            }
        }
    }



    /**
     * Returns the time in the specified format.
     * Used as the filename for a result
     * @return a new string
     */
    private static String timeStamp() {
        Date d = new Date();
        String            df = new String("yyyy-MM-dd_HH-mm-ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        String             s = sdf.format(d);

        return s;
    }
}
