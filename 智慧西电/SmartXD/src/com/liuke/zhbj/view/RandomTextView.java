package com.liuke.zhbj.view;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class RandomTextView extends FrameLayout implements
		ViewTreeObserver.OnGlobalLayoutListener {

	private static final String tag = RandomTextView.class.getSimpleName();

	private static final int MAX = 5;
	private static final int IDX_X = 0;
	private static final int IDX_Y = 1;
	private static final int IDX_TXT_LENGTH = 2;
	private static final int IDX_DIS_Y = 3;
	private static final int TEXT_SIZE = 12;

	private Random random;
	private Vector<String> vecKeywords;
	private int width;
	private int height;
	private int mode = RippleView.MODE_OUT;
	private int fontColor = 0xff0000ff;
	private int shadowColor = 0xdd696969;

	public interface OnRippleViewClickListener {
		void onRippleViewClicked(View view);
	}

	private OnRippleViewClickListener onRippleOutViewClickListener;

	public RandomTextView(Context context) {
		super(context);
		init(null, context);
	}

	public RandomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, context);
	}

	public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, context);
	}


	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setOnRippleViewClickListener(OnRippleViewClickListener listener) {
		onRippleOutViewClickListener = listener;
	}

	/**
	 * 娣诲姞RippleOutView鐨勫唴瀹�
	 * 
	 * @param keyword
	 */
	public void addKeyWord(String keyword) {
		if (vecKeywords.size() < MAX) {
			if (!vecKeywords.contains(keyword))
				vecKeywords.add(keyword);
		}
	}

	public Vector<String> getKeyWords() {
		return vecKeywords;
	}

	public void removeKeyWord(String keyword) {
		if (vecKeywords.contains(keyword)) {
			vecKeywords.remove(keyword);
		}
	}

	private void init(AttributeSet attrs, Context context) {
		random = new Random();
		vecKeywords = new Vector<String>(MAX);
		getViewTreeObserver().addOnGlobalLayoutListener(this);

	}

	@Override
	public void onGlobalLayout() {
		int tmpW = getWidth();
		int tmpH = getHeight();
		if (width != tmpW || height != tmpH) {
			width = tmpW;
			height = tmpH;
			Log.d(tag, "RandomTextView width = " + width + "; height = "
					+ height);
		}
	}

	public void show() {
		this.removeAllViews();

		if (width > 0 && height > 0 && vecKeywords != null
				&& vecKeywords.size() > 0) {
			// 鎵惧埌涓績鐐�
			int xCenter = width >> 1;
			int yCenter = height >> 1;
			// 鍏抽敭瀛楃殑涓暟銆�
			int size = vecKeywords.size();
			int xItem = width / (size + 1);
			int yItem = height / (size + 1);
			LinkedList<Integer> listX = new LinkedList<Integer>();
			LinkedList<Integer> listY = new LinkedList<Integer>();
			for (int i = 0; i < size; i++) {
				// 鍑嗗闅忔満鍊欓�夋暟锛屽垎鍒搴攛/y杞翠綅缃�
				listX.add(i * xItem);
				listY.add(i * yItem + (yItem >> 2));
			}
			LinkedList<RippleView> listTxtTop = new LinkedList<RippleView>();
			LinkedList<RippleView> listTxtBottom = new LinkedList<RippleView>();

			for (int i = 0; i < size; i++) {
				String keyword = vecKeywords.get(i);
				// 闅忔満棰滆壊
				int ranColor = fontColor;
				// 闅忔満浣嶇疆锛岀硻鍊�
				int xy[] = randomXY(random, listX, listY, xItem);

				int txtSize = TEXT_SIZE;
				// 瀹炰緥鍖朢ippleOutView
				final RippleView txt = new RippleView(getContext());
				if (mode == RippleView.MODE_IN) {
					txt.setMode(RippleView.MODE_IN);
				} else {
					txt.setMode(RippleView.MODE_OUT);
				}

				txt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if (onRippleOutViewClickListener != null)
							onRippleOutViewClickListener
									.onRippleViewClicked(view);
					}
				});
				txt.setText(keyword);
				txt.setTextColor(ranColor);
				txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
				txt.setShadowLayer(1, 1, 1, shadowColor);
				txt.setGravity(Gravity.CENTER);
				txt.startRippleAnimation();

				// 鑾峰彇鏂囨湰闀垮害
				// Paint paint = txt.getPaint();
				int strWidth = /* (int) Math.ceil(paint.measureText(keyword)) */txt
						.getMeasuredWidth();
				xy[IDX_TXT_LENGTH] = strWidth;
				// 绗竴娆′慨姝�:淇x鍧愭爣
				if (xy[IDX_X] + strWidth > width - (xItem/* >> 1 */)) {
					int baseX = width - strWidth;
					// 鍑忓皯鏂囨湰鍙宠竟缂樹竴鏍风殑姒傜巼
					xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);
				} else if (xy[IDX_X] == 0) {
					// 鍑忓皯鏂囨湰宸﹁竟缂樹竴鏍风殑姒傜巼
					xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
				}
				xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);
				txt.setTag(xy);
				if (xy[IDX_Y] > yCenter) {
					listTxtBottom.add(txt);
				} else {
					listTxtTop.add(txt);
				}
			}

			attach2Screen(listTxtTop, xCenter, yCenter, yItem);
			attach2Screen(listTxtBottom, xCenter, yCenter, yItem);
		}
	}

	/** 淇RippleOutView鐨刌鍧愭爣灏嗗皢鍏舵坊鍔犲埌瀹瑰櫒涓娿�� */
	private void attach2Screen(LinkedList<RippleView> listTxt, int xCenter,
			int yCenter, int yItem) {
		int size = listTxt.size();
		sortXYList(listTxt, size);
		for (int i = 0; i < size; i++) {
			RippleView txt = listTxt.get(i);
			int[] iXY = (int[]) txt.getTag();
			// 绗簩娆′慨姝�:淇y鍧愭爣
			int yDistance = iXY[IDX_Y] - yCenter;
			// 瀵逛簬鏈�闈犺繎涓績鐐圭殑锛屽叾鍊间笉浼氬ぇ浜巠Item<br/>
			// 瀵逛簬鍙互涓�璺笅闄嶅埌涓績鐐圭殑锛屽垯璇ュ�间篃鏄叾搴旇皟鏁寸殑澶у皬<br/>
			int yMove = Math.abs(yDistance);
			inner: for (int k = i - 1; k >= 0; k--) {
				int[] kXY = (int[]) listTxt.get(k).getTag();
				int startX = kXY[IDX_X];
				int endX = startX + kXY[IDX_TXT_LENGTH];
				// y杞翠互涓績鐐逛负鍒嗛殧绾匡紝鍦ㄥ悓涓�渚�
				if (yDistance * (kXY[IDX_Y] - yCenter) > 0) {
					if (isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X]
							+ iXY[IDX_TXT_LENGTH])) {
						int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);
						if (tmpMove > yItem) {
							yMove = tmpMove;
						} else if (yMove > 0) {
							// 鍙栨秷榛樿鍊笺��
							yMove = 0;
						}
						break inner;
					}
				}
			}

			if (yMove > yItem) {
				int maxMove = yMove - yItem;
				int randomMove = random.nextInt(maxMove);
				int realMove = Math.max(randomMove, maxMove >> 1) * yDistance
						/ Math.abs(yDistance);
				iXY[IDX_Y] = iXY[IDX_Y] - realMove;
				iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);
				// 宸茬粡璋冩暣杩囧墠i涓渶瑕佸啀娆℃帓搴�
				sortXYList(listTxt, i + 1);
			}
			FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
			/* FrameLayout.LayoutParams.WRAP_CONTENT */200,
			/* FrameLayout.LayoutParams.WRAP_CONTENT */200);
			layParams.gravity = Gravity.LEFT | Gravity.TOP;
			layParams.leftMargin = iXY[IDX_X];
			layParams.topMargin = iXY[IDX_Y];
			addView(txt, layParams);
		}
	}

	private int[] randomXY(Random ran, LinkedList<Integer> listX,
			LinkedList<Integer> listY, int xItem) {
		int[] arr = new int[4];
		arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
		return arr;
	}

	/** A绾挎涓嶣绾挎鎵�浠ｈ〃鐨勭洿绾垮湪X杞存槧灏勪笂鏄惁鏈変氦闆嗐�� */
	private boolean isXMixed(int startA, int endA, int startB, int endB) {
		boolean result = false;
		if (startB >= startA && startB <= endA) {
			result = true;
		} else if (endB >= startA && endB <= endA) {
			result = true;
		} else if (startA >= startB && startA <= endB) {
			result = true;
		} else if (endA >= startB && endA <= endB) {
			result = true;
		}
		return result;
	}

	/**
	 * 鏍规嵁涓庝腑蹇冪偣鐨勮窛绂荤敱杩戝埌杩滆繘琛屽啋娉℃帓搴忋��
	 * 
	 * @param endIdx
	 *            璧峰浣嶇疆銆�
	 * @param listTxt
	 *            寰呮帓搴忕殑鏁扮粍銆�
	 * 
	 */
	private void sortXYList(LinkedList<RippleView> listTxt, int endIdx) {
		for (int i = 0; i < endIdx; i++) {
			for (int k = i + 1; k < endIdx; k++) {
				if (((int[]) listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[]) listTxt
						.get(i).getTag())[IDX_DIS_Y]) {
					RippleView iTmp = listTxt.get(i);
					RippleView kTmp = listTxt.get(k);
					listTxt.set(i, kTmp);
					listTxt.set(k, iTmp);
				}
			}
		}
	}
}
