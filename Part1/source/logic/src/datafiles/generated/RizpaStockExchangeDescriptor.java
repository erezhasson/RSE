//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.03.22 at 02:11:54 PM IST 
//


package datafiles.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}rse-stocks"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "rizpa-stock-exchange-descriptor")
public class RizpaStockExchangeDescriptor {

    @XmlElement(name = "rse-stocks", required = true)
    protected RseStocks rseStocks;

    /**
     * Gets the value of the rseStocks property.
     * 
     * @return
     *     possible object is
     *     {@link RseStocks }
     *     
     */
    public RseStocks getRseStocks() {
        return rseStocks;
    }

    /**
     * Sets the value of the rseStocks property.
     * 
     * @param value
     *     allowed object is
     *     {@link RseStocks }
     *     
     */
    public void setRseStocks(RseStocks value) {
        this.rseStocks = value;
    }

}
