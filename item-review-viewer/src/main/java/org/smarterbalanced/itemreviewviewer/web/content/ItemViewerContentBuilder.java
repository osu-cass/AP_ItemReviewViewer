//package org.smarterbalanced.itemreviewviewer.web.content;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Date;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.smarterbalanced.itemreviewviewer.web.services.SettingsReader;
//import org.springframework.context.annotation.Scope;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import tds.iris.abstractions.repository.ContentException;
//import tds.iris.abstractions.repository.IContentBuilder;
//import tds.itempreview.ConfigBuilder;
//import tds.itemrenderer.data.AccLookup;
//import tds.itemrenderer.data.IITSDocument;
//
//@Component
//@Scope("singleton")
//public class ItemViewerContentBuilder implements IContentBuilder {
//	private static final Logger _logger = LoggerFactory.getLogger(ItemViewerContentBuilder.class);
//
//	 private static String GIT_LAB_URL ;
//	private static String DESTINATION_ZIP_FILE_LOCATION;
//	private static final int BUFFER_SIZE = 4096;
//	 private static final String USER_AGENT = "User-Agent";
//	 private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0";
//	 private static final String REFERRER = "Referer";
//	 private static final String REFERRER_VALUE = "https://www.google.com";
//	private static final String FILE_EXTENTION = ".zip";
//	private String _contentPath;
//	private ConfigBuilder _directoryScanner = null;
//
//	public ItemViewerContentBuilder() {
//		init();
//	}
//
//
//	@Override
//	public IITSDocument getITSDocument(String id) throws ContentException {
//		try {
//
//			_logger.info("Getting the Item with the ID:" + id + " in location:" + DESTINATION_ZIP_FILE_LOCATION);
//			String[] parts = id.split("-");
//
//			String bankId = parts[1];
//			String itemNumber = parts[2];
//
//			String versionNumber = "";
//			if(parts.length > 3)
//				versionNumber = parts[3];
//
//			String qualifiedItemNumber = null;
//			String itemNumberFromMetaData = parts[0] + "-" + bankId + "-" + itemNumber;
//
//
//			if(StringUtils.isEmpty(versionNumber))
//				qualifiedItemNumber = parts[0] + "-" + bankId + "-" + itemNumber;
//			else
//				qualifiedItemNumber = parts[0] + "-" + bankId + "-" + itemNumber + "-" + versionNumber;
//
//			String temp = id.substring(6, id.length());
//			String zipFilePath = DESTINATION_ZIP_FILE_LOCATION + qualifiedItemNumber + FILE_EXTENTION;
//			if (!fileExists(zipFilePath)) {
//
//				 String url = "";
//				 if(StringUtils.isEmpty(versionNumber))
//					 url = populateUrl(itemNumber);
//				 else
//					 url = populateUrl(itemNumber, versionNumber);
//				 getItems(new URL(url), zipFilePath);
//			}
//			if (unzip(zipFilePath, DESTINATION_ZIP_FILE_LOCATION)) {
//				_logger.info("Scanning the Directory for the Item Started");
//				_directoryScanner.create();
//				_logger.info("Scanning the Directory for the Item Complete");
//				//return _directoryScanner.getRenderableDocument(id);
//
//				return _directoryScanner.getRenderableDocument(itemNumberFromMetaData);
//			}
//
//		} catch (MalformedURLException e) {
//			_logger.error("Error Getting Item File.Check the URL:" + DESTINATION_ZIP_FILE_LOCATION, e);
//			throw new ContentException(e);
//
//		} catch (Exception e) {
//			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
//			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id));
//
//		}
//		throw new ContentException(String.format("No content found by id %s", id));
//	}
//
//
//
//	@Override
//	public IITSDocument getITSDocumentAcc(String id, AccLookup accLookup) {
//		// TODO Auto-generated method stub
//		try {
//
//			_logger.info("Getting the Item with the ID:" + id + " in location:" + DESTINATION_ZIP_FILE_LOCATION);
//			String[] parts = id.split("-");
//
//			String bankId = parts[1];
//			String itemNumber = parts[2];
//
//			String versionNumber = "";
//			if(parts.length > 3)
//				versionNumber = parts[3];
//
//			String qualifiedItemNumber = null;
//			String itemNumberFromMetaData = parts[0] + "-" + bankId + "-" + itemNumber;
//
//
//			if(StringUtils.isEmpty(versionNumber))
//				qualifiedItemNumber = parts[0] + "-" + bankId + "-" + itemNumber;
//			else
//				qualifiedItemNumber = parts[0] + "-" + bankId + "-" + itemNumber + "-" + versionNumber;
//
//			String temp = id.substring(6, id.length());
//			String zipFilePath = DESTINATION_ZIP_FILE_LOCATION + qualifiedItemNumber + FILE_EXTENTION;
//			if (!fileExists(zipFilePath)) {
//
//				 String url = "";
//				 if(StringUtils.isEmpty(versionNumber))
//					 url = populateUrl(itemNumber);
//				 else
//					 url = populateUrl(itemNumber, versionNumber);
//				 getItems(new URL(url), zipFilePath);
//			}
//			if (unzip(zipFilePath, DESTINATION_ZIP_FILE_LOCATION)) {
//				_logger.info("Scanning the Directory for the Item Started");
//				_directoryScanner.create();
//				_logger.info("Scanning the Directory for the Item Complete");
//				//return _directoryScanner.getRenderableDocument(id);
//
//				return _directoryScanner.getRenderableDocument(itemNumberFromMetaData,accLookup);
//			}
//
//		} catch (MalformedURLException e) {
//			_logger.error("Error Getting Item File.Check the URL:" + DESTINATION_ZIP_FILE_LOCATION, e);
//			throw new ContentException(e);
//
//		} catch (Exception e) {
//			_logger.error("Un known Error while getting or loading  ITEM from GITLAB.", e);
//			throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s", id));
//
//		}
//		throw new ContentException(String.format("No content found by id %s", id));
//
//	}
//
//
//	 private String populateUrl(String itemNumber) {
//
//	 return GIT_LAB_URL+itemNumber;
//	 }
//
//	 private String populateUrl(String itemNumber, String versionNumber) {
//
//	 return GIT_LAB_URL+itemNumber + "/" + versionNumber;
//	 }
//
//
//	 private boolean fileExists(String zipFilePath) {
//
//		File f = new File(zipFilePath);
//		_logger.info("Checking the File Already Exists:" + f.exists());
//		return f.exists();
//	}
//
//	 private boolean getItems(URL fileURL, String fileSavePath) {
//	 _logger.info("Getting the Item with URL:"+fileURL+" with file name:"+fileSavePath+" Started");
//	 boolean isSucceed = true;
//	 CloseableHttpClient httpClient = HttpClients.createDefault();
//	 HttpGet httpGet = new HttpGet(fileURL.toString());
//	 httpGet.addHeader(USER_AGENT, USER_AGENT_VALUE);
//	 httpGet.addHeader(REFERRER, REFERRER_VALUE);
//	 try {
//	 CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
//	 HttpEntity fileEntity = httpResponse.getEntity();
//	 if (fileEntity != null) {
//	 FileUtils.copyInputStreamToFile(fileEntity.getContent(), new
//	 File(fileSavePath));
//	 }
//	 } catch (IOException e) {
//	 _logger.error("IO Exception occurred while Getting the Item with URL:"+fileURL+ e.getMessage());
//	 throw new ContentException(String.format("UnKnown Exception Occurred while getting item ID: %s",e.getMessage()));
//	 }
//	 httpGet.releaseConnection();
//	 _logger.info("Getting the Item with URL:"+fileURL+" with file name:"+fileSavePath+" Success");
//	 return isSucceed;
//	 }
//
//
//	public boolean unzip(String zipFilePath, String destDirectory) throws IOException {
//		_logger.info("Unzipping the File:" + zipFilePath + " to Location:" + destDirectory + " Started");
//		boolean isSucceed = false;
//		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
//		File dir = null;
//		ZipEntry entry = zipIn.getNextEntry();
//		while (entry != null) {
//			String filePath = destDirectory + File.separator + entry.getName();
//			if (!entry.isDirectory()) {
//				extractFile(zipIn, filePath);
//			} else {
//				dir = new File(filePath);
//				dir.mkdir();
//			}
//			zipIn.closeEntry();
//			entry = zipIn.getNextEntry();
//		}
//		zipIn.close();
//		if (dir != null) {
//			_contentPath = DESTINATION_ZIP_FILE_LOCATION + dir.getName();
//			load();
//			isSucceed = true;
//		} else {
//			_logger.error("Downloaed File:" + zipFilePath + " to Location:" + destDirectory
//					+ " is corrupted.make sure Item Exists");
//			new File(zipFilePath).delete();
//			isSucceed = false;
//			return isSucceed;
//		}
//		_logger.info("Unzipping the File:" + zipFilePath + " to Location:" + destDirectory + " Success");
//		return isSucceed;
//	}
//
//	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
//		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
//		byte[] bytesIn = new byte[BUFFER_SIZE];
//		int read = 0;
//		while ((read = zipIn.read(bytesIn)) != -1) {
//			bos.write(bytesIn, 0, read);
//		}
//		bos.close();
//	}
//
//	@Override
//	public void init() throws ContentException {
//
//		try {
//			DESTINATION_ZIP_FILE_LOCATION = SettingsReader.get("iris.ZipFileLocation");
//			 GIT_LAB_URL = SettingsReader.get("iris.GitLabItemUrl");
//		} catch (Exception exp) {
//			_logger.error("Error loading zip file location or git lab url", exp);
//			throw new ContentException(exp);
//		}
//
//	}
//
//	public void load() throws ContentException {
//		try {
//			_logger.info("loading the  the Itam in Directory Started");
//			_directoryScanner = new ConfigBuilder(_contentPath);
//			_logger.info("loading the  the Itam in Directory Completed");
//
//		} catch (Exception exp) {
//			_logger.error("Error loading IRiS content.", exp);
//			throw new ContentException(exp);
//		}
//
//	}
//
////	@Scheduled(cron = "0 0/15 * * *  *")
////	public void createDirectory() {
////
////		long start = System.currentTimeMillis();
////		File dir = new File(DESTINATION_ZIP_FILE_LOCATION);
////		dir.mkdir();
////		long end = System.currentTimeMillis();
////		_logger.info("Directory created in " + (end - start) + " milli seconds at " + new Date());
////	}
//
//}
