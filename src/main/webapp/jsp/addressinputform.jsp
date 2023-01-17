<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title></title>
    <meta charset="utf-8" />
	<script type='text/javascript'>
    function GetMap() {
        Microsoft.Maps.loadModule('Microsoft.Maps.AutoSuggest', {
            callback: function () {
                var viewRect = Microsoft.Maps.LocationRect.fromCorners(new Microsoft.Maps.Location(50.585184, 30.288197), // african ocean
                            new Microsoft.Maps.Location(50.207098, 30.738492));
                var manager = new Microsoft.Maps.AutosuggestManager({
                    placeSuggestions: false,
                    countryCode: 'UA',
                    bounds: viewRect
                    });
                manager.attachAutosuggest('#searchBox', '#searchBoxContainer', selectedSuggestion);
            },
            errorCallback: function(msg){
                alert(msg);
            },
            credentials: "${msKey}"
        });
    }

    function selectedSuggestion(result) {
        //Populate the address textbox values.
        document.getElementById('addressLineTbx').value = result.address.addressLine || '';
        document.getElementById('cityTbx').value = result.address.locality || '';
        document.getElementById('countyTbx').value = result.address.district || '';
        document.getElementById('stateTbx').value = result.address.adminDistrict || '';
        document.getElementById('postalCodeTbx').value = result.address.postalCode || '';
        document.getElementById('countryTbx').value = result.address.countryRegion || '';
        document.getElementById('formattedAddressTbx').value = result.address.formattedAddress || '';
    }
    </script>
    <style>
        #searchBox {
            width: 400px;
        }

        .addressForm {
            margin-top:10px;
            background-color: #008272;
            color: #fff;
            border-radius:10px;
            padding: 10px;
        }

        .addressForm input{
            width:265px;
        }
    </style>
    <script type='text/javascript' src='http://www.bing.com/api/maps/mapcontrol?callback=GetMap' async defer></script>
</head>
<body>
    <div id='searchBoxContainer'>
        <input type='text' id='searchBox'/>
    </div>

    <table class="addressForm">
        <tr><td>Street Address:</td><td><input type="text" id="addressLineTbx"/></td></tr>
        <tr><td>City:</td><td><input type="text" id="cityTbx"/></td></tr>
        <tr><td>County:</td><td><input type="text" id="countyTbx"/></td></tr>
        <tr><td>State:</td><td><input type="text" id="stateTbx"/></td></tr>
        <tr><td>Zip Code:</td><td><input type="text" id="postalCodeTbx"/></td></tr>
        <tr><td>Country:</td><td><input type="text" id="countryTbx"/></td></tr>
        <tr><td>Formatted Address:</td><td><input type="text" id="formattedAddressTbx"/></td></tr>
    </table>
</body>
</html>