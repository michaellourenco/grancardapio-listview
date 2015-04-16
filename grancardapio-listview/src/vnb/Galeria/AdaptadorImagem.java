package vnb.Galeria;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import br.vnbeventos.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class AdaptadorImagem extends BaseAdapter{
	
	// objeto usado para deixar os dados do cache junto
	private class Image {
		String url;
		Bitmap thumb;
	}
	
	// array com os recursos que queremos mostrar
	private Image[] images;
	
	// Um contexto para que possamos mais tarde criar uma view dentro dela
	private Context myContext;

	// O objeto tarefa que ira rodar em background
	private LoadThumbsTask thumbnailGen;
	
	//Construtor
	public AdaptadorImagem(Context c, Object previousList){
		myContext = c;
		
		// Obtém a nossa geração de tarefa de miniaturas pronta para executar
		thumbnailGen = new LoadThumbsTask();
		
		// we'll want to use pre-existing data, if it exists
		if(previousList != null) {
			images = (Image[]) previousList;
			
			// continue processing remaining thumbs in the background
			thumbnailGen.execute(images);
			
			// no more setup required in this constructor
			return;	
	}
		
		// if no pre-existing data, we need to generate it from scratch.

		// initialize array
		images = new Image[imageURLs.length];

		for(int i = 0, j = imageURLs.length; i < j; i++) {
			images[i] = new Image();
			images[i].url = imageURLs[i];
		}
				
		// start the background task to generate thumbs
		thumbnailGen.execute(images);
		
	}
	
		
		/**
		 * Getter: number of items in the adapter's data set
		 */
		public int getCount() {
			return images.length;
		}
		
		/**
		 * Getter: return URL at specified position
		 */
		public Object getItem(int position) {
			return images[position].url;
		}
		
		/**
		 * Getter: return resource ID of the item at the current position
		 */
		public long getItemId(int position) {
			return position;
		}
		
		/**
		 * Getter: return generated data
		 * @return array of Image
		 */
		public Object getData() {
			// stop the task if it isn't finished
			if(thumbnailGen != null && thumbnailGen.getStatus() != AsyncTask.Status.FINISHED) {
				// cancel the task
				thumbnailGen.cancel(true);

			}

			// return generated thumbs
			return images;
		}
		
		/**
		 * Create a new ImageView when requested, filling it with a 
		 * thumbnail or a blank image if no thumb is ready yet.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imgView;
			
			// pull the cached data for the image assigned to this position
			Image cached = images[position];

			// can we recycle an old view?
			if(convertView == null) {

				// no view to recycle; create a new view
				imgView = new ImageView(myContext);
				imgView.setLayoutParams(new GridView.LayoutParams(100,100));

			} else {
		
				// recycle an old view (it might have old thumbs in it!)
				imgView = (ImageView) convertView;
		
			}

			// do we have a thumb stored in cache?
			if(cached.thumb == null) {
				
				// no cached thumb, so let's set the view as blank
				imgView.setImageResource(R.drawable.blank);		
				imgView.setScaleType(ScaleType.CENTER);

			} else {

				// yes, cached thumb! use that image
				imgView.setScaleType(ScaleType.FIT_CENTER);
				imgView.setImageBitmap(cached.thumb);
				
			}


			return imgView;
		}

		
		/**
		 * Notify the adapter that our data has changed so it can
		 * refresh the views & display any newly-generated thumbs
		 */
		private void cacheUpdated() {
			this.notifyDataSetChanged();
		}


		/**
		 * Download and return a thumb specified by url, subsampling 
		 * it to a smaller size.
		 */
		private Bitmap loadThumb(String url) {

			// the downloaded thumb (none for now!)
			Bitmap thumb = null;

			// sub-sampling options
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 4;

			try {

				// open a connection to the URL
				// Note: pay attention to permissions in the Manifest file!
				URL u = new URL(url);
				URLConnection c = u.openConnection();
				c.connect();
				
				// read data
				BufferedInputStream stream = new BufferedInputStream(c.getInputStream());

				// decode the data, subsampling along the way
				thumb = BitmapFactory.decodeStream(stream, null, opts);

				// close the stream
				stream.close();

			} catch (MalformedURLException e) {
				Log.e("VNB Eventos", "malformed url: " + url);
			} catch (IOException e) {
				Log.e("VNB Eventos", "Ocorreu um erro ao carregar as imagens..: " + url);
			}

			// return the fetched thumb (or null, if error)
			return thumb;
		}
		
		// the class that will create a background thread and generate thumbs
		private class LoadThumbsTask extends AsyncTask<Image, Void, Void> {

			/**
			 * Generate thumbs for each of the Image objects in the array
			 * passed to this method. This method is run in a background task.
			 */
			@Override
			protected Void doInBackground(Image... cache) {

				
				// define the options for our bitmap subsampling 
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 4;

				// iterate over all images ...
				for (Image i : cache) {

					// if our task has been cancelled then let's stop processing
					if(isCancelled()) return null;

					// skip a thumb if it's already been generated
					if(i.thumb != null) continue;

					// artificially cause latency!
					SystemClock.sleep(500);
					
					// download and generate a thumb for this image
					i.thumb = loadThumb(i.url);

					// some unit of work has been completed, update the UI
					publishProgress();
				}
				
				return null;
			}


			/**
			 * Update the UI thread when requested by publishProgress()
			 */
			@Override
			protected void onProgressUpdate(Void... param) {
				cacheUpdated();
			}
		}
		
		
		private String[] imageURLs = {
				"http://www.vnbeventos.com.br/img/1752bf3b330f02eba9ac3902fff6ef4dvnb01.jpg",
				"http://www.vnbeventos.com.br/img/1fa29f495c5cbac87c03b0b83b779197vnb02.jpg",
				"http://www.vnbeventos.com.br/img/db1b41083643b88a2f0a01b206876257vnb03.jpg",
				"http://www.vnbeventos.com.br/img/62533e5c157c9dd47ebc236971c5ea5fvnb04.jpg",
				"http://www.vnbeventos.com.br/img/739b750ed51c9fefc9a46d5ef0ddcb67vnb05.jpg",
				"http://www.vnbeventos.com.br/img/c1ecfd56d04165b31480f910edc6a099vnb06.jpg",
				"http://www.vnbeventos.com.br/img/d68e5ac693288fcae86e0f69f3fbc5e5vnb07.jpg",
				"http://www.vnbeventos.com.br/img/514982249c2bb67e6443e65c4498a06dvnb08.jpg",
				"http://www.vnbeventos.com.br/img/fbfb6f25e94a52ceabfbcb6c6c81a0b4vnb09.jpg",
				"http://www.vnbeventos.com.br/img/5cc32df2bbdfde244084bfbb65931ccfvnb10.jpg",
				"http://www.vnbeventos.com.br/img/91d23877ad26fd4d24037ca2da053040vnb15.jpg",
				"http://www.vnbeventos.com.br/img/c3a30f8c23fcc02c354d034b07883dc4vnb16.jpg",
				"http://www.vnbeventos.com.br/img/7cd3c8a28e94a75623a51d7b11b5d015vnb21.jpg",
				"http://www.vnbeventos.com.br/img/798d582a6b78fbbf300418516401a701vnb26.jpg",
				"http://www.vnbeventos.com.br/img/5aef710a89516e552cebfdad92024d84vnb27.jpg",
				"http://www.vnbeventos.com.br/img/428959d7616aa45a8d1bb23fc2daaa53vnb32.jpg",
				"http://www.vnbeventos.com.br/img/130b389ae1f65e2f84beefd427391493vnb37.jpg",
				"http://www.vnbeventos.com.br/img/09282c5696489806dc184930b054de1dvnb42.jpg",
				"http://www.vnbeventos.com.br/img/a952a00b3d13c998b63ee045660ffbe3vnb47.jpg",
				"http://www.vnbeventos.com.br/img/bf859ab1396ad4c4fc2e2a1ec393a1eevnb48.jpg",
				"http://www.vnbeventos.com.br/img/e097c6efa4576b2985481da2276721a6vnb49.jpg",
				"http://www.vnbeventos.com.br/img/1e99d29b37b5becca05b2f48f9d5ab88vnb50.jpg",
				"http://www.vnbeventos.com.br/img/ac3f5edff6e9bb6bd9b23e1364a416bevnb55.jpg",
				"http://www.vnbeventos.com.br/img/74b727a3fb83779c68304e322a481c27vnb56.jpg",
				"http://www.vnbeventos.com.br/img/1407d6705991247069495586f089c8e2vnb57.jpg",
				"http://www.vnbeventos.com.br/img/89358ea0f31afbf3968c84e9f660e960vnb58.jpg",
				"http://www.vnbeventos.com.br/img/352fdc1fc5c69796ebae9662a8006eeavnb60.jpg",
				"http://www.vnbeventos.com.br/img/ac57298db2a9bc0e0bea8252b0408484vnb61.jpg",
				"http://www.vnbeventos.com.br/img/916504d0e11e2c896f34acec55f3d4cdvnb65.jpg",
				"http://www.vnbeventos.com.br/img/e70edca1aad79587e2bc9ea64b9f41f1vnb71.jpg",
				"http://www.vnbeventos.com.br/img/5d10044b9eb3c125f5cd0482bc3e18f1vnb73.jpg",
				"http://www.vnbeventos.com.br/img/7ecc7c088475c17c748f0e5a5d1f8543vnb74.jpg",
				"http://www.vnbeventos.com.br/img/fac9df1ab659dd60bb3f624ca0539311vnb79.jpg",
				"http://www.vnbeventos.com.br/img/0cfec4ffc66444bc277404076966e692vnb83.jpg",
				"http://www.vnbeventos.com.br/img/c3337bd87c1877cf790cdec02f56fb63vnb88.jpg",
				"http://www.vnbeventos.com.br/img/b5702943a6780e0d2a38b60b199b35e9vnb89.jpg",
				"http://www.vnbeventos.com.br/img/49751982d95bd3cdef1091a9791c4d63vnb92.jpg",
				"http://www.vnbeventos.com.br/img/eede918970c516ebabf3129eab389c80vnb98.jpg",
				"http://www.vnbeventos.com.br/img/aa02762ae10f9ca7b0194f9afa6ec881vnb102.jpg",
				"http://www.vnbeventos.com.br/img/a014b35588198600933fa55eea650765vnb104.jpg"
		};
}
