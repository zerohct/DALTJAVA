//package com.app.MediQuirk.services;
//
//@Service
//@Slf4j
//public class VNPayService {
//
//    @Value("${vnpay.tmn-code}")
//    private String tmnCode;
//
//    @Value("${vnpay.hash-secret}")
//    private String hashSecret;
//
//    @Value("${vnpay.payment-url}")
//    private String paymentUrl;
//
//    @Value("${vnpay.return-url}")
//    private String returnUrl;
//
//    public String createPayment(PaymentRequest request) throws UnsupportedEncodingException {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
//        String vnp_IpAddr = "127.0.0.1";
//        String vnp_TmnCode = tmnCode;
//        String orderType = "order";
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + request.getOrderId());
//        vnp_Params.put("vnp_OrderType", orderType);
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", returnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator<String> itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = itr.next();
//            String fieldValue = vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = hmacSHA512(hashSecret, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        return paymentUrl + "?" + queryUrl;
//    }
//
//    private String hmacSHA512(final String key, final String data) {
//        try {
//            if (key == null || data == null) {
//                throw new NullPointerException();
//            }
//            final Mac hmac512 = Mac.getInstance("HmacSHA512");
//            byte[] hmacKeyBytes = key.getBytes();
//            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
//            hmac512.init(secretKey);
//            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
//            byte[] result = hmac512.doFinal(dataBytes);
//            StringBuilder sb = new StringBuilder(2 * result.length);
//            for (byte b : result) {
//                sb.append(String.format("%02x", b & 0xff));
//            }
//            return sb.toString();
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//}