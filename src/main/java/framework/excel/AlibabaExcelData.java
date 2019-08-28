package framework.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * created by XUAN on 2019/8/19
 */
public class AlibabaExcelData extends BaseRowModel {

    @ExcelProperty(index = 0 ,value = "sku編號")
    private String skuCode;

    @ExcelProperty(index = 1 ,value = "商品款號")
    private String sku;

    @ExcelProperty(index = 2 ,value = "商品數量")
    private Integer quantity;

    @ExcelProperty(index = 3 ,value = "平台訂單號")
    private String orderCode;

    @ExcelProperty(index = 4 ,value = "訂單號")
    private String deliCode;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDeliCode() {
        return deliCode;
    }

    public void setDeliCode(String deliCode) {
        this.deliCode = deliCode;
    }
}
