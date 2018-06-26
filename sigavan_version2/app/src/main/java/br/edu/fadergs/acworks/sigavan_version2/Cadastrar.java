package br.edu.fadergs.acworks.sigavan_version2;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Cadastrar extends AppCompatActivity {

    private TextView tvUsername; // Receber o nome do usuario
    private EditText edtTelefone;
    private RadioGroup rgMododeUso;
    private Button btnConfirmar;
    private boolean validation;
    private String textoSelecionado;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        Intent intent = getIntent();
        Bundle bundle;
        bundle = intent.getExtras();


        final String username = bundle.getString("username");
        // Recupera o username digitado anteriormente e coloca no textview
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(username);

        // Referenciando os items com o xml
        edtTelefone = findViewById(R.id.edtTelefone);
        rgMododeUso = findViewById(R.id.rgMododeUso);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rgSelecionado = rgMododeUso.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(rgSelecionado);


                //Todo validação de empty telefone e radiobutton
                if((edtTelefone.length() != 11) || (radioButton == null)) {
                    Toast.makeText(Cadastrar.this,"Dados preenchidos corretamente. Por favor, preencha o número de telefone e o modo de uso corretamente.", Toast.LENGTH_LONG).show();
                    validation = false;
                } else {
                    textoSelecionado = (String) radioButton.getText();
                    validation = true;
                }

                if (validation) {
                    userEmail = FirebaseAuth.getInstance().getCurrentUser();
                    String email = userEmail.getEmail();
                    System.out.println(email);

                    if(textoSelecionado.equals(R.string.motorista)) {
                        PerfilUsuarioMotorista pum = new PerfilUsuarioMotorista(username, edtTelefone.getText().toString(), textoSelecionado, true, email);
                        mDatabaseReference.push().setValue(pum);
                        Toast.makeText(Cadastrar.this,"Motorista cadastrado! Bem vindo " + username + "!", Toast.LENGTH_LONG).show();
                        //TODO Intent -> MotoristaActivity
                    } else if (textoSelecionado.equals(R.string.passageiro)) {
                        PerfilUsuarioPassageiro pup = new PerfilUsuarioPassageiro(username, edtTelefone.getText().toString(), textoSelecionado, false, email, "NC", false, false, false, false, false, false, false);
                        mDatabaseReference.push().setValue(pup);
                        Toast.makeText(Cadastrar.this,"Passageiro cadastrado! Bem vindo " + username + "!", Toast.LENGTH_LONG).show();
                        //TODO Intent -> PassageiroActivity
                    }
                }
            }
        });
    }
}
