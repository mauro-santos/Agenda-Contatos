package br.com.maurosantos.android.agendadecontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.Date;

import br.com.maurosantos.android.agendadecontatos.dominio.entidades.Contato;

/**
 * Created by maurosantos on 25/10/2016.
 */

public class RepositorioContato {

    private SQLiteDatabase conn;

    public RepositorioContato(SQLiteDatabase conn) {
        this.conn = conn;
    }

    private ContentValues preencheContentValues(Contato contato) {
        ContentValues values = new ContentValues();

        values.put("NOME", contato.getNome());
        values.put("TELEFONE", contato.getTelefone());
        values.put("TIPOTELEFONE", contato.getTipoTelefone());
        values.put("EMAIL", contato.getEmail());
        values.put("TIPOEMAIL", contato.getTipoEmail());
        values.put("ENDERECO", contato.getEndereco());
        values.put("TIPOENDERECO", contato.getTipoEndereco());
        values.put("DATASESPECIAIS", (contato.getDatasEspeciais() != null) ? contato.getDatasEspeciais().getTime() : null);
        values.put("TIPODATASESPECIAIS", contato.getTipoDatasEspeciais());
        values.put("GRUPOS", contato.getGrupos());

        return values;
    }

    public void inserirContato(Contato contato) {
        ContentValues values = preencheContentValues(contato);
        conn.insertOrThrow("CONTATO", null, values);
    }

    public void alterarContato(Contato contato) {
        ContentValues values = preencheContentValues(contato);
        conn.update("CONTATO", values, "_id = ?", new String[]{String.valueOf(contato.getId())});
    }

    public void excluirContato(long id) {
        conn.delete("CONTATO", "_id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayAdapter<Contato> listaContatos(Context context) {
        ArrayAdapter<Contato> adpContatos = new ArrayAdapter<Contato>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONTATO", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Contato contato = new Contato();

                contato.setId(cursor.getLong(cursor.getColumnIndex(Contato.ID)));
                contato.setNome(cursor.getString(cursor.getColumnIndex(Contato.NOME)));
                contato.setTelefone(cursor.getString(cursor.getColumnIndex(Contato.TELEFONE)));
                contato.setTipoTelefone(cursor.getString(cursor.getColumnIndex(Contato.TIPOTELEFONE)));
                contato.setEmail(cursor.getString(cursor.getColumnIndex(Contato.EMAIL)));
                contato.setTipoEmail(cursor.getString(cursor.getColumnIndex(Contato.TIPOEMAIL)));
                contato.setEndereco(cursor.getString(cursor.getColumnIndex(Contato.ENDERECO)));
                contato.setTipoEndereco(cursor.getString(cursor.getColumnIndex(Contato.TIPOENDERECO)));
                contato.setDatasEspeciais(new Date(cursor.getLong(cursor.getColumnIndex(Contato.DATASESPECIAIS))));
                contato.setTipoDatasEspeciais(cursor.getString(cursor.getColumnIndex(Contato.TIPODATASESPECIAIS)));
                contato.setGrupos(cursor.getString(cursor.getColumnIndex(Contato.GRUPOS)));

                adpContatos.add(contato);
            } while (cursor.moveToNext());
        }

        return adpContatos;
    }
}
