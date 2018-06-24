package br.edu.fadergs.acworks.sigavan_version2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private String username;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DatabaseReference mDatabase;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
            FirebaseAuth.getInstance().signOut();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("Login","onAuthStateChanged:signed_in:" + user.getUid());
                    System.out.println("Usuario Logado");
                } else {
                    // Login intent
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
                    // User is signed out
                    // Launch Sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.ic_launcher_foreground)
                                    .setTheme(R.style.Theme_AppCompat_FullVan)
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        // Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                mDatabase.orderByValue().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        PerfilUsuarioPassageiro pup = dataSnapshot.getValue(PerfilUsuarioPassageiro.class);

                        // Instancia a sessão atual
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Guarda o email e o nome autenticado
                        String emailLogado = user.getEmail();
                        username = user.getDisplayName();

                        // Pega o Email registrado no banco
                        String emailbanco = dataSnapshot.child("email").getValue().toString();

                        // Pega a variavel primeiro login
                        Boolean primeiroLogin = pup.getPrimeiroLogin();


                        if(primeiroLogin.equals(false)) {
                            // Receber o nome do usuario e levar até a pagina de cadastro
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);

                            Intent intent = new Intent(MainActivity.this, Cadastrar.class);
                            intent.putExtras(bundle);

                            startActivity(intent);
                            finish();
                        } else {
                            // Usuario já cadastrado, verificar se é motorista ou passageiro
                                System.out.println("Logado e já cadastrado!");
                            if((emailLogado.equals(emailbanco)) && (pup.getModoDeUso().equals(R.string.motorista))) {
                                System.out.println("Motorista logado");
                                // TODO Intent -> MotoristaActivity
                            } else if ((emailLogado.equals(emailbanco)) && (pup.getModoDeUso().equals(R.string.passageiro))) {
                                System.out.println("Passageiro logado");
                                // TODO Intent -> PassageiroActivity
                            }
                        }
                    }
                        @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


}