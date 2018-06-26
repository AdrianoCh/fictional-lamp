package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;
    private Button confirmarButton;
    private Button cancelarButton;
    private EditText telefoneEditText;
    private RadioGroup modoDeUsoRadioGroup;
    private TextView nomeUsuarioTextView;
    private String textoModoDeUso;
    private boolean validation;

    private String perfilUsuarioRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmarButton = (Button) findViewById(R.id.confirmarbutton);
        telefoneEditText = (EditText) findViewById(R.id.telefoneEditText);
        modoDeUsoRadioGroup = (RadioGroup) findViewById(R.id.modoDeUsoRadioGroup);
        nomeUsuarioTextView = (TextView) findViewById(R.id.nomeUsuarioTextView);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int modoDeUsoSelecionado = modoDeUsoRadioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(modoDeUsoSelecionado);

                FirebaseUser emailCurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String email = emailCurrentFirebaseUser.getEmail();

                String edttelefone = telefoneEditText.getText().toString();
                int tamanhotelefone = edttelefone.length();

                if ((tamanhotelefone != 11) || (radioButton == null)) {
                    Toast.makeText(MainActivity.this, R.string.errocadastro, Toast.LENGTH_LONG).show();
                } else {
                    textoModoDeUso = (String) radioButton.getText();
                    validation = true;

                    if (textoModoDeUso.equals("Motorista")) {
                        PerfilUsuarioMotorista perfilUsuarioMotorista = new PerfilUsuarioMotorista(perfilUsuarioRegistrado, telefoneEditText.getText().toString(), textoModoDeUso, false, email);
                        mDataDatabaseReference.push().setValue(perfilUsuarioMotorista);
                        Toast.makeText(MainActivity.this, "Motorista cadastrado! Bem vindo " + emailCurrentFirebaseUser.getDisplayName() + "!", Toast.LENGTH_LONG).show();
                        //TODO Intent -> MotoristaActivity
                    } else if (textoModoDeUso.equals("Passageiro")) {
                        PerfilUsuarioPassageiro perfilUsuarioPassageiro = new PerfilUsuarioPassageiro(perfilUsuarioRegistrado, telefoneEditText.getText().toString(), textoModoDeUso, false, email, "NC", null, null, null, null, null, null, null);
                        perfilUsuarioPassageiro.setUid(UUID.randomUUID().toString());
                        mDataDatabaseReference.child(perfilUsuarioPassageiro.getUid()).setValue(perfilUsuarioPassageiro);
                        Toast.makeText(MainActivity.this, "Passageiro cadastrado! Bem vindo " + emailCurrentFirebaseUser.getDisplayName() + "!", Toast.LENGTH_LONG).show();
                        //TODO Intent -> PassageiroActivity
                    }
                }
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER,
                                            AuthUI.EMAIL_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mAuthStateListener = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER,
                                            AuthUI.EMAIL_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        }

        ;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String username) {
        perfilUsuarioRegistrado = username;
        nomeUsuarioTextView.setText(perfilUsuarioRegistrado);

        Toast.makeText(this, "Bem Vindo " + perfilUsuarioRegistrado, Toast.LENGTH_SHORT).show();

        DatabaseReference usersRef = mFirebaseDataBase.getReference().child("users");
        usersRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PerfilUsuarioPassageiro perfilUsuarioPassageiro = dataSnapshot.getValue(PerfilUsuarioPassageiro.class);

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                // Guarda o email autenticado para comparações
                String email = currentFirebaseUser.getEmail().toString();

                // Pega o valor do email no banco
                String emailBanco = dataSnapshot.child("email").getValue().toString();

                // Verifica se existe a variavel primeirologin
                Boolean primeiroLogin = perfilUsuarioPassageiro.getPrimeiroLogin();
                // TODO Carlos - Adicionar a rodinha de loading durante este processo

                if ((primeiroLogin == false) && (email.equals(emailBanco))) {

                    System.out.println("É PRIMEIRA VEZ");
                } else {
                    System.out.println("NÃO É A PRIMEIRA VEZ");

                }
                if ((email.equals(emailBanco)) && (perfilUsuarioPassageiro.getModoDeUso().equals("Motorista"))) {

                    System.out.println("MOTORISTA");
                    Intent myIntent = new Intent(MainActivity.this, MotoristaActivity.class);
                    startActivity(myIntent);
                } else if ((email.equals(emailBanco)) && (perfilUsuarioPassageiro.getModoDeUso().equals("Passageiro"))) {

                    Intent myIntent = new Intent(MainActivity.this, CompletarCadastroPassageiroActivity.class);
                    startActivity(myIntent);

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
        //mDataDatabaseReference.addChildEventListener(mChildEventListener);
    }

    public void onSignedOutCleanUp() {
    }
}
