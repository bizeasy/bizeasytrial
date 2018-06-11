package com.palredErp.feeds.productfeeds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "B2BPriceType", propOrder = {

})
public class B2BPriceType {

	 @XmlElement(name = "B2BPrice", defaultValue = "")
	    protected String b2bPrice;
	    @XmlElement(name = "Currency", required = true, defaultValue = "")
	    protected String currency;
	    @XmlElement(name = "FromDate", defaultValue = "")
	    protected String fromDate;
	    @XmlElement(name = "ThruDate", defaultValue = "")
	    protected String thruDate;
	    
	    public String getB2BPrice() {
	        return b2bPrice;
	    }

	    
	    public void setB2BPrice(String value) {
	        this.b2bPrice = value;
	    }

	   
	    public String getCurrency() {
	        return currency;
	    }

	    
	    public void setCurrency(String value) {
	        this.currency = value;
	    }

	   
	    public String getFromDate() {
	        return fromDate;
	    }

	    
	    public void setFromDate(String value) {
	        this.fromDate = value;
	    }

	   
	    public String getThruDate() {
	        return thruDate;
	    }

	    
	    public void setThruDate(String value) {
	        this.thruDate = value;
	    }

}