/**
 * 
 */
package qhqx.sysop;

import java.io.File;
import java.util.TimerTask;

/**
 * @author Administrator
 *
 */
public class TempFileClean extends TimerTask {

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		deleteDir("c:\\pic\\feature2\\");

	}
	public void deleteDir(String dirPath){
		System.out.println("É¾³ýÁÙÊ±ÎÄ¼þ...");
		File dir = new File(dirPath);
		if(!dir.isDirectory()){
			return;
		}
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].isDirectory()){
				deleteDir(files[i].getAbsolutePath());
				files[i].delete();
			}else{
				files[i].delete();
			}
		}
		//dir.delete();
	}

}
