package com.example.derp;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class AddToDatabase extends AppCompatActivity {

    //definuj texty
    String alertJazykNenastaven = "Zadejte prosím Jazyk";
    String alertDateNenastaven = "Zadejte prosím Datum";
    String alertTimeNenastaven = "Zadejte prosím Čas";
    String alertPopisNenastaven = "Zadejte prosím Popis";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //najdi všechny inputy
        EditText jazykInput = findViewById(R.id.jazykinput);
        EditText popisInput = findViewById(R.id.descriptioninput);
        SeekBar rateInput = findViewById(R.id.rateinput);
        EditText dateInput = findViewById(R.id.dateinput);
        EditText timeInput = findViewById(R.id.timeinput);

        //ulož datum z kalendáře a vlož ho do inputy
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        dateInput.setText(date);

        //najdi buttony
        Button saveButton = findViewById(R.id.savebtn);
        Button dateBtn = findViewById(R.id.datebtn);

        //připoj se k databázi
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        //po kliknutí na kalendář...
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //zobraz kalendář
                Intent intent = new Intent(AddToDatabase.this, Calendar.class);
                startActivity(intent);
            }
        });

        //po kliknutí na uložit
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //získej hodnoty z inputů
                String jazyk = jazykInput.getText().toString();
                String popis = popisInput.getText().toString();
                String rate = String.valueOf(rateInput.getProgress());
                String time = timeInput.getText().toString();
                String date = dateInput.getText().toString();

                //zkontroluj, jestli jsou hodnoty nastavené a pokud ne tak to hlaš a nepokračuj
                //U "rate" je to zbytečné, protože nejde nastavit neplatná hodnota
                if (date.equals("")) {
                    Toast.makeText(v.getContext(),alertDateNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (jazyk.equals("")) {
                    Toast.makeText(v.getContext(),alertJazykNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.equals("")) {
                    Toast.makeText(v.getContext(),alertTimeNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (popis.equals("")) {
                    Toast.makeText(v.getContext(),alertPopisNenastaven,Toast.LENGTH_SHORT).show();
                    return;
                }

                //přidej čas vytvoření
                long createdTime = System.currentTimeMillis();

                //vytvož nový záznam, přidej do něj data a přidej ho do databáze
                realm.beginTransaction();
                TDA zaznam = realm.createObject(TDA.class);

                zaznam.setDate(date);
                zaznam.setJazyk(jazyk);
                zaznam.setPopis(popis);
                zaznam.setTime(time);
                zaznam.setRate(rate);
                zaznam.setCreatedTime(createdTime);

                realm.commitTransaction();

                //vrať se zpět na Main
                Intent intent = new Intent(AddToDatabase.this, Main.class);
                startActivity(intent);

            }
        });
    }
}