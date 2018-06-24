package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiro);

        confirmarPresencaButton = (Button) findViewById(R.id.confirmarPresencaButton);
        presencaRadioGroup = (RadioGroup) findViewById(R.id.presencaRadioGroup);
        mensagemTextView = (TextView) findViewById(R.id.mensagemTextView);
        dataTextView = (TextView) findViewById(R.id.dataTextView);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = mFirebaseAuth.getCurrentUser();

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users").child("presenca");

        confirmarPresencaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int presencaSelecionada = presencaRadioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(presencaSelecionada);
                String textoPresenca = (String) radioButton.getText();
                //TODO: TENNTAR COLOCAR VALOR NO PRESENÇA
                mDataDatabaseReference.push().setValue("OLA");

            }
        });

        String nome = usuario.getDisplayName();

        mensagemTextView.setText(nome + ", você está presente?");

        Calendar c = Calendar.getInstance();
        int numeroDiaSemana = c.get(Calendar.DAY_OF_WEEK);
        String dia = "";

        if(numeroDiaSemana == 1){
            dia = "Domingo";
        } else if(numeroDiaSemana == 2) {
            dia = "Segunda";
        } else if(numeroDiaSemana == 3) {
            dia = "Terça";
        } else if(numeroDiaSemana == 4) {
            dia = "Quarta";
        } else if(numeroDiaSemana == 5) {
            dia = "Quinta";
        } else if(numeroDiaSemana == 6) {
            dia = "Sexta";
        } else if(numeroDiaSemana == 7) {
            dia = "Sábado";
        }

        String data = getTime("dd/MM/yyyy");

        dataTextView.setText(dia + ", " + data);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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










//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public static String getTime(String format){
        if (format.isEmpty()) {
            throw new NullPointerException("A pattern não pode ser NULL!");
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat(format);
        Date data = calendar.getTime();
        return formato.format(data);
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void onSignedInInitialize(String username) {
    perfilUsuarioRegistrado = username;

    Toast.makeText(this, "Bem Vindo " + perfilUsuarioRegistrado, Toast.LENGTH_SHORT).show();

    mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            PerfilUsuarioPassageiro perfilUsuarioPassageiro = dataSnapshot.getValue(PerfilUsuarioPassageiro.class);

            Calendar c = Calendar.getInstance();
            int numeroDiaSemana = c.get(Calendar.DAY_OF_WEEK);

            String nomeDia ="";

            switch(numeroDiaSemana) {
                case 1:
                    nomeDia = "domingo";
                    break;
                case 2:
                    nomeDia = "segunda";
                    break;
                case 3:
                    nomeDia = "terça";
                    break;
                case 4:
                    nomeDia = "quarta";
                    break;
                case 5:
                    nomeDia = "quinta";
                    break;
                case 6:
                    nomeDia = "sexta";
                    break;
                case 7:
                    nomeDia = "sabado";
                    break;
                default:
                    nomeDia = "";
            }

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            String email = currentFirebaseUser.getEmail().toString();
            String emailConstructor = perfilUsuarioPassageiro.getEmail().toString();
            //Boolean primeiroLogin = perfilUsuarioMotorista.getPrimeiroLogin();
            Boolean diaBdDomingo = perfilUsuarioPassageiro.getDomingo();
            Boolean diaBdSegunda = perfilUsuarioPassageiro.getSegunda();
            Boolean diaBdTerca = perfilUsuarioPassageiro.getTerca();
            Boolean diaBdQuarta = perfilUsuarioPassageiro.getQuarta();
            Boolean diaBdQuinta = perfilUsuarioPassageiro.getQuinta();
            Boolean diaBdSexta = perfilUsuarioPassageiro.getSexta();
            Boolean diaBdSabado = perfilUsuarioPassageiro.getSabado();

            if(nomeDia.equals("domingo")){
                if((email.equals(emailConstructor)) && (diaBdDomingo == true)){

                    System.out.println("Você esta presente no domingo???????????????????????????????????????????????");
                }
            }

            if(nomeDia.equals("sabado")){
                if(email.equals(emailConstructor) && (diaBdSabado == true)){
                    System.out.println("Voce esta presente no sábado ???????????????????????????????????????");
                }
            }


            //if((primeiroLogin == false) && (email.equals(emailContructor))){
            //    //TODO: MANTER NA ACTIVITY DE CADASTRO
            //    System.out.println("É PRIMEIRA VEZ");
            //} else{
            //    System.out.println("NÃO É A PRIMEIRA VEZ");
            //    //TODO: LEVAR PARA ACTIVITY DE USUARIO JA CADASTRADO
            }
            //if((email.equals(emailContructor)) && (perfilUsuarioMotorista.getModoDeUso().equals("Motorista"))){
            //    //TODO: Levar PARA ACTIVITY DE MOTORISTA
            //    System.out.println("MOTORISTA");
            //} else if((email.equals(emailContructor)) && (perfilUsuarioMotorista.getModoDeUso().equals("Passageiro"))){
            //    //TODO: LEVAR PARA ACTIVITY DE PASSAGEIRO
            //    System.out.println("PASSAGEIRO");
            //    Intent myIntent = new Intent(PassageiroActivity.this, PassageiroActivity.class);
            //    startActivity(myIntent);

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



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

