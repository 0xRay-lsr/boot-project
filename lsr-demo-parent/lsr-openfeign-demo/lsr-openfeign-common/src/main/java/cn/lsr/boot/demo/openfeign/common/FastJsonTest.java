package cn.lsr.boot.demo.openfeign.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class FastJsonTest {
    public static void main(String[] args) {
        String jsonString = "{ \"header\": { \"test\": \"name\" }, \"body\": { \"commReq\": {}, \"input\": { \"gender\": \"%\", \"name\": \"lishirui\", \"userInfo\": { \"name\": \"lishirui\", \"age\": \"27\" } }, \"sys\": { \"businessStartBranchNo\": \"00000001\", \"consumerServiceCode\": \"1\", \"dataCenterId\": \"SZ\", \"envId\": \"\", \"globalBusinessTrackNo\": \"17002939031345905288749350371380\", \"macValue\": \"\", \"messageFormatVersion\": \"1.0.0000\", \"partitionDate\": \"20231118\", \"routeMappingElementInfo\": \"\", \"sendSystemId\": \"apss\", \"serviceCallSeqNo\": \"17002939031345905288749350371380\", \"serviceCode\": \"UserFlowTran\", \"serviceTypeCode\": \"10\", \"serviceVersion\": \"1.0.000\", \"shardId\": \"\", \"startChannelCode\": \"01\", \"startSystemId\": \"1111\", \"subDbId\": \"\", \"subTableId\": \"\", \"targetSystemId\": \"1111\", \"tenantId\": \"\", \"transInitiateTime\": \"20231118155143134\", \"transSendTime\": \"20231118155143134\", \"userLanguage\": \"\", \"zoneId\": \"\" } } }";

        JSONObject jsonObject = JSON.parseObject(jsonString);

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("body.input.gender");

        String filteredJsonString = JSON.toJSONString(jsonObject, filter);
        System.out.println(filteredJsonString);
    }
}
