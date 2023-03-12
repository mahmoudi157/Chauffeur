package com.app.chaufeur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input;
    Button add_button;
    private HashMap<String, Object> map = new HashMap<>();
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference Database = _firebase.getReference("Database");
    private String pos = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addBook(title_input.getText().toString().trim(),
                        author_input.getText().toString().trim(),
                        Integer.valueOf(pages_input.getText().toString().trim()));

                map = new HashMap<>();
                map.put("Head", title_input.getText().toString());
                map.put("Text", author_input.getText().toString());
                Database.child(pos).updateChildren(map);
                map.clear();

             /*   map = new HashMap<>();
                map.put("name", title_input.getText().toString());
                map.put("prenom", author_input.getText().toString());
                map.put("nemero", pages_input.getText().toString());
                Database.child((Math.random()*100000000)+"post").updateChildren(map);
                map.clear();*/
                title_input.setText("");
                author_input.setText("");
                pages_input.setText("");
                Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                Myfunction.showMessage(getApplicationContext(), "Success");


            }
        });
    }
}