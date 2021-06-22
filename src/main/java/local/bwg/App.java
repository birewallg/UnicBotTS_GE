package local.bwg;

import local.bwg.model.BotOptions;

/**
 * @author BirewallG
 */
public class App {
    public static void main(String[] args) {
        try {
            BotOptions options = new BotOptions();
            new CoreExp(
                    options.getTeamspeakBotIpAddress(),
                    options.getTeamspeakBotPort(),
                    options.getTeamspeakBotLogin(),
                    options.getTeamspeakBotPassword()
                );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("\n Error: incorrect parameters\nnot connect to the server");
        }
    }
}