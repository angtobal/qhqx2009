<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib uri="http://www.esri.com/adf/web" prefix="a"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>QHQXGIS</title>
<style type="text/css">
html,body { margin:0; padding:0; font-size:12px; color:#333333;} 
</style>
<link rel="stylesheet" title="base" href="css/base-style.css"
	type="text/css" media="screen" />
<link rel="stylesheet" title="base" href="css/esri_styles.css"
	type="text/css" media="screen" />
<link rel="stylesheet" title="base" href="css/results.css"
	type="text/css" media="screen" />
<link rel="stylesheet" title="base" href="css/taskmenu.css"
	type="text/css" media="screen" />
<link rel="stylesheet" title="base" href="themes/blue.css"
	type="text/css" media="screen" />

<script type="text/javascript" language="javascript"
	src="js/resource.js"></script>
<script type="text/javascript" language="javascript"
	src="js/esri_navigator.js"></script>
<script type="text/javascript" language="javascript" src="js/slider.js"></script>
<script type="text/javascript" language="javascript" src="js/taskbox.js"></script>
<script type="text/javascript" language="javascript"
	src="js/mapviewer.js"></script>
<script type="text/javascript" language="javascript" src="js/results.js"></script>
<script type="text/javascript" language="Javascript"
	src="js/esri_edit.js"></script>
<script type="text/javascript" language="Javascript"
	src="js/esri_colorchooser.js"></script>
<script type="text/javascript" language="javascript"
	src="js/identify.js"></script>  
<script type="text/javascript" language="javascript">
<!--  //树形导航的JS代码
document.write("<style type=text/css>#master { left: -183px; position: absolute; top: 25px; visibility: visible; z-index: 999}</style>")
var ie = document.all ? 1 : 0
var ns = document.layers ? 1 : 0
var master = new Object("element")
master.curLeft = -200; master.curTop = 10;
master.gapLeft = 0;  master.gapTop = 0;
master.timer = null;

if(ie){var sidemenu = document.all.master;}
if(ns){var sidemenu = document.master;}
setInterval("FixY()",100);

function moveAlong(layerName, paceLeft, paceTop, fromLeft, fromTop){
 clearTimeout(eval(layerName).timer)
 if(eval(layerName).curLeft != fromLeft){
  if((Math.max(eval(layerName).curLeft, fromLeft) - Math.min(eval(layerName).curLeft, fromLeft)) < paceLeft){eval(layerName).curLeft = fromLeft}
  else if(eval(layerName).curLeft < fromLeft){eval(layerName).curLeft = eval(layerName).curLeft + paceLeft}
   else if(eval(layerName).curLeft > fromLeft){eval(layerName).curLeft = eval(layerName).curLeft - paceLeft}
  if(ie){document.all[layerName].style.left = eval(layerName).curLeft}
  if(ns){document[layerName].left = eval(layerName).curLeft}
 }
 if(eval(layerName).curTop != fromTop){
   if((Math.max(eval(layerName).curTop, fromTop) - Math.min(eval(layerName).curTop, fromTop)) < paceTop){eval(layerName).curTop = fromTop}
  else if(eval(layerName).curTop < fromTop){eval(layerName).curTop = eval(layerName).curTop + paceTop}
   else if(eval(layerName).curTop > fromTop){eval(layerName).curTop = eval(layerName).curTop - paceTop}
  if(ie){document.all[layerName].style.top = eval(layerName).curTop}
  if(ns){document[layerName].top = eval(layerName).curTop}
 }
 eval(layerName).timer=setTimeout('moveAlong("'+layerName+'",'+paceLeft+','+paceTop+','+fromLeft+','+fromTop+')',30)
}

function setPace(layerName, fromLeft, fromTop, motionSpeed){
 eval(layerName).gapLeft = (Math.max(eval(layerName).curLeft, fromLeft) - Math.min(eval(layerName).curLeft, fromLeft))/motionSpeed
 eval(layerName).gapTop = (Math.max(eval(layerName).curTop, fromTop) - Math.min(eval(layerName).curTop, fromTop))/motionSpeed
 moveAlong(layerName, eval(layerName).gapLeft, eval(layerName).gapTop, fromLeft, fromTop)
}

var expandState = 0

function expand(){
 if(expandState == 0){setPace("master", 0, 10, 10); if(ie){document.menutop.src = "images/menui.gif"}; expandState = 1;}
 else{setPace("master", -183, 10, 10); if(ie){document.menutop.src = "images/menuo.gif"}; expandState = 0;}
}

function FixY(){
 if(ie){sidemenu.style.top = document.body.scrollTop+10}
 if(ns){sidemenu.top = window.pageYOffset+10}
}
-->
</script>
</head>

<body>
<div id="qhqxgis" onload="initLayout();">
<f:view>
<a:context value="#{mapContext}" />
<h:form id="mapForm">



<table>
<tr><td style="padding-top:1px; vertical-align:top;">
	
    
    	<table id="master" width="198" border="0" cellspacing="0" cellpadding="0">
			<tr><td><img border="0" height="6" src="images/menutop.gif"  width="180"></td>
			<td rowspan="2" valign="top"><img id="menu" onMouseOver="javascript:expand()" border="0" height="350" name="menutop" src="images/menuo.gif" width="30"></td></tr>
			<tr><td valign="top"><table width="100%" border="0" cellspacing="5" cellpadding="0"><tr><td height="300" valign="top"><table bgcolor="#FFFFFF"  width="100%" height="100%" border="1" cellpadding="0" cellspacing="5" bordercolor="#666666"><tr><td valign="top">
			
			<div style="width:156px;overflow:hidden;"><a:toc id="toc1" value="#{mapContext.webToc}" mapId="map1" style="left:0px;" clientPostBack="true" /> </div>
			
		</td></tr></table></td></tr></table></td></tr></table>	 
    	 
    
</td>
<td width="100%" style="padding-top:1px;">
	<table cellpadding="0" cellspacing="0" border="0"><tr><td>
		<div id="content" style="position: absolute; top: 5px; left: 0px; z-index: 1;">
			<a:map value="#{mapContext.webMap}" id="map1" width='740' height='595' scaleBar="#{mapContext.attributes['webappScaleBar']}" />

			<div id="overview" style="display: none">
			<a:overview id="ov1" value="#{mapContext.webOverview}" mapId="map1" width="150" height="100" lineColor="#f00" />
			</div>
		</div>
		<div id="tool" style="position: absolute; top: 5px; left: 400px; z-index: 2;">
			<table>
				<tr>
					<td><div style="float: left"><a:task id="mapToolsTask"
						value="#{mapContext.attributes.webappMapToolsTask}"
						taskInfo="#{mapContext.attributes.webappMapToolsTask.taskInfo}"
						mapId="map1" windowingSupport="false" style="padding:0px;margin:0px;"
						xslFile="maptoolstask.xsl" />
						</div>
					</td>
					<td>
						<div style="float: left;padding:2px"><input width="25px" type="image"
							height="25px" title="Toggle Overview Map"
							onmouseout="//EsriUtils.setImageSrc(this, 'images/show-overview-map.png');"
							onclick="esriToggleOverviewMap(); return false;"
							onmousedown="//EsriUtils.setImageSrc(this, 'images/show-overview-map.png');"
							onmouseover="//EsriUtils.setImageSrc(this, 'images/show-overview-map.png');"
							onload="EsriUtils.setImageSrc(this, 'images/show-overview-map.png');"
							src="images/show-overview-map.png"
							id="button_mapToolsTask_action_toggleOverview"
							name="button_mapToolsTask_action_toggleOverview" />
						</div>
					</td>
					<td>
						<div style="float: left"><a:task id="mapExtent"
							value="#{mapContext.attributes.mapExtent}"
							taskInfo="#{mapContext.attributes.mapExtent.taskInfo}"
							mapId="map1" windowingSupport="false" style="padding:0px;margin:0px;bgcolor:#666666"/>
						</div>
					</td>
				</tr>
			</table>
			<div id="legend" style="position: absolute; top: 100px; left: 290px; z-index: 2;">
				<h:graphicImage value="#{legendImgView.imgURL}"/> 
			</div>
		</div>
    </td></tr></table>
</td>
</tr>
</table>



</h:form>
</f:view></div>


</body>
</html>


