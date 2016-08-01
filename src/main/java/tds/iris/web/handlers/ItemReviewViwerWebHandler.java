package tds.iris.web.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.IContentHelper;
import tds.iris.data.ItemRendererResponse;
import tds.iris.web.data.ContentRequest;
import tds.iris.web.data.ContentRequestItem;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.ItemRenderGroup;
import tds.itemrenderer.webcontrols.PageSettings.UniqueIdType;
import tds.student.web.handlers.BaseContentRendererController;

@Scope("prototype")
@Controller
public class ItemReviewViwerWebHandler extends BaseContentRendererController {
	private static final Logger _logger = LoggerFactory.getLogger(IrisWebHandler.class);
	private static List<ContentRequestItem> _items;
	private static final String ITEM_PREFIX = "I-";
	@Autowired
	private IContentHelper _contentHelper;

	@PostConstruct
	public void init() {
		setPageSettingsUniqieIdType(UniqueIdType.GroupId);
	}

	@RequestMapping(value = "itemRenderer/item/{bankID},{itemID}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ItemRendererResponse GetItemById(HttpServletResponse response,@PathVariable("bankID") String bankID, @PathVariable("itemID") String itemID)
			throws Exception {

		MockHttpServletResponse mockResponse = new MockHttpServletResponse();

		if(itemID!=null && !itemID.isEmpty()){
			if(bankID==null || bankID.isEmpty()){
				bankID="187";
			}
		ContentRequest contentRequest = getContentRequest(bankID,itemID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), mockResponse);
		}
		
		String content = mockResponse.getContentAsString(); 
		
		return new ItemRendererResponse(content);
	}
	
	@RequestMapping(value = "view/stimuli/{stimuliID}", produces = "application/json")
	@ResponseBody
	public void GetStimuliById(HttpServletResponse response, @PathVariable("stimuliID") String stimuliID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(stimuliID, stimuliID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}
	@RequestMapping(value = "versions/item/{itemID}", produces = "application/json")
	@ResponseBody
	public void GetItemVersionsById(HttpServletResponse response, @PathVariable("itemID") String itemID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(itemID, itemID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}
	@RequestMapping(value = "versions/stimuli/{stimuliID}", produces = "application/json")
	@ResponseBody
	public void GetStimuliVersionsById(HttpServletResponse response, @PathVariable("stimuliID") String stimuliID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(stimuliID, stimuliID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}


	private ContentRequest getContentRequest(String bankID,String itemID) throws ContentRequestException {
		try {
			ContentRequestItem cre = new ContentRequestItem();
			cre.setId(ITEM_PREFIX +bankID+"-"+ itemID);
			_items = new ArrayList<ContentRequestItem>();
			_items.add(cre);
			ContentRequest cr = new ContentRequest();
			cr.setItems(_items);
			return cr;
		} catch (Exception exp) {
			_logger.error("Error Occurred while building ContentRequest from itemID:" + itemID, exp);
			throw new ContentRequestException("Error deserializing ContentRequest from JSON. " + exp.getMessage());
		}
	}
}
