package tds.iris.web.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.IContentHelper;
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
	private static final String ITEM_PREFIX = "I-187-";
	@Autowired
	private IContentHelper _contentHelper;

	@PostConstruct
	public void init() {
		setPageSettingsUniqieIdType(UniqueIdType.GroupId);
	}

	@RequestMapping(value = "view/item/{itemID}", produces = "application/json")
	@ResponseBody
	public void GetItemById(HttpServletResponse response, @PathVariable("itemID") String itemID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(itemID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}
	
	@RequestMapping(value = "view/stimuli/{stimuliID}", produces = "application/json")
	@ResponseBody
	public void GetStimuliById(HttpServletResponse response, @PathVariable("stimuliID") String stimuliID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(stimuliID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}
	@RequestMapping(value = "versions/item/{itemID}", produces = "application/json")
	@ResponseBody
	public void GetItemVersionsById(HttpServletResponse response, @PathVariable("itemID") String itemID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(itemID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}
	@RequestMapping(value = "versions/stimuli/{stimuliID}", produces = "application/json")
	@ResponseBody
	public void GetStimuliVersionsById(HttpServletResponse response, @PathVariable("stimuliID") String stimuliID)
			throws Exception {
		ContentRequest contentRequest = getContentRequest(stimuliID);
		ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup(contentRequest);

		if (!StringUtils.isEmpty(contentRequest.getLayout()))
			itemRenderGroup.setLayout(contentRequest.getLayout());

		renderGroup(itemRenderGroup, new AccLookup(), response);
	}


	private ContentRequest getContentRequest(String itemID) throws ContentRequestException {
		try {
			ContentRequestItem cre = new ContentRequestItem();
			cre.setId(ITEM_PREFIX + itemID);
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
