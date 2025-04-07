package com.frend.planit.global.aws.s3;

import com.frend.planit.global.aws.s3.S3PresignedPostResponse.FormData;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.max-upload-size}")
    private long maxUploadSize;

    private final S3Client s3Client;

    public S3PresignedPostResponse createPresignedPostUrl(String fileName, String contentType) {
        // 기본값 설정
        String presignedUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com";
        String amzDate = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneOffset.UTC).format(java.time.Instant.now());
        String dateStamp = LocalDate.now(ZoneOffset.UTC)
                .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        String credential =
                accessKey + "/" + dateStamp + "/" + region + "/s3/aws4_request";

        // 서명 생성
        String policyDocument = createPolicyDocument(fileName, contentType, amzDate, credential);
        String encodedPolicy = Base64.getEncoder()
                .encodeToString(policyDocument.getBytes(StandardCharsets.UTF_8));
        byte[] signingKey = getSignatureKey(dateStamp);
        String signature = toHex(hmacSHA256(encodedPolicy, signingKey));

        // 폼 데이터 생성
        FormData formData = new FormData(
                fileName,
                encodedPolicy,
                contentType,
                "AWS4-HMAC-SHA256",
                credential,
                amzDate,
                signature
        );
        S3PresignedPostResponse postResponse = new S3PresignedPostResponse(
                presignedUrl,
                formData
        );

        return postResponse;
    }

    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new ServiceException(ErrorType.S3_DELETE_FAILED, e);
        }
    }

    public void deleteFiles(List<ObjectIdentifier> fileNames) {
        try {
            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(fileNames).build())
                    .build();

            s3Client.deleteObjects(deleteRequest);
        } catch (Exception e) {
            throw new ServiceException(ErrorType.S3_DELETE_FAILED, e);
        }
    }

    private String createPolicyDocument(String fileName, String contentType, String amzDate,
            String credential) {
        String expirationTime = java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.now().plusSeconds(180));

        return "{\n" +
                "  \"expiration\": \"" + expirationTime + "\",\n" +
                "  \"conditions\": [\n" +
                "    {\"bucket\": \"" + bucketName + "\"},\n" +
                "    [\"starts-with\", \"$key\", \"" + fileName + "\"],\n" +
                "    {\"x-amz-algorithm\": \"AWS4-HMAC-SHA256\"},\n" +
                "    {\"x-amz-credential\": \"" + credential + "\"},\n" +
                "    {\"x-amz-date\": \"" + amzDate + "\"},\n" +
                "    [\"starts-with\", \"$Content-Type\", \"" + contentType + "\"],\n" + // 여기!
                "    [\"content-length-range\", 0, " + maxUploadSize + "]\n" +
                "  ]\n" +
                "}";
    }

    private byte[] hmacSHA256(String data, byte[] key) {
        try {
            String algorithm = "HmacSHA256";
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key, algorithm));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ServiceException(ErrorType.S3_UPLOAD_FAILED, e);
        }
    }

    private byte[] getSignatureKey(String dateStamp) {
        byte[] kSecret = ("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = hmacSHA256(dateStamp, kSecret);
        byte[] kRegion = hmacSHA256(region, kDate);
        byte[] kService = hmacSHA256("s3", kRegion);
        return hmacSHA256("aws4_request", kService);
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}