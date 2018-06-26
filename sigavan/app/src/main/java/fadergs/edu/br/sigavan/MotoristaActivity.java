package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.w3c.dom.Text;

public class MotoristaActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private  FloatingActionButton floatingActionButton2;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorista);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MotoristaActivity.this, CadastrarFaculdadeActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MotoristaActivity.this, CadastrarPassageiroActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rvPassageiros);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("/users");
        FirebaseRecyclerAdapter<PerfilUsuarioPassageiro, pupviewholder> recyclerAdapter=new FirebaseRecyclerAdapter<PerfilUsuarioPassageiro,pupviewholder> (
                PerfilUsuarioPassageiro.class,
                R.layout.viewholder,
                mDatabaseReference) {
            @Override
            protected void populateViewHolder (pupviewholder viewholder, PerfilUsuarioPassageiro pup, int position) {
                viewholder.setNome(pup.getNome());
                // viewholder.setPresenca();
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }
    public static class pupviewholder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_nome;
        TextView textView_presenca;
        public pupviewholder(View itemView) {
            super(itemView);
            mView = itemView;
            textView_nome = (TextView) itemView.findViewById(R.id.nomepassageiro);
            textView_presenca = (TextView) itemView.findViewById(R.id.presencapassageiro);

        }
        public void setNome (String nome) {
            textView_nome.setText(nome);
        }
        public void setPresenca (String presenca) {
            textView_nome.setText(presenca);
        }
    }
}
