package fadergs.edu.br.sigavan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

public class CompletarCadastroPassageiroActivity extends AppCompatActivity {
    private CheckBox domingoChecbox;
    private CheckBox segundaCheckbox;
    private CheckBox tercaChecbox;
    private CheckBox quartaCheckbox;
    private CheckBox quintaCheckbox;
    private CheckBox sextaCheckbox;
    private CheckBox sabadoCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro_passageiro);

        domingoChecbox = (CheckBox) findViewById(R.id.domingoCheckbox);
        segundaCheckbox = (CheckBox) findViewById(R.id.segundaCheckbox);
        tercaChecbox = (CheckBox) findViewById(R.id.tercaCheckbox);
        quartaCheckbox = (CheckBox) findViewById(R.id.quartaCheckbox);
        quintaCheckbox = (CheckBox) findViewById(R.id.quintaCheckbox);
        sextaCheckbox = (CheckBox) findViewById(R.id.sextaCheckbox);
        sabadoCheckbox = (CheckBox) findViewById(R.id.sabadoCheckbox);



    }
}
