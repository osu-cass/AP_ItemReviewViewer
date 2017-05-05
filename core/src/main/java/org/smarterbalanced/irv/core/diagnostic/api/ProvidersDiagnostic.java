package org.smarterbalanced.irv.core.diagnostic.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Providers diagnostic.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "providers")
class ProvidersDiagnostic extends BaseDiagnostic {

	private static final Logger logger = LoggerFactory.getLogger(ProvidersDiagnostic.class);

	@XmlElement(name = "GITLAB-API-HTTP-status")
	private Integer irisStatus = null;

	@XmlTransient
	private String baseUrl = null;

	/**
	 * Instantiates a new Providers diagnostic.
	 *
	 * @param baseUrl
	 *            the base application url
	 */
	ProvidersDiagnostic(String baseUrl) {
		this.errors = new ArrayList<>();
		this.baseUrl = baseUrl;
	}

	/**
	 * Instantiates a new Providers diagnostic.
	 */
	/*
	 * The empty constructor is here for the XML serializer. Your IDE may claim
	 * it is not used. It is. DO NOT REMOVE!
	 */
	ProvidersDiagnostic() {
	}

	/**
	 * Run diagnostics.
	 * <p>
	 * Validates the Amazon S3 connection, iris is running, the blackbox is
	 * running, and the word list handler is running.
	 * </p>
	 */
	void runDiagnostics() {
		String baseUrl = this.baseUrl;
		validateIris(baseUrl);
		generateStatus();
	}

	private Integer getHttpStatus(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		Integer responseCode = connection.getResponseCode();
		connection.disconnect();
		return responseCode;
	}

	private void validateIris(String baseUrl) {
		try {
			URL url = new URL(baseUrl + "/item/187-3212");
			Integer status = getHttpStatus(url);
			if (status == 200 || status == 404) {
				this.irisStatus = 200;
			} else {
				this.irisStatus = status;
				addError("Item viewer service API ");
				logger.error(
						"Item viewer service API returned a failing HTTP status code. HTTP code: " + status.toString());
			}
		} catch (IOException e) {
			addError("An I/O error occurred when trying to connect to the item viewer service API. "
					+ "Please review the system logs.");
			logger.error("Unable to connect to item viewer service API. Exception: " + e.getMessage());
		}
	}

	/**
	 * Gets iris status.
	 *
	 * @return the iris status
	 */
	public Integer getIrisStatus() {
		return irisStatus;
	}

	/**
	 * Sets iris status.
	 *
	 * @param irisStatus
	 *            the iris status
	 */
	public void setIrisStatus(Integer irisStatus) {
		this.irisStatus = irisStatus;
	}

	/**
	 * Gets base application url.
	 *
	 * @return the base application url
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets base application url.
	 *
	 * @param baseUrl
	 *            the base application url
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
