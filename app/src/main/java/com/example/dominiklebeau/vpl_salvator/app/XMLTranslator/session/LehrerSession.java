package com.example.dominiklebeau.vpl_salvator.app.XMLTranslator.session;

/**
 * Created by IPat on 23.01.2016.
 */
public class LehrerSession extends Stunde {
    private final String name;

    public LehrerSession(final String name, final int stunde) {
        this(name, (byte)stunde);
    }

    public LehrerSession(final String name, final byte stunde) {
        this(name, stunde, stunde);
    }

    public LehrerSession(final String name, final int startStunde, final int endStunde) {
        this(name, (byte) startStunde, (byte) endStunde);
    }

    public LehrerSession(final String name, final byte startStunde, final byte endStunde) {
        super(startStunde, endStunde);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
