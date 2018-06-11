
package com.palredErp.feeds.productfeeds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "B2BPromoPriceType", propOrder = {

})
public class B2BPromoPriceType {

	@XmlElement(name = "B2BPromoPrice", defaultValue = "")
    protected String b2bPromoPrice;
    @XmlElement(name = "Currency", required = true, defaultValue = "")
    protected String currency;
    @XmlElement(name = "FromDate", defaultValue = "")
    protected String fromDate;
    @XmlElement(name = "ThruDate", defaultValue = "")
    protected String thruDate;
    
    public String getB2BPromoPrice() {
        return b2bPromoPrice;
    }

    
    public void setB2BPromoPrice(String value) {
        this.b2bPromoPrice = value;
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