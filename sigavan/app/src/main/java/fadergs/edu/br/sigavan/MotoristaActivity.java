package fadergs.edu.br.sigavan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static fadergs.edu.br.sigavan.PassageiroActivity.getTime;

public class MotoristaActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private  FloatingActionButton floatingActionButton2;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseReference;

    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDataDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;
    private Spinner selecionarFaculdadeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorista);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        selecionarFaculdadeSpinner = (Spinner) findViewById(R.id.selecionarFaculdadeSpinner);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDataDatabaseReference = mFirebaseDataBase.getReference();

        selecionarFaculdadeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String selecionado = item.toString();
                System.out.println("A SELEÇÃO FOI: " + selecionado);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerAdapter<PassageiroViewHolderObjeto, pupviewholder> recyclerAdapter = new FirebaseRecyclerAdapter<PassageiroViewHolderObjeto, pupviewholder>(
                PassageiroViewHolderObjeto.class,
                R.layout.viewholder,
                pupviewholder.class,
                mDatabaseReference) {
            @Override
            protected void populateViewHolder(final pupviewholder viewholder, PassageiroViewHolderObjeto pup, int position) {
                //viewholder.setNome("testeA");
                //viewholder.setPresenca("testeB");
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                DatabaseReference usersRef = mFirebaseDataBase.getReference().child("users");
                usersRef.orderByValue().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        PassageiroViewHolderObjeto passageiroViewHolderObjeto = dataSnapshot.getValue(PassageiroViewHolderObjeto.class);

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        // Guarda o email autenticado para comparações
                        String email = currentFirebaseUser.getEmail().toString();

                        // Pega o valor do email no banco
                        String emailBanco = dataSnapshot.child("email").getValue().toString();
                        System.out.println("TESTE EMAIL: " +email);

                        if(email.equals(emailBanco)){
                            popularSpinner();
                            final String data = getTime("dd-MM-yyyy");

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            final DatabaseReference emailRef = mFirebaseDataBase.getReference().child("users");
                            Query query1 = emailRef.orderByChild("email").equalTo(email).limitToFirst(1);
                            //Query query1 = emailRef.orderByChild("modoDeUso").equalTo("Passageiro");
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String teste = dataSnapshot.getChildren().toString();
                                    System.out.println("TESTEEEEEEEEEEEEEEE" + teste);

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String passageiroKey = childSnapshot.getKey();
                                        System.out.println("RESULTADO QUERY COM CHAVE: " + passageiroKey);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //Se ocorrer um erro
                                }
                            });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            textView_presenca.setText(presenca);
        }
    }

    public void popularSpinner() {
        mDataDatabaseReference = mFirebaseDataBase.getReference().child("users");
        mDataDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> faculdades = new ArrayList<String>();

                for (DataSnapshot faculdadesSnapshot : dataSnapshot.getChildren()) {
                    Object nomeFsculdade = faculdadesSnapshot.child("faculdades").getValue();
                    String modificado = "";
                    if (nomeFsculdade == null) {
                        System.out.println("É NULL");
                    } else {
                        String nome = nomeFsculdade.toString();
                        String[] separado = nome.split(",");
                        for (String item : separado) {
                            if (item.contains("{")) {
                                modificado = item.replaceAll("\\{", "");
                                faculdades.add(modificado);
                            } else if (item.contains("}")) {
                                modificado = item.replaceAll("\\}", "");
                                faculdades.add(modificado);
                            } else {
                                faculdades.add(item);
                            }
                        }
                    }
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(MotoristaActivity.this, android.R.layout.simple_spinner_item, faculdades);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selecionarFaculdadeSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
