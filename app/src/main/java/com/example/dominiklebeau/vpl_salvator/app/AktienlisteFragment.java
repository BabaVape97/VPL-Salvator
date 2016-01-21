package com.example.dominiklebeau.vpl_salvator.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/*
    TODO: DIESE DATEI KOMPLETT MODIFIZIEREN
    DIESE .JAVA DATEI ORIENTIERT SICH AN DER CORE.JAVA IN XMLTRANSLATOR ORDNER

    IN DIESER DATEI IST LEDIGLICH EIN VPLMASTER OBJEKT NÖTIG, BEISPIEL:
    VplMaster vpl = new VplMaster(NOCH_UNBEKANNTE_PARAMETER);

    ! DIESE DATEI IST LEDIGLICH ZUR DARSTELLUNG DER DATEN VERANTWORTLICH, NICHT ZUR VERARBEITUNG !
 */

public class AktienlisteFragment extends Fragment {

    // Der ArrayAdapter ist jetzt eine Membervariable der Klasse AktienlisteFragment
    ArrayAdapter<String> mAktienlisteAdapter;

    public AktienlisteFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MenÃ¼ bekannt geben, dadurch kann unser Fragment MenÃ¼-Events verarbeiten
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_aktienlistefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wir prÃ¼fen, ob MenÃ¼-Element mit der ID "action_daten_aktualisieren"
        // ausgewÃ¤hlt wurde und geben eine Meldung aus
        int id = item.getItemId();
        if (id == R.string.action_daten_aktualisieren) {

            // Erzeugen einer Instanz von HoleDatenTask und starten des asynchronen Tasks
            HoleDatenTask holeDatenTask = new HoleDatenTask();
            holeDatenTask.execute("Aktie");

            // Auslesen der ausgewählten Aktienliste aus den SharedPreferences
            SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String prefAktienlisteKey = getString(R.string.preference_aktienliste_key);
            String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);
            String aktienliste = sPrefs.getString(prefAktienlisteKey, prefAktienlisteDefault);

            // Auslesen des Anzeige-Modus aus den SharedPreferences
            String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
            Boolean indizemodus = sPrefs.getBoolean(prefIndizemodusKey, false);

            // Starten des asynchronen Tasks und Übergabe der Aktienliste
            if (indizemodus) {
                String indizeliste = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";
                holeDatenTask.execute(indizeliste);
            }
            else {
                holeDatenTask.execute(aktienliste);
            }


            // Starten des asynchronen Tasks und Ãœbergabe der Aktienliste
            holeDatenTask.execute(aktienliste);

            // Den Benutzer informieren, dass neue Aktiendaten im Hintergrund abgefragt werden
            Toast.makeText(getActivity(), "Aktiendaten werden abgefragt!", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String LOG_TAG = AktienlisteFragment.class.getSimpleName();

        Log.v(LOG_TAG, "verbose     - Meldung");
        Log.d(LOG_TAG, "debug       - Meldung");
        Log.i(LOG_TAG, "information - Meldung");
        Log.w(LOG_TAG, "warning     - Meldung");
        Log.e(LOG_TAG, "error       - Meldung");


        String [] aktienlisteArray = {
                "Stundenplan",
                "Vertretungsplan",
                "Klausurplan",
                "Abgabgetermine",
                "Feiertage",
                "Ferien",
                "Dokumente",
                "Frei",
                "Frei"
        };
        List<String> aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));

        mAktienlisteAdapter =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_aktienliste, // ID der XML-Layout Datei
                        R.id.list_item_aktienliste_textview, // ID des TextViews
                        aktienListe); // Beispieldaten in einer ArrayList


        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

        ListView aktienlisteListView = (ListView)
                rootView.findViewById(R.id.listview_aktienliste);
        aktienlisteListView.setAdapter(mAktienlisteAdapter);

        aktienlisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int
                    position, long id) {
                String aktienInfo = (String)
                        adapterView.getItemAtPosition(position);
// Intent erzeugen und Starten der AktiendetailActivity mit explizitem Intent
                Intent aktiendetailIntent = new Intent(getActivity(), AktiendetailActivity.class);
                aktiendetailIntent.putExtra(Intent.EXTRA_TEXT, aktienInfo);
                startActivity(aktiendetailIntent);
            }
        });
        return rootView;
    }

    // Innere Klasse HoleDatenTask fÃ¼hrt den asynchronen Task auf eigenem Arbeitsthread aus
    public class HoleDatenTask extends AsyncTask<String, Integer, String[]> {

        private final String LOG_TAG = HoleDatenTask.class.getSimpleName();
        private ArrayList<String> leseXmlAktiendatenAus(String xmlString) {
            // TODO: Sowas wie:
            // return vpl.getCore().parseStringArray(xmlString);

            return null; //Temporäre Zeile. Bitte bei Unnotwendigkeit entfernen.
        }


        @Override
        protected String[] doInBackground(String... strings) {
            //TODO: Sowas wie:
            //return leseXmlAktiendatenAus(vpl.getCore().fetchXmlFromLink(SALVATOR_LINK));

            return null; //Temporäre Zeile. Bitte bei Unnotwendigkeit entfernen.
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            // Auf dem Bildschirm geben wir eine Statusmeldung aus, immer wenn
            // publishProgress(int...) in doInBackground(String...) aufgerufen wird
            Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String[] strings) {

            // Wir lÃ¶schen den Inhalt des ArrayAdapters und fÃ¼gen den neuen Inhalt ein
            // Der neue Inhalt ist der RÃ¼ckgabewert von doInBackground(String...) also
            // der StringArray gefÃ¼llt mit Beispieldaten
            if (strings != null) {
                mAktienlisteAdapter.clear();
                for (String aktienString : strings) {
                    mAktienlisteAdapter.add(aktienString);
                }
            }

            // Hintergrundberechnungen sind jetzt beendet, darÃ¼ber informieren wir den Benutzer
            Toast.makeText(getActivity(), "Daten vollständig geladen!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

