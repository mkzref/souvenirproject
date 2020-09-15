package com.example.souvenir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.souvenir.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Tracking extends AppCompatActivity {
    EditText etLngg;
    Button showdataonmap;
    double latitude, longitude;
    EditText etLatt;
    TextView datal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        datal = findViewById(R.id.DATALIST);
        Intent intent=getIntent();//from user adapter
        final String datalist = intent.getStringExtra("Data");
        datal.setText(datalist);
        final String Points = intent.getStringExtra("points");
       // Toast.makeText(getApplicationContext() , Points + " " , Toast.LENGTH_LONG).show();
        String[] parts = Points.split(",", 2);
        String part1 = parts[0];
        String part2 = parts[1];
        etLatt=findViewById(R.id.etLat);
        etLngg=findViewById(R.id.etLng);
        etLatt.setText(part2);
        etLngg.setText(part1);
        showdataonmap=findViewById(R.id.Update);
        showdataonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLatt.getText().toString().isEmpty() && etLngg.getText().toString().isEmpty()) {
                    // Toast.makeText(getBaseContext(), "Please Enter Latitude or Longitude", Toast.LENGTH_SHORT).show();
                    etLatt.setError("Please Enter Latitude");
                    etLngg.setError("Please Enter Longitude");
                    etLngg.requestFocus();
                    // Focus the EditText for Latitude
                    etLatt.requestFocus();

                    return;

                }
                else if (etLatt.getText().toString().isEmpty()){
                    // Toast.makeText(getBaseContext(), "Please Enter Longitude", Toast.LENGTH_SHORT).show();
                    etLatt.setError("Please Enter Latitude");
                    etLatt.requestFocus();


                }


                else if (etLngg.getText().toString().isEmpty()){
                    //Toast.makeText(getBaseContext(), "Please Enter Longitude", Toast.LENGTH_SHORT).show();
                    etLngg.setError("Please Enter Longitude");
                    etLngg.requestFocus();
                }
                else{
                    // Getting user input latitude
                    latitude = Double.parseDouble(etLatt.getText().toString());
                    longitude = Double.parseDouble(etLngg.getText().toString());
                    Intent intent = new Intent(Tracking.this, MapsActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);




                }

                // Getting reference to EditText et_lng




                etLatt.setText("");
                etLngg.setText("");





            }
        });



    }


}
