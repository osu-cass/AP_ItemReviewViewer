/*******************************************************************************
 * Educational Online Test Delivery System Copyright (c) 2014 American
 * Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0 See accompanying
 * file AIR-License-1_0.txt or at http://www.smarterapp.org/documents/
 * American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
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
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import AIR.Common.Json.JsonHelper;
import AIR.Common.Web.ContentType;
import AIR.Common.Web.TDSReplyCode;
import AIR.Common.data.ResponseData;
import tds.iris.abstractions.repository.ContentException;
import tds.iris.abstractions.repository.IContentHelper;
import tds.iris.web.data.ContentRequest;
import tds.iris.web.data.ContentRequestItem;
import tds.iris.web.data.ContentRequestPassage;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.AccProperties;
import tds.itemrenderer.data.ItemRenderGroup;
import tds.itemrenderer.web.ITSDocumentXmlSerializable;
import tds.itemrenderer.web.XmlWriter;
import tds.itemrenderer.webcontrols.ErrorCategories;
import tds.itemrenderer.webcontrols.PageLayout;
import tds.itemrenderer.webcontrols.PageSettings.UniqueIdType;
import tds.itemrenderer.webcontrols.rendererservlet.ContentRenderingException;
import tds.itemrenderer.webcontrols.rendererservlet.RendererServlet;
import tds.blackbox.ContentRequestException;
import tds.student.web.handlers.BaseContentRendererController;

@Scope ("prototype")
@Controller
public class IrisWebHandler extends BaseContentRendererController
{
  private static final Logger _logger = LoggerFactory.getLogger (IrisWebHandler.class);
  private static List<ContentRequestItem> _items;

  @Autowired
  private IContentHelper      _contentHelper;

  @PostConstruct
  public void init () {
    setPageSettingsUniqieIdType (UniqueIdType.GroupId);
  }

  @RequestMapping (value = "content/load", produces = "application/xml")
  @ResponseBody
  public void loadContentRequest (HttpServletRequest request, HttpServletResponse response) throws Exception {

    ContentRequest contentRequest = getContentRequest (modifyPostData (request));
    ItemRenderGroup itemRenderGroup = _contentHelper.loadRenderGroup (contentRequest);

   
    if (!StringUtils.isEmpty (contentRequest.getLayout ()))
      itemRenderGroup.setLayout (contentRequest.getLayout ());

    renderGroup (itemRenderGroup, new AccLookup (), response);
  }

  @RequestMapping (value = "content/reload")
  @ResponseBody
  public ResponseData<String> reloadContent (HttpServletRequest request, HttpServletResponse response) throws ContentRequestException, IOException {
    _contentHelper.reloadContent ();
    return new ResponseData<String> (TDSReplyCode.OK.ordinal (), "Reload succeeded.", "");
  }

  @ExceptionHandler ({ ContentException.class })
  @ResponseBody
  public ResponseData<String> handleContentException (ContentException excp, HttpServletResponse response) {
    _logger.error (excp.getMessage (), excp);
    response.setStatus (HttpStatus.INTERNAL_SERVER_ERROR_500);
    return new ResponseData<String> (TDSReplyCode.Error.getCode (), excp.getMessage (), "");
  }

  InputStream modifyPostData (HttpServletRequest request) throws IOException, Exception
  {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader (new InputStreamReader (request.getInputStream ()));
    } catch (IOException e) {
      _logger.error (e.getMessage (), e);
      throw new IOException ("Error while reading request.", e);
    }
    String line = null;
    StringBuilder builder = new StringBuilder ();
    try {
      while ((line = bufferedReader.readLine ()) != null) {
        builder.append (line);
      }
    } catch (IOException e1) {
      _logger.error (e1.getMessage (), e1);
      throw new IOException ("Error while reading request buffer.", e1);
    }
    String postData = builder.toString ();

    // if student input '&' comes up as &amp;amp;
    postData = postData.replace ("&amp;", "&");

    // escaping double quote if it comes encoded
    int index = -1;
    /*
     * while ((index = postData.indexOf ("&quot;")) != -1 && ((index - 1 >= 0 &&
     * postData.charAt (index - 1) != '\\') || index == 0)) { postData =
     * postData.substring (0, index) + "\\" + postData.substring (index); }
     */

    // Begin: Handling '\'
    StringBuilder str = new StringBuilder ();
    String specialChars = "\\\"";
    while (postData.contains ("\\"))
    {
      index = postData.indexOf ("\\");
      if (index + 1 < postData.length ())
      {
        if (!specialChars.contains ("" + postData.charAt (index + 1)) || (index + 2 < postData.length () && postData.charAt (index + 1) == '"' && postData.charAt (index + 2) == ','))
        {
          str.append (postData.substring (0, index + 1) + "\\");
          postData = postData.substring (index + 1);
        }
        else
        {
          str.append (postData.substring (0, index + 2));
          postData = postData.substring (index + 2);
        }
      }

    }
    str.append (postData);
    postData = str.toString ();
    // End: Handling '\'

    // Handling double quot
    postData = postData.replace ("&quot;", "\\\"");
    // Begin: Decoding html entities and handling '<p></p>' in response
    postData = StringEscapeUtils.unescapeHtml (postData);
    postData = postData.replace ("<p>", "");
    postData = postData.replace ("</p>", "</br>");
    // End: Handling '<p></p>' in response
    
    return new ByteArrayInputStream (postData.getBytes ());
  }
 // creating first pull request 
private  ContentRequest getContentRequest (InputStream inputStream) throws ContentRequestException {
try {
  BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
  String line = null;
  StringBuilder builder = new StringBuilder ();
  while ((line = bufferedReader.readLine ()) != null) {
    builder.append (line);
  }
  ContentRequestItem cre = new ContentRequestItem();
  cre.setId(builder.toString());
  _items= new ArrayList<ContentRequestItem>();
  _items.add( cre);
  ContentRequest cr = new ContentRequest();
  cr.setItems(_items);
  return cr;
//  return JsonHelper.deserialize (builder.toString (), ContentRequest.class);
} catch (Exception exp) {
  _logger.error ("Error deserializing ContentRequest from JSON", exp);
  throw new ContentRequestException ("Error deserializing ContentRequest from JSON. " + exp.getMessage ());
}
}

}