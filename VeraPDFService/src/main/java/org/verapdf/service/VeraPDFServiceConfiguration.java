package org.verapdf.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class VeraPDFServiceConfiguration extends Configuration {
	private String verapdfPath;
	private String logiusUrl;

	@JsonProperty
	public String getLogiusUrl() {
		return logiusUrl;
	}

	@JsonProperty
	public void setLogiusUrl(String logiusUrl) {
		this.logiusUrl = logiusUrl;
	}

	@JsonProperty
	public String getVerapdfPath() {
		return verapdfPath;
	}

	@JsonProperty
	public void setVerapdfPath(String verapdfPath) {
		this.verapdfPath = verapdfPath;
	}
}
