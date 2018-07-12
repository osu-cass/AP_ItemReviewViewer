<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Item: ${item}</title>
    <script src="${pageContext.request.contextPath}/Scripts/Libraries/jquery/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/Scripts/Utilities/util_xdm.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/Scripts/iris.client.js" type="text/javascript"></script>
    <!-- Styling for this page only and not for IRiS interface. -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/IrisStyles/iris.css">

    <script type="text/javascript">
        function loadItem(){
            IRiS.setFrame(frames[0]);
            // set the vendor guid.
            //Note: in the OSS IRiS case we do not care for this.
            var vendorId = '2B3C34BF-064C-462A-93EA-41E9E3EB8333';
            var token = '${token}';
            var scrollToDivId = '${scrollToDivId}';
            var readOnly = ${readOnly};
            IRiS.loadToken(vendorId, token, readOnly, scrollToDivId);
        }

        function score(data){
            IRiS.getResponse().done(function(xmlResponse) {
                var id = data.detail.id;
                var version = data.detail.version != 'undefined' ? '?version=' + data.detail.version : '';
                var scoreResponse = new CustomEvent('itemViewer:Response', { bubbles: true, cancelable: false, detail: {score: null, error: false}});
                var otherResponse = new Event('TestEvent', { bubbles: true, cancelable: false });
                window.parent.document.dispatchEvent(otherResponse);
                if(xmlResponse) {
                    $.ajax({
                        type: 'POST',
                        url: '/item/score/' + id + version,
                        data: xmlResponse,
                        success: function(jsonResponse) {
                            scoreResponse.detail.score = jsonResponse.points;

                           // if(jsonResponse.points > 0) {
                            //}else if(jsonResponse.points === -9) {
                             //   $('#noPoints').hidden = false;
                             //   $('#incorrectAlert').hidden = true;
                             //   $('#noPoints').innerHTML = 'blah';
                            //} else{
                            //    $('#correctAlert').hidden = true;
                            //    $('#incorrectAlert').hidden = false;
                            //    $('#noPoints').hidden = true;
                            //    $('#incorrectAlert').innerHTML = 'blah';
                           // }
                        },
                        dataType: "json",
                        contentType: "application/json; charset=utf-8"
                    }).fail(function(err){
                        console.log(err);
                        scoreResponse.detail.error = true;
                    });
                    window.parent.document.dispatchEvent(scoreResponse);

                }else{
                    alert("You must answer the question to receive a score.");
                }
            });
        }

        function irisSetup(){
            window.Util.XDM.addListener('IRiS:ready', loadItem);
            window.parent.addEventListener('itemViewer:Score', score);
            loadItem();
        }
    </script>
    <style>
        body {
            margin: 0;
        }
        iframe {
            border: none;
            height: 100%;
            width: 100%;
        }
        .irisContainer {
            overflow: auto;
            -webkit-overflow-scrolling: touch;
            height: 100%;
            width: 100%;
        }
    </style>
</head>
<body>
<div class="irisContainer">
    <iframe id="irisWindow" src="${pageContext.request.contextPath}/IrisPages/itemRender.xhtml" onload="irisSetup()"></iframe>
</div>
</body>
</html>
