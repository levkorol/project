package com.leokorol.testlove;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class HoroscopeActivity extends AppCompatActivity {
    private TextView _textViewTitle;
    private RecyclerView _recyclerViewHoroscopeItems;
    private TextView _textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);
        _textViewTitle = findViewById(R.id.textViewTitle);
        _recyclerViewHoroscopeItems = findViewById(R.id.recyclerViewHoroscopeItems);
        _textViewContent = findViewById(R.id.textViewContent);
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, new Locale("ru"));
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        _textViewTitle.setText("Гороскоп на " + today.monthDay + " " + month);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        _recyclerViewHoroscopeItems.setLayoutManager(llm);
        new GetHttpText().execute();
    }

    public void goToMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private class GetHttpText extends AsyncTask<Void, Integer, HoroscopeItem[]> {
        private String[] _zodiacSignsRussian = new String[] {
                "Овен",     "Телец",    "Близнецы",
                "Рак",      "Лев",      "Дева",
                "Весы",     "Скорпион", "Стрелец",
                "Козерог",  "Водолей",  "Рыбы"
        };
        protected HoroscopeItem[] doInBackground(Void... voids) {
            publishProgress(0);
            URL url;
            HttpURLConnection urlConnection = null;
            HoroscopeItem[] horoscopeItems;
            try {
                url = new URL("http://ignio.com/r/export/win/xml/daily/com.xml");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in, "cp1251");
                ZodiacHandler handler = new ZodiacHandler();
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser newSAXParser = saxParserFactory.newSAXParser();
                XMLReader parser = newSAXParser.getXMLReader();
                parser.setContentHandler(handler);
                InputSource source = new InputSource(isw);
                parser.parse(source);
                String[] result = handler.getParsedData();

                horoscopeItems = new HoroscopeItem[result.length];
                for (int i = 0; i < horoscopeItems.length; i++) {
                    horoscopeItems[i] = new HoroscopeItem(_zodiacSignsRussian[i], result[i]);
                }

            } catch (Exception e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return horoscopeItems;
        }

        protected void onProgressUpdate(Integer... progress) {
            HoroscopeActivity.this._textViewContent.setText("Загрузка...");
        }

        protected void onPostExecute(HoroscopeItem[] result) {
            if (result != null) {
                HoroscopeItemAdapter hia = new HoroscopeItemAdapter(result);
                _recyclerViewHoroscopeItems.setAdapter(hia);
                HoroscopeActivity.this._textViewContent.setText("");
            } else {
                HoroscopeActivity.this._textViewContent.setText("Невозможно загрузить гороскоп");
            }
        }

        public class ZodiacHandler extends DefaultHandler {
            private HashMap<String, Zodiacs> _nameToZodiacSign = new HashMap<>();
            private boolean _in_today = false;
            private Zodiacs _currentZodiacSign = null;
            private String[] _parsedData = new String[12];

            public ZodiacHandler() {
                _nameToZodiacSign.put("aries", Zodiacs.Aries);
                _nameToZodiacSign.put("taurus", Zodiacs.Taurus);
                _nameToZodiacSign.put("gemini", Zodiacs.Gemini);
                _nameToZodiacSign.put("cancer", Zodiacs.Cancer);
                _nameToZodiacSign.put("leo", Zodiacs.Leo);
                _nameToZodiacSign.put("virgo", Zodiacs.Virgo);
                _nameToZodiacSign.put("libra", Zodiacs.Libra);
                _nameToZodiacSign.put("scorpio", Zodiacs.Scorpio);
                _nameToZodiacSign.put("sagittarius", Zodiacs.Sagittarius);
                _nameToZodiacSign.put("capricorn", Zodiacs.Capricorn);
                _nameToZodiacSign.put("aquarius", Zodiacs.Aquarius);
                _nameToZodiacSign.put("pisces", Zodiacs.Pisces);
            }

            public String[] getParsedData() {
                String[] result = new String[_parsedData.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = _parsedData[i].trim();
                }
                return result;
            }

            @Override
            public void startDocument() throws SAXException {
            }

            @Override
            public void endDocument() throws SAXException {
            }

            /** Gets be called on opening tags like:
             * <tag>
             * Can provide attribute(s), when xml was like:
             * <tag attribute="attributeValue">*/
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes atts) throws SAXException {
                super.startElement(uri, localName, qName, atts);
                if (_nameToZodiacSign.containsKey(localName.toLowerCase())) {
                    _currentZodiacSign = _nameToZodiacSign.get(localName.toLowerCase());
                }
                if (localName.toLowerCase().equals("today")) {
                    _in_today = true;
                }
            }


            /** Gets be called on closing tags like:
             * </tag> */
            @Override
            public void endElement(String namespaceURI, String localName, String qName)
                    throws SAXException {
                if (_nameToZodiacSign.containsKey(localName.toLowerCase())) {
                    _currentZodiacSign = null;
                }
                if (localName.toLowerCase().equals("today")) {
                    _in_today = false;
                }
            }

            @Override
            public void characters(char ch[], int start, int length) {
                if(_in_today && _currentZodiacSign != null) {
                    int index = _currentZodiacSign.index();
                    if (_parsedData[index] == null) {
                        _parsedData[index] = new String(ch, start, length);
                    } else {
                        _parsedData[index] += new String(ch, start, length);
                    }
                }
            }
        }
    }
}
