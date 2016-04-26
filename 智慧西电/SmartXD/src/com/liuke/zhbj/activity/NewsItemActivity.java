package com.liuke.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.liuke.zhbj.R;

public class NewsItemActivity extends Activity implements OnClickListener {

	private ImageButton ibBack;
	private ImageButton ibTextSize;
	private ImageButton ibShare;
	private WebView webView;
	private ProgressBar pbLoad;
	private WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {
		setListener();
		Intent intent = getIntent();
		String jumpUrl = intent.getStringExtra("jumpUrl");
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		System.out.println("jumpUrl:"+jumpUrl);
		webView.loadUrl(jumpUrl);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webView.setVisibility(View.GONE);
				pbLoad.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				pbLoad.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
				super.onPageFinished(view, url);
			}

		});
	}

	private void setListener() {
		ibBack.setOnClickListener(this);
		ibTextSize.setOnClickListener(this);
		ibShare.setOnClickListener(this);
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_item);
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibTextSize = (ImageButton) findViewById(R.id.ib_textsize);
		ibShare = (ImageButton) findViewById(R.id.ib_share);
		webView = (WebView) findViewById(R.id.web_view);
		pbLoad = (ProgressBar) findViewById(R.id.pb_load);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_textsize:
			String[] items = { "��С������", "С������", "��������", "�������", "���������" };
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("����ѡ��");
			builder.setIcon(R.drawable.ic_launcher);
			builder.setSingleChoiceItems(items, 2,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								webSettings.setTextSize(TextSize.SMALLEST);
								break;
							case 1:
								webSettings.setTextSize(TextSize.SMALLER);
								break;
							case 2:
								webSettings.setTextSize(TextSize.NORMAL);
								break;
							case 3:
								webSettings.setTextSize(TextSize.LARGER);
								break;
							case 4:
								webSettings.setTextSize(TextSize.LARGEST);
								break;
							default:
								break;
							}
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			break;
		case R.id.ib_share:
//			showShare();
			break;

		default:
			break;
		}
	}

//	private void showShare() {
//		ShareSDK.initSDK(this);
//		OnekeyShare oks = new OnekeyShare();
//		// �ر�sso��Ȩ
//		oks.disableSSOWhenAuthorize();
//		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
//		oks.setTitle("����...");
//		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//		oks.setTitleUrl("http://sharesdk.cn");
//		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
//		oks.setText("���Ƿ����ı�");
//		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
//		oks.setImagePath(getCacheDir().getAbsolutePath()+"/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
//		
//		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//		oks.setUrl("http://sharesdk.cn");
//		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//		oks.setComment("���ǲ��������ı�");
//		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
//		oks.setSite(getString(R.string.app_name));
//		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//		oks.setSiteUrl("http://sharesdk.cn");
//
//		// ��������GUI
//		oks.show(this);
//	}
}
