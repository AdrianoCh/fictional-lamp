package fadergs.edu.br.sigavan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;

    private Button confirmarButton;
    private Button cancelarButton;
    private EditText nomeEditText;
    private EditText telefoneEditText;

    private PerfilUsuario perfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmarButton = (Button) findViewById(R.id.confirmarButton);
        cancelarButton = (Button) findViewById(R.id.cancelarButton);
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
        telefoneEditText = (EditText) findViewById(R.id.telefoneEditText);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerfilUsuario perfilUsuario = new PerfilUsuario(nomeEditText.getText().toString(), telefoneEditText.getText().toString(), null);
                mDataDatabaseReference.push().setValue(perfilUsuario);
            }
        });
    }
}
