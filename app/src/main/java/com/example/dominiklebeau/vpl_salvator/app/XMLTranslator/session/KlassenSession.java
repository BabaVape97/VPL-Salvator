package com.example.dominiklebeau.vpl_salvator.app.XMLTranslator.session;

/**
 * Created by IPat on 22.01.2016.
 */
public class KlassenSession extends Stunde {
    private final boolean gymnasial;
    private final int klassenstufe;

    public KlassenSession(final String rawData) {
        this(fetchKlasse(rawData), fetchStartStunde(rawData), fetchEndStunde(rawData));
    }

    private static String fetchKlasse(String rawData) {
        final char[] rawDataChars = rawData.toCharArray();
        int pointer = 0;

        while (!(rawDataChars[pointer]=="(".charAt(0))) {
            pointer++;
        }

        StringBuilder sb = new StringBuilder(new String(rawDataChars));
        sb.setLength(pointer);

        return sb.toString();
    }

    private static byte fetchStartStunde(String rawData) {

    }

    private static byte fetchEndStunde(String rawData) {

    }

    public KlassenSession(final String klasse, final int stunde) {
        this(klasse, (byte)stunde);
    }

    public KlassenSession(final String klasse, final byte stunde) {
        this(klasse, stunde, stunde);
    }

    public KlassenSession(final String klasse, final int startStunde, final int endStunde) {
        this(klasse, (byte) startStunde, (byte) endStunde);
    }

    public KlassenSession(final String klasse, final byte startStunde, final byte endStunde) {
        super(startStunde, endStunde);


    }
}
