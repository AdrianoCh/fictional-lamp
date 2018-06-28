package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PassageiroActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private String perfilUsuarioRegistrado;

    private Button confirmarPresencaButton;
    private RadioGroup presencaRadioGroup;
    private TextView mensagemTextView;
    private TextView dataTextView;
    private TextView informaPresenca;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro);

        confirmarPresencaButton = (Button) findViewById(R.id.confirmarPresencabutton);
        presencaRadioGroup = (RadioGroup) findViewById(R.id.presencaRadioGroup);
        mensagemTextView = (TextView) findViewById(R.id.mensagemTextView);
        dataTextView = (TextView) findViewById(R.id.dataTextView);
        informaPresenca = (TextView) findViewById(R.id.informaPresencaTextView);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = mFirebaseAuth.getCurrentUser();

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");

        confirmarPresencaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int presencaSelecionada = presencaRadioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(presencaSelecionada);
                final String textoPresenca = (String) radioButton.getText();
                DatabaseReference usersRef = mFirebaseDataBase.getReference().child("users");
                usersRef.orderByValue().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        PerfilUsuarioPassageiro perfilUsuarioPassageiro = dataSnapshot.getValue(PerfilUsuarioPassageiro.class);

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        final String email = currentFirebaseUser.getEmail().toString();
                        String emailBanco = dataSnapshot.child("email").getValue().toString();
                        String primeiroLogin = perfilUsuarioPassageiro.getPrimeiroLogin().toString();

                        System.out.println("TESTE ANTES DO IF : " + primeiroLogin);

                        final DatabaseReference emailRef = mFirebaseDataBase.getReference().child("users");
                        Query query1 = emailRef.orderByChild("email").equalTo(email).limitToFirst(1);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String passageiroKey = childSnapshot.getKey();
                                    System.out.println("RESULTADO QUERY COM CHAVE: " + passageiroKey);

                                    Object resultadoObject = dataSnapshot.child(passageiroKey).child("aulas").getValue();

                                    if (resultadoObject != null) {
                                        String resultado = resultadoObject.toString();

                                        String separado[] = resultado.split("=");
                                        String formatado = separado[0].replaceAll("\\{", "").trim();

                                        String data = getTime("dd-MM-yyyy");

                                        emailRef.child(passageiroKey).child("aulas").child(formatado).child("presenca").child(data).setValue(textoPresenca);
                                        informaPresenca.setText(getString(R.string.primeira_parte_mensagem) + " " + textoPresenca + " " + getString(R.string.segunda_parte_mensagem));
                                        emailRef.child(passageiroKey).child(data).setValue(textoPresenca);
                                    } else {
                                        mensagemTextView.setText("Você não possui aulas cadastradas");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Se ocorrer um erro
                            }
                        });
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
        });

        String nome = usuario.getDisplayName();

        mensagemTextView.setText(nome + ", você está presente?");

        String data = getTime("dd/MM/yyyy");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarpactivity);
        setSupportActionBar(toolbar);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_passageiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AuthUI.getInstance().signOut(this);
                Intent intent3 = new Intent(PassageiroActivity.this, SplashScreen.class);
                startActivity(intent3);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    public static String getTime(String format) {
        if (format.isEmpty()) {
            throw new NullPointerException("A pattern não pode ser NULL!");
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat(format);
        Date data = calendar.getTime();
        return formato.format(data);
    }

    public void onSignedInInitialize(String username) {
        perfilUsuarioRegistrado = username;
        DatabaseReference usersRef = mFirebaseDataBase.getReference().child("users");
        usersRef.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PerfilUsuarioPassageiro perfilUsuarioPassageiro = dataSnapshot.getValue(PerfilUsuarioPassageiro.class);

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String email = currentFirebaseUser.getEmail().toString();
                String emailServidor = dataSnapshot.child("email").getValue().toString();

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

    public void onSignedOutCleanUp() {
        if (mChildEventListener != null) {
            mDataDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}