package cn.huanzi.qch.baseadmin.common.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * ip
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpVo {
    private String country;
    private String countryCode;
    private String regionName;
    private String city;
    private String zip;
    private String query;

}
