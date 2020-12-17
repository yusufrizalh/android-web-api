package id.yusufrizalh.webserviceandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 1: global variable
    private EditText edit_nama, edit_jabatan, edit_gaji;
    private Button btn_simpan, btn_lihat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2: mengenali semua komponen
        edit_nama = findViewById(R.id.edit_nama);
        edit_jabatan = findViewById(R.id.edit_jabatan);
        edit_gaji = findViewById(R.id.edit_gaji);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_lihat = findViewById(R.id.btn_lihat);

        // 3: event handling
        btn_simpan.setOnClickListener(this);
        btn_lihat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_simpan) {
            simpanData();
        }
        if (v == btn_lihat) {
            startActivity(new Intent(MainActivity.this, LihatDataActivity.class));
        }
    }

    private void simpanData() {
        // apa saja yang akan disimpan
        final String nama = edit_nama.getText().toString().trim();
        final String jabatan = edit_jabatan.getText().toString().trim();
        final String gaji = edit_gaji.getText().toString().trim();

        // membuat AsyncTask
        class SimpanData extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,
                        "Menyimpan Data", "Harap tunggu...",
                        false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Konfigurasi.KEY_PGW_NAMA, nama);
                params.put(Konfigurasi.KEY_PGW_JABATAN, jabatan);
                params.put(Konfigurasi.KEY_PGW_GAJI, gaji);
                HttpHandler handler = new HttpHandler();
                String hasil = handler.sendPostRequest(Konfigurasi.URL_ADD, params);
                return hasil;
            }

            @Override
            protected void onPostExecute(String pesan) {
                super.onPostExecute(pesan);
                loading.dismiss();
                clearText();
                Toast.makeText(MainActivity.this,
                        "pesan: " + pesan, Toast.LENGTH_SHORT).show();
            }
        }
        SimpanData simpanData = new SimpanData();
        simpanData.execute();
    }

    private void clearText() {
        edit_nama.setText("");
        edit_gaji.setText("");
        edit_jabatan.setText("");
        edit_nama.requestFocus();
    }
}