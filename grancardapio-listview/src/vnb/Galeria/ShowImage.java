package vnb.Galeria;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import br.vnbeventos.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ShowImage extends Activity implements OnClickListener {

	private static final String URL = "";
	
	private ProgressDialog dialog;
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.showimagem);
	
    // find our ImageView in the layout
    ImageView img = (ImageView)findViewById(R.id.foto);

    // retrieve the set of data passed to us by the Intent
    Bundle extras = getIntent().getExtras();

    String URL = (String) getIntent().getSerializableExtra("URLIMAGEM");
    Log.i("12345 Variavel URL: ", URL);

       
    // set the ImageView to display the specified resource ID
    //img.setImageResource(resource);

    // close the Activity when a user taps/clicks on the image.
   // img.setOnClickListener(this);
    
    //Abre a janela com a barra de progresso
  		dialog = ProgressDialog.show(this,"VNB Eventos",
  				"Buscando imagem, por favor aguarde...", false, true);
  		
  	downloadImagem(URL);
    

}
	
	//Faz o donwload da imagem em uma nova Thread
		private void downloadImagem(final String urlImg){
			new Thread(){
				@Override
				public void run(){
					try{
						//Faz o download da imagem
						URL url = new URL(urlImg);
						InputStream is = url.openStream();
						final Bitmap imagem = BitmapFactory.decodeStream(is);
						is.close();
						//Atualiza Tela
						atualizaTela(imagem);
															
					}catch (MalformedURLException e){
						return;
					}catch (IOException e){
						//Uma aplicação real deveria tratar este erro
						Log.e("Erro ao fazer o download", e.getMessage(),e);
					}
				}
			}.start();
		}
	
		private void atualizaTela(final Bitmap imagem){
			handler.post(new Runnable(){
				public void run(){
					//Fecha a janela de progresso
					dialog.dismiss();
					ImageView imgView = (ImageView) findViewById(R.id.foto);
					imgView.setImageBitmap(imagem);
				}
				
				});
			}
		
		
	/*
	 * finishes (closes) the activity when the user clicks on the image
	 */
	public void onClick(View v) {
		finish();
	}
}
