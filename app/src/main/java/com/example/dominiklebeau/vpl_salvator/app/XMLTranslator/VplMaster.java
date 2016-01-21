package com.example.dominiklebeau.vpl_salvator.app.XMLTranslator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IPat on 08.01.2016.
 */
public class VplMaster {

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

    private HashMap<String, ArrayList<String>> data;

    public VplMaster(ArrayList<ArrayList<String>> vplRawData) {
        final ArrayList<String> vplConvertedData = new ArrayList<>();
        data = new HashMap<>();
        for (ArrayList<String> vplPartData : vplRawData) {
            vplConvertedData.addAll(vplPartData);
        }
        applyDataToObject(vplConvertedData);
    }

    public void applyDataToObject(ArrayList<String> vplRawData) {

    }
}
