package br.com.maurosantos.android.agendadecontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

/**
 * Created by maurosantos on 25/10/2016.
 */

public class RepositorioContato {

    private SQLiteDatabase conn;

    public RepositorioContato(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public void inserirContatos() {
        for (int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put("TELEFONE", "555555555");
            conn.insertOrThrow("CONTATO", null, values);
        }
    }

    public ArrayAdapter<String> listaContatos(Context context) {
        ArrayAdapter<String> adpContatos = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONTATO", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                String telefone = cursor.getString(1);
                adpContatos.add(telefone);
            } while (cursor.moveToNext());
        }

        return adpContatos;
    }
}
