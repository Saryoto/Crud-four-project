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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends Activity {
	EditText txtNamaEvent;
	EditText txtTempat;
	Button btnSave;
	Button btnDelete;
	String idmem;

	// Progress Dialog
	private ProgressDialog pDialog;

	// instansiasi objek JSON parser
	JSONParser jsonParser = new JSONParser();

	// inisialisasi url 
	private static final String url_detail_event = "http://192.168.43.251/candi/detail_event.php";
	private static final String url_update_event = "http://192.168.43.251/candi/update_event.php";
	private static final String url_hapus_event = "http://192.168.43.251/candi/hapus_event.php";

	// inisialisasi nama node dari json yang dihasilkan oleh php
	private static final String TAG_SUKSES = "sukses";
	private static final String TAG_MEMBER = "member";
	private static final String TAG_IDMEMBER = "id";
	private static final String TAG_NAMA_EVENT = "nama_event";
	private static final String TAG_TEMPAT = "tempat";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);

		// inisialisasi button
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);

		// ambil data anggota detail dari intent
		Intent i = getIntent();

		// ambil member id dari intent
		idmem = i.getStringExtra(TAG_IDMEMBER);

		// buat method ambil detail anggota pada background thread
		new AmbilDetailAnggota().execute();

		// klik even tombol save
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// buat background thread untuk simpan anggota
				new SimpanAnggotaDetail().execute();
			}
		});

		// klik even tombol delete
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// buat method hapus anggota dalam background thread
				new HapusAnggota().execute();
			}
		});
	}

	/**
	 * Background Async Task untuk menmpilkan data detail anggota
	 * */
	class AmbilDetailAnggota extends AsyncTask<String, String, String> {

		// Sebelum memulai background thread tampilkan Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditEventActivity.this);
			pDialog.setMessage("Mengambil data anggota. Silahkan tunggu...");
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
						url_detail_event, "GET", params1);

				// cek log untuk json respon
				Log.d("Anggota Detail", json.toString());

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
							txtNamaEvent = (EditText) findViewById(R.id.inputNamaEvent);
							txtTempat = (EditText) findViewById(R.id.inputTempat);
							try {
								// tampilkan data member di EditText
								txtNamaEvent.setText(member.getString(TAG_NAMA_EVENT));
								txtTempat.setText(member.getString(TAG_TEMPAT));
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

	/**
	 * Background Async Task untuk menyimpan/mengupdate data anggota
	 * */
	class SimpanAnggotaDetail extends AsyncTask<String, String, String> {

		// Sebelum memulai background thread tampilkan Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditEventActivity.this);
			pDialog.setMessage("Menyimpan Data Event ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		// simpan anggota
		protected String doInBackground(String... args) {

			// data yang akan diupdate dari EditTexts
			String nama_event = txtNamaEvent.getText().toString();
			String tempat = txtTempat.getText().toString();

			// Membangun Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idmem", idmem));
			params.add(new BasicNameValuePair("nama_event", nama_event));
			params.add(new BasicNameValuePair("tempat", tempat));

			// mengirim data yang diupdate lewat request http
			// Dengan method POST
			JSONObject json = jsonParser.makeHttpRequest(url_update_event,
					"POST", params);

			// cek json sukses tag (apakah 1 atau 0)
			try {
				int sukses = json.getInt(TAG_SUKSES);

				if (sukses == 1) {

					// sukses mengupdate data
					Intent i = getIntent();
					// kirim result code 100 untuk notifikasi kalau update
					setResult(100, i);
					finish();
				} else {

					// gagal update data
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * setelah update data selesai background task akan menghilangkan
		 * progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

			// dismiss dialog ketika selesai
			pDialog.dismiss();
		}
	}

	/**
	 * Background Async Task untuk menghapus data anggota
	 * */
	class HapusAnggota extends AsyncTask<String, String, String> {

		// Sebelum memulai background thread Tampilkan Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditEventActivity.this);
			pDialog.setMessage("Menghapus data Anggota...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		// menghapus data anggota
		protected String doInBackground(String... args) {
			int sukses;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("idmem", idmem));

				// ambil member/anggota detail dengan request HTTP
				JSONObject json = jsonParser.makeHttpRequest(
						url_hapus_event, "POST", params);

				// cek log untuk json respon
				Log.d("Delete Anggota", json.toString());

				// json sukses tag
				sukses = json.getInt(TAG_SUKSES);
				if (sukses == 1) {

					// Member sukses di delete
					// beritahu activity sebelumnya dengan mengirim result code
					Intent i = getIntent();
					setResult(100, i);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Setelah hapus data selesai background task akan menghilangkan
		 * progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

			// dismiss dialog setelah proses hapus selesai
			pDialog.dismiss();
		}
	}
}
