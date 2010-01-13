/**
 * 
 */
package qhqx.ags;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import qhqx.bean.LegendImageView;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.IImageDescription;
import com.esri.arcgis.carto.IImageDisplay;
import com.esri.arcgis.carto.IImageResult;
import com.esri.arcgis.carto.IImageType;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerLayout;
import com.esri.arcgis.carto.ISymbolBackground;
import com.esri.arcgis.carto.ImageDescription;
import com.esri.arcgis.carto.ImageDisplay;
import com.esri.arcgis.carto.ImageType;
import com.esri.arcgis.carto.PngPictureElement;
import com.esri.arcgis.carto.SymbolBackground;
import com.esri.arcgis.carto.esriImageFormat;
import com.esri.arcgis.carto.esriImageReturnType;
import com.esri.arcgis.display.IRgbColor;
import com.esri.arcgis.display.ISimpleFillSymbol;
import com.esri.arcgis.display.ISimpleLineSymbol;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelopeGEN;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.interop.AutomationException;

/**
 * @author yan
 * 
 */
public class CustomMapLegend extends LegendInfo {

	private String randomStr;
	private int lgdImgRetType;

	public CustomMapLegend(AGSLocalMapResource localResource)throws AutomationException, IOException {
		super(localResource);
	}

	public void customLegendStyle(double patchHeight) throws AutomationException, IOException{
		generateLegendFromLayer();
		
		legend.getFormat().setVerticalItemGap(0);
		legend.getFormat().setHorizontalItemGap(0);
		legend.getFormat().setShowTitle(false);
		
		legend.getFormat().setHorizontalPatchGap(0);
		legend.getFormat().setVerticalPatchGap(-3);
		//patchHeight = 12;
		legend.getFormat().setDefaultPatchHeight(patchHeight);
		legend.getFormat().setDefaultPatchWidth(patchHeight * 2);
		for(int i = 0; i < legend.getItemCount(); i++){
			legend.getItem(i).setShowLabels(false);
			legend.getItem(i).setShowDescriptions(false);
			System.out.println(legend.getItem(i).getLayerNameSymbol().getFont());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public String printLegendWithUrl() throws AutomationException, IOException{
		customLegendStyle(12);
		
		IImageResult imgResult = buildLegendPic(esriImageReturnType.esriImageReturnURL, 240, 60);
		
		if(lgdImgRetType == esriImageReturnType.esriImageReturnURL){
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			LegendImageView lgdImgView = (LegendImageView) externalContext.getSessionMap().get("legendImgView");
			
			String url = imgResult.getURL();
			String[] tmp = url.split(":");
			
			url = tmp[0] + "://" + lgdImgView.getServerDomin() + ":" + tmp[2];
			System.out.println(new Date() + url);
			//url.replace("yq", "");
			
			lgdImgView.setImgURL(url);
			
			externalContext.getSessionMap().put("legendImgView", lgdImgView);
			return imgResult.getURL();
		}/*else{
			randomStr = randomString(8);
			FileOutputStream fos = new FileOutputStream("c:\\pic\\feature2\\" + randomStr + ".jpg");
			fos.write(imgResult.getMimeData());
			fos.close();
			return null;
		}*/
		return null;
		
	}

	private IImageResult buildLegendPic(int returnType, int height, int width) throws AutomationException, IOException {
		IImageType imgType = (IImageType) serverContext.createObject(ImageType
				.getClsid());
		imgType.setFormat(esriImageFormat.esriImageJPG);
		//imgType.setReturnType(esriImageReturnType.esriImageReturnURL);
		imgType.setReturnType(returnType);

		IImageDisplay imgDisp = (IImageDisplay) serverContext
				.createObject(ImageDisplay.getClsid());
		//imgDisp.setHeight(192);
		//imgDisp.setWidth(70);
		imgDisp.setHeight(height);
		imgDisp.setWidth(width);
		imgDisp.setDeviceResolution(96);

		IImageDescription imgDesc = (IImageDescription) serverContext
				.createObject(ImageDescription.getClsid());
		imgDesc.setDisplay(imgDisp);
		imgDesc.setType(imgType);

		IImageDisplay mapDisp = (IImageDisplay) serverContext
				.createObject(ImageDisplay.getClsid());
		mapDisp.setDeviceResolution(96);
		mapDisp.setHeight(200);
		mapDisp.setWidth(150);

		IMapServerLayout mapSvrLayout = mapServer;
		IMapDescription mapDesc = mapServer.getServerInfo(
				mapServer.getDefaultMapName()).getDefaultMapDescription();
		//mapDesc.getLayerDescriptions().getElement(2).setVisible(false);//012356
		//mapDesc.getLayerDescriptions().getElement(4).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(1).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(2).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(4).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(5).setVisible(false);
		IImageResult imgResult = mapSvrLayout.exportLegend(legend, mapDesc,
				mapDisp, null, imgDesc);
		
		mapDesc.getLayerDescriptions().getElement(2).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(5).setVisible(false);
		mapDesc.getLayerDescriptions().getElement(6).setVisible(false);
		
		return imgResult;
	}

	/**
	 * @throws AutomationException
	 * @throws IOException
	 */
	public void attachLegendToFocusMap() throws AutomationException, IOException{
		
		IGraphicsContainer container = (IGraphicsContainer) focusMap;
		focusMap.clearMapSurrounds();
		IElement elem = (IElement) serverContext.createObject(PngPictureElement.getClsid());
		
		customLegendStyle(9);
		/*System.out.println(legend.getItemCount());
		printLegend(esriImageReturnType.esriImageReturnMimeData);*/

		focusMap.addMapSurround(legend);
		
		IEnvelopeGEN env1 = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
		env1.putCoords(103.0, 31.5, 104.9, 38.5);
		elem.setGeometry((IGeometry) env1);
		
		
		exportLegendPic("c:\\pic\\feature2\\");
		
		PngPictureElement pngPicElem = (PngPictureElement) serverContext.createObject(PngPictureElement.getClsid());
		try{
			pngPicElem.importPictureFromFile("c:\\pic\\feature2\\" + randomStr + ".jpg");
			System.out.println("c:\\pic\\feature2\\" + randomStr + ".jpg");
			//File tmpf = new File("c:\\pic\\feature2\\" + randomStr + ".jpg");
			//tmpf.delete();
			IEnvelopeGEN env2 = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
			env2.putCoords(103.0, 31.5, 104.9, 39.5);
			pngPicElem.setGeometry((IGeometry) env2);
			container.addElement(pngPicElem, 0);
			//container.addElement(legend.getItem(0).getGraphics().next(), 0);
		}catch(NullPointerException err){
			err.printStackTrace();
			System.out.println("所选要素图例不存在 ");
		}
		//container.addElement((IElement) msf, 0);
	}

	/**
	 * @throws AutomationException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void exportLegendPic(String dirPath) throws AutomationException, IOException,
			FileNotFoundException {
		IImageResult imgResult = buildLegendPic(esriImageReturnType.esriImageReturnMimeData, 250, 70);
		randomStr = randomString(8);
		FileOutputStream fos = new FileOutputStream(dirPath + randomStr + ".jpg");
		fos.write(imgResult.getMimeData());
		fos.close();
	}
	
	public ISymbolBackground createSymbolBackground() throws AutomationException, IOException{
		ISymbolBackground symbolBackground = (ISymbolBackground) serverContext.createObject(SymbolBackground.getClsid());
		ISimpleFillSymbol fillSymbol = (ISimpleFillSymbol) serverContext.createObject(SimpleFillSymbol.getClsid());
		ISimpleLineSymbol lineSymbol = (ISimpleLineSymbol) serverContext.createObject(SimpleLineSymbol.getClsid());
		IRgbColor rgb = (IRgbColor) serverContext.createObject(RgbColor.getClsid());
		
		rgb.setRGB(0xffffff);
		
		lineSymbol.setColor(rgb);
		fillSymbol.setColor(rgb);
		fillSymbol.setOutline(lineSymbol);
		symbolBackground.setFillSymbol(fillSymbol);
		
		return symbolBackground;
	}
	
	public String randomString(int size) {// 随机字符串
        char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q',
                'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
                'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
        Random random = new Random(); // 初始化随机数产生器
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(c[Math.abs(random.nextInt()) % c.length]);
        }
        return sb.toString();
    }

	public int getLgdImgRetType() {
		return lgdImgRetType;
	}

	public void setLgdImgRetType(int lgdImgRetType) {
		this.lgdImgRetType = lgdImgRetType;
	}
}
