package start;

import javax.swing.SwingUtilities;

import com.db4o.Db4oEmbedded;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;

import models.FuckShitModel;
import models.MySecondModel;
import models.MyShitFuckingModel;
import models.MyXXXModel;
import views.DB4oObjectViewer;

public class StartDb4o {

	public StartDb4o() {

		populateSomeSampleRecords();


	}

	
	private void populateSomeSampleRecords() {
		
		MyXXXModel m = new MyXXXModel();

		for (int i = 0; i < 4; i++) {
			m = new MyXXXModel();
			m.save();
		}


		FuckShitModel m2 = new FuckShitModel();

		for (int i = 0; i < 4; i++) {
			m2 = new FuckShitModel();
			m2.save();
		}


		MyShitFuckingModel m3 = new MyShitFuckingModel();

		for (int i = 0; i < 4; i++) {
			m3 = new MyShitFuckingModel();
			m3.save();
		}


		MySecondModel m4 = new MySecondModel();

		for (int i = 0; i < 4; i++) {
			m4 = new MySecondModel();
			m4.save();
		}
	}

	/**
	 * @param <MainJFrame>
	 * @param args
	 */
	public static void main(String[] args) {
		
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.file().generateUUIDs(ConfigScope.GLOBALLY);

		new StartDb4o();
		// MainJFrame m = new MainJFrame();
		// m.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				DB4oObjectViewer dov = new DB4oObjectViewer();
				dov.setVisible(true);
			}
			
		});
		
	}

}
