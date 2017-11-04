import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.krb5.internal.ktab.KeyTabConstants;
import sun.security.krb5.internal.ktab.KeyTabEntry;
import sun.security.krb5.internal.ktab.KeyTabOutputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;


public class Ktab {
    public static void main(String[] args) {
        boolean parsingError = false;
        PrincipalName name = null;
        int kvno = 0;
        if (args.length != 2)
        {
            parsingError = true;
        } else {
            try {
                kvno = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex){
                parsingError = true;
            }
        }
        if (parsingError) {
            System.err.println("Usage: java Ktab <principal name> <kvno>");
            System.err.println("");
            System.err.println("Example: java Ktab tomciaaa@0X90.LT 1");
            System.exit(1);
       }

        try {
            name = new PrincipalName(args[0]);
        } catch (RealmException e) {
            System.err.println(args[0] + " is an invalid principal name");
            System.exit(1);
        }

        KeyTab tab = KeyTab.getInstance();
        char[] passphrase = null;
        try {
            passphrase = System.console().readPassword();
        } catch (Exception e) {
            System.err.println("Cannot use console, falling back to compatible mode (will echo password if using prompt)");
        }
        if (passphrase == null) {
            BufferedReader stdin = new BufferedReader( new InputStreamReader(System.in) );
            try {
                passphrase = stdin.readLine().toCharArray();
            } catch (IOException e) {
                System.err.println("Cannot read password: " + e.getMessage());
                System.exit(1);
            }
        }

        try {
            tab.addEntry(name, passphrase, kvno, false);
        } catch (KrbException e) {
            System.err.println("Cannot create entry in keytab: " + e.getMessage());
            System.exit(1);
        }

        try {
            dumpToStdout(tab.getEntries());
        } catch (IOException ex){
            System.err.println("Failed to write keytab out: " + ex.getMessage());
        }
    }

    public static void dumpToStdout(KeyTabEntry[] entries) throws IOException {
        try (KeyTabOutputStream kos =
                     new KeyTabOutputStream(Base64.getEncoder().wrap(System.out))) {
            kos.writeVersion(KeyTabConstants.KRB5_KT_VNO);
            for (int i = 0; i < entries.length; i++) {
                kos.writeEntry(entries[i]);
            }
        }
    }
}

