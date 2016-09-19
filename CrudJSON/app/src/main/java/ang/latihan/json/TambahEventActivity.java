package ang.latihan.json;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TambahEventActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputNamaEvent;
	EditText inputTempat;

	// inisialisasi url tambahanggota.php
	private static String url_tambah_event = "http://192.168.43.251/candi/tambah_event.php";

	// inisialisasi nama node dari json yang dihasilkan oleh php (utk class ini
	// hanya node "sukses")
	private static final String TAG_SUKSES = "sukses";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tambah_event);

		// inisialisasi Edit Text
		inputNamaEvent = (EditText) findViewById(R.id.inputNamaEvent);
		inputTempat = (EditText) findViewById(R.id.inputTempat);

		// inisialisasi button
		Button btnTambahEvent = (Button) findViewById(R.id.btnTambahEvent);

		// klik even tombol tambah anggota
		btnTambahEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// buat method pada background thread
				new BuatAnggotaBaru().execute();
			}
		});
	}

	/**
	 * Background Async Task untuk menambah data anggota baru
	 * */
	class BuatAnggotaBaru extends AsyncTask<String, String, String> {

		// sebelum memulai background thread tampilkan Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TambahEventActivity.this);
			pDialog.setMessage("Menambah data..silahkan tunggu");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		// menambah data
		protected String doInBackground(String... args) {
			String nama_event = inputNamaEvent.getText().toString();
			String tempat = inputTempat.getText().toString();

			// Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nama_event", nama_event));
			params.add(new BasicNameValuePair("tempat", tempat));

			// mengambil JSON Object dengan method POST
			JSONObject json = jsonParser.makeHttpRequest(url_tambah_event,
					"POST", params);

			// periksa respon log cat 
			Log.d("Respon tambah anggota", json.toString());

			try {
				int sukses = json.getInt(TAG_SUKSES);
				if (sukses == 1) {
					
					// jika sukses menambah data baru
					Intent i = new Intent(getApplicationContext(),
							TambahEventActivity.class);
					startActivity(i);

					// tutup activity ini
					finish();
				} else {
					
					// jika gagal dalam menambah data
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// hilangkan dialog ketika selesai menambah data baru
			pDialog.dismiss();
		}
	}
}
