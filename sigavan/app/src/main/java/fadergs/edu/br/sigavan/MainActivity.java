package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private String perfilUsuarioRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmarButton = (Button) findViewById(R.id.confirmarButton);
        cancelarButton = (Button) findViewById(R.id.cancelarButton);
        telefoneEditText = (EditText) findViewById(R.id.telefoneEditText);
        modoDeUsoRadioGroup = (RadioGroup) findViewById(R.id.modoDeUsoRadioGroup);
        nomeUsuarioTextView = (TextView) findViewById(R.id.nomeUsuarioTextView);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser usuario = mFirebaseAuth.getCurrentUser();

        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int modoDeUsoSelecionado = modoDeUsoRadioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(modoDeUsoSelecionado);
                String textoModoDeUso = (String) radioButton.getText();

                FirebaseUser emailCurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String email = emailCurrentFirebaseUser.getEmail();

                if(textoModoDeUso == "Motorista"){
                    PerfilUsuarioMotorista perfilUsuarioMotorista = new PerfilUsuarioMotorista(perfilUsuarioRegistrado, telefoneEditText.getText().toString(), textoModoDeUso, false, email);
                    mDataDatabaseReference.push().setValue(perfilUsuarioMotorista);
                } else {
                    PerfilUsuarioPassageiro perfilUsuarioPassageiro = new PerfilUsuarioPassageiro(perfilUsuarioRegistrado, telefoneEditText.getText().toString(), textoModoDeUso, false, email, "NC");
                    mDataDatabaseReference.push().setValue(perfilUsuarioPassageiro);
                }

            }
        });

    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null){
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

    }

    @Override
    protected void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String username) {
        perfilUsuarioRegistrado = username;
        nomeUsuarioTextView.setText(perfilUsuarioRegistrado);

        Toast.makeText(this, "Bem Vindo " + perfilUsuarioRegistrado, Toast.LENGTH_SHORT).show();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PerfilUsuarioMotorista perfilUsuarioMotorista = dataSnapshot.getValue(PerfilUsuarioMotorista.class);

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String email = currentFirebaseUser.getEmail().toString();
                String emailContructor = perfilUsuarioMotorista.getEmail().toString();
                Boolean primeiroLogin = perfilUsuarioMotorista.getPrimeiroLogin();

                if((primeiroLogin == false) && (email.equals(emailContructor))){
                    //TODO: MANTER NA ACTIVITY DE CADASTRO
                    System.out.println("É PRIMEIRA VEZ");
                } else{
                    System.out.println("NÃO É A PRIMEIRA VEZ");
                    //TODO: LEVAR PARA ACTIVITY DE USUARIO JA CADASTRADO
                }
                if((email.equals(emailContructor)) && (perfilUsuarioMotorista.getModoDeUso().equals("Motorista"))){
                    //TODO: Levar PARA ACTIVITY DE MOTORISTA
                    System.out.println("MOTORISTA");
                } else if((email.equals(emailContructor)) && (perfilUsuarioMotorista.getModoDeUso().equals("Passageiro"))){
                    //TODO: LEVAR PARA ACTIVITY DE PASSAGEIRO
                    System.out.println("PASSAGEIRO");
                    Intent myIntent = new Intent(MainActivity.this, PassageiroActivity.class);
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
        };

        mDataDatabaseReference.addChildEventListener(mChildEventListener);
    }

    public void onSignedOutCleanUp() {

    }
}
