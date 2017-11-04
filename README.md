# Base64KeytabGenerator

A simple (example) keytab generator that writes output to STDOUT in base64 encoded form. This is useful when generating the keytab to be stored in an external vault without storing sensitive data on the hard drive.

## Gotchas

This thing only works by tapping into the Java internal classes, which as of Java 8 are still easy to (ab)use but Java 9 features such as modules will make it much harder (if not impossible). There are odly few decent implementations of low-level Kerberos code and while the Keytab format is relatively straight-forward (see [official MIT Kerberos docs](https://web.mit.edu/kerberos/krb5-1.12/doc/formats/keytab_file_format.html)) the way to calculate the key at least from a cursory glance requires running standard crypto but in a very special setting (for example see [comments on Java classes](https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/sun/security/krb5/internal/crypto/dk/DkCrypto.java#L48-L54) and the referenced [RFC-3961](http://www.ietf.org/rfc/rfc3961.txt) and [RFC-3962](http://www.ietf.org/rfc/rfc3962.txt)).

TL;DR abusing Java internals was better than rolling my own crypto :)
