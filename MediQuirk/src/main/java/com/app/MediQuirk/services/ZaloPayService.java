//package com.app.MediQuirk.services;
//
//@Service
//@Slf4j
//public class ZaloPayService {
//
//    @Value("${zalopay.app-id}")
//    private String appId;
//
//    @Value("${zalopay.key1}")
//    private String key1;
//
//    @Value("${zalopay.key2}")
//    private String key2;
//
//    @Value("${zalopay.endpoint}")
//    private String endpoint;
//
//    public String createPayment(PaymentRequest request) throws Exception {
//        Map<String, Object> order = new HashMap<>();
//        order.put("app_id", appId);
//        order.put("app_trans_id", generateAppTransId());
//        order.put("app_user", "demo");
//        order.put("app_time", System.currentTimeMillis());
//        order.put("amount", request.getAmount());
//        order.put("description", "Thanh toan don hang " + request.getOrderId());
//        order.put("bank_code", "");
//
//        String data = appId + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|" + order.get("amount")
//                + "|" + order.get("app_time") + "|" + order.get("embed_data") + "|" + order.get("item");
//        order.put("mac", hmacSHA256(data, key1));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(order, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);
//
//        // Parse response and return payment URL
//        // You'll need to implement this part based on ZaloPay's response format
//        return "ZaloPay payment URL";
//    }
//
//    private String generateAppTransId() {
//        return String.valueOf(System.currentTimeMillis());
//    }
//
//    private String hmacSHA256(String data, String key) throws Exception {
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
//        sha256_HMAC.init(secret_key);
//        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
//    }
//}
