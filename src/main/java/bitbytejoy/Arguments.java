package bitbytejoy;

import org.apache.commons.cli.*;

public class Arguments {
    public CommandLine parseArguments(String[] args) {
        Options options = new Options();
        options
                .addOption("input", true, "Absolute file path of the file containing a list of words")
                .addOption("output", true, "Absolute file path of the file used to output calculated anagrams");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            printHelp(options);
            System.exit(1);
        }

        if (!cmd.hasOption("input")) {
            printHelp(options);
            System.exit(1);
        }

        return cmd;
    }

    public void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("anagram", options);
    }
}
