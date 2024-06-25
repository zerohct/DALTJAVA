//package com.app.MediQuirk.services;
//
//@Service
//@Slf4j
//public class MomoService {
//
//    @Value("${momo.endpoint}")
//    private String endpoint;
//
//    @Value("${momo.partner-code}")
//    private String partnerCode;
//
//    @Value("${momo.access-key}")
//    private String accessKey;
//
//    @Value("${momo.secret-key}")
//    private String secretKey;
//
//    @Value("${momo.redirect-url}")
//    private String redirectUrl;
//
//    @Value("${momo.ipn-url}")
//    private String ipnUrl;
//
//    public MomoResponse createPayment(PaymentRequest request) throws Exception {
//        String requestId = String.valueOf(System.currentTimeMillis());
//        String orderInfo = "Thanh toan don hang " + request.getOrderId();
//        String rawHash = "partnerCode=" + partnerCode +
//                "&accessKey=" + accessKey +
//                "&requestId=" + requestId +
//                "&amount=" + request.getAmount() +
//                "&orderId=" + request.getOrderId() +
//                "&orderInfo=" + orderInfo +
//                "&redirectUrl=" + redirectUrl +
//                "&ipnUrl=" + ipnUrl;
//
//        String signature = createSignature(rawHash);
//
//        JSONObject json = new JSONObject();
//        json.put("partnerCode", partnerCode);
//        json.put("accessKey", accessKey);
//        json.put("requestId", requestId);
//        json.put("amount", request.getAmount());
//        json.put("orderId", request.getOrderId());
//        json.put("orderInfo", orderInfo);
//        json.put("redirectUrl", redirectUrl);
//        json.put("ipnUrl", ipnUrl);
//        json.put("requestType", "captureMoMoWallet");
//        json.put("extraData", "");
//        json.put("lang", "vi");
//        json.put("signature", signature);
//
//        String responseData = callMoMoAPI(json.toString());
//
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(responseData, MomoResponse.class);
//    }
//
//    private String createSignature(String rawHash) throws Exception {
//        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//        hmacSHA256.init(secretKeySpec);
//        byte[] hashBytes = hmacSHA256.doFinal(rawHash.getBytes());
//        return bytesToHex(hashBytes);
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }
//
//    private String callMoMoAPI(String jsonString) throws IOException {
//        URL url = new URL(endpoint);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
//        try (OutputStream os = conn.getOutputStream()) {
//            os.write(jsonString.getBytes());
//            os.flush();
//        }
//
//        StringBuilder response = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//        }
//
//        return response.toString();
//    }
//}
