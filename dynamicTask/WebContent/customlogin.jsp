<html>
<head>
<title>ArcGIS Web Mapping Application Login</title>
<script type="text/javascript">

     /* Function to get secret question */
    function getSecretQuestion() {
    
      // Clean the messages
      cleanForgotMsg();
      
      // Clean existing secret questions
      document.getElementById("txbxForgotQuestion").value = "";
      
      var username = document.getElementById("txbxForgotUsername").value;
      if( username == null || username == undefined || username.length == 0 ) {
        showMessage("User Name cannot be empty.", "error", "forgot");
        return;
      }
      
      var url = "?command=getquestion";
      var params = "&username=" + username;      
      sendAjaxRequest( url,params,"question");
    }
    
    /* Function to validate login request */
    function validateLoginReq() {
    
      cleanLoginMsg();
    
      var username = "";
      username = document.getElementById("txbxLoginUsername").value;
      var password = "";
      password = document.getElementById("txbxLoginPassword").value;
      
      if(username == null || username.length == 0 || password == null || password.length == 0 ) {
        showMessage('User Name or Password cannot be empty.', 'error', 'login' );
        return false;
      }
    }
    
    /* Function to show message on a form */
    function showMessage(msg,  msgType,  formName) {
      /* Messages for forgot password form */
      if(formName != null && formName != undefined && formName == "forgot" ) {      
        /* Success */
        if( msgType != null && msgType != undefined && msgType == "success" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxForgotMsg2").appendChild(textNode);
          return;
        }        
        /* Failure */
        if( msgType != null && msgType != undefined && msgType == "error" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxForgotMsg1").appendChild(textNode);
          return;
        }
      }
      
      /* Messages for login form */
      if(formName != null && formName != undefined && formName == "login" ) {      
        /* Success */
        if( msgType != null && msgType != undefined && msgType == "success" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxLoginMsg2").appendChild(textNode);
          return;
        }        
        /* Failure */
        if( msgType != null && msgType != undefined && msgType == "error" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxLoginMsg1").appendChild(textNode);
          return;
        }
      }
      
      /* Messages for change form */
      if(formName != null && formName != undefined && formName == "change" ) {      
        /* Success */
        if( msgType != null && msgType != undefined && msgType == "success" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxChangeMsg2").appendChild(textNode);
          return;
        }        
        /* Failure */
        if( msgType != null && msgType != undefined && msgType == "error" ) {
          var textNode = document.createTextNode(msg);
          document.getElementById("txbxChangeMsg1").appendChild(textNode);
          return;
        }
      }
    }
    
    /* Function to clean login messages */
    function cleanLoginMsg() {
      
      var txbx1 = document.getElementById("txbxLoginMsg1");
      if( txbx1 != null ) {
        var children = txbx1.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx1.removeChild( children[i] );
        }
      }
      
      var txbx2 = document.getElementById("txbxLoginMsg2");
      if( txbx2 != null ) {
        var children = txbx2.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx2.removeChild( children[i] );
        }
      }
    }
    
    /* Function to clean forgot password messages */
    function cleanForgotMsg() {
      
      var txbx1 = document.getElementById("txbxForgotMsg1");
      if( txbx1 != null ) {
        var children = txbx1.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx1.removeChild( children[i] );
        }
      }
      
      var txbx2 = document.getElementById("txbxForgotMsg2");
      if( txbx2 != null ) {
        var children = txbx2.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx2.removeChild( children[i] );
        }
      }  
      
      var txbx3 = document.getElementById("txbxForgotNewPwd");
      if( txbx3 != null ) {
        var children = txbx3.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx3.removeChild( children[i] );
        }
      }          
    }
    
    /* Function to clean forgot password messages */
    function cleanChangeMsg() {
      
      var txbx1 = document.getElementById("txbxChangeMsg1");
      if( txbx1 != null ) {
        var children = txbx1.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx1.removeChild( children[i] );
        }
      }
      
      var txbx2 = document.getElementById("txbxChangeMsg2");
      if( txbx2 != null ) {
        var children = txbx2.childNodes;        
        for(var i=0; i<children.length; i++) {
          txbx2.removeChild( children[i] );
        }
      }          
    }
    
    
    
    /* Function to validate change password request */
    function validateChangePasswdReq() {
    
      cleanChangeMsg();
    
      var username = "";
      username = document.getElementById("txbxChangeUsername").value;
      if( username == null || username.length == 0 ) {
        showMessage("User Name cannot be empty.", "error", "change");
        return false;
      }
      
      var oldPasswd = "";
      oldPasswd = document.getElementById("txbxChangeOldPasswd").value;
      if( oldPasswd == null || oldPasswd.length == 0 ) {
        showMessage("Old Password cannot be empty.", "error", "change");
        return false;
      }
      
      var newPasswd = "";
      newPasswd = document.getElementById("txbxChangeNewPasswd").value;
      if( newPasswd == null || newPasswd.length == 0 ) {
        showMessage("New Password cannot be empty.", "error", "change");
        return false;
      }
      
      var confirmPasswd = "";
      confirmPasswd = document.getElementById("txbxChangeConfirmPasswd").value;
      if( confirmPasswd == null || confirmPasswd.length == 0 ) {
        showMessage("Confirm Password cannot be empty.", "error", "change");
        return false;
      }
      
      /* Confirm Password and New Password must be same */
      if( newPasswd != confirmPasswd ) {
        showMessage("New Password and Confirm Password do not match.", "error", "change");
        return false;
      }
      
      /* New Password must not be same as the Old Password */
      if( oldPasswd == newPasswd ) {
        showMessage("New Password cannot be same as the Old Password.", "error", "change");
        return false;
      }
      
    }    

    
    /* Function to get a new password for forgot password case*/
    function getNewPassword() {
    
      cleanForgotMsg();
    
      var username = document.getElementById("txbxForgotUsername").value;
      if( username == null || username == undefined || username.length == 0 ) {
        showMessage("User Name cannot be empty.", "error", "forgot");
        return;
      }
      
      var question = document.getElementById("txbxForgotQuestion").value;
      if( question == null || question == undefined || question.length == 0 ) {
        showMessage("Secret Question cannot be empty.", "error", "forgot");
        return;
      }
      
      var answer = document.getElementById("txbxForgotAnswer").value;
      if( answer == null || answer == undefined || answer.length == 0 ) {
        showMessage("Secret Answer cannot be empty.", "error", "forgot");
        return;
      }
      
      var url = "?command=forgotpassword";
      var params = "&username=" + username + "&question=" + question + "&answer=" + answer;    
      sendAjaxRequest( url,params,"password");
    } 
    
     /* Function to process the xml http request */
    function sendAjaxRequest( url,params,changeText) {
      var req = null;
      if(window.XMLHttpRequest && !(window.ActiveXObject)) {
        try {
          req = new XMLHttpRequest();
          } catch(e) {
          req = false;
         }
      } else if(window.ActiveXObject) {
          try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
          } catch(e) {
            try {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(e) {
                req = false;
            }
      }
     } else {
       // The browser does not support AJAX calls. Its probably too old.
       showMessage("Your browser does not support AJAX. Please see the browsers supported by ArcGIS Server.", "error", "forgot");
     }
     
     if(req) {
       req.onreadystatechange = function() { processForgotPasswdResp(req, changeText); };
       url=url+params;      
       req.open("POST", url, true);
       req.send("");
      }
  }
  
    /* Function to process the xml response from getSecretQuestion request */
    function processForgotPasswdResp( httpreq,  type) {
       if(httpreq.readyState ==4) {        
        // Success
        if(httpreq.status == 200) {
           if( type == "question" ) {
            document.getElementById("txbxForgotQuestion").value=httpreq.responseText;
          }else if ( type == "password" ) {
            var textNode = document.createTextNode("New password:");
            document.getElementById("txbxForgotMsg2").appendChild(textNode);
            textNode = document.createTextNode(httpreq.responseText);
            document.getElementById("txbxForgotNewPwd").appendChild(textNode);
          }
          return;
        }else {
          // Failure
          showMessage(httpreq.responseText, "error", "forgot");
          return;
        }
      }
    }
    
  </script>
</head>
<%! String command;
    String status;
    String message;
  %>

<%  command = request.getParameter("command");
    status = request.getParameter("status");
    message = request.getParameter("message");
  %>

<body style="margin: 0px">
<div style="background-color: rgb(53, 103, 174);"><img
  align="center" src="./images/header.jpeg" /></div>

<% if((command != null) && (command.equalsIgnoreCase("forgotpassword")) ) { 
      %>
<form method="POST" action="?command=forgotpassword&" name="forgot"><br />
<br />
<br />
<br />
<br />
<br />
<table cellpadding="2" style="background-color: #6699cc" align="center">
  <tbody>
    <tr>
      <td style="font-family: sans-serif; font-size: 14; width: 322"
        align="center">Forgot Password</td>
    </tr>
  </tbody>
</table>
<table align="center" style="background-color: #99ccff; width: 330">
  <thead>
    <tr>
    </tr>
  </thead>
  <tbody>
    <tr style="width: 316">
      <td />
      <td style="font-family: sans-serif; font-size: 14">User Name:</td>
      <td><input type="text" size="20" id="txbxForgotUsername"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">Secret
      Question:</td>
      <td><input type="text" size="20" id="txbxForgotQuestion"></input>
      </td>
      <td align="left"><input type="button" value="Get It"
        onclick="getSecretQuestion();"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">Secret Answer:</td>
      <td><input type="password" size="20" id="txbxForgotAnswer"></input></td>
      <td />
    </tr>
      <tr>
      <td />
      <td />
      <td align="right"><input type="button" value="Submit" onclick="getNewPassword();" /></td>
      <td />
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr>
      <td><a style="font-family: sans-serif; font-size: 14;"
        href="customlogin.jsp">Back to Login</a></td>
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr align="center">     
      <td id="txbxForgotMsg1" style="font-family: sans-serif; font-size: 14; color: red">
      </td>
    </tr>
    <tr align="center">
      <td id="txbxForgotMsg2" style="font-family: sans-serif; font-size: 14; color: blue">
      </td>
    </tr>
    <tr align="center">
      <td id="txbxForgotNewPwd" style="font-family: sans-serif; font-size: 14;">
      </td>
    </tr>
  </tbody>
</table>
</form>


<% } else if( (command != null) && (command.equalsIgnoreCase("changepassword")) ) { %>


<form method="POST" action="?command=changepassword&" name="change"
  onsubmit="return validateChangePasswdReq();"><br />
<br />
<br />
<br />
<br />
<br />
<table cellpadding="2" style="background-color: #6699cc" align="center">
  <tbody>
    <tr>
      <td style="font-family: sans-serif; font-size: 14; width: 310"
        align="center">Change Password</td>
    </tr>
  </tbody>
</table>
<table align="center" style="background-color: #99ccff; width: 318">
  <thead>
    <tr>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">User Name:</td>
      <td><input type="text" size="20" id="txbxChangeUsername"
        name="username"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">Old
      Password:</td>
      <td><input type="password" size="20" id="txbxChangeOldPasswd"
        name="oldpassword"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">New
      Password:</td>
      <td><input type="password" size="20" id="txbxChangeNewPasswd"
        name="newpassword"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">Confirm New
      Password:</td>
      <td><input type="password" size="20" id="txbxChangeConfirmPasswd"
        name="confirmpassword"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td />
      <td align="right"><input type="submit" value="Submit"></input></td>
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr>
      <td><a style="font-family: sans-serif; font-size: 14;"
        href="customlogin.jsp">Back to Login</a></td>
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr>
      <td id="txbxChangeMsg1"
        style="font-family: sans-serif; font-size: 14; color: red">
      <% if( status != null && status.equalsIgnoreCase("failed")){      
         if( message != null ) {
           out.println(message);
           } else {
          out.println("Unable to change password.");
        } }%>
      </td>
      <td id="txbxChangeMsg2"
        style="font-family: sans-serif; font-size: 14; color: blue">
      <% if( status != null && status.equalsIgnoreCase("success")){     
         if( message != null ) {
           out.println(message);
           } else {
          out.println("Your password has been changed successfully.");
        } }%>
      </td>
    </tr>
  </tbody>
</table>
</form>
<% } else { %>







<form method="POST" action="index.html?command=login&" name="login"
  onsubmit="return validateLoginReq();"><br />
<br />
<br />
<br />
<br />
<br />
<table cellpadding="2" style="background-color: #6699cc" align="center">
  <tbody>
    <tr>
      <td style="font-family: sans-serif; font-size: 14; width: 232"
        align="center">Login</td>
    </tr>
  </tbody>
</table>
<table align="center" style="background-color: #99ccff; width : 240">
  <thead>
    <tr>
    </tr>
  </thead>
  <tbody>
    <tr style="width: 230">
      <td />
      <td style="font-family: sans-serif; font-size: 14">User Name:</td>
      <td><input type="text" size="20" id="txbxLoginUsername"
        name="username"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td style="font-family: sans-serif; font-size: 14">Password:</td>
      <td><input type="password" size="20" id="txbxLoginPassword"
        name="password"></input></td>
      <td />
    </tr>
    <tr>
      <td />
      <td />
      <td align="right"><input type="submit" value="Log In" /></td>
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr>
      <td><a style="font-family: sans-serif; font-size: 14"
        href="customlogin.jsp?command=forgotpassword">Forgot Password</a></td>
      <td>|</td>
      <td><a style="font-family: sans-serif; font-size: 14"
        href="customlogin.jsp?command=changepassword">Change Password</a></td>
    </tr>
  </tbody>
</table>
<table align="center">
  <tbody>
    <tr>
      <td id="txbxLoginMsg1" style="font-family: sans-serif; font-size: 14; color: red">
      <% if( (status != null) && (status.equalsIgnoreCase("failed"))) {
         if(message != null) { 
           out.println(message);  
         }
         else { 
           out.println("You are not authorized to access this application.");
         } }%>
         </td>
      <td id="txbxLoginMsg2" style="font-family: sans-serif; font-size: 14; color: blue"></td>
    </tr>
  </tbody>
</table>
</form>
<% }%>
</body>
</html>