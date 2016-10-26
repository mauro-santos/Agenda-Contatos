package br.com.maurosantos.android.agendadecontatos;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.maurosantos.android.agendadecontatos.database.DataBase;
import br.com.maurosantos.android.agendadecontatos.dominio.RepositorioContato;
import br.com.maurosantos.android.agendadecontatos.dominio.entidades.Contato;

public class NewContactActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtEndereco;
    private EditText edtDatasEspeciais;
    private EditText edtGrupos;

    private Spinner spnTipoTelefone;
    private Spinner spnTipoEmail;
    private Spinner spnTipoEndereco;
    private Spinner spnTipoDatasEspeciais;

    private ArrayAdapter<String> adpTipoTelefone;
    private ArrayAdapter<String> adpTipoEmail;
    private ArrayAdapter<String> adpTipoEndereco;
    private ArrayAdapter<String> adpTipoDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtDatasEspeciais = (EditText) findViewById(R.id.edtDatasEspeciais);
        edtGrupos = (EditText) findViewById(R.id.edtGrupos);

        spnTipoTelefone = (Spinner) findViewById(R.id.spnTipoTelefone);
        spnTipoEmail = (Spinner) findViewById(R.id.spnTipoEmail);
        spnTipoEndereco = (Spinner) findViewById(R.id.spnTipoEndereco);
        spnTipoDatasEspeciais = (Spinner) findViewById(R.id.spnTipoDatasEspeciais);

        adpTipoTelefone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoEmail = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpTipoDatasEspeciais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipoDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTipoTelefone.setAdapter(adpTipoTelefone);
        spnTipoEmail.setAdapter(adpTipoEmail);
        spnTipoEndereco.setAdapter(adpTipoEndereco);
        spnTipoDatasEspeciais.setAdapter(adpTipoDatasEspeciais);

        adpTipoTelefone.add("Casa");
        adpTipoTelefone.add("Trabalho");
        adpTipoTelefone.add("Outros");

        adpTipoEmail.add("Celular");
        adpTipoEmail.add("Trabalho");
        adpTipoEmail.add("Casa");
        adpTipoEmail.add("Principal");
        adpTipoEmail.add("Fax Trabalho");
        adpTipoEmail.add("Fax Casa");
        adpTipoEmail.add("Pager");
        adpTipoEmail.add("Outros");

        adpTipoEndereco.add("Casa");
        adpTipoEndereco.add("Trabalho");
        adpTipoEndereco.add("Outros");

        adpTipoDatasEspeciais.add("Aniversário");
        adpTipoDatasEspeciais.add("Data Comemorativa");
        adpTipoDatasEspeciais.add("Outros");

        contato = new Contato();

        ExibeDataListener listener = new ExibeDataListener();
        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);

        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao conectar ao banco de dados: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_contact, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_salvar:
                if (contato == null) {
                    inserir();
                }
                finish();
                break;
            case R.id.mn_alterar:
                break;
            case R.id.mn_excluir:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inserir() {
        try {
            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());
            contato.setEndereco(edtEndereco.getText().toString());

            //contato.setDatasEspeciais(new Date());

            contato.setGrupos(edtGrupos.getText().toString());

            contato.setTipoTelefone(String.valueOf(spnTipoTelefone.getSelectedItemPosition()));
            contato.setTipoEmail(String.valueOf(spnTipoEmail.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(spnTipoEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnTipoDatasEspeciais.getSelectedItemPosition()));

            repositorioContato.inserirContato(contato);
        } catch (Exception ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir os dados: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    private void exibeData() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dlg.show();
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            Date data = calendar.getTime();

            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
            String dt = format.format(data);

            edtDatasEspeciais.setText(dt);

            contato.setDatasEspeciais(data);
        }
    }
}
