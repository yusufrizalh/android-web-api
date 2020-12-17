package id.yusufrizalh.webserviceandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class LihatDetailActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_id, edit_nama, edit_jabatan, edit_gaji;
    Button btn_ubah, btn_hapus;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_detail);

        // memunculkan back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_id = findViewById(R.id.edit_id);
        edit_nama = findViewById(R.id.edit_nama);
        edit_jabatan = findViewById(R.id.edit_jabatan);
        edit_gaji = findViewById(R.id.edit_gaji);
        btn_ubah = findViewById(R.id.btn_ubah);
        btn_hapus = findViewById(R.id.btn_hapus);

        btn_ubah.setOnClickListener(this);
        btn_hapus.setOnClickListener(this);

        Intent myIntent = getIntent();
        id = myIntent.getStringExtra(Konfigurasi.PGW_ID);
        edit_id.setText(id);

        getDataJSON();
    }

    private void getDataJSON() {
        class GetDataJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailActivity.this,
                        "Mengambil Data", "Harap tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String hasil = handler.sendGetResponse(Konfigurasi.URL_GET_DETAIL, id);
                return hasil;
            }

            @Override
            protected void onPostExecute(String pesan) {
                super.onPostExecute(pesan);
                loading.dismiss();
                displayDetailData(pesan);
            }
        }
        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute();
    }

    private void displayDetailData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject object = result.getJSONObject(0);
            String nama = object.getString(Konfigurasi.TAG_JSON_NAMA);
            String jabatan = object.getString(Konfigurasi.TAG_JSON_JABATAN);
            String gaji = object.getString(Konfigurasi.TAG_JSON_GAJI);

            edit_nama.setText(nama);
            edit_jabatan.setText(jabatan);
            edit_gaji.setText(gaji);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LihatDetailActivity.this, LihatDataActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_ubah) {
            ubahData();
        }
        if (v == btn_hapus) {
            konfirmasiHapusData();
        }
    }

    private void ubahData() {
        // perintah untuk mengubah data
        // variabel yang ingin diubah
        final String nama = edit_nama.getText().toString().trim();
        final String jabatan = edit_jabatan.getText().toString().trim();
        final String gaji = edit_gaji.getText().toString().trim();

        class UbahData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailActivity.this,
                        "Mengubah Data", "Harap tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_PGW_ID, id);
                params.put(Konfigurasi.KEY_PGW_NAMA, nama);
                params.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                params.put(Konfigurasi.KEY_PGW_GAJI, gaji);
                HttpHandler handler = new HttpHandler();
                String hasil = handler.sendPostRequest(Konfigurasi.URL_UPDATE, params);
                return hasil;
            }

            @Override
            protected void onPostExecute(String pesan) {
                super.onPostExecute(pesan);
                loading.dismiss();
                Toast.makeText(LihatDetailActivity.this,
                        "pesan: " + pesan, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LihatDetailActivity.this, LihatDataActivity.class));
            }
        }
        UbahData ubahData = new UbahData();
        ubahData.execute();
    }

    private void konfirmasiHapusData() {
        // alert dialog untuk konfirmasi
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menghapus Data");
        builder.setMessage("Apakah anda yakin menghapus?");
        builder.setIcon(getResources().getDrawable(android.R.drawable.ic_delete));
        builder.setPositiveButton("YAKIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // proses menghapus data
                hapusData();
            }
        });
        builder.setNegativeButton("BATAL", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hapusData() {
        class HapusData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LihatDetailActivity.this,
                        "Menghapus Data", "Harap tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String hasil = handler.sendGetResponse(Konfigurasi.URL_DELETE, id);
                return hasil;
            }

            @Override
            protected void onPostExecute(String pesan) {
                super.onPostExecute(pesan);
                loading.dismiss();
                Toast.makeText(LihatDetailActivity.this,
                        "pesan: " + pesan, Toast.LENGTH_SHORT).show();
                startActivityew Intent(LihatDetailActivity.this, LihatDataActivity.class));
            }
        }
        HapusData hapusData = new HapusData();
        hapusData.execute();
    }
}