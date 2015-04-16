package vnb.WebService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import br.vnbeventos.R;

import vnb.Galeria.AdaptadorImagem;
import vnb.WebService.ConexaoHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class WebService extends Activity{
       
	public String [] listaFotos;
	int posicao=0;
		
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);  
        
    	Bundle extras = getIntent().getExtras(); 
    	String Cobertura = (String) getIntent().getSerializableExtra("idEvento");
        
				Log.i("buscar", "iniciou o evento"); 
				String url="http://www.vnbeventos.com.br/conecta-android/fotos/id/"+Cobertura;
				Log.i("Evento: ", url);
				
				String respostaRetornada = null;
				Log.i("Try", "vai entrar no try");
				
				try {
					respostaRetornada = ConexaoHttpClient.executaHttpGet(url);
					String resposta = respostaRetornada.toString();
					Log.i("fotos", ""+resposta);
					
					char separaimagens = ',';
					int contadorFotos = 0;
					for (int i=0; i<resposta.length(); i++)
						if(separaimagens == resposta.charAt(i))
							contadorFotos++;
					
					listaFotos = new String[contadorFotos];
					
					char leChars = resposta.charAt(0);
					String foto = "";
					for(int i=0; i<resposta.length(); i++)
					{
						leChars = resposta.charAt(i);
						if (leChars != ',')
							foto+= (char) leChars;
						else
						{
							Log.i("Foto.:",""+foto);
							listaFotos[posicao]="http://www.vnbeventos.com.br/img/"+foto;
							Log.i("Foto na posicao ["+posicao+"]",""+listaFotos[posicao]);
							posicao++;
							foto="";	
							
						}
						
					}
					Log.i("FIM","FIM da leitura de fotos");
					Toast.makeText(WebService.this, "Fotos Encontradas!", Toast.LENGTH_LONG).show();
						
					
				}
				catch(Exception erro){
					Log.i("erro", "erro = "+erro);
					Toast.makeText(WebService.this, "Erro.: "+erro, Toast.LENGTH_LONG).show();
					 
	}
    
}

}