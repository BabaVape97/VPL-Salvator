package com.example.dominiklebeau.vpl_salvator.app.XMLTranslator.session;

/**
 * Created by IPat on 23.01.2016.
 */
public class Stunde {

    //TODO: Mehrere Stunden?

    private final byte startStunde;
    private final byte endStunde;

    public Stunde(final byte startStunde, final byte endStunde) {
        this.startStunde = startStunde;
        this.endStunde = endStunde;
    }

    public byte getStartStunde() {
        return startStunde;
    }

    public byte getEndStunde() {
        return endStunde;
    }
}
