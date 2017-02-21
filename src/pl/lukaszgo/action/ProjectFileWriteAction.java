package pl.lukaszgo.action;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import pl.lukaszgo.Settings;

/**
 * Created by lukaszgo on 2017-01-18.
 */

public class ProjectFileWriteAction {

    private String chosenProject;

    public ProjectFileWriteAction(String chosenProject) {
        this.chosenProject = chosenProject;
    }

    public void execute() {
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(Settings.DB_SCHEMAS_FILE_PATH));

            bufferedWriter.write(prepareDBUserLineToWrite());
            bufferedWriter.newLine();
            bufferedWriter.write("db.pass=syndis");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String prepareDBUserLineToWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("db.user=").append(chosenProject);

        return stringBuilder.toString();
    }
}
