package framework.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * alibaba easyexcel测试类
 * created by XUAN on 2019/8/19
 */
public class AlibabaEasyExcel {


    List<AlibabaExcelData> datas = new ArrayList<>();

    @Before
    public void initData() {
        for (int i = 0; i < 10; i++) {
            AlibabaExcelData data = new AlibabaExcelData();
            data.setSkuCode("skuCode" + i);
            data.setSku("sku" + i);
            data.setQuantity(i);
            data.setOrderCode("orderCode" + i);
            data.setDeliCode("deliCode" + i);
            datas.add(data);
        }
    }

    @Test
    public void testOutPutExcel() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, AlibabaExcelData.class);
        writer.write(datas, sheet);
        writer.finish();
        byte[] bytes = out.toByteArray();
        File file = new File("D:\\test.xlsx");
        if (file.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
        } else {
            System.out.println("文件已存在");
        }
    }
}
