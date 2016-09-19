package ang.latihan.json;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailCandiActivity extends Activity {
    TextView txtNama;
    TextView txtAlamat;
    TextView txtKeterangan;
    TextView txtGambar;
    String idmem;

    // Progress Dialog
    private ProgressDialog pDialog;

    // instansiasi objek JSON parser
    JSONParser jsonParser = new JSONParser();

    // inisialisasi url
    private static final String url_detail_candi = "http://192.168.43.251/candi/detail_candi.php";


    // inisialisasi nama node dari json yang dihasilkan oleh php
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_MEMBER = "member";
    private static final String TAG_IDMEMBER = "id";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_ALAMAT = "alamat";
    private static final String TAG_KETERANGAN = "keterangan";
    private static final String TAG_GAMBAR = "gambar";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_candi);

        // inisialisasi button

        // ambil data anggota detail dari intent
        Intent i = getIntent();

        // ambil member id dari intent
        idmem = i.getStringExtra(TAG_IDMEMBER);

        // buat method ambil detail anggota pada background thread
        new AmbilDetailAnggota().execute();

    }

    /**
     * Background Async Task untuk menmpilkan data detail anggota
     * */
    class AmbilDetailAnggota extends AsyncTask<String, String, String> {

        // Sebelum memulai background thread tampilkan Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailCandiActivity.this);
            pDialog.setMessage("Mengambil data candi. Silahkan tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Ambil detail anggota background thread
         * */
        protected String doInBackground(String... params) {

            // Cek jika tag sukses bernilai 1 atau 0
            int sukses;
            try {

                // Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("idmem", idmem));

                // ambil detail anggota dengan request HTTP
                // dengan menggunakan method GET
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_candi, "GET", params1);

                // cek log untuk json respon
                Log.d("Candi Detail", json.toString());

                // json sukses tag
                sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {

                    // sukses mengambil detail anggota
                    JSONArray memberObj = json.getJSONArray(TAG_MEMBER);

                    // ambil objek member pertama dari JSON Array
                    final JSONObject member = memberObj.getJSONObject(0);

                    // update UI dari Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtNama = (TextView) findViewById(R.id.inputNama);
                            txtAlamat = (TextView) findViewById(R.id.inputAlamat);
                            txtKeterangan = (TextView) findViewById(R.id.inputKeterangan);
                            txtGambar = (TextView) findViewById(R.id.inputGambar);
                            try {
                                // tampilkan data member di EditText
                                txtNama.setText(member.getString(TAG_NAMA));
                                txtAlamat.setText(member.getString(TAG_ALAMAT));
                                txtKeterangan.setText(member.getString(TAG_KETERANGAN));
                                txtGambar.setText(member.getString(TAG_GAMBAR));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                } else {

                    // jika idmem tidak ditemukan
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // setelah background task selesai hilangkan progress dialog
        protected void onPostExecute(String file_url) {

            // dismiss dialog setelah mendapatkan data detail anggota
            pDialog.dismiss();
        }
    }
}
