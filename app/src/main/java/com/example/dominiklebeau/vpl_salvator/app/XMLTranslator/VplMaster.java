package com.example.dominiklebeau.vpl_salvator.app.XMLTranslator;

import android.util.Log;

import com.example.dominiklebeau.vpl_salvator.app.XMLTranslator.session.KlassenSession;
import com.example.dominiklebeau.vpl_salvator.app.XMLTranslator.session.LehrerSession;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IPat on 08.01.2016.
 */
public class VplMaster {

    // Jeweils für einen Vertretungsplan pro Tag ein VplMaster-Objekt erstellen

    private boolean log;
    private final static String log_tag = "VplMaster";

    final String vplDateWochenTag;
    String title;
    String vplDate;                 // Gültiger Tag des Vpls
    String releaseDate;               // Angabe zur Veröffentlichung des Vpls
    String releaseTime;
    KlassenSession hofdienst;
    ArrayList<KlassenSession> missingClasses;
    ArrayList<LehrerSession> missingTeachers;

    private enum xmlKeys {
        Stunde,
        Lehrer,
        Klasse,
        Fach,
        VLehrer,
        VRaum,
        VFach,
        Status              // ( / Bemerkung )
    }

    private final ArrayList<HashMap<String, String>> data;

    private void logInfo(String info) {
        if (log) Log.i(log_tag, info);
    }

    private void logWarn(String warning) {
        if (log) Log.w(log_tag, warning);
    }

    public void setLogStatus(boolean log) {
        this.log = log;
    }

    public VplMaster(String wochentag, ArrayList<ArrayList<String>> vplRawData, boolean log) {
        setLogStatus(log);
        vplDateWochenTag = wochentag;

        data = new ArrayList<>();

        final ArrayList<String> vplConvertedData = new ArrayList<>();
        for (ArrayList<String> vplPartData : vplRawData) {
            vplConvertedData.addAll(vplPartData);
        }
        applyDataToObject(vplConvertedData);
    }

    public VplMaster(String wochentag, ArrayList<ArrayList<String>> vplRawData) {
        this(wochentag, vplRawData, false);
    }

    public void applyDataToObject(ArrayList<String> vplRawData) {
        final String preVplDate = vplRawData.get(0);
        vplDate = new StringBuilder().append(preVplDate,preVplDate.indexOf("(")+1,preVplDate.length() - 1).toString();
        logInfo("vplDate (0): " + vplDate);

        final String preVplRelDate = vplRawData.get(1);
        releaseDate = new StringBuilder().append(preVplRelDate, 0, preVplRelDate.indexOf(" ")).toString();
        releaseTime = new StringBuilder().append(preVplRelDate, preVplRelDate.indexOf(" ")+1, preVplRelDate.length()).toString();
        logInfo("releaseDate (1): " + releaseDate);
        logInfo("releaseTime (2): " + releaseTime);
    }
}
