<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Item: ${item}</title>
    <script src="${pageContext.request.contextPath}/Scripts/Libraries/jquery/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/Scripts/Utilities/util_xdm.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/Scripts/iris.client.js" type="text/javascript"></script>
    <script src="../Assets/scripts/mapAccoms.js" type="text/javascript"></script>



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


            parent.window.addEventListener("acc-update", function(e) {
                var newAccoms = decodeURIComponent(e.detail).split(";");
                var accomsTemp = mapAccoms();
                var accomsArr = [];
                var newToken = JSON.parse(token);
                var updatedToken;
                for(var i = 0; i < newAccoms.length; i++) {
                    if (newAccoms[i] === "" || newAccoms[i] === "DISABLED")
                    {
                        newAccoms.splice(i, 1);
                    }
                    accomsArr.push(accomsTemp.get(newAccoms[i]));
                }
                while(newToken.accommodations.length != 0) {
                    newToken.accommodations.pop();
                }

                for(var i = 0; i < newAccoms.length; i++) {
                    if(newAccoms[i].includes('&')) {
                        var temp = newAccoms[i].split("&");

                        newToken.accommodations.push({type: accomsArr[i], codes: temp});
                    }
                    else {
                        newToken.accommodations.push({type: accomsArr[i], codes: [newAccoms[i]]});
                    }
                }

                updatedToken = JSON.stringify(newToken);
                IRiS.loadToken(vendorId, updatedToken, readOnly, scrollToDivId);
            }, true);
        }

        //handles the event where the score button is clicked.
        function onScoreClicked(){
            //raises an event to be caught on the react side requesting info about the item.
            var getItemDetails = new CustomEvent('itemViewer:Response', {bubbles: true, cancelable: false});
            window.parent.document.dispatchEvent(getItemDetails);
        }

        //handles the close button click for the score resutls.
        function closeScores(){
            /*var toClose =*/ $(".scoreResult").addClass("hidden");
            /*for(var i = 0; i < toClose.length; i++){
             toClose[i].classList.add("hidden");
             }*/
        }

        //handles the event that the react side of the application sends back with the item info.
        //It then makes a scoring request and displays the results.
        function score(data){
            closeScores();
            IRiS.getResponse().done(function(xmlResponse) {
                const id = data.detail.id;
                const version = data.detail.version != 'undefined' ? '?version=' + data.detail.version : '';
                if(xmlResponse) {
                    $.ajax({
                        type: 'POST',
                        url: '/item/score/' + id + version,
                        data: xmlResponse,
                        success: function(data) {
                            if(data.points > 0){
                                $("#correctPoints").innerHTML = data.points.toString() + " points";
                                $("#correct").removeClass('hidden');
                            } else if (data.points === -9){
                                $("#correctText").innerHTML += "This item cannot automatically be scored.";
                                $("#correct").removeClass('hidden');
                            } else {
                                $("#incorrect").removeClass('hidden');
                            }
                        },
                        error: function(err){
                            $("#serverError").removeClass('hidden');
                            console.log(err);
                        },
                        dataType: "json",
                        contentType: "application/json; charset=utf-8"
                    });
                } else {
                    $("#unansweredText").innerHTML = "Please answer the question.";
                    $("#unanswered").removeClass('hidden');
                }
            });
        }

        //sets up event listeners for Iris setup and scoring.
        function irisSetup(){
            window.Util.XDM.addListener('IRiS:Ready', loadItem);
            window.parent.addEventListener('itemViewer:Score', score);
            loadItem();
        }

    </script>
    <script>

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

        #irisContainer {
            height: 100%;
            overflow: auto;
            -webkit-overflow-scrolling: touch;
            width: 100%;
        }

        #scoringContainer {
            margin-bottom: 10%;
            padding: 1%;
            position: relative;
            width: 100%;
        }

        #score {
            background-color: #499D47;
            border-color: #428D40;
            border-radius: 5px;
            border-style: solid;
            color: white;
            font-size: 14px;
            float: right;
            line-height: 1.57;
            padding: 6.72 13.44px;

        }

        #correct {
            background-color: #E0F0D9;
            color: #3F7635;
        }

        #incorrect {
            background-color: #F1DEDE;
            color: #B65158;
        }

        #unanswered {
            background-color: #FFD980;
            color: #D08000;
        }

        #serverError {
            background-color: red;
            color: white;
        }

        .scoreResult {
            border-radius: 5px;
            display: inline-block;
            font: 400 13.33px Arial;
            float: left;
            max-width: 80%;
            padding: 2%;
            position: absolute;
            width: 80%;
        }

        .close-btn {
            background: transparent;
            border: none;
            color: inherit;
            font-size: 20px;
            float: right;
            line-height: 50%;
        }

        .close-btn:after {
            content: "\00d7";
        }

        .content {
            display: inline;
        }

        .hidden {
            visibility: hidden;
        }

    </style>
</head>
<body>
<div id="irisContainer">
    <iframe id="irisWindow" src="${pageContext.request.contextPath}/IrisPages/itemRender.xhtml" onload="irisSetup()"></iframe>
    <div id="scoringContainer">
        <button id="score" class="btn btn-primary" onClick="onScoreClicked()">Score</button>
        <div class="scoreResult hidden" id="correct">
            <p id="correctText" class="content"><b>Congrats!</b> Your answer is correct. <span id="correctPoints"></span></p>
            <button class="close-btn" onClick="closeScores()"/>
        </div>
        <div class="scoreResult hidden" id="incorrect">
            <p id="incorrectText" class="content"><b>Sorry!</b> Your answer is incorrect.</p>
            <button class="close-btn" onClick="closeScores()"/>
        </div>
        <div class="scoreResult hidden" id="unanswered">
            <p id="unansweredText" class="content">This item cannot be scored.</p>
            <button class="close-btn" onClick="closeScores()"/>
        </div>
        <div class="scoreResult hidden" id="serverError">
            <p id="serverErrorText" class="content">There was an error with the scoring server.</p>
            <button class="close-btn" onClick="closeScores()"/>
        </div>
    </div>
</div>
</body>
</html>
