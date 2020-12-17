package id.yusufrizalh.webserviceandroid;

public class Konfigurasi {
    // lokasi link web service berada
    public static final String URL_ADD = "http://172.16.101.80/pegawai/tambahPgw.php";
    public static final String URL_GET_ALL = "http://172.16.101.80/pegawai/tampilSemuaPgw.php";
    public static final String URL_GET_DETAIL = "http://172.16.101.80/pegawai/tampilPgw.php?id=";
    public static final String URL_UPDATE = "http://172.16.101.80/pegawai/updatePgw.php";
    public static final String URL_DELETE = "http://172.16.101.80/pegawai/hapusPgw.php?id=";

    // key value data json yang muncul di browser
    public static final String KEY_PGW_ID = "id";
    public static final String KEY_PGW_NAMA = "name";
    public static final String KEY_PGW_JABATAN = "desg";
    public static final String KEY_PGW_GAJI = "salary";

    // tag JSON
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_JSON_ID = "id";
    public static final String TAG_JSON_NAMA = "name";
    public static final String TAG_JSON_JABATAN = "desg";
    public static final String TAG_JSON_GAJI = "salary";

    // variabel ID pegawai
    public static final String PGW_ID = "emp_id";

}
