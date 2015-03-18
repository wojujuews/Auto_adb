package adb.zhangxd.utils;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image {
	
	private String TAG = this.getClass().getName();
	
	public void screenShot(String imageName,String localPath){
		LogUtil.info(TAG, "start screenShoting...");
		String tmpImage = String.format("/data/local/tmp/%s.png",imageName);
		Shell.runAdbCmd(String.format("shell screencap -p %s",tmpImage));
		Shell.runAdbCmd(String.format("pull %s %s", tmpImage,localPath));
		Shell.runAdbCmd(String.format("shell rm -f %s", tmpImage));
	}
	
	public boolean isSame(String originalImage,String ownImage,double percent ) throws FileNotFoundException, IOException{
		
		BufferedImage original = ImageIO.read(new FileInputStream(originalImage));
		BufferedImage own = ImageIO.read(new FileInputStream(ownImage));
		
		if((original.getWidth() != own.getWidth()) ||
				original.getHeight() != own.getHeight()){
			return false;
		}
		
		int mWidth = own.getWidth();
		int mHight = own.getHeight();
		
		int diffPiexs = 0;
		
		for(int y = 0 ; y < mHight; y++){
			for(int x = 0; x < mWidth; x++){
				if(own.getRGB(x, y) != original.getRGB(x, y)){
					diffPiexs++;
				}
			}
		}
		double totalPixels = (mWidth * mWidth);
		double diffPercent = diffPiexs / totalPixels;
		return diffPercent <= 1.0 - percent;
	}
	
	
}
