package fadergs.edu.br.sigavan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastrarPassageiroActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;

    private EditText emailPassageiroEditText;
    private Spinner faculdadeSpinner;
    private Button confirmarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_passageiro);

        emailPassageiroEditText = (EditText) findViewById(R.id.emailPassageiroEditText);
        faculdadeSpinner = (Spinner) findViewById(R.id.faculdadeSpinner);
        confirmarButton = (Button) findViewById(R.id.confirmarCadastroButton);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        faculdadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String selecionado = item.toString();
                System.out.println("A SELEÇÃO FOI: " + selecionado);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final DatabaseReference emailRef = mFirebaseDataBase.getReference().child("users");
        emailRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String email = currentFirebaseUser.getEmail().toString();

                String emailBanco = dataSnapshot.child("email").getValue().toString();

                if (email.equals(emailBanco)) {
                    popularSpinner();

                    confirmarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String emailPassageiro = emailPassageiroEditText.getText().toString();

                            Query query1 = emailRef.orderByChild("email").equalTo(emailPassageiro).limitToFirst(1);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String passageiroKey = childSnapshot.getKey();
                                        System.out.println("RESULTADO QUERY COM CHAVE: " + passageiroKey);

                                        String faculdadeSelecionada = (String) faculdadeSpinner.getSelectedItem();
                                        System.out.println("VALOR SELECIONADO ANTES DE SALVAR : " + faculdadeSelecionada);
                                        String[] faculdadeSeparada = faculdadeSelecionada.split("=");

                                        emailRef.child(passageiroKey).child("aulas").child(faculdadeSeparada[0].trim()).child("motorista").setValue(email);
                                        emailRef.child(passageiroKey).child("aulas").child(faculdadeSeparada[0].trim()).child("turno").setValue(faculdadeSeparada[1].trim());
                                        emailRef.child(passageiroKey).child(faculdadeSeparada[0]).setValue(faculdadeSeparada[1]);

                                        new AlertDialog.Builder(CadastrarPassageiroActivity.this)
                                                .setTitle("Cadastro de Aluno")
                                                .setMessage("Deseja cadastrar outro aluno?")
                                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        emailPassageiroEditText.setText("");
                                                    }
                                                })
                                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(CadastrarPassageiroActivity.this, MotoristaActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Se ocorrer um erro
                                }
                            });
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


    public void popularSpinner() {
        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");
        mDataDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> faculdades = new ArrayList<String>();

                for (DataSnapshot faculdadesSnapshot : dataSnapshot.getChildren()) {
                    Object nomeFsculdade = faculdadesSnapshot.child("faculdades").getValue();
                    String modificado = "";
                    if (nomeFsculdade == null) {
                        System.out.println("É NULL");
                    } else {
                        String nome = nomeFsculdade.toString();
                        String[] separado = nome.split(",");
                        for (String item : separado) {
                            if (item.contains("{")) {
                                modificado = item.replaceAll("\\{", "");
                                faculdades.add(modificado);
                            } else if (item.contains("}")) {
                                modificado = item.replaceAll("\\}", "");
                                faculdades.add(modificado);
                            } else {
                                faculdades.add(item);
                            }
                        }
                    }
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CadastrarPassageiroActivity.this, android.R.layout.simple_spinner_item, faculdades);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                faculdadeSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
