/*
 This code implements the XDM API for use within item preview app.
 */

(function (XDM, CM) {

    // we load one page in advance, but we don't want that to cause a cascade of page show/load
    Blackbox.getConfig().preventShowOnLoad = true;
    //Adding this onto TDS for now so it is available in the dictionary handler.
    var irisUrl = location.href;
    var buttonsLoaded = false;
    CM.accessibilityEnabled = true;
    // Functions that are used by toolbar buttons

    //Calculator
    var calculatorBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Calculator.toggle();
        }
    };

    //Global Notes
    var globalNotesBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage && TDS.Notes) {
            TDS.Notes.open();
        }
    };

    //Masking
    var showMask = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Masking.toggle();
        }
    };

    var dictionaryBtn = function(ev) {
        var currentPage = ContentManager.getCurrentPage();
        if (currentPage) {
            Dictionary.toggle();
        }
    };


    // setup cross domain api
    XDM.init(window);



    function getItemId(item) {
        return "I-" + item.bankKey + "-" + item.itemKey;
    }

    function getItemMap(requestedItems) {
        var distinctItemCount = 0;

        var itemMap = requestedItems.reduce(function (map, item) {
            ++distinctItemCount;
            map[getItemId(item)] = item;
            return map;
        }, {});

        if (requestedItems.length !== distinctItemCount) {
            throw new Error('One or more of the requested items appears multiple times in this request.');
        }

        return itemMap;
    }

    function getExistingPage(requestedItems) {

        var requestedItemCount = Object.keys(requestedItems).length,
            partialMatches = false,
            matchedPage = null,
            matchedItems = null;

        // go through each page to try matching items
        CM.getPages().forEach(function (page) {
            var items = page.getItems(),
                matches = [];

            // check this page for items which are in the current content request
            items.forEach(function (item) {
                var itemId = getItemId(item),
                    matchedItem = requestedItems[itemId];

                if (matchedItem) {
                    matches.push({
                        loaded: item,
                        requested: matchedItem
                    });
                }
            });

            if (matches.length === items.length && items.length === requestedItemCount) {
                // exact match, save the page and items
                matchedPage = page;
                matchedItems = matches;
            } else if (matches.length) {
                // only some items matched
                partialMatches = true;
            }
        });

        if (partialMatches) {
            throw new Error('One or more of the items requested have already been loaded. Make sure the content request is the same as the orginal (e.g. it can\'t contain different response or label values).');
        }

        return {
            page: matchedPage,
            itemPairs: matchedItems
        };
    }

    function loadContent(xmlDoc) {
        if (typeof xmlDoc == 'string') {
            xmlDoc = Util.Xml.parseFromString(xmlDoc);
        }

        // create array of content json from the xml
        var deferred = $.Deferred();
        var contents = CM.Xml.create(xmlDoc);
        var content = contents[0];

        var itemMap = getItemMap(content.items);
        var result = getExistingPage(itemMap);

        //if the page is already loaded we want to force a reload because the accommodations may have changed.
        if (result.page) {
            // show the page
            TDS.Dialog.hideProgress();
            ContentManager.removePage(result.page);
            // If there is a word list loaded clear the cached words because they may have changed.
            if(window.WordListPanel){
                window.WordListPanel.clearCache();
            }
        }

        page = CM.createPage(content);

        page.render();
        page.once('loaded', function () {
            TDS.Dialog.hideProgress();
            CM.accessibilityEnabled = true;
            page.show();
            CM.accessibilityEnabled = false;
            deferred.resolve();
        });

        if(!buttonsLoaded) {
            Blackbox.showButton('btnMask', showMask, true);
            Blackbox.showButton('btnCalculator', calculatorBtn, true);
            Blackbox.showButton('btnGlobalNotes', globalNotesBtn, true);
            buttonsLoaded = true;
        }
        if (TDS.getAccommodationProperties().getDictionary()) {
            Blackbox.showButton('btnDictionary', dictionaryBtn, true);
        }
        /*
        If the print size is specified we need to set it because the previous
        If not set it to zero because this may not be the first item we are loading and the zoom level
        may have been set when we loaded an item earlier.
        */
        var printSize = CM.getAccProps().getPrintSize();
        if(printSize) {
            CM.getZoom().setLevel(printSize, true);
        } else {
            CM.getZoom().setLevel(0, true);
        }

        return deferred.promise();
    }

    //function that is passed to Blackbox.changeAccommodations to modify the accommodations
    //in our case we just want to clear out any accommodations that are set.
    function clearAccommodations(accoms) {
        accoms.clear()
    }

    //parses any accommodations from the token, and sets them on the Blackbox.
    function setAccommodations(token) {
        var parsed = JSON.parse(token);
        //Call changeAccommodations once to reset all accommodations to their default values
        Blackbox.changeAccommodations(clearAccommodations);
        if(parsed.hasOwnProperty('accommodations')) {
            Blackbox.setAccommodations(parsed['accommodations']);
            //Call changeAccommodations a second time to apply the new accommodations that were set
            //by setAccommodations
            Blackbox.changeAccommodations(function(accoms){})
        }
    }

    function loadToken(vendorId, token) {
        Messages.set('TDS.WordList.illustration', 'Illustration', 'ENU');
        TDS.Dialog.showProgress();
        var url = irisUrl + '/Pages/API/content/load?id=' + vendorId;
        setAccommodations(token);
        return $.post(url, token, null, 'text').then(function (data) {
            return loadContent(data);
        }, function (data) {
            window.alert("Unable to load item.\n" +
                "Please make sure you entered the correct bank and item numbers.")

        });
    }

    
    /*
    	Merged back functions needed for scoring
    */
    function loadGroupedContentToken(vendorId, token) {
        TDS.Dialog.showProgress();
        var url = location.href + '/Pages/API/content/loadContent?id=' + vendorId;
        setAccommodations(token);
        return $.post(url, token, null, 'text').then(function (data) {
            return loadGroupedContent(data);
        });
    }

    function setItemResponse(item, response) {
        if (item && item instanceof ContentItem) {
            item.setResponse(response);
        } else {
            throw new Error('invalid item; could not set response');
        }
    }

    function setItemLabel(item, label) {
        if (item && item instanceof ContentItem) {
            item.setQuestionLabel(label);
        } else {
            throw new Error('invalid item; could not set label');
        }
    }

    function setResponse(value) {
        var entity = CM.getCurrentPage().getActiveEntity();
        //Begin Hack: 1327 remove <p> from response with prev/next button
        while(value.search("&amp;")!=-1) // '&' comes as '&amp;amp;' in response
        	value = value.replace("&amp;", "&"); 
		value = $('<div/>').html(value).text();
		while(value.search("<p>")!=-1)
			value = value.replace ("<p>", "");
		while(value.search("</p>")!=-1)
			value = value.replace ("</p>", "</br>");
		//End Hack
        setItemResponse(entity, value);
    }

    function setResponses(itemResponses) {
        var items = CM.getCurrentPage().getItems();
        itemResponses.forEach(function (itemResponse) {
            var itemFromPosition, itemFromId;
            if (typeof itemResponse.position === 'number') {
                itemFromPosition = items[itemResponse.position - 1];
            }

            if (itemResponse.id) {
                itemFromId = items.filter(function (item) {
                    var itemId = getItemId(item);
                    return itemId === itemResponse.id;
                })[0];
            }

            if (itemFromPosition && itemFromId && itemFromPosition !== itemFromId) {
                throw new Error('item position and id do not match');
            }

            setItemResponse(itemFromPosition || itemFromId, itemResponse.response);

            if (itemResponse.label) {
                setItemLabel(itemFromPosition || itemFromId, itemResponse.label);
            }
        });
    }

    function getResponse() {
        var entity = CM.getCurrentPage().getActiveEntity();
        if (entity instanceof ContentItem) {
            return entity.getResponse().value;
        }
        return null;
    }

    function getIndexOfCurrentPage() {
        var currentPage = CM.getCurrentPage(),
            pages = CM.getPages(),
            index = pages.indexOf(currentPage);
    }

    function getNavigability() {
        var n = {
            pages: null,
            index: 0,

            haveNextPage: false,
            haveNextPaginatedItem: false,

            havePrevPage: false,
            havePrevPaginatedItem: false,

            update: function () {
                this.haveNextPage = false;
                this.haveNextPaginatedItem = false;
                this.havePrevPage = false;
                this.havePrevPaginatedItem = false;

                var currentPage = CM.getCurrentPage();

                this.pages = this.pages || CM.getPages(),
                this.index = this.pages.indexOf(currentPage);

                this.haveNextPage = this.index < this.pages.length - 1;
                this.havePrevPage = this.index > 0;

                var pagination = currentPage.plugins.get('pagination');

                if (pagination) {
                    this.haveNextPaginatedItem = pagination.haveNext();
                    this.havePrevPaginatedItem = pagination.havePrev();
                }
            }
        };

        n.update();

        return n;
    }

    function go(direction) {
        var n = getNavigability();

        // too easy to take wrong branch if we use terse logic here,
        // so we'll just use the clear version

        if (direction === 'next' && n.haveNextPaginatedItem) {
            CM.requestNextPage()
        }
        else if (direction === 'prev' && n.havePrevPaginatedItem) {
            CM.requestPreviousPage();
        }
        else if (direction === 'next' && n.haveNextPage) {
            n.pages[++n.index].show();
        }
        else if (direction === 'prev' && n.havePrevPage) {
            n.pages[--n.index].show();
        }

        n.update();
        sendNavUpdate(n);
    }

    function showNext() {
        go('next');
    }

    function showPrev() {
        go('prev');
    }

    function sendNavUpdate(navigability) {
        var n = navigability;
        XDM(window.parent).post('IRiS:navUpdate',
            n.havePrevPage || n.havePrevPaginatedItem,
            n.haveNextPage || n.haveNextPaginatedItem
        );
    }

    XDM.addListener('IRiS:loadToken', loadToken);
    XDM.addListener('IRiS:loadContent', loadGroupedContentToken);
    XDM.addListener('IRiS:getResponse', getResponse);
    XDM.addListener('IRiS:setResponse', setResponse);
    XDM.addListener('IRiS:setResponses', setResponses);

    XDM.addListener('IRiS:showNext', showNext);
    XDM.addListener('IRiS:showPrev', showPrev);
    
    
    
    
    
    
    

    Blackbox.events.on('ready', function () {
        XDM(window.parent).post('IRiS:ready');
    });

})(window.Util.XDM, window.ContentManager);