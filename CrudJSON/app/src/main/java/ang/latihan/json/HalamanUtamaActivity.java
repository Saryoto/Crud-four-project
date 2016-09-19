package ang.latihan.json;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HalamanUtamaActivity extends Activity {
	Button btnLihatCandi;
	Button btnTambahEvent;
	Button btnDaftarEvent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.halaman_utama);

		// inisialisasi button/tombol
		btnLihatCandi = (Button) findViewById(R.id.btnDaftarCandi);
		btnTambahEvent = (Button) findViewById(R.id.btnTambahEvent);
		btnDaftarEvent = (Button) findViewById(R.id.btnDaftarEvent);
		Button btnExit = (Button) findViewById(R.id.btnExit);

		// even klik untuk menampilkan class SemuaAnggotaActivity
		btnLihatCandi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				// Tampilkan semua anggota activity lewat intent
				Intent i = new Intent(getApplicationContext(),
						DaftarCandiActivity.class);
				startActivity(i);
			}
		});
		btnDaftarEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// Tampilkan semua anggota activity lewat intent
				Intent i = new Intent(getApplicationContext(),
						DaftarEventActivity.class);
				startActivity(i);
			}
		});

		// even klik menampilkan TambahAnggotaACtivity
		btnTambahEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				// Tampilkan tambah anggota activity lewat intent
				Intent i = new Intent(getApplicationContext(),
						TambahEventActivity.class);
				startActivity(i);
			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// Tampilkan semua anggota activity lewat intent
			finish();
			}
		});
	}
}
