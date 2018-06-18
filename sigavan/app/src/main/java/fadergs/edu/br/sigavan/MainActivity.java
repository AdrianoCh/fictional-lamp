package fadergs.edu.br.sigavan;

import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Button confirmarButton;
    private Button cancelarButton;
    private EditText nomeEditText;
    private EditText telefoneEditText;
    private RadioGroup modoDeUsoRadioGroup;

    private String perfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmarButton = (Button) findViewById(R.id.confirmarButton);
        cancelarButton = (Button) findViewById(R.id.cancelarButton);
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
        telefoneEditText = (EditText) findViewById(R.id.telefoneEditText);
        modoDeUsoRadioGroup = (RadioGroup) findViewById(R.id.modoDeUsoRadioGroup);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int modoDeUsoSelecionado = modoDeUsoRadioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(modoDeUsoSelecionado);
                String textoModoDeUso = (String) radioButton.getText();

                PerfilUsuario perfilUsuario = new PerfilUsuario(nomeEditText.getText().toString(), telefoneEditText.getText().toString(), textoModoDeUso);
                mDataDatabaseReference.push().setValue(perfilUsuario);
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

    public void onSignedInInitialize(String username){
        perfilUsuario = username;
        Toast.makeText(this, "Bem Vindo " + perfilUsuario, Toast.LENGTH_SHORT).show();
    }

    public void onSignedOutCleanUp(){

    }
}
