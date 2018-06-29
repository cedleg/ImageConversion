package model;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@XmlRootElement
public class Format {

    @Id
    private String name;
    private String extension;
    private String mimeType;
    private boolean defaut = true;

    @ElementCollection(fetch=FetchType.EAGER)
    private Map<String, String> properties = new HashMap<>();
    //@ManyToOne
    //private Project project;

    public Format() {
    }



	public Format(String name, String extension, String mimeType, boolean defaut, Map<String, String> properties) {
		super();
		this.name = name;
		this.extension = extension;
		this.mimeType = mimeType;
		this.defaut = defaut;
		this.properties = properties;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}



	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public boolean isDefaut() {
		return defaut;
	}

	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public void addProperties(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public String toString() {
		return "Format [name=" + name + ", mimeType=" + mimeType + ", defaut=" + defaut + ", properties=" + properties
				+ "]";
	}

}
