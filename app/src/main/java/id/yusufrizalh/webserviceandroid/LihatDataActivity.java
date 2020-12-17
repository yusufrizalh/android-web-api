package id.yusufrizalh.webserviceandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LihatDataActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    // 1: inisialisasi global variable
    private ListView list_view;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        // memunculkan back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 2: mengenali komponen
        list_view = findViewById(R.id.list_view);

        // 3: event handling utk list view
        list_view.setOnItemClickListener(this);

        // perintah utk mengmbil data JSON
        getJSON();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDataActivity.this,
                        "Mengambil Data", "Harap tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String hasil = handler.sendGetResponse(Konfigurasi.URL_GET_ALL);
                return hasil;
            }

            @Override
            protected void onPostExecute(String pesan) {
                super.onPostExecute(pesan);
                loading.dismiss();
                JSON_STRING = pesan;
                Log.d("JSON_STRING1", JSON_STRING);
                // untuk menampilkan semua data JSON yang berhasil diambil dari server
                displayAllData();
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            Log.d("JSON_STRING2", JSON_STRING);

            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                String id = object.getString(Konfigurasi.TAG_JSON_ID);
                String name = object.getString(Konfigurasi.TAG_JSON_NAMA);

                HashMap<String, String> pegawai = new HashMap<>();
                pegawai.put(Konfigurasi.TAG_JSON_ID, id);
                pegawai.put(Konfigurasi.TAG_JSON_NAMA, name);

                // ubah fotmat json menjadi array list
                list.add(pegawai);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // membuat adapter untuk meletakkan arraylist kedalam listview
        ListAdapter adapter = new SimpleAdapter(
                getApplicationContext(), list,
                R.layout.activity_list_item,
                new String[]{Konfigurasi.TAG_JSON_ID, Konfigurasi.TAG_JSON_NAMA},
                new int[]{R.id.txt_id, R.id.txt_name}
        );
        list_view.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent myIntent = new Intent(LihatDataActivity.this, LihatDetailActivity.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String pgwId = map.get(Konfigurasi.TAG_JSON_ID).toString();
        myIntent.putExtra(Konfigurasi.PGW_ID, pgwId);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LihatDataActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}