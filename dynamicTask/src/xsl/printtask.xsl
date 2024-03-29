<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!-- Copyright (c) 2000-2003, Environmental Systems Research Institute, Inc. All rights reserved. -->
  <!-- XSL file for Task control -->
  <xsl:import href="core.xsl"/>
  <xsl:import href="printtask-layout-nodes.xsl"/>
  <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
  <!--
  To customize the appearance of the task control, enter values for each attribute you wish to customize in their
  respective opening and closing <xsl:variable> tags below.  You can customize as many or as few attributes
  as you wish.

  Any attributes defined in the web control's JSP tag will take precedence over custom values defined below.

  Both class and style attributes are permitted in a single tag.  Properties defined below or in the web control's JSP tag attributes
  will override class properties.  However, if a property that is defined in the class is not defined below or in the JSP tag attributes
  then the class definition will handle that property.
  -->
  <xsl:variable name="customStyle"/>
  <xsl:variable name="customStyleClass"/>
  <!-- End of variable customization -->

  <xsl:variable name="contextPath"><xsl:value-of select="//context-path"/>/</xsl:variable>

  <xsl:variable name="common" select="//common-resources-registered"/>

  <xsl:variable name="taskId"><xsl:value-of select="//id"/></xsl:variable>

  <xsl:variable name="activeTool"><xsl:value-of select="//active-tool"/></xsl:variable>

  <!-- Set a final value for the style attribute -->
  <xsl:variable name="style">
    <xsl:choose>
      <xsl:when test="//style != ''">
        <xsl:value-of select="//style"/>
      </xsl:when>
      <xsl:when test="$customStyle != ''">
        <xsl:value-of select="$customStyle"/>
      </xsl:when>
    </xsl:choose>
  </xsl:variable>

  <!-- Set a final value for the style class attribute -->
  <xsl:variable name="styleClass">
    <xsl:choose>
      <xsl:when test="//styleClass != ''">
        <xsl:value-of select="//styleClass"/>
      </xsl:when>
      <xsl:when test="$customStyleClass != ''">
        <xsl:value-of select="$customStyleClass"/>
      </xsl:when>
    </xsl:choose>
  </xsl:variable>

  <!-- Define the DIV Task Id -->
  <xsl:variable name="taskCellId">EsriTaskCell_<xsl:value-of select="$taskId"/></xsl:variable>

  <!-- Window Manager Name -->
  <xsl:variable name="winMgr">taskWindowManager</xsl:variable>

  <!-- Window Manager Hidden Element Name -->
  <xsl:variable name="winMgrElementName">taskWinProp</xsl:variable>

  <!-- ################# -->
  <!-- Begin HTML output -->
  <!-- ################# -->
  <xsl:template match="/">
    <xsl:comment>Printing Task Renderer Start (<xsl:value-of select="$taskId"/>)</xsl:comment>
    <xsl:if test="$common = 'false'">
      <xsl:apply-imports/>
    </xsl:if>
    <xsl:if test="//first-time = 'true'">
      <xsl:element name="script">
        <xsl:attribute name="language">Javascript</xsl:attribute>
        <xsl:attribute name="src">
          <xsl:value-of select="$contextPath"/>js/esri_window.js</xsl:attribute>
      </xsl:element>
      <xsl:element name="script">
        <xsl:attribute name="language">Javascript</xsl:attribute>
        <xsl:attribute name="src">
          <xsl:value-of select="$contextPath"/>js/esri_window_mgr.js</xsl:attribute>
      </xsl:element>
      <xsl:element name="script">
        <xsl:attribute name="language">Javascript</xsl:attribute>
        <xsl:attribute name="src">
          <xsl:value-of select="$contextPath"/>js/esri_task.js</xsl:attribute>
      </xsl:element>
     
      <xsl:element name="input">
        <xsl:attribute name="id"><xsl:value-of select="$winMgrElementName"/></xsl:attribute>
        <xsl:attribute name="name"><xsl:value-of select="$winMgrElementName"/></xsl:attribute>
        <xsl:attribute name="type">HIDDEN</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="//window-properties"/>
        </xsl:attribute>
      </xsl:element>
      <xsl:element name="script">
        <xsl:attribute name="language">Javascript</xsl:attribute>
          var <xsl:value-of select="$winMgr"/> = new EsriWindowManager("<xsl:value-of select="$winMgr"/>", document.getElementById("<xsl:value-of select="$winMgrElementName"/>"));
        </xsl:element>
    </xsl:if>
    
     <!-- print-task -->
    <xsl:element name="script">
      <xsl:attribute name="language">Javascript</xsl:attribute>
      <xsl:attribute name="src"><xsl:value-of select="$contextPath"/>js/esri_task_printing.js</xsl:attribute>
    </xsl:element>
    
    <xsl:choose>
      <xsl:when test="count(/task/task-layouts/task-layout) > 0">
        <xsl:for-each select="/task/task-layouts/task-layout">
          <xsl:variable name="layoutTaskCellId">
            <xsl:choose>
              <xsl:when test="position() = 1"><xsl:value-of select="$taskCellId"/></xsl:when>
              <xsl:otherwise><xsl:value-of select="$taskCellId"/><xsl:value-of select="position()"/></xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <xsl:variable name="winTitle">
            <xsl:choose>
              <xsl:when test="position() = 1"><xsl:value-of select="//task/task-descriptor/display-name"/></xsl:when>
              <xsl:otherwise><xsl:value-of select="@id"/></xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <xsl:element name="div">
            <xsl:attribute name="id">
              <xsl:value-of select="$layoutTaskCellId"/>
            </xsl:attribute>
            <xsl:if test="$style != '' or @style != ''">
              <xsl:attribute name="style">
                <xsl:if test="@style != ''">
                  <xsl:value-of select="@style"/>;
                </xsl:if>
                <xsl:if test="$style != '' and position() = 1">
                  <xsl:value-of select="$style"/>
                </xsl:if>
              </xsl:attribute>
            </xsl:if>
            <xsl:if test="$styleClass != ''">
              <xsl:attribute name="class">
                <xsl:value-of select="$styleClass"/>
              </xsl:attribute>
            </xsl:if>
            <xsl:choose>
              <!-- TabularLayout -->
              <xsl:when test='@layout="tabularLayout"'>
                <xsl:call-template name="tabularLayout">
                  <xsl:with-param name="tabularLayoutNode" select="."/>
                </xsl:call-template>
              </xsl:when>
              <!-- AbsoluteLayout -->
              <xsl:when test='@layout="absoluteLayout"'>
                <xsl:call-template name="absoluteLayout">
                  <xsl:with-param name="absoluteLayoutNode" select="."/>
                </xsl:call-template>
              </xsl:when>
            </xsl:choose>
          </xsl:element>

          <xsl:if test="//show-in-window = 'true' or (//show-in-window = 'false' and position() > 1)">
            <xsl:call-template name="showInWindow">
              <xsl:with-param name="id">
                <xsl:value-of select="$layoutTaskCellId"/>
              </xsl:with-param>
              <xsl:with-param name="winTitle">
                <xsl:value-of select="$winTitle" />
              </xsl:with-param>
              <xsl:with-param name="layout">
                <xsl:value-of select="@layout" />
              </xsl:with-param>
            </xsl:call-template>
          </xsl:if>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:element name="div">
          <xsl:attribute name="id"><xsl:value-of select="$taskCellId"/></xsl:attribute>
          <xsl:if test="$style != ''">
            <xsl:attribute name="style">
              <xsl:value-of select="$style"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="$styleClass != ''">
            <xsl:attribute name="class">
              <xsl:value-of select="$styleClass"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:apply-templates select="/" mode="defaultLayout"/>
        </xsl:element>

        <xsl:if test="//show-in-window = 'true'">
          <xsl:call-template name="showInWindow">
            <xsl:with-param name="id">
              <xsl:value-of select="$taskCellId"/>
            </xsl:with-param>
            <xsl:with-param name="winTitle">
              <xsl:value-of select="//task/task-descriptor/display-name"/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:if test="$activeTool != ''">
      <xsl:comment>Setting Active Tool <xsl:value-of select="$activeTool"/>
      </xsl:comment>
      <xsl:element name="script">
        <xsl:attribute name="type">text/javascript</xsl:attribute>
        <xsl:attribute name="language">Javascript</xsl:attribute>
        function esriTaskInitActiveTool() {
          document.getElementById("button_<xsl:value-of select="$activeTool"/>").click();
        }
        esriInitItems.push("esriTaskInitActiveTool()");
      </xsl:element>
    </xsl:if>

    <xsl:element name="script">
      <xsl:attribute name="type">text/javascript</xsl:attribute>
      <xsl:attribute name="language">Javascript</xsl:attribute>
      function esriTaskInit<xsl:value-of select="$taskId"/>() {
        new EsriTask("<xsl:value-of select="$taskId"/>", "<xsl:value-of select="//map-id"/>");
        EsriControls.addPostBackTagHandler("print-task", processCreatePrintPage);
      }
      esriInitItems.push("esriTaskInit<xsl:value-of select="$taskId"/>()");
    </xsl:element>

    <xsl:comment>Printing Task Renderer End (<xsl:value-of select="$taskId"/>)</xsl:comment>
  </xsl:template>
  
  <!-- =================================================== -->
  <!-- Default Layout -->
  <!-- =================================================== -->
  <xsl:template match="/" mode="defaultLayout">
     <!-- Empty -->
  </xsl:template>

  
  <!-- =================================================== -->
  <!-- Print task Tabular Layout -->
  <!-- =================================================== -->
  <xsl:template name="tabularLayout">
    <xsl:param name="tabularLayoutNode"/>
    <xsl:element name="table">
      <!-- 
       <xsl:if test="@style != ''">
        <xsl:attribute name="style">
          <xsl:value-of select="@style"/>
        </xsl:attribute>
      </xsl:if>
       -->
      <xsl:element name="tbody">
        <xsl:for-each select="components">
          <xsl:element name="tr">
            <xsl:for-each select="component">
              <xsl:variable name="thisType">
                <xsl:value-of select="@type"/>s</xsl:variable>
              <xsl:variable name="thisName" select="."/>
              <xsl:variable name="thisStyle" select="@style"/>
              <xsl:variable name="thisStyleClass" select="@styleClass"/>
              <xsl:variable name="thisColspan" select="@colspan"/>
              <xsl:variable name="thisRowspan" select="@rowspan"/>

              <xsl:choose>
                <!-- Process the Actions -->
                <xsl:when test='$thisType="task-actions"'>
                  <xsl:for-each select="//task-actions/task-action[action-properties/@visible = 'true' and $thisName=@name]">
                    <xsl:element name="td">
                      <xsl:attribute name="align">right</xsl:attribute>
                      <xsl:if test="$thisStyle != ''">
                        <xsl:attribute name="style">
                          <xsl:value-of select="$thisStyle"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisStyleClass != ''">
                        <xsl:attribute name="class">
                          <xsl:value-of select="$thisStyleClass"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisColspan != ''">
                        <xsl:attribute name="colspan">
                          <xsl:value-of select="$thisColspan"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisRowspan != ''">
                        <xsl:attribute name="rowspan">
                          <xsl:value-of select="$thisRowspan"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:call-template name="processActionNodeByLayout">
                        <xsl:with-param name="actionNode" select="."/>
                      </xsl:call-template>
                    </xsl:element>
                  </xsl:for-each>
                </xsl:when>

                <!-- Process the Tools -->
                <xsl:when test='$thisType="task-tools"'>
                  <xsl:for-each select="//task-tools/task-tool[tool-properties/@visible = 'true' and $thisName=@name]">
                    <xsl:element name="td">
                      <xsl:if test="$thisStyle != ''">
                        <xsl:attribute name="style">
                          <xsl:value-of select="$thisStyle"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisStyleClass != ''">
                        <xsl:attribute name="class">
                          <xsl:value-of select="$thisStyleClass"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisColspan != ''">
                        <xsl:attribute name="colspan">
                          <xsl:value-of select="$thisColspan"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisRowspan != ''">
                        <xsl:attribute name="rowspan">
                          <xsl:value-of select="$thisRowspan"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:call-template name="processToolNodeByLayout">
                        <xsl:with-param name="toolNode" select="."/>
                      </xsl:call-template>
                    </xsl:element>
                  </xsl:for-each>
                </xsl:when>

                <!-- Process the Params-->
                <xsl:when test='$thisType="task-params"'>
                  <xsl:for-each select="//task-params/task-param[$thisName=@name]">
                    <xsl:element name="td">
                        
                      <xsl:choose>
                          <xsl:when test="$thisStyle != ''">
                              <xsl:attribute name="style">
                                <xsl:value-of select="$thisStyle"/>
                                <xsl:if test="@visible = 'false'">
                                    <xsl:text>;display:none;</xsl:text>
                                </xsl:if>
                             </xsl:attribute>                           
                          </xsl:when>
                          <xsl:otherwise>
                                <xsl:if test="@visible = 'false'">
                                    <xsl:attribute name="style">
                                        <xsl:text>display:none;</xsl:text>
                                    </xsl:attribute>  
                                </xsl:if>
                          </xsl:otherwise>
                      </xsl:choose>

                      <xsl:if test="$thisStyleClass != ''">
                        <xsl:attribute name="class">
                          <xsl:value-of select="$thisStyleClass"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisColspan != ''">
                        <xsl:attribute name="colspan">
                          <xsl:value-of select="$thisColspan"/>
                        </xsl:attribute>
                      </xsl:if>

                      <xsl:if test="$thisRowspan != ''">
                        <xsl:attribute name="rowspan">
                          <xsl:value-of select="$thisRowspan"/>
                        </xsl:attribute>
                      </xsl:if>            
                      
                      <xsl:choose>
                          <xsl:when test="@type = 'LABEL'">
                              <xsl:value-of select="display-name"/>                            
                          </xsl:when>
                          <xsl:otherwise>
                              <xsl:call-template name="processParamNodeByLayout">
                                <xsl:with-param name="paramNode" select="."/>
                                <xsl:with-param name="layout">tabularLayout</xsl:with-param>
                              </xsl:call-template>
                          </xsl:otherwise>
                      </xsl:choose>
                    </xsl:element>
                  </xsl:for-each>
                </xsl:when>
              </xsl:choose>
            </xsl:for-each>
          </xsl:element> <!-- end of tr -->
        </xsl:for-each>

      </xsl:element>
    </xsl:element> <!-- end of table -->
  <!-- div end -->
  </xsl:template>

  <!-- =================================================== -->
  <!-- Absolute Layout -->
  <!-- =================================================== -->
  <xsl:template name="absoluteLayout">
    <!-- Empty -->
  </xsl:template>


  <!-- ############################## -->
  <!-- Show Task in Floating Window -->
  <!-- ############################## -->
  <xsl:template name="showInWindow">
    <xsl:param name="id"/>
    <xsl:param name="winTitle"/>
    <xsl:param name="layout">defaultLayout</xsl:param>
    <xsl:variable name="pe">pe_<xsl:value-of select="$id"/></xsl:variable>
    <xsl:variable name="win">win_<xsl:value-of select="$id"/></xsl:variable>
    <xsl:element name="script">
      <xsl:attribute name="language">Javascript</xsl:attribute>
      function esriTaskWindowInit<xsl:value-of select="$id"/>(){
        var <xsl:value-of select="$pe"/> = new EsriPageElement("<xsl:value-of select="$pe"/>");
        <xsl:value-of select="$pe"/>.divObject = document.getElementById("<xsl:value-of select="$id"/>");
        <xsl:value-of select="$pe"/>.divId = "<xsl:value-of select="$id"/>";
        <xsl:choose>
          <xsl:when test="//task/task-descriptor/@visible = 'true'">
            var <xsl:value-of select="$win" /> = new EsriWindow("<xsl:value-of select="$win"/>", "<xsl:value-of select="$winTitle"/>", <xsl:value-of select="$pe"/>);
          </xsl:when>
          <xsl:otherwise>
            var <xsl:value-of select="$win" /> = new EsriWindow("<xsl:value-of select="$win"/>", "", <xsl:value-of select="$pe"/>);
          </xsl:otherwise>
        </xsl:choose>
        <xsl:value-of select="$win" />.init(document.forms[EsriControls.maps["<xsl:value-of select="//map-id"/>"].formId]);
        <xsl:value-of select="$win" />.fittable = <xsl:value-of select="$layout != 'absoluteLayout'" />;
        <xsl:value-of select="$win" />.fit();
        <xsl:value-of select="$winMgr"/>.addWindow(<xsl:value-of select="$win" />, true);
      }
      esriInitItems.push("esriTaskWindowInit<xsl:value-of select="$id"/>()");
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
