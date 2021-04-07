package local.bwg;

import local.bwg.model.BotOptions;

/**
 * @author BirewallG
 */
public class App {
    public static void main(String[] args) {
        try {
            if (args.length == 4) {
                new CoreExp(args[0], args[1], args[2], args[3]);
            } else if (args.length == 3) {
                new CoreExp(args[0], "9987", args[1], args[2]);
            } else
                new CoreExp(
                        BotOptions.getTeamspeakBotIpAddress(),
                        BotOptions.getTeamspeakBotPort(),
                        BotOptions.getTeamspeakBotLogin(),
                        BotOptions.getTeamspeakBotPassword()
                );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("\n Error: incorrect parameters\nnot connect to the server");
        }
    }
}