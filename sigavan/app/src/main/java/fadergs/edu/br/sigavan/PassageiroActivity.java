package fadergs.edu.br.sigavan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PassageiroActivity extends AppCompatActivity {
    private Button confirmarPresencaButton;
    private RadioGroup presencaRadioGroup;
    private TextView mensagemTextView;
    private TextView dataTextView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;

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
}
