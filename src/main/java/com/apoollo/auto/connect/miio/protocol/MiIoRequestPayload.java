/**
 * 
 */
package com.apoollo.auto.connect.miio.protocol;

/**
 * @author liuyulong
 * @since 2025-04-03
 */
public class MiIoRequestPayload {

	private String id;// UNIX timestamp
	private String method;// miIO.config_router
	private MiIoRequestPayloadParams params;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the params
	 */
	public MiIoRequestPayloadParams getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(MiIoRequestPayloadParams params) {
		this.params = params;
	}

}
