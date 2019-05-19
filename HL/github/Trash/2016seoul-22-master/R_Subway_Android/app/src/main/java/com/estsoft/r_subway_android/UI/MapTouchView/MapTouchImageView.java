package com.estsoft.r_subway_android.UI.MapTouchView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by estsoft on 2016-06-23.
 */
public class MapTouchImageView extends ImageView implements View.OnTouchListener {

    private static final String TAG = "MapTouchListener";
    private static final boolean D = false;

    //최대 이미지 배율 상수 (뷰 * maxMagnification = 이미지)
    // 1 = minMag, 10 = 2 times width
    private int maxMagnification = 10;

    // 드래그, 줌 Sensitivity ; 1 을 기준으로 내려갈수록 적게 움직임
    private float dragSensitivity = 0.8f;
    private float pinchSensitivity = 0.7f;

    //뷰보다 이미지크기가 큰가
    private boolean biggerThanViewWidth = false;
    private boolean biggerThanViewHeight = false;

    //최대 배율
    private float maxMag = (float) maxMagnification;
    //최소 배율 (뷰 = 이미지)
    private float minMag = 1f;
    private float minMagSupport = 1.3f;
    private float heightSupport = 50f;
    //기본 배율 (Default minMig);
    private float defaultMag = 1f;

    // 변경된 이미지 크기
    private int scaledImageWidth;
    private int scaledImageHeight;
    // 변경된 이미지 위치
    private int movedImageX;
    private int movedImageY;

    //드래그 모드인지 핀티줌 모드인지 구분
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int DONE = 3;
    static final int START = 4;
    int mode = NONE;
    int select = NONE;

    //
    private Matrix usingMatrix = new Matrix();
    private Matrix savedMatrix1 = new Matrix();
    private Matrix savedMatrix2 = new Matrix();

    //
    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    // 너비 높이 타겟 결정
    int target = WIDTH;

    //
    private boolean isInit = false;

    //드래그시 좌표 저장
    int posX1 = 0, posY1 = 0, posX2 = 0, posY2 = 0;

    //드래그시 좌표 PointF(two floats)
    private PointF start = new PointF();
    private PointF mid = new PointF();

    //핀치시 두좌표간의 거리 저장
    float oldDist = 1f;
    float newDist = 1f;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInit == false) {
            init();   isInit = true;
        }
    }

    //context, attr, defStyleAttr 이 뭔지 궁금함
    public MapTouchImageView(Context context, AttributeSet attrs, int defStyleAttr)  throws Exception  {
        super(context, attrs, defStyleAttr);
        setScaleType( ScaleType.MATRIX );
    }

    public MapTouchImageView(Context context, AttributeSet attrs)  throws Exception  {
        this(context, attrs, 0);
    }

    public MapTouchImageView(Context context) throws Exception {
        this(context, null);
    }

    public void init() {
        if (isInit == false) {
            isInit = true;
            setOnTouchListener(this);
            float[] value = new float[9];
            usingMatrix.getValues(value);
            value[0] = value[4] = 1.0f;
            usingMatrix.setValues(value);
            matrixTurning(usingMatrix, this);

            setImageMatrix(usingMatrix);
            setImagePit();
        }
    }
    //생성시 한번만 도는 애
    public void setImagePit() {

        //매트릭스 값
        float[] value = new float[9];
        this.usingMatrix.getValues( value );

//        Log.d(TAG, "Matrix = " + usingMatrix.toString());

        //뷰 크기
        int width = this.getWidth();
//        Log.d(TAG, "setImagePit123123123: " + width);
        int height = this.getHeight();
//        Log.d(TAG, "ViewSize = " + width + "/" + height);


        //이미지 크기
        Drawable drawable = this.getDrawable();
        if ( drawable == null ) return;

        // getIntrinsic : 이미지뷰에 있는 src 이미지의 실제 사이즈
        // drawable 디렉토리에 있을 경우 디바이스의 Density 비율에 따라 자동 확대
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

//        Log.w(TAG, "ImageSize = " + imageWidth + "/" + imageHeight);

        //value[0], value[4]의 Default = 1f
        scaledImageWidth = (int)(imageWidth * value[0]);
        scaledImageHeight = (int)(imageHeight * value[4]);

//        Log.d(TAG, "ScaleSize = " + scaledImageWidth + "/" + scaledImageHeight);


        //이미지가 바깥으로 나가지 않도록
        value[0] = value[4] = calMinMaxMag( imageWidth, imageHeight, width, height );


        //가운데 위치
//        value[2] = 0;
//        value[5] = 0;
//
//        scaledImageWidth = (int)(imageWidth * value[0]);
//        scaledImageHeight = (int)(imageHeight * value[4]);
//        if ( scaledImageWidth < width ) {
//            value[2] = (float) width / 2 - (float)scaledImageWidth / 2;
//        }
//        if ( scaledImageHeight < height ) {
//            value[5] = (float) height / 2 - (float)scaledImageHeight / 2;
//        }

        if ( biggerThanViewWidth ) value[2] = calMidTranslateFactor(imageWidth, width, minMag);
        if ( biggerThanViewHeight) value[5] = calMidTranslateFactor(imageHeight, height, minMag) + heightSupport;

        movedImageX = (int)value[2];
        movedImageY = (int)value[5];

        usingMatrix.setValues( value );
        setImageMatrix(usingMatrix);

    }

    private float calMidTranslateFactor( int imageFactor, int viewFactor,  float Mag ) {
        float transFactor = 0;
        int scaledFactor = (int)(imageFactor * Mag);
        transFactor = (float) viewFactor / 2 - (float) scaledFactor / 2;
        return transFactor;
    }

    private float calMinMaxMag( int imageWidth, int imageHeight, int viewWidth, int viewHeight ) {

        float mag = minMag;

//        Log.d(TAG, "calMinMaxMag: imageHeight = " + imageHeight);
//        Log.d(TAG, "calMinMaxMag: ViewHeight = " + viewHeight);

        if ( imageWidth > viewWidth || imageHeight > viewHeight ) {
//            Width matches ViewWidth
//            target = WIDTH;

//            Height matches ViewHeight
            target = HEIGHT;
            if ( imageWidth < imageHeight ) target = HEIGHT;
            if ( target == WIDTH ) mag = ((float)viewWidth / imageWidth) * minMagSupport ;
            if ( target == HEIGHT ) mag = ((float)viewHeight / imageHeight) * minMagSupport;
//            Log.d(TAG, "calMinMaxMag: " + mag);


            // Default Magnification set
            // 이미지크기 2400 / 뷰크기 720 = 0.3
            // 최소 이미지 배율 나옴
            minMag = mag;
            // 최대 이미지 배율
            setMaxMag( minMag, maxMagnification );
            defaultMag = minMag;

            return minMag;

        }

        return defaultMag;

    }

    private void setMaxMag ( float minMag, float maxMagnification ){
        maxMag = minMag * ( 1 + (0.1f * maxMagnification));
//        Log.d(TAG, "setMaxMag: " + minMag );
//        Log.d(TAG, "setMaxMag: " + maxMag );
//        Log.d(TAG, "setMaxMag: " + 0.1f * maxMagnification);
//        Log.d(TAG, "setMaxMag: " + maxMagnification);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 이미지 터치 전에 할 일
        beforeTouch( mode, usingMatrix, event );

        ImageView imageView = (ImageView)v;

        switch ( event.getAction() & MotionEvent.ACTION_MASK ) {
            case MotionEvent.ACTION_DOWN :
                mode = DRAG;
                select = START;
                savedMatrix1.set(usingMatrix);
                start.set( event.getX(), event.getY() );
//                posX1 = (int) event.getX();
//                posY1 = (int) event.getY();
//                Log.d(TAG, "ACTION_DOWN");
//                Log.d(TAG, "MODE = DRAG");
//                Log.d(TAG, "POST : " + posX1 + " : " + posY1);
                break;

            case MotionEvent.ACTION_UP :
//                Log.d(TAG, "ACTION_UP");
                mode = NONE;
                select = select == START ? DONE : NONE;
                break;     // 첫번째 손가락을 떼었을 경우

            case MotionEvent.ACTION_POINTER_UP :    // 두번째 손가락을 떼었을 경우
                mode = NONE;
                select = NONE;
                break;

            case MotionEvent.ACTION_POINTER_DOWN :
                //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치 줌으로 판별
                mode = ZOOM;
                newDist = spacing(event);
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix1.set(usingMatrix);
                    midpoint( mid, event );
                }

                select = NONE;

//                Log.d(TAG, "ACTION_POINTER_DOWN");
//                Log.d(TAG, "MODE = ZOOM");
//                Log.d(TAG, "NEW_DIST : " + newDist);
//                Log.d(TAG, "OLD_DIST : " + oldDist);
                break;


            case MotionEvent.ACTION_MOVE :

                if ( mode == DRAG ) { //드래그 중
//                    posX2 = (int) event.getX();
//                    posY2 = (int) event.getY();
                    float distX = event.getX() - start.x;
                    float distY = event.getY() - start.y;


                    //usingMatrix 안의 X,Y로
                    if (Math.abs( distX ) > 10 || Math.abs( distY ) > 10 ) {
                        usingMatrix.set(savedMatrix1);
                        usingMatrix.postTranslate( distX * dragSensitivity, distY * dragSensitivity);

                        select = NONE;

//                        Log.d(TAG, "ACTION_MOVE");
//                        Log.d(TAG, "MODE = DRAGGING");
//                        Log.d(TAG, "POST : " + posX2 + " : " + posY2);

                    }

                } else if ( mode == ZOOM ) { //핀치 중

                    newDist = spacing( event );

                    if ( Math.abs(newDist - oldDist) > 10 ) {


                        savedMatrix1.set(usingMatrix);
//                        아래 코드는 수정하거나 삭제하지 말길
//                        usingMatrix.set(savedMatrix1);

                        float scale = (newDist / oldDist);
                        float scaleRevision = (scale -1) * pinchSensitivity;
                        scale = 1 + scaleRevision;

//                        Log.d(TAG, "ACTION_MOVE");
//                        Log.d(TAG, "ZOOM IN!");
//                        Log.d(TAG, "MODE = PINCH_ZOOMING");
//                        Log.d(TAG, "NEW_DIST : " + newDist);
//                        Log.d(TAG, "OLD_DIST : " + oldDist);
//                        Log.d(TAG, "Scale : " + scale);

                        oldDist = newDist;

//                        Log.d(TAG, "onTouch: SCALE " + scale );
//                        Log.d(TAG, "onTouch: 포스트 전 " + usingMatrix.toString() + " mode = " + mode);
                        float[] val = new float[9];
                        usingMatrix.getValues(val);
                        usingMatrix.postScale(scale, scale, mid.x, mid.y);

                        select = NONE;

                    }

                }

                break;

        }

        // 매트릭스 값 튜닝
        matrixTurning(usingMatrix, imageView);

        imageView.setImageMatrix(usingMatrix);

        // 이미지 터치 후에 할 일
        afterTouch( mode, usingMatrix, event );


//        return false; // getAction()에서 ACTION_DOWN 만 서치됨.
        return true;
    }

    private float spacing( MotionEvent event ) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt( x * x + y * y );
    }

    private void midpoint(PointF point, MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        point.set(x, y);
    }

    protected void matrixTurning( Matrix matrix, ImageView imageView ) {

        // 매트릭스 값
        float[] value = new float[9];
        matrix.getValues(value);
        float[] savedValue = new float[9];
        savedMatrix1.getValues(savedValue);

        //뷰 크기
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        //이미지 크기
        Drawable drawable = imageView.getDrawable();
        if ( drawable == null ) return;

        // getIntrinsic : 이미지뷰에 있는 src 이미지의 실제 사이즈
        // drawable 디렉토리에 있을 경우 디바이스의 Density 비율에 따라 자동 확대
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        scaledImageWidth = (int)(imageWidth * value[0]);
        scaledImageHeight = (int)(imageHeight * value[4]);

        // 이미지가 밖으로 나가지 않도록
        // 우, 하 고정
        if (value[2] < width - scaledImageWidth) value[2] = width - scaledImageWidth;
        if (value[5] < height - scaledImageHeight) value[5] = height - scaledImageHeight;
        // 좌, 상 고정
        if (value[2] > 0) value[2] = 0;
        if (value[5] > 0) value[5] = 0;

        // maxMag 이상 확대하지 않도록
        if (value[0] > maxMag || value[4] > maxMag) {
            value[0] = savedValue[0];
            value[4] = savedValue[4];
            value[2] = savedValue[2];
            value[5] = savedValue[5];
        }

        // 화면보다 작게 축소하지 않도록
        if ( value[0] < minMag || value[4] < minMag ) {
            value[0] = value[4] = minMag;
            value[2] = savedValue[2];
            value[5] = savedValue[5];
        }
//        if ( imageWidth > width || imageHeight > height ) {
//            if (scaledImageWidth < width && scaledImageHeight < height) {
//                int target = WIDTH;
//                if (imageWidth < imageHeight) target = HEIGHT;
//                if (target == WIDTH) value[0] = value[4] = (float) width / imageWidth;
//                if (target == HEIGHT) value[0] = value[4] = (float) height / imageHeight;
//
//                scaledImageWidth = (int) (imageWidth * value[0]);
//                scaledImageHeight = (int) (imageHeight * value[4]);
//
//                if (scaledImageWidth > width) value[0] = value[4] = (float) width / imageWidth;
//                if (scaledImageHeight > height) value[0] = value[4] = (float) height / imageHeight;
//            }
//        }
        // 원래부터 작은 것을 본래보다 작게 하지 않도록
//        else {
//            if (value[0] < 1) value[0] = 1;
//            if (value[4] < 1) value[4] = 1;
//        }

        // 그리고 가운데 위치하도록
        scaledImageWidth = (int)(imageWidth * value[0]);
        scaledImageHeight = (int)(imageHeight * value[4]);
//        Log.d("123123", scaledImageWidth + " / " + scaledImageHeight);

        // 뷰와 확대된 이미지 비교
        // 가운데 위치
        if ( scaledImageWidth > width ) biggerThanViewWidth = true;
        else biggerThanViewWidth = false;
        if ( scaledImageHeight > height ) biggerThanViewHeight = true;
        else biggerThanViewHeight = false;
        if ( !biggerThanViewWidth ) value[2] = calMidTranslateFactor(imageWidth, width, value[0]);
        if ( !biggerThanViewHeight ) value[5] = calMidTranslateFactor(imageHeight, height, value[4]);

//        if ( scaledImageWidth < width ) {
//            Log.d("34", "NEVER@");
//            value[2] = (float) width / 2 - (float)scaledImageWidth / 2;
//        }
//        if ( scaledImageHeight < height ) {
//
//            value[5] = (float) height / 2 - (float)scaledImageHeight / 2;
//        }

        movedImageX = (int)value[2];
        movedImageY = (int)value[5];

        matrix.setValues( value );
//        Log.d(TAG, "matrixTurning: " + matrix.toString());
        savedMatrix2.set( matrix );
    }


    //Override 용 함수들
    public void afterTouch ( int mode, Matrix matrix, MotionEvent event ) {}
    public void beforeTouch ( int mode,Matrix matrix, MotionEvent event ) {}
    public void afterInit ( ) {}


    //getter setter
    public int getMaxMagnification() {
        return maxMagnification;
    }

    public void setMaxMagnification(int maxMagnification) {
        this.maxMagnification = maxMagnification;
    }

    public float getDragSensitivity() {
        return dragSensitivity;
    }

    public void setDragSensitivity(float dragSensitivity) {
        this.dragSensitivity = dragSensitivity;
    }

    public float getPinchSensitivity() {
        return pinchSensitivity;
    }

    public void setPinchSensitivity(float pinchSensitivity) {
        this.pinchSensitivity = pinchSensitivity;
    }

    public float getMaxMag() {
        return maxMag;
    }

    public void setMaxMag(float maxMag) {
        setMaxMag( minMag, maxMagnification );
        this.maxMag = maxMag;
    }

    public float getMinMag() {
        return minMag;
    }

    public void setMinMag(float minMag) {
        this.minMag = minMag;
    }

    public float getDefaultMag() {
        return defaultMag;
    }

    public void setDefaultMag(float defaultMag) {
        this.defaultMag = defaultMag;
    }

    public int getScaledImageWidth() {
        return scaledImageWidth;
    }

    public int getScaledImageHeight() {
        return scaledImageHeight;
    }

    public int getMovedImageX() {
        return movedImageX;
    }

    public int getMovedImageY() {
        return movedImageY;
    }

    public int getViewWidth(){
        return this.getWidth();
    }

    public int getViewHeight(){
        return this.getHeight();
    }

    public Matrix getUsingMatrix() {
        return usingMatrix;
    }

    public void setUsingMatrix(Matrix usingMatrix) {
        this.usingMatrix = usingMatrix;
    }
}
