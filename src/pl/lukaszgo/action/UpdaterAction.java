package pl.lukaszgo.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pl.lukaszgo.Settings;

/**
 * Created by lukaszgo on 2017-01-18.
 */
public class UpdaterAction extends Thread {

    private ProcessBuilder processBuilder;

    private Process process;

    private List<String> commands;

    private boolean isBeta;

    public UpdaterAction(List<String> commands, boolean isBeta) {
        super("ImportAction thread");
        this.commands = commands;
        this.isBeta = isBeta;
    }

    @Override
    public void run() {
        prepareProcessBuilder();
        execute();
    }

    private void prepareProcessBuilder() {
        processBuilder = new ProcessBuilder(commands);
    }

    private void execute() {
        try {
            processBuilder.directory(determineProcessBuilderDirectory());
            process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private File determineProcessBuilderDirectory() {
        if(!isBeta) {
            return new File(Settings.TRUNK_OMS_UPDATER);
        }

        return new File(Settings.BETA_OMS_UPDATER);
    }
}
