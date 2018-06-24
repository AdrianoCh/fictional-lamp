package br.edu.fadergs.acworks.sigavan_version2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Cadastrar extends AppCompatActivity {

    String username;
    TextView tvUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        Intent intent = getIntent();
        Bundle bundle;
        bundle = intent.getExtras();

        username = bundle.getString("username").toString();

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText(username);

        //Todo validação de empty telefone e radiobutton
    }


}
