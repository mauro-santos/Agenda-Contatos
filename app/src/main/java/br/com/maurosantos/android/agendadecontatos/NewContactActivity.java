package br.com.maurosantos.android.agendadecontatos;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

import br.com.maurosantos.android.agendadecontatos.app.MessageBox;
import br.com.maurosantos.android.agendadecontatos.app.ViewHelper;
import br.com.maurosantos.android.agendadecontatos.database.DataBase;
import br.com.maurosantos.android.agendadecontatos.dominio.RepositorioContato;
import br.com.maurosantos.android.agendadecontatos.dominio.entidades.Contato;
import br.com.maurosantos.android.agendadecontatos.util.DateUtils;

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

        adpTipoTelefone = ViewHelper.createArrayAdapter(this, spnTipoTelefone);
        adpTipoEmail = ViewHelper.createArrayAdapter(this, spnTipoEmail);
        adpTipoEndereco = ViewHelper.createArrayAdapter(this, spnTipoEndereco);
        adpTipoDatasEspeciais = ViewHelper.createArrayAdapter(this, spnTipoDatasEspeciais);

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

        ExibeDataListener listener = new ExibeDataListener();
        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);
        edtDatasEspeciais.setKeyListener(null);

        // Formulário utilizado para o Editar.
        Bundle bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.containsKey(MainActivity.PARAM_CONTATO))) {
            contato = ((Contato) bundle.getSerializable(MainActivity.PARAM_CONTATO));
            preencheDados();
        } else {
            contato = new Contato();
        }

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioContato = new RepositorioContato(conn);
        } catch (SQLException ex) {
            MessageBox.showAlert(this, "Erro", "Erro ao conectar ao banco de dados" + ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_contact, menu);

        if (contato.getId() != 0) {
            menu.getItem(1).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_salvar:
                if (salvar()) {
                    finish();
                }
                break;
            case R.id.mn_excluir:
                if (excluir()) {
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencheDados() {
        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        spnTipoTelefone.setSelection(Integer.parseInt(contato.getTipoTelefone()));
        edtEmail.setText(contato.getEmail());
        spnTipoEmail.setSelection(Integer.parseInt(contato.getTipoEmail()));
        edtEndereco.setText(contato.getEndereco());
        spnTipoEndereco.setSelection(Integer.parseInt(contato.getTipoEndereco()));
        edtDatasEspeciais.setText(DateUtils.dateToString(contato.getDatasEspeciais()));
        spnTipoDatasEspeciais.setSelection(Integer.parseInt(contato.getTipoDatasEspeciais()));
        edtGrupos.setText(contato.getGrupos());
    }

    private boolean salvar() {
        try {
            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());
            contato.setEndereco(edtEndereco.getText().toString());
            //contato.setDatasEspeciais: no método select.
            contato.setGrupos(edtGrupos.getText().toString());

            contato.setTipoTelefone(String.valueOf(spnTipoTelefone.getSelectedItemPosition()));
            contato.setTipoEmail(String.valueOf(spnTipoEmail.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(spnTipoEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnTipoDatasEspeciais.getSelectedItemPosition()));

            if (contato.getNome().isEmpty()) {
                MessageBox.showInfo(this, "Atenção", "Informe o nome");
                return false;
            } else {
                if (contato.getId() == 0) {
                    repositorioContato.inserirContato(contato);
                } else {
                    repositorioContato.alterarContato(contato);
                }

                return true;
            }
        } catch (Exception ex) {
            MessageBox.showAlert(this, "Erro", "Erro ao salvar os dados" + ex.getMessage());
            return false;
        }
    }

    private boolean excluir() {
        try {
            repositorioContato.excluirContato(contato.getId());
            return true;
        } catch (Exception ex) {
            MessageBox.showAlert(this, "Erro", "Erro ao excluir os dados" + ex.getMessage());
            return false;
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
            if (hasFocus) {
                exibeData();
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtDatasEspeciais.setText(DateUtils.dateToString(year, month, dayOfMonth));
            contato.setDatasEspeciais(DateUtils.getDate(year, month, dayOfMonth));
        }
    }
}
