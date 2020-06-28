package local.bwg;

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
                new CoreExp("10.0.0.63", "9987", "UnicBot", "1zomdPun");
        } catch (Exception ignore) {
            ignore.printStackTrace();
            System.out.printf("\n Error: incorrect parameters\nnot connect to the server");
        }
    }
}