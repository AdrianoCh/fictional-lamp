package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

public class CompletarCadastroPassageiroActivity extends AppCompatActivity {
    private CheckBox domingoChecbox;
    private CheckBox segundaCheckbox;
    private CheckBox tercaChecbox;
    private CheckBox quartaCheckbox;
    private CheckBox quintaCheckbox;
    private CheckBox sextaCheckbox;
    private CheckBox sabadoCheckbox;
    private Button confirmarDias;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro_passageiro);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        domingoChecbox = (CheckBox) findViewById(R.id.domingoCheckbox);
        segundaCheckbox = (CheckBox) findViewById(R.id.segundaCheckbox);
        tercaChecbox = (CheckBox) findViewById(R.id.tercaCheckbox);
        quartaCheckbox = (CheckBox) findViewById(R.id.quartaCheckbox);
        quintaCheckbox = (CheckBox) findViewById(R.id.quintaCheckbox);
        sextaCheckbox = (CheckBox) findViewById(R.id.sextaCheckbox);
        sabadoCheckbox = (CheckBox) findViewById(R.id.sabadoCheckbox);
        confirmarDias = (Button) findViewById(R.id.confirmarDias);

        Boolean domingoSelecionado = false;
        Boolean segundaSelecionado = false;
        Boolean tercaSelecionado = false;
        Boolean quartaSelecionado = false;
        Boolean quintaSelecionado = false;
        Boolean sextaSelecionado = false;
        Boolean sabadoSelecionado = false;

        if (domingoChecbox.isChecked()) {
            domingoSelecionado = true;
        }
        if (segundaCheckbox.isChecked()) {
            segundaSelecionado = true;
        }
        if (tercaChecbox.isChecked()) {
            tercaSelecionado = true;
        }
        if (quartaCheckbox.isChecked()) {
            quartaSelecionado = true;
        }
        if (quintaCheckbox.isChecked()) {
            segundaSelecionado = true;
        }
        if (sextaCheckbox.isChecked()) {
            sextaSelecionado = true;
        }
        if (sabadoCheckbox.isChecked()) {
            sabadoSelecionado = true;
        }

        final DatabaseReference emailRef = mFirebaseDataBase.getReference().child("users");
        emailRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String emailLogado = currentFirebaseUser.getEmail().toString();

                String emailBanco = dataSnapshot.child("email").getValue().toString();

                if (emailLogado.equals(emailBanco)) {
                    confirmarDias.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Query query1 = emailRef.orderByChild("email").equalTo(emailLogado).limitToFirst(1);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String passageiroKey = childSnapshot.getKey();
                                        System.out.println("RESULTADO QUERY COM CHAVE: " + passageiroKey);

                                        String teste = dataSnapshot.child(passageiroKey).child("aulas").getValue().toString();
                                        System.out.println("TESTE CPNSULTA" + teste);
                                        String[] separado = teste.split("=");
                                        System.out.println("TESTE CPNSULTA" + separado[0] + separado[1]);

                                        if (domingoChecbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Domingo").setValue("true");
                                        }
                                        if (segundaCheckbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Segunda").setValue("true");
                                        }
                                        if (tercaChecbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Terca").setValue("true");
                                        }
                                        if (quartaCheckbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Quarta").setValue("true");
                                        }
                                        if (quintaCheckbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Quinta").setValue("true");
                                        }
                                        if (sextaCheckbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Sexta").setValue("true");
                                        }
                                        if (sabadoCheckbox.isChecked()) {
                                            emailRef.child(passageiroKey).child("aulas").child(separado[0].replaceAll("\\{", "").trim()).child("presenca").child("Sabado").setValue("true");
                                        } else {
                                            Toast.makeText(CompletarCadastroPassageiroActivity.this, "Selecione uma opção", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    Intent myIntent = new Intent(CompletarCadastroPassageiroActivity.this, PassageiroActivity.class);
                                    startActivity(myIntent);
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

}
