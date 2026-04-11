package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@RequiredArgsConstructor
@Data   /* 包含上述五種 @ */
@NoArgsConstructor   /* 這個註解會自動產生一個「無參數的建構子」public BMI() [可用來 new BMI() 建立物件] */
@AllArgsConstructor  /* 這個註解會自動產生一個「全參數的建構子」public BMI(Double height, Double weight, Double bmi) 
                                                [可用來 new BMI(身高, 體重, BMI指數) 建立物件] */
/* 建立名為 BMI 的 Constructor [必須與檔名相同] */
public class BMI {

	/* 此處變數名稱會成為 JSON 格式的 key，收到的變數值會成為 JSON 格式的 value
	 * 例如單層 JSON 格式：
	 * {
	 *   "height": 170.0,
	 *   "weight": 60.0,
	 *   "bmi": 20.76,
	 * }
	 */
	private Double height; // 身高 (公尺)
	private Double weight; // 體重 (公斤)
	private Double bmi;    // BMI 指數
}
