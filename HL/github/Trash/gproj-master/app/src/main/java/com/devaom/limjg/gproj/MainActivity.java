package com.devaom.limjg.gproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends Activity {
    ImageViewTouchable imageView;
    FrameLayout mainLayout;
    LinearLayout linearLayout_floatingButton;
    LinearLayout linearLayout2_floatingButton;
    TextView textView_floatingButton;
    static float imgSizeWidth, imgSizeHeight, imgSizeHeightGetYHeight, statusBarHeight, softKeyHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (FrameLayout)findViewById(R.id.mainLayout);

        linearLayout_floatingButton = new LinearLayout(this);
        linearLayout_floatingButton.setPadding(0,0,0,30);
        linearLayout_floatingButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout2_floatingButton = new LinearLayout(this);
        linearLayout2_floatingButton.setLayoutParams(new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout2_floatingButton.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout_floatingButton.setOrientation(LinearLayout.VERTICAL);
        textView_floatingButton = new TextView(this);
        textView_floatingButton.setText(" ▽ 터치하세요! ▽ ");
        textView_floatingButton.setTextSize(20f);
        textView_floatingButton.setTextColor(getResources().getColor(R.color.textColor));
        textView_floatingButton.setGravity(Gravity.CENTER);

        linearLayout_floatingButton.setBackgroundResource(R.color.selectionColor);
        linearLayout_floatingButton.addView(textView_floatingButton);
        linearLayout_floatingButton.addView(linearLayout2_floatingButton);
        linearLayout_floatingButton.setGravity(Gravity.CENTER);
        linearLayout2_floatingButton.setGravity(Gravity.CENTER);
        //mainLayout.addView(linearLayout_floatingButton);

        Button buttonLiving_floatingButton = new Button(this);
        Button buttonFood_floatingButton = new Button(this);

        buttonFood_floatingButton.setText("먹거리");
        buttonFood_floatingButton.setTextColor(getResources().getColor(R.color.textColor));
        buttonFood_floatingButton.setTextSize(22);
        buttonFood_floatingButton.setBackgroundResource(R.drawable.food);

        buttonLiving_floatingButton.setText("숙박");
        buttonLiving_floatingButton.setTextColor(getResources().getColor(R.color.textColor));
        buttonLiving_floatingButton.setTextSize(22);
        buttonLiving_floatingButton.setBackgroundResource(R.drawable.house);

        LinearLayout.LayoutParams position = new LinearLayout.LayoutParams(1000, 300, 2);

        linearLayout2_floatingButton.addView(buttonLiving_floatingButton, position);
        linearLayout2_floatingButton.addView(buttonFood_floatingButton, position);
        linearLayout2_floatingButton.setVisibility(LinearLayout.GONE);

        buttonFood_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(textView_floatingButton.getText().toString(), "맛집");
            }
        });
        buttonLiving_floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(textView_floatingButton.getText().toString(), "숙박");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendIntent(String stationName, String category){
        Intent intent = new Intent(MainActivity.this, StationActivity.class);
        intent.putExtra("station",String.valueOf( stationName ));
        intent.putExtra("category",String.valueOf( category ));
        startActivity(intent);
    }

    void setFBText(String text){
        textView_floatingButton.setText(text);
    }

    void setFBVisibility(int visibility){
        if(visibility == View.VISIBLE) {
            linearLayout_floatingButton.setVisibility(View.VISIBLE);
            linearLayout2_floatingButton.setVisibility(View.VISIBLE);
            //Log.v("Visibility","VISIBLE");
        }else if(visibility == View.INVISIBLE){
            linearLayout_floatingButton.setVisibility(View.INVISIBLE);
            //Log.v("Visibility","INVISIBLE");
        }else{
            Log.v("ERR","FloatingButton Visibility 에러");
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ResolutionManager resolutionManager = new ResolutionManager(this);
        imgSizeWidth = resolutionManager.getxWidth();
        imgSizeHeight = resolutionManager.getImageViewSize();
        imgSizeHeightGetYHeight = resolutionManager.getyHeight();
        statusBarHeight = resolutionManager.getStatusBarHeight();
        softKeyHeight = resolutionManager.getSoftKeyHeight();

        imageView = new ImageViewTouchable(this);
        imageView.setImageResource(R.drawable.metro_map50);
        imageView.setDrawingCacheEnabled(false);
        mainLayout.removeAllViews();
        mainLayout.addView(imageView);
        mainLayout.addView(linearLayout_floatingButton);
        Log.v("FocusChanged","A");
    } // 좀 유의깊게 봐야할 듯. mainLayout을 removeAllViews 하지 않으면 에러 발생..
}

class ImageViewTouchable extends ImageView{ // Pinch-Zoom이 가능하게 해주는 Class 이면서 특정 영역을 터치했을 때 해당 영역이 어떤 역의 영역인지를 반환한다.
    private Context context;
    float density = getResources().getDisplayMetrics().density/3.5f;
    //final float originalDensity = getResources().getDisplayMetrics().density;
    private float minimumScale = 1.9535f;
    private float maximumScale = 5.0f;
    private float scale = 5.0f;
    private float xPointer[] = new float[2];
    private float yPointer[] = new float[2];
    private float movementX = 0;
    private float movementY = 0;

    float imgWidth = MainActivity.imgSizeWidth;
    float imgHeight = MainActivity.imgSizeHeight;
    float imgSizeGetYHeight = MainActivity.imgSizeHeightGetYHeight;
    float HeightForStatusSoft = MainActivity.statusBarHeight + MainActivity.softKeyHeight;
    float modelFactor = 1f;
    private float initX = imgWidth/2;
    private float initY = imgHeight/2;
    private boolean moveDetect = false;
    private double firstDist, secondDist, secondMinusFirstDist;
    float realImageHeight;
    //EditText densityMinus;
    public ImageViewTouchable(final Context context) {
        super(context);
        super.setClickable(true);
        this.context = context;

        realImageHeight = 1952 * this.imgWidth / 2408 ; //여백을 제외한 실제 이미지의 높이

        setScaleX(maximumScale);
        setScaleY(maximumScale);
        setPivotX(initX);
        setPivotY(initY);

        /*
        Log.v("Model",Build.MODEL); //NEXUS 5X는 modelFactor = 0.8f
        if(Build.MODEL.contains("LG-H791")){ //NEXUS 5X
            modelFactor = 0.8f;
        }else if(Build.MODEL.contains("SM-N920L")){ // Note 5
            modelFactor = 1f;
        }else if(Build.MODEL.contains("SM-G935")){ // S7 Edge
            modelFactor = 0.8f;
        }
        */

        if(Build.MODEL.contains("LG")){
            HeightForStatusSoft = MainActivity.statusBarHeight + MainActivity.softKeyHeight;
        }else if(Build.MODEL.contains("SM")){
            HeightForStatusSoft = 0;
        }
        Log.v("MODEL",Build.MODEL);

        /*
        densityMinus = (EditText)((MainActivity)context).findViewById(R.id.densityMinus);
        Button densityPlus = (Button)((MainActivity)context).findViewById(R.id.densityPlus);
        TextView densityText = (TextView)((MainActivity)context).findViewById(R.id.densityTextView);
        densityText.setText("Model = "+Build.MODEL);
        densityMinus.setText(String.valueOf(modelFactor));
        densityPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                modelFactor = Float.valueOf(densityMinus.getText().toString());
                Toast.makeText(context,"modelFactor = "+modelFactor,Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                if( event.getPointerCount() < 2 && event.getActionIndex() < 2) {
                    xPointer[event.getPointerId(event.getActionIndex())] = event.getX(event.getPointerId(event.getActionIndex()));
                    yPointer[event.getPointerId(event.getActionIndex())] = event.getY(event.getPointerId(event.getActionIndex()));
                    moveDetect = true;
                    Log.v("DOWNTOUCH", "(" + event.getX() + "," + event.getY() + ")");
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if( event.getPointerCount() < 3 ) {
                    xPointer[event.getPointerId(event.getActionIndex())] = event.getX(event.getPointerId(event.getActionIndex()));
                    yPointer[event.getPointerId(event.getActionIndex())] = event.getY(event.getPointerId(event.getActionIndex()));
                    firstDist = Math.sqrt(Math.pow(xPointer[0] - xPointer[1], 2) + Math.pow(yPointer[0] - yPointer[1], 2)); // 두번째 손가락을 짚는 순간 첫번째 거리가 정해짐.
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if(event.getPointerCount() == 1){
                    movementX = movementX - ( xPointer[0] - (event.getX()) );//x가 증가한다는 것은 pivot이 x 방향으로 가고있다는 것을 의미한다.
                    movementY = movementY - ( yPointer[0] - (event.getY()) );

                    setPivotX(initX - movementX);

                    if( initY - movementY >= initY + realImageHeight/2){
                        setPivotY( initY + realImageHeight/2 );
                        movementY = -realImageHeight/2;
                    }else if( initY - movementY <= initY - realImageHeight/2 ){
                        setPivotY( initY - realImageHeight/2 );
                        movementY = realImageHeight/2;
                    }else{
                        setPivotY( initY - movementY );
                    }

                    if( initX - movementX >= initX + initX ){
                        setPivotX( initX + initX );
                        movementX = -initX;
                    } else if (initX - movementX <= initX - initX ){
                        setPivotX( initX - initX );
                        movementX = initX;
                    } else {
                        setPivotX( initX - movementX );
                    }

                    if( (event.getX()) >= xPointer[0] +3 || (event.getX()) <= xPointer[0]-3 || (event.getY()) >= yPointer[0]+3 || (event.getY()) <= yPointer[0]-3 ){
                        moveDetect = false;
                    } //ACTION_DOWN 후 MOVE가 변길이 3 사각형을 벗어나면 Xml파싱을 하지않게되고 intent도 안넘김.
                }
                else if(event.getPointerCount() == 2){

                    try{
                    secondDist = Math.sqrt(Math.pow( (event.getX(event.getPointerId(0))) - (event.getX(event.getPointerId(1))), 2)
                            + Math.pow( (event.getY(event.getPointerId(0))) - (event.getY(event.getPointerId(1))), 2) );
                    secondMinusFirstDist = secondDist - firstDist;
                    scale = (float)(scale + secondMinusFirstDist / firstDist); // scale에 length/firstDist 값(즉, 거리변화값)이 지속적으로 누적되는 형태
                    if(scale >= maximumScale){
                        scale = maximumScale;
                    }else if(scale <= minimumScale){
                        scale = minimumScale;
                    }
                    setScaleX(scale);
                    setScaleY(scale);

                    }catch(IllegalArgumentException e){
                        Log.v("Exception","IllegalArguemntException!!");
                    }

                }else{
                    break;
                }
                break;

            case MotionEvent.ACTION_UP:
                final float RANGE = 10*density;

                if(moveDetect && event.getPointerCount() == 1){ // 한개 손가락으로 moveDetect가 true일 경우.
                    String startTag = "";
                    int eventType;
                    float x = 0;
                    float y = 0;
                    String stationName = null;
                    try { // 이 구문의 목적은 pointers.xml에서 선택한 역이 어떤 역인지 알려주는 역할! 어떤역인지 저장할것. 만약 몇 호선인지까지 저장하게 되면 환승역은 골치아파짐.
                          // x, y, stationName은 하나의 station 종료태그를 지날때마다 초기화해야함.
                        XmlResourceParser pointers = getResources().getXml(R.xml.pointers);
                        breakOut:
                        while ( ( eventType = pointers.getEventType() ) != XmlResourceParser.END_DOCUMENT) {
                            switch ( eventType ){

                                case XmlResourceParser.START_DOCUMENT:
                                    break;

                                case XmlResourceParser.START_TAG:
                                    startTag = pointers.getName();
                                    break;

                                case XmlResourceParser.TEXT:
                                    float xFactor = ((event.getX() - imgWidth/2) / (imgWidth / 2))*( 1440 - imgWidth );
                                    //float yFactor = ((event.getY() - imgHeight/2) / (imgHeight / 2)*( 2280 - imgHeight - HeightForStatusSoft) )*modelFactor;
                                    float yFactor = ((event.getY() - imgHeight/2) / (imgHeight / 2)*( 2280 - imgHeight - HeightForStatusSoft) );
                                    Log.v("currentModelFactor",String.valueOf(modelFactor));
                                    switch(startTag){
                                        case "x":
                                            x = Float.parseFloat( pointers.getText() ) - (1440 - imgWidth + xFactor)/2;
                                            Log.v("X-imgWidth",""+(event.getX() - imgWidth/2));
                                            Log.v("value",""+ xFactor);
                                            Log.v("DOWNTOUCH","("+event.getX()+","+event.getY()+")");
                                            break;
                                        case "y":
                                            y = Float.parseFloat( pointers.getText() ) - (2280 - imgHeight + yFactor)/2;
                                            Log.v("imgHeight",""+imgHeight);
                                            Log.v("imgSizeGetYHeight",""+imgSizeGetYHeight);
                                            Log.v("realImageHeight",""+realImageHeight);
                                            break;
                                        case "name":
                                            stationName = pointers.getText();
                                            break;
                                    }// START_TAG에서 저장되었던 startTag가 x, y, name를 판별해 그 값들을 일일이 저장해서 END_TAG 부분에서 터치한 좌표값이 해당 범위 내에 있는지 확인
                                    break;

                                case XmlResourceParser.END_TAG:
                                    if( pointers.getName().equals("station") && (event.getX()) >= x - RANGE && (event.getX()) <= x + RANGE &&
                                            (event.getY()) <= y + RANGE && (event.getY()) >= y - RANGE){ // 종료태그가 station이면
                                        // 종료태그가 station이고 터치한 좌표값이 위의 XmlResourceParser.TEXT의 case에서 저장한 범위 안에 있으면,
                                        // 이에 해당하는 역명을 출력한다.
                                        Toast.makeText(context,stationName,Toast.LENGTH_SHORT).show();

                                        ((MainActivity)context).setFBVisibility(View.VISIBLE);
                                        ((MainActivity)context).setFBText(stationName);
                                        //((MainActivity)context).sendIntent(stationName);
                                        //startActivity 메서드는 이 클래스(ImageViewTouchable)에서는 호출할 수 없기때문에 이 방법을 사용함.
                                        break breakOut;
                                    }else{
                                        ((MainActivity)context).setFBVisibility(View.INVISIBLE);
                                    }
                                    break;

                            }
                            pointers.next();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        }
        invalidate();
        return super.onTouchEvent(event);
    }
}

class ResolutionManager{
    float xWidth, yHeight; //단말기의 디스플레이(스크린)사이즈
    float softKeyHeight = 0; //소프트키의 사이즈
    float statusBarHeight = 0;
    float searchBarHeight = 0;
    Activity activity;

    public ResolutionManager(Activity activity){
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        xWidth = size.x;
        yHeight = size.y;
        Log.v("SIZE:DISPLAY",xWidth+","+yHeight);

        Resources res = activity.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height","dimen","android");
        if(resourceId > 0){
            softKeyHeight = res.getDimensionPixelSize(resourceId);
        }
        Log.v("SIZE:SOFTKEY",String.valueOf(softKeyHeight));

        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        statusBarHeight = rectgle.top;
        Log.v("SIZE:STATUSBAR",String.valueOf(statusBarHeight));
        // 단 이거는 onWindowsFocusChanged에서 실행할것..

        RelativeLayout searchBar = (RelativeLayout)activity.findViewById(R.id.searchBar);
        searchBarHeight = searchBar.getHeight();
        Log.v("SIZE:SEARCHBAR_WH",searchBar.getWidth()+","+searchBar.getHeight());
        Log.v("SIZE:SEARCHBAR_X,Y",searchBar.getLeft()+","+searchBar.getTop());
        Log.v("SIZE:SEARCHBAR_X,Y",searchBar.getLeft()+","+searchBar.getY());
        Log.v("SIZE:SEARCHBAR_Bottom",""+searchBar.getBottom());
        //이거도 onWindowsFocusChanged에서 실행되어야.
    }

    public float getxWidth(){
        return xWidth;
    }

    public float getyHeight(){
        return yHeight;
    }

    public float getSoftKeyHeight(){
        return softKeyHeight;
    }

    public float getStatusBarHeight(){
        return statusBarHeight;
    }

    public float getSearchBarHeight(){
        return searchBarHeight;
    }

    public float getImageViewSize(){
        if(isLGPhone() == false) {
            Log.i("phoneType", "Not LGPhone");
            return getyHeight() - getStatusBarHeight() - getSearchBarHeight();
        }else if(isLGPhone() == true){
            Log.i("phoneType", "LGPhone");
            return getyHeight() - getStatusBarHeight() - getSearchBarHeight() - getSoftKeyHeight(); // 맞을지 확인하기.
        }else{
            return -1;
        }
    }

    public boolean isLGPhone(){
        //메뉴버튼 존재유무
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        //뒤로가기버튼 존재유무
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) { // lg폰 소프트키일 경우
            Log.i("phoneType", "LGPhone");
            return true;
        } else { // 삼성폰 등.. 메뉴 버튼, 뒤로가기 버튼 존재
            Log.i("phoneType", "Not LGPhone");
            return false;
        }
    }
}