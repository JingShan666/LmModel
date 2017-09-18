package wei.moments.oss;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import wei.moments.utils.Regular;


/**
 * Created by Administrator on 2016/7/11 0011.
 * 阿里云OSS(文档：https://help.aliyun.com/document_detail/32047.html?spm=5176.doc32046.6.318.vobQFO)
 */
public class OSSManager {

    private static final String TAG = "OSSManager";
    private static OSSManager ossManager;
    private static OSSClient ossClient = null;
    private static String accessKeyId = "LTAI8ZYYxvRZq7tt";
    private static String accessKeySecret = "XkM2thoDYzMEM3fq9z4uz04WXWhXdT";
    private static String bucketName = "bbc-oss1";                //阿里云存储空间
    private static String endpoint = "oss-cn-beijing.aliyuncs.com"; //Endpoint 是阿里云 OSS 服务在各个区域的地址
    private static String remoteHost = "https://" + bucketName + "." + endpoint + "/";   //上传后的远程路径 = https://endpoint.endpoint/objectKey
    public static final String imagePath = "dynamic_image/";
    public static final String videoPath = "dynamic_video/";
    public static final String videoThumlPath = "dynamic_thuml_video/";
    public String fileUrl_asyncUpLoad = ""; //asyncUpload异步上传后的 远程 url
    private Context mContext;
    /**
     * 发布动态  图片类型
     */
    public static final int IMAGE_TYPE = 1;
    /**
     * 发布动态  视频类型
     */
    public static final int VIDEO_TYPE = 2;
    /**
     * 发布动态  视频第一帧缩略图 类型
     */
    public static final int VIDEO_THUMI_TYPE = 3;

    public static String getRemoteHost() {
        return remoteHost;
    }

    /**
     * asyncUpload异步上传后的 远程 url
     *
     * @return
     */
    public String getFileUrl_asyncUpLoad() {
        return fileUrl_asyncUpLoad;
    }

    private OSSManager() {
    }

    public static OSSManager getInstance(Context context) {
        if (ossManager == null) {
            ossManager = new OSSManager();
            init(context);
        }
        return ossManager;
    }


    private static void init(Context context) {


        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(2); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        ossClient = new OSSClient(context, endpoint, credentialProvider, conf);

    }


    /**
     * 异步上传
     *
     * @param uploadFilePath
     * @param callback          进度回调
     * @param completedCallback 上传成功或失败 回调
     * @return task.cancel(); // 可以取消任务.
     * task.waitUntilFinished(); // 可以等待直到任务完成
     */
    public OSSAsyncTask asyncUpload(String uploadFilePath, int type, OSSProgressCallback callback,
                                    OSSCompletedCallback<PutObjectRequest,
                                            PutObjectResult> completedCallback) {

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, accessKeyId, uploadFilePath);
        if (uploadFilePath != null && uploadFilePath.contains("/")) {
            String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1, uploadFilePath.length());
            if (!TextUtils.isEmpty(fileName)) {
                fileName = Regular.FilteringChineseCharacters(fileName);
            }

            //可以加目录 ：video/fileName
            switch (type) {
                case IMAGE_TYPE:
                    String objectKey = imagePath + fileName;
                    put.setObjectKey(objectKey);
                    fileUrl_asyncUpLoad = remoteHost + imagePath + fileName;
                    break;
                case VIDEO_TYPE:
                    objectKey = videoPath + fileName;
                    put.setObjectKey(objectKey);
                    fileUrl_asyncUpLoad = remoteHost + objectKey;
                    break;
            }
        }
        // 异步上传时可以设置进度回调
        if (callback != null)
            put.setProgressCallback(callback);

        OSSAsyncTask task = ossClient.asyncPutObject(put, completedCallback);

        return task;

    }


    /**
     * 通过 result.getETag() 获取上传成功后的url
     * 同步上传  int type : com.bbc.lm.content.Constants
     */
    public PutObjectResult synchUpLoad(String uploadFilePath, int type, OSSProgressCallback callback) throws ClientException, ServiceException {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, accessKeyId, uploadFilePath);
        // 文件元信息的设置是可选的
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("application/octet-stream"); // 设置content-type
        // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
        // put.setMetadata(metadata);

        String fileName = "";
//        if (!TextUtils.isEmpty(uploadFilePath) && uploadFilePath.contains("/")) {
//            fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1, uploadFilePath.length());
//            if (!TextUtils.isEmpty(fileName)) {
//                fileName = Regular.FilteringChineseCharacters(fileName);
//            }
            //可以加目录 ：video/fileName
            switch (type) {
                case IMAGE_TYPE:
                    fileName = (imagePath + "lm_img"+System.currentTimeMillis()+".jpg");
                    break;
                case VIDEO_THUMI_TYPE:
                    fileName = (videoThumlPath + "lm_img"+System.currentTimeMillis()+".jpg");
                    break;
                case VIDEO_TYPE:
                    fileName = (videoPath + "lm_video"+System.currentTimeMillis()+".mp4");
                    break;
            }
            put.setObjectKey(fileName);
//        }

        if (callback != null) {
            put.setProgressCallback(callback);
        }
        PutObjectResult putResult = ossClient.putObject(put);
        putResult.setETag(remoteHost + fileName);
        return putResult;
    }

    //异步下载
    public void down() {
        /*
        // 构造下载文件请求
GetObjectRequest get = new GetObjectRequest("<bucketName>", "<objectKey>");

OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
    @Override
    public void onSuccess(GetObjectRequest HomeRequest, GetObjectResult result) {
        // 请求成功
        Log.d("Content-Length", "" + getResult.getContentLength());

        InputStream inputStream = result.getObjectContent();

        byte[] buffer = new byte[2048];
        int len;

        try {
            while ((len = inputStream.read(buffer)) != -1) {
                // 处理下载的数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(GetObjectRequest HomeRequest, ClientException clientExcepion, ServiceException serviceException) {
        // 请求异常
        if (clientExcepion != null) {
            // 本地异常如网络异常等
            clientExcepion.printStackTrace();
        }
        if (serviceException != null) {
            // 服务异常
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
        }
    }
});

// task.cancel(); // 可以取消任务

// task.waitUntilFinished(); // 如果需要等待任务完成

         */
    }


}