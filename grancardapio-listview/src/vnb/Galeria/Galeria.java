package vnb.Galeria;

import br.vnbeventos.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v4.app.NavUtils;

public class Galeria extends Activity implements OnItemClickListener  {
	
	private AdaptadorImagem adaptador;

	int posicao = 0;
	String[] image = null;
	String link;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);
        
        // Obtém dados gerados antes de uma mudança de configuração, se existir
        final Object data = getLastNonConfigurationInstance();
        
        // criar instância do nosso adaptador, enviando todos os dados
        adaptador = new AdaptadorImagem(this, data);

        // Anexar ao grid layout na interface do usuário
     	GridView grid = (GridView) findViewById(R.id.gridview);
     	grid.setAdapter(adaptador);
     	grid.setOnItemClickListener(this);
    
    }
    
    /* Quando um item for clicado queremos mostrar
    * a imagem determinada em uma nova atividade feita
    * apenas para mostrar uma imagem grande.
    */
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	

    	// cria a Intent para abrir a nossa ShowImage activity.
    	Intent i = new Intent(this, ShowImage.class);
   
    	// Passar um par chave: valor para o pacote 'extra' para
    	// A intenção para que a atividade está ciente de qual
    	// Foto foi selecionada.
    	i.putExtra("imageToDisplay", id);
    	Log.i("12345 Imagem Passada: ", ""+id);
    	    	
    	String Teste = (String) adaptador.getItem(position);
    	Log.i("Resultado da variável teste: ", Teste);
    	
    	i.putExtra("URLIMAGEM", Teste);

    	// Inicia a Activity
    	startActivity(i);
    }

	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adaptador.getData();
	}
    
    
}
