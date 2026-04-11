package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.BMI;
import com.example.demo.response.ApiResponse;

import lombok.Getter;


/* 【若 @Controller 改成 @RestController 可以省略 @ResponseBody 那一行】 */
@Controller
/* 設定此 Controller 的主路徑 (資料夾) */
@RequestMapping("/api")
public class ApiController {

	
	/** 範例1. API 簡單範例 [@GetMapping 設定API路徑 + @ResponseBody 回傳內容]
	 * 
	 * 路徑1: /home
	 * 路徑2: /welcome
	 *
	 * 網址1: http://localhost:8080/api/home
	 * 網址2: http://localhost:8080/api/welcome
	 */
	
	/* @GetMapping 設定串接 Get 方法
	  參數 value 設定API的網址路徑 (此範例為2導1)
	    , produces 指定輸出的格式 (可避免瀏覽器將中文解析為亂碼) */
	@GetMapping(value = {"/home", "/welcome"}, produces = "text/plain; charset=utf-8")
	
	/* @ResponseBody 設定 Resp. 底下 body 內容 【若外層為 @RestController 則可省略這行】 */
	@ResponseBody               
	public String home() {
		return "我是 API 首頁~ (兩路徑皆可)";
	}
	

	
	/** 範例2. 含?參數之API [用 @RequestParam 設定參數路徑]
	 *
	 * 路徑1: /greet?name=John&age=18
	 * 網址1: http://localhost:8080/api/greet?name=John&age=18
	 * 結果1: 嗨 John, 18歲 (成年)
	 * 
	 * 路徑2: /api/greet?name=Mary
	 * 網址2: http://localhost:8080/api/greet?name=Mary
	 * 結果2: 嗨 Mary, 0歲 (未成年)
	 * 
	 * 限制: name 為必要參數, age 為可選參數(有初始值 0)
	 * */
	
	/* 設定 GET 路徑 /greet [有三種寫法] */
	// @GetMapping(value = {"..."})
	// @GetMapping(value = "...")
	// @GetMapping("...")
	@GetMapping("/greet")
	
	@ResponseBody
	/* 函式 greet(String userName, Integer userAge) */
	/* 用 @RequestParam 設定參數路徑 ?{value}=變數1&{value}=變數2 */
	public String greet( @RequestParam(value = "name", required = true) String userName,
						 @RequestParam(value = "age", required = false, defaultValue = "0") Integer userAge 
						 ) {
		
		// 將 變數1, 變數2 組合成字串 result
		String result = String.format( "嗨 %s, %d歲 (%s)", userName, userAge, (userAge>=18?"成年":"未成年") );
		
		// 回傳 result 給瀏覽器
		return result;
	}
	
	
	/** 範例3. 範例2簡化版寫法 */
	@GetMapping("/greet2")
	
	@ResponseBody
	/* 若 請求參數名稱value 與 方法參數名稱 相同，可省略 value=請求參數名稱
	 * required = True 為預設值，可省略 */
	public String greet2( @RequestParam String name,
						  @RequestParam(required = false, defaultValue = "0") Integer age 
						  ) {
		
		/* 作為簡化範例，直接回 call 範例2函式即可 */
		return greet(name, age);
	}
	
	
	
	/** 範例4. BMI計算
	 * 路徑: /bmi?h=170&w=60
	 * 網址: http://localhost:8080/api/bmi?h=170&w=60
	 * 
	 * 判斷: bmi <= 18 顯示過輕, bmi > 23 顯示過重
	 * 
	 * 執行結果: 身高:170cm 體重:60kg bmi=20.76(正常)
	*/
	
	@GetMapping("/bmi")
	@ResponseBody
	/* 建立 bmi(Double h, Double w) 函式，用來取值 & 輸出結果 */
	public String bmi(@RequestParam Double h, @RequestParam Double w) {
		
		// 計算 BMI 
		double bmi = w / Math.pow(h/100, 2);
		// 用三元運算子判斷 bmi 的狀態，存成 result 字串
		String result = bmi <= 18 ? "過輕" : bmi > 23 ? "過重" : "正常";
		
		// 將結果組合成 string 回傳瀏覽器 
		return String.format("身高: %.0f cm, 體重: %.0f kg, BMI= %.2f (%s)", h, w, bmi, result);
	}
	
	
	
	/** 範例5-1. BMI計算 [輸出單層 json 格式]
	 * 路徑: /json/bmi?h=170&w=60
	 * 網址: http://localhost:8080/api/json/bmi?h=170&w=60
	 * 
	 * 判斷: bmi <= 18 顯示過輕, bmi > 23 顯示過重
	 * 
	 * 執行結果: (單層 json 格式)
	 * {
     *   "height": 170.0,
     *   "weight": 60.0,
     *   "bmi": 20.76,
	 * }
	*/
	
	@GetMapping("/json/bmi")
	@ResponseBody
	/* 建立 calcBmi(Double h, Double w) 函式
	 * 回傳值為 BMI 物件 [預先新建類別，路徑 model/BMI.java] */
	public BMI calcBmi(@RequestParam Double h, @RequestParam Double w) {
		
		double bmi = w / Math.pow(h/100, 2);
		
		/* 用對應參數建立 BMI 物件 [類別路徑 model/BMI.java] */
		BMI bmi_obj = new BMI(h, w, bmi);
		
		/* 回傳結果給瀏覽器 [Spring Boot 內建 Jackson 套件會自動將 BMI物件 轉成 json 格式] */
		return  bmi_obj;
	}
	
	
	/** 範例5-2. BMI計算 [輸出兩層 json 格式]
	 * 路徑: /json2/bmi?h=170&w=60
	 * 網址: http://localhost:8080/api/json2/bmi?h=170&w=60
	 * 
	 * 執行結果: [多層 json 格式]
	 * {
	 *    "status": 200,
	 *    "message": "計算成功",
	 *    "data": {
	 *      "height": 170.0,
	 *      "weight": 60.0,
	 *      "bmi": 20.76,
	 *     }
	 * }
	 */
	
	@GetMapping("/json2/bmi")
	@ResponseBody
	/* 同上建立 calcBmi2(h, w) 函式
	 * 回傳值改為 ApiResponse<BMI> 物件 [預先新建類別，路徑 response/ApiResponse.java] */
	public ApiResponse<BMI> calcBmi2(@RequestParam Double h, @RequestParam Double w){
		
		double bmi = w / Math.pow(h/100, 2);
		
		// 用對應參數建立 BMI 物件
		BMI bmi_obj = new BMI(h, w, bmi);
		
		/* 依序打包參數 (message, data) 成 ApiResponse 物件 */
		ApiResponse<BMI> apiResponse = new ApiResponse<BMI>("計算成功", bmi_obj);
		
		return apiResponse;
	}
	
	
	/** 範例5-3. BMI計算 (輸出隱含狀態碼的 的 json 格式)
	 * 路徑: /json3/bmi?h=170&w=60
	 * 網址(status 為 200): http://localhost:8080/api/json3/bmi?h=170&w=60
	 * 網址(status 為 400): http://localhost:8080/api/json3/bmi?h=-80
	 * 網址(status 為 400): http://localhost:8080/api/json3/bmi?h=-80&w=-60
	 * 
	 * 執行結果: (隱含狀態碼的 json 格式)
	 * {
	 *    "status": 200,
	 *    "message": 對應的msg自訂內容,
	 *    "data": {
	 *      "height": 170.0,
	 *      "weight": 60.0,
	 *      "bmi": 20.76,
	 *     }
	 * }
	 */
	@GetMapping("/json3/bmi")
	@ResponseBody
	/* 同上建立 calcBmi2(h, w) 函式 [實務技巧：設定 (required = false) 可避免少輸入參數時，大跳到工程錯誤頁！]
	 * 而最外層用 ResponseEntity<> 包裝回傳物件 ApiResponse<BMI>
	 * 讓我們得以調用 ResponseEntity 的方法來同時回傳狀態碼與資料！ */
	public ResponseEntity<ApiResponse<BMI>> calcBmi3(@RequestParam (required = false) Double h, 
			                                         @RequestParam (required = false) Double w){
		
		/** 若身高或體重小於等於0，透過 ResponseEntity 回傳 HTTP 400 Bad Request 與 .body() 內的錯誤訊息 */
		if(h <= 0 || w <= 0) {
			return ResponseEntity.badRequest().body(
					/* ApiResponse<BMI> 物件格式為 (message, data) [類別路徑 response/ApiResponse.java] */
				    new ApiResponse<>("錯誤：身高體重必須大於0！", null)
				    /** 為制式化程式碼，我們也可改呼叫自訂之 .error() 靜態方法來產生錯誤回應 */
//				    ApiResponse.error("錯誤2：身高體重必須大於0！")
				    );
		}
		/** 若身高或體重為 null，提醒要輸入參數 */
		if(h == null || w == null) {
			return ResponseEntity.badRequest().body( ApiResponse.error("錯誤：請輸入參數！") );
		}
		
		
		/** 若身高體重正常，則計算 BMI 並回傳 HTTP 200 OK 與 .body() 內的計算結果 */
		// 計算參數並建立 BMI 物件
		double bmi = w / Math.pow(h/100, 2);
		BMI bmi_obj = new BMI(h, w, bmi);
		
		/* ResponseEntity.ok() 代表回傳 HTTP 200  */
		return ResponseEntity.ok(
				/* ApiResponse<BMI> 物件格式為 (message, data) [類別路徑 response/ApiResponse.java] */
				new ApiResponse<BMI>("計算成功~", bmi_obj)
				/** 為制式化程式碼，我們也可改呼叫自訂之 .success() 靜態方法來產生成功回應 */
//				ApiResponse.success("計算成功2~", bmi_obj)
				);
	}
	
	
	
	
	
/** Controller 尾*/
}
