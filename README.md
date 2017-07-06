### 演示

![http://upload-images.jianshu.io/upload_images/2756952-3d0857d4a44739d4.gif](http://upload-images.jianshu.io/upload_images/2756952-3d0857d4a44739d4.gif)

### 描述

* 网络请求库的二次封装，支持扩展，底层实现可自由切换，默认实现了OkHttpExecutorFactory，支持get/post/postJson/uploadFile/downloadFile等常用功能；

* 扩展请继承ExecutorFactory实现具体的IExecutor即可，底层可以是OkHttp，也可以是HttpClient或者URLConnection；

### 背景

* 网络请求在app开发中占据很重要的角色，也算是核心技术要点之一。github上也有很多著名的开源库，如Xutils，AsyncHttpClient，Okhttp等等。我相信很多人如同我一样，习惯性的拿这些开源的加入到自己的项目中，但当有一天，因为某些原因我们需要对网络层进行一次切换，那么是不是问题就变的严重了，我们需要改大量的代码来满足这次更改。为了避免这样的问题发生，我们需要对网络层进行二次包装一下；

### 使用方式：

	public class MyApplication extends Application {
	
	    @Override
	    public void onCreate() {
	        super.onCreate();
			//在程序入口进行工厂初始化，默认的为okhttp的实现工厂
	        NetWorkUtils.init(new OkHttpExecutorFactory());
	    }
	}

	//get方式请求
	private void doGet() {
		String url = "http://127.0.0.1:8080/test/test";
		RequestParams params = new RequestParams.Builder().url(url).method(Method.GET).params("key1", "hello").params("key2", 	"doGet").tag(this).build();
		NetWorkUtils.getInstance().doStart(params, callback);
    }

	//post方式请求
   	private void doPost() {
		String url = "http://127.0.0.1:8080/test/test";
		RequestParams params = new RequestParams.Builder().url(url).method(Method.POST).params("key1", "hello").params("key2", "doPost").tag(this).build();
		NetWorkUtils.getInstance().doStart(params, callback);
   	}

	//postJson方式请求
	private void doPostJson() {
		String url = "http://127.0.0.1:8080/test/test";
		String body = "{ \"key1\":\"hello\",\"key2\":\"doPostJson\" }";
		RequestParams params = new RequestParams.Builder().url(url).method(Method.POST_JSON).body(body).tag(this).build();
		NetWorkUtils.getInstance().doStart(params, callback);
   	}

	//文件上传
    private void doUploadFile() {
		String url = "http://127.0.0.1:8080/test/upload";
		String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "atlas-master.zip";
		RequestParams params = new RequestParams.Builder().url(url).method(Method.UPLOAD).files("fileKey", new File(filePath)).tag(this).build();
		NetWorkUtils.getInstance().doStart(params, callback);
    }

	//文件下载
    private void doDownload() {
		String url = "http://127.0.0.1:8080/test/download";
		String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "atlas-master.zip";
		RequestParams params = new RequestParams.Builder().url(url).method(Method.DOWNLOAD).downLoadFilePath(filePath).tag(this).build();
		NetWorkUtils.getInstance().doStart(params, callback);
    }

	//请求回调，主线程中进行
 	private ICallBack callback = new ICallBack() {

		@Override
		public void onStart() {
		    Log.d(NetWorkUtils.class.getSimpleName(), "网络开始加载");
		}

		@Override
		public void onComplite() {
		    Log.d(NetWorkUtils.class.getSimpleName(), "网络加载完成");
		}

		@Override
		public void onError(Exception e) {
		    Log.d(NetWorkUtils.class.getSimpleName(), "出现错误");
		}


		@Override
		public void onSuccess(Object tag, String result) {

				Log.d(NetWorkUtils.class.getSimpleName(), "result = "+result);
		    /**
		     * 可以根据不同的tag来解析数据
		     * if(tag.equest("doGet")){
		     *
		     * }else if(tag.equest("doPost")){
		     *
		     * }
		     * ............
		     */
		}

		@Override
		public void onProgress(float progress) {
		    Log.d(NetWorkUtils.class.getSimpleName(), "上传或下载进度 : "+progress);
		}
   	 };

	//支持根据tag来取消具体的网络请求
 	protected void onDestroy() {
		NetWorkUtils.getInstance().cancleRequest(this);
		super.onDestroy();
    }


