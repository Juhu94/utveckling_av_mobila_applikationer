package com.example.julian.da345a_mobila_applikationer_p1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for viewing a income or expense in detail.
 * If it is a expense, the activity will show a fitting image to the category chosen.
 */
public class DetailedViewActivity extends AppCompatActivity {

    private TextView tvUser;
    private TextView tvTitel;
    private TextView tvSumma;
    private TextView tvDate;
    private TextView tvKategori;
    private ImageView ivKategoriBild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        init();

        Intent intent = getIntent();
        String kategori = intent.getStringExtra("kategori");
        String type = intent.getStringExtra("typ");

        if(type.equals("Inkomst")){
            tvSumma.setText("Belopp: " + intent.getStringExtra("summa") + ":-");
        }
        else if(type.equals("Utgift")){
            tvSumma.setText("Pris: " + intent.getStringExtra("summa") + ":-");

            if(kategori.equals("Livsmedel")){
                ivKategoriBild.setImageResource(R.drawable.kategori_livsmedel);
                Toast.makeText(this, kategori, Toast.LENGTH_SHORT).show();
            }
            else if(kategori.equals("Fritid")){
                ivKategoriBild.setImageResource(R.drawable.kategori_fritid);
                Toast.makeText(this, kategori, Toast.LENGTH_SHORT).show();
            }
            else if(kategori.equals("Resor")){
                ivKategoriBild.setImageResource(R.drawable.kategori_resor);
                Toast.makeText(this, kategori, Toast.LENGTH_SHORT).show();
            }
            else if(kategori.equals("Boende")){
                ivKategoriBild.setImageResource(R.drawable.kategori_boende);
                Toast.makeText(this, kategori, Toast.LENGTH_SHORT).show();
            }
            else if(kategori.equals("Övrigt")){
                ivKategoriBild.setImageResource(R.drawable.kategori_ovrigt);
                Toast.makeText(this, kategori, Toast.LENGTH_SHORT).show();
            }
        }

        tvUser.setText(intent.getStringExtra("user"));
        tvTitel.setText("Titel: " + intent.getStringExtra("titel"));
        tvDate.setText("Datum: " + intent.getStringExtra("datum"));
        tvKategori.setText("Kategori: ");
    }

    private void init(){
        tvUser = findViewById(R.id.tvDetailedViewUser);
        tvTitel = findViewById(R.id.tvDetailedViewTitel);
        tvSumma = findViewById(R.id.tvDetailedViewSumma);
        tvDate = findViewById(R.id.tvDetailedViewDatum);
        tvKategori = findViewById(R.id.tvDetailedViewKategori);
        ivKategoriBild = findViewById(R.id.ivDetailedViewKategoriBild);
    }
}
