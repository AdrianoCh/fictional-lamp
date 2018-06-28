package fadergs.edu.br.sigavan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CadastrarFaculdadeActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;

    private EditText nomeFaculdadeEditText;
    private RadioGroup turnoRadioGroup;
    private Button confirmarCadastroButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_faculdade);

        nomeFaculdadeEditText = (EditText) findViewById(R.id.nomeFaculdadeEditText);
        turnoRadioGroup = (RadioGroup) findViewById(R.id.turnoRadioGroup);
        confirmarCadastroButton = (Button) findViewById(R.id.confirmarCadastroButton);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarCadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int turnoSelecionado = turnoRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(turnoSelecionado);
                String textoTurno = (String) radioButton.getText();

                String nome = nomeFaculdadeEditText.getText().toString();

                salvarBanco(nome, textoTurno);
            }
        });
    }

    public void salvarBanco(final String nome, final String turno) {
        final DatabaseReference emailRef = mFirebaseDataBase.getReference().child("users");
        emailRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String email = currentFirebaseUser.getEmail().toString();

                String emailBanco = dataSnapshot.child("email").getValue().toString();

                if (email.equals(emailBanco)) {
                    Query query1 = emailRef.orderByChild("email").equalTo(email).limitToFirst(1);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String motoristaKey = childSnapshot.getKey();
                                System.out.println("RESULTADO QUERY COM CHAVE: " + motoristaKey);

                                emailRef.child(motoristaKey).child("faculdades").child(nome).setValue(turno);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
