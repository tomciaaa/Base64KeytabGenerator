import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.krb5.internal.ktab.KeyTabConstants;
import sun.security.krb5.internal.ktab.KeyTabEntry;
import sun.security.krb5.internal.ktab.KeyTabOutputStream;

import java.io.IOException;
import java.util.Base64;


public class Ktab {
    public static void main(String[] args) throws KrbException, IOException {
        KeyTab tab = KeyTab.getInstance();
        PrincipalName name = new PrincipalName("test@CRAP.NET");
        tab.addEntry(name, "test".toCharArray(), 0, false);
        dumpToStdout(tab.getEntries());
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

