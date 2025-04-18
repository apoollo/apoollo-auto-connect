/**
 * 
 */
package com.apoollo.auto.connect.brand.xiaomi.protocol;

/**
 * @author liuyulong
 * @since 2025-04-03
 */
public class MiIoRequestPayloadParams {

	private String ssid;// WiFi network
	private String passwd;// WiFi password
	private String uid;// identifies the device owner. The device will phone home and report this to
						// Xiaomi.
	/**
	 * @return the ssid
	 */
	public String getSsid() {
		return ssid;
	}
	/**
	 * @param ssid the ssid to set
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
