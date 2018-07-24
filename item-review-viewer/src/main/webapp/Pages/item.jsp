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
        //loads the item using IRiS.
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

        //handles the event where the score button is clicked.
        function onScoreClicked(){
            //raises an event to be caught on the react side requesting info about the item.
            var getItemDetails = new CustomEvent('itemViewer:Response', {bubbles: true, cancelable: false});
            window.parent.document.dispatchEvent(getItemDetails);
        }

        //handles the close button click for the score resutls.
        function close(){
            var toClose = document.getElementsByClassName("scoreResult");
            console.log(toClose);
            toClose.foreach(e.classList.add("hidden"));
            document.getElementById('score').classList.remove('hidden');
        }

        //handles the event that the react side of the application sends back with the item info.
        //It then makes a scoring request and displays the results.
        function score(data){
            IRiS.getResponse().done(function(xmlResponse) {
                const id = data.detail.id;
                const version = data.detail.version != 'undefined' ? '?version=' + data.detail.version : '';
                if(xmlResponse) {
                    console.log("ID:"+ id + version);
                    $.ajax({
                        type: 'POST',
                        url: '/item/score/' + id + version,
                        data: xmlResponse,
                        success: function(data) {
                            console.log("Response From Server:" + data['points']);
                            if(data.points > 0){
                                var correctElm = document.getElementById("correct");
                                console.log(correctElm);
                                document.getElementById("correctPoints").innerHTML = data.points.toString();
                                document.getElementById("score").classList.add('hidden');
                                correctElm.classList.remove("hidden");
                            } else if (data.points < 0){
                                document.getElementById("errorText").innerHTML = "This item cannot be scored.";
                                document.getElementById("error").classList.remove('hidden');
                                document.getElementById("score").classList.add('hidden');
                            } else {
                                document.getElementById("incorrect").classList.remove('hidden');
                                document.getElementById("score").classList.add('hidden');
                            }
                        },
                        error: function(err){
                            console.log(err);
                        },
                        dataType: "json",
                        contentType: "application/json; charset=utf-8"
                    });
                } else {
                    document.getElementById("errorText").innerHTML = "Please answer the question.";
                    document.getElementById("error").classList.remove('hidden');
                    document.getElementById("score").classList.add("hidden");
                }
            });
        }

        //sets up event listeners for Iris setup and scoring.
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
        #score {
            align: right;
            border-radius: 5px;
            color: white;
            background-color: #00ABF4;
        }

        .hidden {
            visibility: hidden;
        }

        .scoreResult {
            border-radius: 5px;
            align: left;
        }

        .correct {
            color: white;
            background-color: forestgreen;
        }

        .incorrect {
            color: white;
            background-color: red;
        }
    </style>
</head>
<body>
<div class="irisContainer">
    <iframe id="irisWindow" src="${pageContext.request.contextPath}/IrisPages/itemRender.xhtml" onload="irisSetup()"></iframe>
    <button id="score" onclick="onScoreClicked();">Score</button>
    <div class="scoreResult correct hidden" id="correct">
        <p id="correctText">Correct! you scored <span id="correctPoints"></span> points</p>
        <button id="close-correct" onclick="close()">X</button>
    </div>
    <div class="scoreResult incorrect hidden" id="incorrect"><p id="incorrectText">That is incorrect.</p><button id="close-incorrect" onclick="close();">X</button></div>
    <div class="scoreResult incorrect hidden" id="error"><p id="errorText">This item cannot be scored.</p><button id="close-error" onclick="close();">X</button></div>
</div>
</body>
</html>
