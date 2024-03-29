<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.esri.com/adf/web" prefix="a"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:subview id="editSubview">
<h:panelGrid columns="2">
	<h:outputLabel value="#{mapContext.attributes.mapEditor.editLabel}"></h:outputLabel>
	<f:verbatim>
		<div id="selectEditLayerId"/>
	</f:verbatim>
</h:panelGrid>
<h:panelGrid columns="8" style="border:thin solid rgb(190,190,190); width:100%;">
	<a:button id="select" mapId="map1" clientAction="EsriMapRectangle" serverAction="#{mapContext.attributes.mapEditor.select}"
		defaultImage="./images/tasks/editing/selectfeature.gif" hoverImage="./images/tasks/editing/selectfeatureU.gif" selectedImage="./images/tasks/editing/selectfeatureD.gif"
		toolTip="EditTask.TaskInfo.Tip.selectfeatures"/>

	<a:button id="clearSelect" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.clearSelect}" defaultImage="./images/tasks/editing/clearsel.gif"
		hoverImage="./images/tasks/editing/clearselU.gif" selectedImage="./images/tasks/editing/clearselD.gif" toolTip="EditTask.TaskInfo.Tip.clearselection"/>

	<a:button id="showVertices" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.showVertices}" defaultImage="./images/tasks/editing/showvert.gif"
		hoverImage="./images/tasks/editing/showvertU.gif" selectedImage="./images/tasks/editing/showvertD.gif"
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.showvertices" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? 'hiddenButton':'disabledButton'}"/>

	<a:button id="undo" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.undo}" defaultImage="./images/tasks/editing/undo.gif"
		hoverImage="./images/tasks/editing/undoU.gif" selectedImage="./images/tasks/editing/undoD.gif"
		disabled="#{!mapContext.attributes.mapEditor.undoable}" toolTip="EditTask.TaskInfo.Tip.undoedits"/>

	<a:button id="redo" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.redo}" defaultImage="./images/tasks/editing/redo.gif"
		hoverImage="./images/tasks/editing/redoU.gif" selectedImage="./images/tasks/editing/redoD.gif"
		disabled="#{!mapContext.attributes.mapEditor.redoable}" toolTip="EditTask.TaskInfo.Tip.redoedits"/>

	<a:button id="save" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.save}" defaultImage="./images/tasks/editing/saveediting.gif"
		hoverImage="./images/tasks/editing/saveeditingU.gif" selectedImage="./images/tasks/editing/saveeditingD.gif" toolTip="EditTask.TaskInfo.Tip.saveedits"/>

	<a:button id="discard" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.discard}" defaultImage="./images/tasks/editing/stopeditingdiscard.gif"
		hoverImage="./images/tasks/editing/stopeditingdiscardU.gif" selectedImage="./images/tasks/editing/stopeditingdiscardD.gif"
		toolTip="EditTask.TaskInfo.Tip.discardedits" rendered="#{!mapContext.attributes.mapEditor.pooledEditing}"/>
	
	<a:button id="enterxy" mapId="map1" defaultImage="./images/tasks/editing/enterxy.gif" hoverImage="./images/tasks/editing/enterxyU.gif"
		selectedImage="./images/tasks/editing/enterxyD.gif" toolTip="EditTask.TaskInfo.Tip.enterxy" onclick="showEnterXYWindow();return false;"/>
</h:panelGrid>

<h:panelGrid style="width:100%;" styleClass="esriWindowTitleBar" rendered="#{mapContext.attributes.mapEditor.addFeaturesEnabled}">
	<h:outputText styleClass="esriWindowTitleText" value="#{mapContext.attributes.mapEditor.createFeatureLabel}" />
</h:panelGrid>
<h:panelGroup rendered="#{mapContext.attributes.mapEditor.addFeaturesEnabled}">
	<a:button id="addPolyline" mapId="map1" clientAction="EsriEditingPolyline" serverAction="#{mapContext.attributes.mapEditor.addPolyline}"
		defaultImage="./images/tasks/editing/line.gif" hoverImage="./images/tasks/editing/lineU.gif" selectedImage="./images/tasks/editing/lineD.gif"
		toolTip="EditTask.TaskInfo.Tip.addpolylinefeature" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'LINE')? '' : 'hiddenButton'}"/>

	<a:button id="addPoint" mapId="map1" clientAction="EsriEditingPoint" serverAction="#{mapContext.attributes.mapEditor.addPoint}" defaultImage="./images/tasks/editing/point.gif"
		hoverImage="./images/tasks/editing/pointU.gif" selectedImage="./images/tasks/editing/pointD.gif" toolTip="EditTask.TaskInfo.Tip.addpointfeature" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? '' : 'hiddenButton'}"/>

	<a:button id="addPolygon" mapId="map1" clientAction="EsriEditingPolygon" serverAction="#{mapContext.attributes.mapEditor.addPolygon}"
		defaultImage="./images/tasks/editing/polygon.gif" hoverImage="./images/tasks/editing/polygonU.gif" selectedImage="./images/tasks/editing/polygonD.gif"
		toolTip="EditTask.TaskInfo.Tip.addpolygon" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POLYGON')? '' : 'hiddenButton'}"/>

</h:panelGroup>

<h:panelGrid style="width:100%;" styleClass="esriWindowTitleBar" rendered="#{mapContext.attributes.mapEditor.editFeaturesEnabled}">
	<h:outputText styleClass="esriWindowTitleText" value="#{mapContext.attributes.mapEditor.editFeatureLabel}" />
</h:panelGrid>
<h:panelGroup rendered="#{mapContext.attributes.mapEditor.editFeaturesEnabled}">

	<a:button id="copyPoint" mapId="map1" clientAction="EsriEditingLine" serverAction="#{mapContext.attributes.mapEditor.copyPoint}" defaultImage="./images/tasks/editing/copy.gif"
		hoverImage="./images/tasks/editing/copyU.gif" selectedImage="./images/tasks/editing/copyD.gif" 
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.copyfeatures" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? 'disabledButton' : 'hiddenButton'}"/>

	<a:button id="copyPolyline" mapId="map1" clientAction="EsriEditingLine" serverAction="#{mapContext.attributes.mapEditor.copyPolyline}" defaultImage="./images/tasks/editing/copyline.gif"
		hoverImage="./images/tasks/editing/copylineU.gif" selectedImage="./images/tasks/editing/copylineD.gif" 
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.copyfeatures" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'LINE')? 'disabledButton' : 'hiddenButton'}"/>

	<a:button id="copyPolygon" mapId="map1" clientAction="EsriEditingLine" serverAction="#{mapContext.attributes.mapEditor.copyPolygon}" defaultImage="./images/tasks/editing/copypolygon.gif"
		hoverImage="./images/tasks/editing/copypolygonU.gif" selectedImage="./images/tasks/editing/copypolygonD.gif" 
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.copyfeatures" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POLYGON')? 'disabledButton' : 'hiddenButton'}"/>

	<a:button id="move" mapId="map1" clientAction="EsriEditingLine" serverAction="#{mapContext.attributes.mapEditor.move}" defaultImage="./images/tasks/editing/move.gif"
		hoverImage="./images/tasks/editing/moveU.gif" selectedImage="./images/tasks/editing/moveD.gif"
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.movefeatures"/>

	<a:button id="delete" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.delete}" defaultImage="./images/tasks/editing/delete.gif"
		hoverImage="./images/tasks/editing/deleteU.gif" selectedImage="./images/tasks/editing/deleteD.gif" 
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.deletefeature"/>

	<a:button id="mergePolyline" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.mergePolyline}" defaultImage="./images/tasks/editing/mergeline.gif"
		hoverImage="./images/tasks/editing/mergelineU.gif" selectedImage="./images/tasks/editing/mergelineD.gif" 
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.mergefeatures" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'LINE')? 'disabledButton' : 'hiddenButton'}"/>
		
	<a:button id="splitLine" mapId="map1" clientAction="EsriMapPoint" serverAction="#{mapContext.attributes.mapEditor.splitLine}"
		defaultImage="./images/tasks/editing/splitline.gif" hoverImage="./images/tasks/editing/splitlineU.gif" selectedImage="./images/tasks/editing/splitlineD.gif"
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.splitlinefeature" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'LINE')? 'disabledButton' : 'hiddenButton'}"/>

	<a:button id="mergePolygon" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.mergePolygon}" defaultImage="./images/tasks/editing/mergepolygon.gif"
		hoverImage="./images/tasks/editing/mergepolygonU.gif" selectedImage="./images/tasks/editing/mergepolygonD.gif"
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.mergefeatures" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POLYGON')? 'disabledButton' : 'hiddenButton'}"/>
		
	<a:button id="splitPolygon" mapId="map1" clientAction="EsriMapPolyline" serverAction="#{mapContext.attributes.mapEditor.splitPolygon}"
		defaultImage="./images/tasks/editing/splitpolygon.gif" hoverImage="./images/tasks/editing/splitpolygonU.gif" selectedImage="./images/tasks/editing/splitpolygonD.gif"
		disabled="#{!mapContext.attributes.mapEditor.selected}" toolTip="EditTask.TaskInfo.Tip.splitpolygonfeature" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POLYGON')? 'disabledButton' : 'hiddenButton'}"/>

	<a:button id="addVertex" mapId="map1" clientAction="EsriEditingPoint" serverAction="#{mapContext.attributes.mapEditor.addVertex}"
		defaultImage="./images/tasks/editing/insertvertex.gif" hoverImage="./images/tasks/editing/insertvertexU.gif" selectedImage="./images/tasks/editing/insertvertexD.gif"
		disabled="#{!mapContext.attributes.mapEditor.reshapeable}" toolTip="EditTask.TaskInfo.Tip.addvertex" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? 'hiddenButton' : 'disabledButton'}"/>
	<a:button id="moveVertex" mapId="map1" clientAction="EsriEditingLine" serverAction="#{mapContext.attributes.mapEditor.moveVertex}"
		defaultImage="./images/tasks/editing/movevertex.gif" hoverImage="./images/tasks/editing/movevertexU.gif" selectedImage="./images/tasks/editing/movevertexD.gif"
		disabled="#{!mapContext.attributes.mapEditor.reshapeable}" toolTip="EditTask.TaskInfo.Tip.movevertex" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? 'hiddenButton' : 'disabledButton'}"/>
	<a:button id="deleteVertex" mapId="map1" clientAction="EsriEditingPoint" serverAction="#{mapContext.attributes.mapEditor.deleteVertex}"
		defaultImage="./images/tasks/editing/deletevertex.gif" hoverImage="./images/tasks/editing/deletevertexU.gif" selectedImage="./images/tasks/editing/deletevertexD.gif"
		disabled="#{!mapContext.attributes.mapEditor.reshapeable}" toolTip="EditTask.TaskInfo.Tip.deletevertex" styleClass="#{(mapContext.attributes.mapEditor.shapeType == 'POINT')? 'hiddenButton' : 'disabledButton'}"/>

</h:panelGroup>

<h:panelGrid style="width:100%;" styleClass="esriWindowTitleBar" rendered="#{mapContext.attributes.mapEditor.editAttributesEnabled}">
	<h:outputText styleClass="esriWindowTitleText" value="#{mapContext.attributes.mapEditor.attributesLabel}" />
</h:panelGrid>

<h:panelGrid columns="5" style="width:100%;" rendered="#{mapContext.attributes.mapEditor.editAttributesEnabled}">
	<a:button id="firstOID" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.firstOID}" defaultImage="./images/tasks/editing/first.gif"
		hoverImage="./images/tasks/editing/first.gif" selectedImage="./images/tasks/editing/firstD.gif" styleClass="hiddenButton" toolTip="EditTask.TaskInfo.Tip.first"/>

	<a:button id="preOID" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.preOID}" defaultImage="./images/tasks/editing/previous.gif"
		hoverImage="./images/tasks/editing/previous.gif" selectedImage="./images/tasks/editing/previousD.gif" 
		styleClass="hiddenButton" toolTip="EditTask.TaskInfo.Tip.previous"/>

	<a:button id="nextOID" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.nextOID}" defaultImage="./images/tasks/editing/next.gif"
		hoverImage="./images/tasks/editing/next.gif" selectedImage="./images/tasks/editing/nextD.gif" styleClass="hiddenButton" toolTip="EditTask.TaskInfo.Tip.next"/>

	<a:button id="lastOID" mapId="map1" serverAction="#{mapContext.attributes.mapEditor.lastOID}" defaultImage="./images/tasks/editing/last.gif"
		hoverImage="./images/tasks/editing/last.gif" selectedImage="./images/tasks/editing/lastD.gif" styleClass="hiddenButton" toolTip="EditTask.TaskInfo.Tip.last"/>
	<f:verbatim>
		<div id="indexSelect"></div>
	</f:verbatim>
</h:panelGrid>
<c:if test="${mapContext.attributes.mapEditor.editAttributesEnabled}">
	<div id="attsDiv" class="noAttributesPanel">No features selected</div>
</c:if>
<div style="width: 100%; text-align: right;"><a href="#" onclick="refreshEditor();editorSettingsWin.show();" style="font-family: verdana;"><c:out	value="${mapContext.attributes.mapEditor.settingsLabel}"/></a></div>
<div style="color: #004080; width: 100%; text-align: left; overflow: auto; background-color: #E4E4E4"><span id="editMessageBar" class="esriWindowStatusBarText"><c:out
	value="${mapContext.attributes.mapEditor.statusMessage}" /></span></div>
<h:messages styleClass="error" />
<a:button id="editorWindowsRefreshId" mapId="map1" type="hidden" value="refresh" serverAction="#{mapContext.attributes.mapEditor.refresh}"/>
</f:subview>