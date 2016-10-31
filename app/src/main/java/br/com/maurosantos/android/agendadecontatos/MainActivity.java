package br.com.maurosantos.android.agendadecontatos;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import br.com.maurosantos.android.agendadecontatos.app.MessageBox;
import br.com.maurosantos.android.agendadecontatos.database.DataBase;
import br.com.maurosantos.android.agendadecontatos.dominio.RepositorioContato;
import br.com.maurosantos.android.agendadecontatos.dominio.entidades.Contato;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageButton btnAdicionar;
    private EditText edtPesquisa;
    private ListView lstContatos;

    private ArrayAdapter<Contato> adpContatos;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;

    public static final String PARAM_CONTATO = "CONTATO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdicionar = (ImageButton) findViewById(R.id.btnAdicionar);
        edtPesquisa = (EditText) findViewById(R.id.edtPesquisa);
        lstContatos = (ListView) findViewById(R.id.lstContatos);

        btnAdicionar.setOnClickListener(this);
        lstContatos.setOnItemClickListener(this);

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);

            adpContatos = repositorioContato.listaContatos(this);

            lstContatos.setAdapter(adpContatos);
        } catch (SQLException ex) {
            MessageBox.showAlert(this, "Erro", "Erro ao criar o banco de dados: " + ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, NewContactActivity.class);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adpContatos = repositorioContato.listaContatos(this);
        lstContatos.setAdapter(adpContatos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contato contato = adpContatos.getItem(position);

        Intent it = new Intent(this, NewContactActivity.class);
        it.putExtra(PARAM_CONTATO, contato);

        startActivityForResult(it, 0);
    }
}
