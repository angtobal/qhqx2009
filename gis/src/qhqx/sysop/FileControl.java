/**
 * 
 */
package qhqx.sysop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author yan
 *
 */
public class FileControl {
	//private String rootDirPath = "d:\\arcgisserver\\arcgisjobs\\line_gpserver\\";
	private LinkedList<File> targetDir = new LinkedList<File>();
	
	
	public LinkedList<File> inputFileList(String rootDirPath) throws FileNotFoundException, IOException{
		LinkedList<File > input = new LinkedList<File>();
		File[] dir = new File(rootDirPath).listFiles();
		for(int i = 0; i < dir.length; i++){
			//System.out.println(dir[i].getName());
			if(dir[i].isDirectory()){
				this.targetDir.add(dir[i]);
				File[] files = dir[i].listFiles();
				for(int j = 0; j < files.length; j++){
					//System.out.println("   " + files[j].getName());
					if(files[j].isDirectory()){
						System.out.println("111");
						File[] targetfiles = files[j].listFiles();
						for(int k = 0; k < targetfiles.length; k++){
							//System.out.println("             " + targetfiles[k].getName());
							if(targetfiles[k].isFile()){
								input.add(targetfiles[k]);
								copyFile(targetfiles[k].getAbsolutePath(), "D:\\arcgisserver\\arcgisjobs\\");
							}
						}
					}
				}
			}
		}
		return input;
	}
	
	public void deleteDir(String dirPath){
		System.out.println("删除临时文件...");
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
		dir.delete();
	}
	
	public void copyFile(String inputpath, String outputpath) throws FileNotFoundException, IOException{
		FileInputStream fis = new FileInputStream(inputpath);
		FileOutputStream fos = new FileOutputStream(outputpath + new File(inputpath).getName());
		byte[] buf = new byte[1024];
		int i = 0;
		while((i = fis.read(buf)) != -1){
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
		System.out.println("生成目标文件：" + outputpath + new File(inputpath).getName());
	}

	public LinkedList<File> getTargetDir() {
		return targetDir;
	}
	
}
