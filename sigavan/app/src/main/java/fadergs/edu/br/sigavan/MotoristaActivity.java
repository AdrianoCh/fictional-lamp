package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MotoristaActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorista);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MotoristaActivity.this, CadastrarFaculdadeActivity.class);
                startActivity(intent);
            }
        });
    }
}
