package com.cnbaosi.dto.tool;
 
import java.io.Serializable;
 
/**
 * @description Code/Name风格传输封装类
 * @author Zhidong.Guo
 *
 */
@SuppressWarnings("serial")
public class FeignCodeName implements Serializable {
    // 编码
    private String code;
    // 名称
    private String name;
 
    public FeignCodeName() {
    }
 
    public FeignCodeName(String code, String name) {
        this.code = code;
        this.name = name;
    }
 
    public String getCode() {
        return this.code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public String getName() {
        return this.name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
}