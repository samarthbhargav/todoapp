<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>TODO App</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/styles.css" rel="stylesheet">

    <script>
    var createElement = (function() {
	    // Detect IE using conditional compilation
	    if (/*@cc_on @*//*@if (@_win32)!/*@end @*/false) {
	        // Translations for attribute names which IE would otherwise choke on
	        var attrTranslations = {
	            "class": "className",
	            "for": "htmlFor"
	        };
	
	        var setAttribute = function(element, attr, value) {
	            if (attrTranslations.hasOwnProperty(attr)) {
	                element[attrTranslations[attr]] = value;
	            } else if (attr == "style") {
	                element.style.cssText = value;
	            } else {
	                element.setAttribute(attr, value);
	            }
	        };
	
	        return function(tagName, attributes) {
	            attributes = attributes || {};
	
	            // See http://channel9.msdn.com/Wiki/InternetExplorerProgrammingBugs
	            if (attributes.hasOwnProperty("name") ||
	                attributes.hasOwnProperty("checked") ||
	                attributes.hasOwnProperty("multiple")) {
	                var tagParts = ["<" + tagName];
	                if (attributes.hasOwnProperty("name")) {
	                    tagParts[tagParts.length] =
	                        ' name="' + attributes.name + '"';
	                    delete attributes.name;
	                } if (attributes.hasOwnProperty("checked") &&
	                    "" + attributes.checked == "true") {
	                    tagParts[tagParts.length] = " checked";
	                    delete attributes.checked;
	                }
	                if (attributes.hasOwnProperty("multiple") &&
	                    "" + attributes.multiple == "true") {
	                    tagParts[tagParts.length] = " multiple";
	                    delete attributes.multiple;
	                }
	                tagParts[tagParts.length] = ">";
	
	                var element = document.createElement(tagParts.join(""));
	            } else {
	                var element = document.createElement(tagName);
	            }
	
	            for (var attr in attributes) {
	                if (attributes.hasOwnProperty(attr)) {
	                    setAttribute(element, attr, attributes[attr]);
	                }
	            }
	
	            return element;
	        };
	    } else {
	        return function(tagName, attributes) {
	            attributes = attributes || {};
	            var element = document.createElement(tagName); 
	            for (var attr in attributes) {
	                if (attributes.hasOwnProperty(attr)) {
	                    element.setAttribute(attr, attributes[attr]);
	                }
	            }
	            return element;
	        };
	    }
	})();
    function postToURL(url) {    	
    	var form = createElement("form", {action: url,
                                      method: "POST",
                                      style: "display: none"});
		form.submit();
    }
    </script>
  </head>

  <body>

    <nav class="navbar navbar-inverse">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">TODO App</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
          	<#if username??>
            	<li class="active"><a href="/home">Home</a></li>
            	<li><a href="/logout">Logout</a></li>
            </#if>            
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>