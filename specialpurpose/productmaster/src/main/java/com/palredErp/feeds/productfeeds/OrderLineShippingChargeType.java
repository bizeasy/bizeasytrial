//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.10 at 09:40:51 AM IST 
//


package com.palredErp.feeds.productfeeds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderLineShippingChargeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineShippingChargeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShipGroupSequenceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ShippingCharge" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineShippingChargeType", propOrder = {
    "shipGroupSequenceId",
    "shippingCharge"
})
public class OrderLineShippingChargeType {

    @XmlElement(name = "ShipGroupSequenceId", required = true)
    protected String shipGroupSequenceId;
    @XmlElement(name = "ShippingCharge", required = true)
    protected String shippingCharge;

    /**
     * Gets the value of the shipGroupSequenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipGroupSequenceId() {
        return shipGroupSequenceId;
    }

    /**
     * Sets the value of the shipGroupSequenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipGroupSequenceId(String value) {
        this.shipGroupSequenceId = value;
    }

    /**
     * Gets the value of the shippingCharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShippingCharge() {
        return shippingCharge;
    }

    /**
     * Sets the value of the shippingCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShippingCharge(String value) {
        this.shippingCharge = value;
    }

}
