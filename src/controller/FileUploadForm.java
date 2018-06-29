package controller;
import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileUploadForm {
	
	private byte[] filedata;
	private String meta;

	public FileUploadForm() {}

	public byte[] getFileData() {
		return filedata;
	}
	
	public String getMeta() {
		return meta;
	}
	
	@FormParam("filedata")
	@PartType("application/octet-stream")
	public void setFileData(final byte[] filedata) {
		this.filedata = filedata;
	}
	
	@FormParam("meta")
	@PartType("text/plain")
	public void setMeta(String meta) {
		this.meta = meta;
	}
}