
package com.code.integration.promotionsdrugstest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for adjustPromotionsDrugsTestResults complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adjustPromotionsDrugsTestResults">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="drugstestresults" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adjustPromotionsDrugsTestResults", propOrder = {
    "drugstestresults"
})
public class AdjustPromotionsDrugsTestResults {

    protected String drugstestresults;

    /**
     * Gets the value of the drugstestresults property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrugstestresults() {
        return drugstestresults;
    }

    /**
     * Sets the value of the drugstestresults property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrugstestresults(String value) {
        this.drugstestresults = value;
    }

}
