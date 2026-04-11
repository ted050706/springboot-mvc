package com.example.demo.response;

import com.example.demo.model.BMI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   /* 包含五種 @ */
@NoArgsConstructor
@AllArgsConstructor
/* 建立名為 ApiResponse 的泛型 Constructor [必須與檔名相同，可用來 new ApiResponse() 建立物件] */
public class ApiResponse<T> {
	
	private String message; // 要輸出的自訂文字訊息 例如: 查詢成功, 新增失敗, 身高體重不能小於0 ... 等
    T data; // 輸出資料 data
    
    
    /** 以下為兩個靜態方法，分別用於產生成功和失敗的 API 回應 */
    // 自定成功回應範本 success(msg, data)
    public static <T> ApiResponse<T> success(String msg, T data) {
		return new ApiResponse<>(msg, data);
	}
    
    // 自定失敗回應範本 error(msg)
	public static <T> ApiResponse<T> error(String msg) {
		return new ApiResponse<>(msg, null);
	}
	
    
}
