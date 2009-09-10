/**
 * 
 */
package qhqx.ags;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.IImageDescription;
import com.esri.arcgis.carto.IImageDisplay;
import com.esri.arcgis.carto.IImageResult;
import com.esri.arcgis.carto.IImageType;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.ILegend;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerLayout;
import com.esri.arcgis.carto.ISymbolBackground;
import com.esri.arcgis.carto.ImageDescription;
import com.esri.arcgis.carto.ImageDisplay;
import com.esri.arcgis.carto.ImageType;
import com.esri.arcgis.carto.Legend;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.PngPictureElement;
import com.esri.arcgis.carto.SymbolBackground;
import com.esri.arcgis.carto.VerticalLegendItem;
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
import com.esri.arcgis.server.IServerContext;

/**
 * @author yan
 * 
 */
public class CustomMapLegend {

	/**
	 * 
	 */
	private IServerContext serverContext;
	private MapServer mapServer;

	private IMap focusMap;
	private ILayer layer;
	private ILegend legend;
	
	private String randomStr;

	public CustomMapLegend(AGSLocalMapResource localResource)
			throws AutomationException, IOException {
		serverContext = localResource.getServerContext();
		mapServer = localResource.getLocalMapServer();
		focusMap = mapServer.getMap(mapServer.getDefaultMapName());
		System.out.println(mapServer.getDefaultMapName());
		legend = (ILegend) serverContext.createObject(Legend.getClsid());
	}

	public void generateLegendFromLayer() throws AutomationException,
			IOException {
		legend.clearItems();

		VerticalLegendItem vLegendItem = (VerticalLegendItem) serverContext.createObject(VerticalLegendItem.getClsid());
		/*HorizontalLegendItem hLegendItem = (HorizontalLegendItem) serverContext
				.createObject(HorizontalLegendItem.getClsid());
		layer = mapServer.getLayer(mapServer.getDefaultMapName(), 2);
		hLegendItem.setLayerByRef(layer);*/
		layer = mapServer.getLayer(mapServer.getDefaultMapName(), 3);
		vLegendItem.setLayerByRef(layer);
		legend.addItem(vLegendItem);
		
		customLegendStyle();

	}

	public void generateLegendFromMap() throws AutomationException, IOException {
		legend.clearItems();
		for (int i = 0; i < focusMap.getMapSurroundCount(); i++) {
			if (focusMap.getMapSurround(i) instanceof com.esri.arcgis.carto.Legend) {
				legend = (ILegend) focusMap.getMapSurround(i);
			}
		}
		// legend.removeItem(arg0);
	}
	
	public void customLegendStyle() throws AutomationException, IOException{
		double patchHeight;
		/*ILegendFormat legendForm = (ILegendFormat) serverContext.createObject(LegendFormat.getClsid());
		legendForm = legend.getFormat();*/
		legend.getFormat().setVerticalItemGap(0);
		legend.getFormat().setHorizontalItemGap(0);
		legend.getFormat().setShowTitle(false);
		System.out.println("horizontalPatchGap: " + legend.getFormat().getHorizontalPatchGap());
		System.out.println(legend.getFormat().getDefaultPatchHeight());
		System.out.println(legend.getFormat().getDefaultPatchWidth());
		
		legend.getFormat().setHorizontalPatchGap(0);
		legend.getFormat().setVerticalPatchGap(-3);
		patchHeight = 6;
		legend.getFormat().setDefaultPatchHeight(patchHeight);
		legend.getFormat().setDefaultPatchWidth(patchHeight * 2);
		for(int i = 0; i < legend.getItemCount(); i++){
			System.out.println(i);
			legend.getItem(i).setShowLabels(false);
			legend.getItem(i).setShowDescriptions(false);
		}
		
		System.out.println("horizontalPatchGap: " + legend.getFormat().getHorizontalPatchGap());
		System.out.println(legend.getFormat().getDefaultPatchHeight());
		System.out.println(legend.getFormat().getDefaultPatchWidth());
	}

	public IImageResult printLegend() throws AutomationException, IOException {
		IImageType imgType = (IImageType) serverContext.createObject(ImageType
				.getClsid());
		imgType.setFormat(esriImageFormat.esriImageJPG);
		//imgType.setFormat(esriImageReturnType.esriImageReturnURL);
		imgType.setReturnType(esriImageReturnType.esriImageReturnMimeData);

		IImageDisplay imgDisp = (IImageDisplay) serverContext
				.createObject(ImageDisplay.getClsid());
		imgDisp.setHeight(300);
		imgDisp.setWidth(120);
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

		/*IActiveView activeView = (IActiveView) focusMap;
		legend.fitToBounds(activeView.getScreenDisplay(), arg1, arg2)
		*/
		IMapServerLayout mapSvrLayout = mapServer;
		IMapDescription mapDesc = mapServer.getServerInfo(
				mapServer.getDefaultMapName()).getDefaultMapDescription();
		IImageResult imgResult = mapSvrLayout.exportLegend(legend, mapDesc,
				mapDisp, null, imgDesc);
		System.out.println(imgResult.getURL());
		System.out.println(imgResult.getMimeData());
		randomStr = randomString(8);
		FileOutputStream fos = new FileOutputStream("c:\\pic\\feature2\\" + randomStr + ".jpg");
		fos.write(imgResult.getMimeData());
		fos.close();
		return imgResult;
	}

	/**
	 * @throws AutomationException
	 * @throws IOException
	 */
	public void createLegend() throws AutomationException, IOException{
		System.out.println("legend");
		
		IGraphicsContainer container = (IGraphicsContainer) focusMap;
		focusMap.clearMapSurrounds();
		IElement elem = (IElement) serverContext.createObject(PngPictureElement.getClsid());
		generateLegendFromLayer();
		System.out.println(legend.getItemCount());
		printLegend();
		//IPageLayout pageLayout = (IPageLayout) focusMap;
		/*IActiveView activeView = (IActiveView) focusMap;
		System.out.println("________________________" + activeView.getFocusMap());
		System.out.println(activeView.getScreenDisplay());
		
		IMapSurroundFrame msf = (IMapSurroundFrame)serverContext.createObject(MapSurroundFrame.getClsid());
		IMapFrame mapFrm = (IMapFrame) serverContext.createObject(MapFrame.getClsid()); // = (MapFrame)
							// serverContext.createObject(MapFrame.getClsid());
		//System.out.println(mapFrm.getMap().getName());
		mapFrm.setMapByRef(focusMap);// = (MapFrame) container.findFrame(focusMap);
		IUID elementUID = (IUID) serverContext.createObject(UID.getClsid());
		System.out.println("hbb");
		elementUID.setValue("{7A3F91E4-B9E3-11d1-8756-0000F8751720}");
		System.out.println(mapFrm + " " + elementUID);

		// The createsurroundframe method takes the UID of the element and
		// an optional style.
		IMapSurroundFrame mapSurroundFrame;
		mapSurroundFrame = mapFrm.createSurroundFrame(elementUID, null);
		mapSurroundFrame.getMapSurround().setName("Legend");
		
		mapSurroundFrame.setBackground(createSymbolBackground());
		
		IEnvelopeGEN env = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
		env.putCoords(103.5, 31.5, 104.5, 38.5);
		elem.setGeometry((IGeometry) env);
		elem = (IElement) mapSurroundFrame;
		
		
		IMapSurround ms = mapSurroundFrame.getMapSurround();
		ILegend lgd = (ILegend) ms;*/
		
		//container.addElement(elem, 0);

		
		/*for(int i = 0; i < focusMap.getMapSurroundCount(); i++){
			if(focusMap.getMapSurround(i) instanceof com.esri.arcgis.carto.Legend){
				legend = (ILegend) focusMap.getMapSurround(i);
				// focusMap.getMapSurround(i)
				msf.setMapSurroundByRef(focusMap.getMapSurround(i));
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaa"+i);
			}
		}*/
		System.out.println(focusMap.getMapSurroundCount());
		System.out.println(legend.getItemCount());
		focusMap.addMapSurround(legend);
		
		System.out.println(focusMap.getMapSurroundCount());
		System.out.println(focusMap.getMapSurround(0).getName());
		//msf.setMapSurroundByRef(focusMap.getMapSurround(0));
		// elem = (IElement) (IGraphicsComposite)legend;
		IEnvelopeGEN env1 = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
		env1.putCoords(103.5, 31.5, 104.5, 38.5);
		elem.setGeometry((IGeometry) env1);
		
		//elem = (IElement) msf;
		
		//ITrackCancel trackCancel = (ITrackCancel) serverContext.createObject(TrackCancel.getClsid());
		
		legend.setName("abc");
		legend.setTitle("图例");
		
		/*legend.draw(activeView.getScreenDisplay(), trackCancel, (Envelope) env);
		focusMap.getMapSurround(0).draw(activeView.getScreenDisplay(), trackCancel, (Envelope) env);
		msf.setBackground(createSymbolBackground());
		msf.getMapSurround().draw(activeView.getScreenDisplay(), trackCancel, (Envelope) env);*/
		System.out.println("aaaa");
		printLegend();
		/*focusMap.addMapSurround(mapSurroundFrame.getMapSurround());
		focusMap.delayDrawing(false);*/
		
		/*System.out.println("active:" + focusMap.getActiveGraphicsLayer());

		//ICompositeGraphicsLayer layer = (ICompositeGraphicsLayer) serverContext.createObject(CompositeGraphicsLayer.getClsid());
		ICompositeGraphicsLayer layer = (ICompositeGraphicsLayer) focusMap.getBasicGraphicsLayer();
		//IGraphicsLayer graphicLayer = (IGraphicsLayer) serverContext.createObject(CompositeGraphicsLayer.getClsid());
		IGraphicsLayer graphicLayer = layer.addLayer("New Graphics Layer", null);
		focusMap.setActiveGraphicsLayerByRef((ILayer)graphicLayer);*/
		
		PngPictureElement pngPicElem = (PngPictureElement) serverContext.createObject(PngPictureElement.getClsid());
		try{
			pngPicElem.importPictureFromFile("c:\\pic\\feature2\\" + randomStr + ".jpg");
			System.out.println("c:\\pic\\feature2\\" + randomStr + ".jpg");
			File tmpf = new File("c:\\pic\\feature2\\" + randomStr + ".jpg");
			tmpf.delete();
			IEnvelopeGEN env2 = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
			env2.putCoords(103.5, 31.5, 104.5, 37.5);
			pngPicElem.setGeometry((IGeometry) env2);
			container.addElement(pngPicElem, 0);
			//container.addElement(legend.getItem(0).getGraphics().next(), 0);
		}catch(NullPointerException err){
			err.printStackTrace();
			System.out.println("所选要素图例不存在 ");
		}
		//container.addElement((IElement) msf, 0);
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
}
