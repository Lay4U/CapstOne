package com.devaom.limjg.gproj;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StationActivity extends AppCompatActivity { // MainActivity로부터 Intent로 받은 '터치된 역'을 알아내어 그 역에 대한 주변 상가의 정보를 보여준다.
    public static String serverIP; //서버 IP
    public static String clientId; //애플리케이션 클라이언트 아이디값";
    public static String clientSecret; //애플리케이션 클라이언트 시크릿값";
    private static final String TAG_RESULTS = "result";
    public static String selectedStation, selectedCategory;
    private String myJSON;
    private JSONArray stationArray = null;
    private StringBuilder sb;
    private TextView textViewStationName;
    public ListViewAdapter listViewAdapter;
    private ShopListFragment shopListFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_station);

        clientId = getString(R.string.client_id);
        clientSecret = getString(R.string.client_secret);
        serverIP = getString(R.string.server_ip);

        textViewStationName = (TextView)findViewById(R.id.textViewStationName);
        sb = new StringBuilder();

        selectedStation = new Intent( getIntent() ).getStringExtra("station");
        selectedCategory = new Intent( getIntent() ).getStringExtra("category");
        textViewStationName.setText(selectedStation+"역");

        //getData("http://"+serverIP+"/getdata.php");
        listViewAdapter = new ListViewAdapter();

        //listView.setAdapter(listViewAdapter); //만일 이것이 thread 안에서 사용되면 에러 발생!
        shopListFragment = new ShopListFragment();
        shopListFragment.setListAdapter(listViewAdapter);
        getSupportFragmentManager().beginTransaction().replace(R.id.listFrameLayout,shopListFragment).commit();

        //ImageJSONThread.setContext(this);

        try {
            ParseJSONThread parseJSONThread = new ParseJSONThread(listViewAdapter, 1);
            //GetNaverJSON 클래스를 통해 JSON을 받아서 파싱하는 스레드 클래스.
            parseJSONThread.start();
            parseJSONThread.join(); // 이걸 넣어야 해당 thread가 끝날때까지 Main thread가 대기함.

            ImageJSONThread imageJSONThread = new ImageJSONThread(listViewAdapter,this);
            // ParseJSONThread를 통해 ListViewAdapter에 저장된 각 점포명(title)을 키워드에 넣고 검색/이미지 API를 통해 thumbnail 이미지링크를 가져온다.
            // 그리고 해당 이미지링크를 listViewAdapter 객체의 각 ListItem에 저장.
            //imageJSONThread.setContext(this);
            imageJSONThread.start();
            imageJSONThread.join();

            GetImageThread getImageThread = new GetImageThread(listViewAdapter, this);
            //imageJSONThread로부터 각 listViewAdapter의 ListItem에 저장된 이미지링크에 연결해서 해당 이미지를 가져오고, 그 이미지들을 각 Shop 객체에 저장한다.
            getImageThread.start();
            getImageThread.join();

        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }


    /*
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            stationArray = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<stationArray.length();i++){
                JSONObject c = stationArray.getJSONObject(i);
                String title = c.getString("title");
                String description = c.getString("description");

                for(int j = 0 ; j < 10 ; j++) { // 나중에 삭제하기~
                    sb.append(title + "&");
                    sb.append(description + "\n");
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //tv.setText(sb.toString()); //현재 tv는 다른 이름으로 바꿔버렸다. 이것은 Log로 나중에 알아볼것~
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    */
}

class GetNaverJSON{ // 네이버 API를 이용하는 실질적인 Class
    private String response;
    private int display, start; //display는 출력 건수, start는 검색 시작 위치. start는 5씩 증가해야겠지.
    private String mode; // 지역검색인지, 이미지검색인지 결정한다.
    public static final String API_LOCAL = "local";
    public static final String API_IMAGE = "image";
    private String searchKeyword = "";

    public GetNaverJSON(){
        setStart(1);
        setDisplay(5);
        setMode(API_LOCAL);
        setSearchKeyword(searchKeyword);
    } // 디폴트 값들.

    public void request(){
        try {
            String text = URLEncoder.encode(searchKeyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/"+mode+"?query="+text+"&display="+display+"&start="+start;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", StationActivity.clientId);
            con.setRequestProperty("X-Naver-Client-Secret", StationActivity.clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());
            this.response = response.toString();
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDisplay(int display){
        this.display = display;
    }

    public void setStart(int start){
        this.start = start;
    }

    public void setMode(String mode){
        this.mode = mode;
    }

    public void setSearchKeyword(String searchKeyword){
        this.searchKeyword = searchKeyword;
    }

    public String getResponse(){
        response = response.replaceAll("&amp;","&");
        response = response.replaceAll("<b>","");
        response = response.replaceAll("</b>","");
        return response;
    }
}

class Shop implements Comparable<Shop> {
    private String title;
    private String description;
    private String telephone;
    private String address;
    private String imageLink = null;
    private int mapx = 0;
    private int mapy = 0;
    private float rate = 0;
    private Drawable thumbnail = null;
    private String category;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setMapx(int mapx){
        this.mapx = mapx;
    }
    public int getMapx(){
        return this.mapx;
    }
    public void setMapy(int mapy){
        this.mapy = mapy;
    }
    public int getMapy(){
        return this.mapy;
    }
    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }
    public String getImageLink(){
        return this.imageLink;
    }
    public Drawable getThumbnail(){
        return this.thumbnail;
    }
    public void setThumbnail(Drawable thumbnail){
        this.thumbnail = thumbnail;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    public String getTelephone(){
        return this.telephone;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCategory(){
        return this.category;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Shop o) {
        if(this.rate > o.rate){
            return 1;
        }else if(this.rate < o.rate){
            return -1;
        }else{
            return 0;
        }
    }
}

class ListViewAdapter extends BaseAdapter{

    private ArrayList<Shop> shopArrayList = new ArrayList<>();

    public void setShopArrayList(ArrayList<Shop> shopArrayList){
        this.shopArrayList = shopArrayList;
    }

    public ArrayList<Shop> getShopArrayList(){
        return shopArrayList;
    }

    @Override
    public int getCount() {
        return shopArrayList.size();
    } // Adapter에 사용되는 데이터의 개수 리턴. 필수로 구현

    @Override
    public Object getItem(int position) {
        return shopArrayList.get(position);
    } // 지정한 위치(position)에 있는 데이터 리턴. 필수로 구현.

    @Override
    public long getItemId(int position) {
        return position;
    } // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID리턴. 필수로 구현.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //인터넷에는 이거 쓰라고 나와있었음.
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.listViewImage);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView addrTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Shop shop = shopArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        if(shop.getThumbnail() == null) {
            Log.v("getThumbnail","null");
        }

        if(shop.getThumbnail() != null)
            thumbnailImageView.setImageDrawable(shop.getThumbnail());

        titleTextView.setText(shop.getTitle());
        addrTextView.setText(shop.getAddress());

        return convertView;
    }

    public void addItem(String title, String addr, int mapx, int mapy, String description, String telephone, String category) {
        Shop shop = new Shop();

        shop.setTitle(title);
        shop.setAddress(addr);
        shop.setMapx(mapx);
        shop.setMapy(mapy);
        shop.setDescription(description);
        shop.setTelephone(telephone);
        shop.setCategory(category);

        shopArrayList.add(shop);
    }

    public void setImageLink(int position, String imageLink){
        //imageLink = "기본값";
        Shop shop = (Shop)getItem(position);
        shop.setImageLink(imageLink);
        Log.v("inadaptimagelink",imageLink);
        shopArrayList.set(position,shop);
    }
}

class ParseJSONThread extends Thread { // GetNaverJSON에서 JSON을 받아서 해당 데이터를 파싱하고, 여기서 새로 shopArrayList 객체를 만들어서 listViewAdapter에 등록함.
    private ListViewAdapter listViewAdapter;
    private int start = 1;
    private int addMode = 0;

    public ParseJSONThread(ListViewAdapter listViewAdapter, int start){
        this.listViewAdapter = listViewAdapter;
        this.start = start;
    }

    @Override
    public void run() {
        ArrayList<Shop> shopArrayList = listViewAdapter.getShopArrayList();

        try {
            GetNaverJSON naverJSON = new GetNaverJSON();
            naverJSON.setStart(start);
            naverJSON.setSearchKeyword(StationActivity.selectedStation+"역 "+StationActivity.selectedCategory);
            naverJSON.request();

            JSONObject JSONObj = new JSONObject(naverJSON.getResponse());
            JSONArray JSONArr = JSONObj.getJSONArray("items");

            for(int i=0;i<JSONArr.length();i++) {
                JSONObject c = JSONArr.getJSONObject(i);
                String title = c.getString("title");
                String description = c.getString("description");
                String telephone = c.getString("telephone");
                String address = c.getString("address");
                String category = c.getString("category");
                int mapx = c.getInt("mapx");
                int mapy = c.getInt("mapy");

                Shop shop = new Shop();
                shop.setTitle(title);
                shop.setDescription(description);
                shop.setTelephone(telephone);
                shop.setAddress(address);
                shop.setCategory(category);
                shop.setMapx(mapx);
                shop.setMapy(mapy);

                if(addMode == 0) { // 뒤에 추가
                   shopArrayList.add(shop);
                }else if(addMode == 1){ // 앞에 추가
                    shopArrayList.add(i, shop); // 0번 앞에, 1번 앞에, 2번 앞에, 3번 앞에 ....
                }
                //listViewAdapter.addItem(title,address,mapx,mapy,description,telephone,category); //listViewAdapter,addItem 메소드는 해당 파라미터들을 통해 Shop 객체를 생성한다.
            }
            listViewAdapter.setShopArrayList(shopArrayList);
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

    public void setAddMode(int addMode){
        this.addMode = addMode;
    }
}

class ImageJSONThread extends Thread{
    Context context;
    Shop shop;
    ListViewAdapter listViewAdapter;

    /*
    public ImageJSONThread (ListViewAdapter listViewAdapter){
        this.listViewAdapter = listViewAdapter;
    }
    */
    public ImageJSONThread (ListViewAdapter listViewAdapter, Context context){
        this.listViewAdapter = listViewAdapter;
        this. context = context;
    }

    public void setContext(Context c) {
        context = c;
    }

    @Override
    public void run() {
        for(int i = 0; i < listViewAdapter.getCount(); i++) {
            try {
                Log.v("ListViewAdaptercount",String.valueOf(listViewAdapter.getCount()));
                shop = (Shop) listViewAdapter.getItem(i);

                // Thumbnail을 위한 로직
                GetNaverJSON getNaverJSON = new GetNaverJSON();
                getNaverJSON.setSearchKeyword(shop.getTitle());
                getNaverJSON.setDisplay(1);
                getNaverJSON.setMode(GetNaverJSON.API_IMAGE);
                getNaverJSON.request();

                JSONObject jsonObject = new JSONObject(getNaverJSON.getResponse());
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                listViewAdapter.setImageLink(i, jsonObject2.getString("thumbnail"));

                //여기부터 점포명에 특정 문자열이 포함되면 섬네일 이미지링크를 대체함.
                String startTag = "";
                String name = "";
                String img = "";
                XmlResourceParser shopimg = context.getResources().getXml(R.xml.shopimg);

                int eventType;
                breakOut :
                while ( (eventType = shopimg.getEventType()) != XmlResourceParser.END_DOCUMENT) {
                    switch(eventType){
                        case XmlResourceParser.START_DOCUMENT:
                            break;
                        case XmlResourceParser.START_TAG:
                            startTag = shopimg.getName();
                            break;
                        case XmlResourceParser.TEXT:
                            switch(startTag){
                                case "name":
                                    name = shopimg.getText();
                                    break;
                                case "img":
                                    img = shopimg.getText();
                                    break;
                            }
                            break;
                        case XmlResourceParser.END_TAG:
                            if(shopimg.getName().equals("shop") && shop.getTitle().contains(name)){
                                listViewAdapter.setImageLink(i, img);
                                break breakOut;
                            }
                            break;
                    }
                    shopimg.next();
                }
                //여기까지

            } catch (Exception e) {
                e.printStackTrace();
            }
        } //for문 종료
    }
}

class GetImageThread extends Thread{
    private Bitmap bitmap;
    private ListViewAdapter listViewAdapter;
    private Context context;
    private int start = 0;

    public GetImageThread(ListViewAdapter listViewAdapter, Context context){
        this.listViewAdapter = listViewAdapter;
        this.context = context;
    }

    @Override
    public void run() {
        int i;
        for( i = start ; i < listViewAdapter.getCount() ; i++) {
            bitmap = null;
            try {
                Shop shop = (Shop) listViewAdapter.getItem(i);
                URL url = new URL(shop.getImageLink());
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.connect();

                InputStream is = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                bitmap = Bitmap.createScaledBitmap(bitmap, 1500, 1500, true);

            }catch(Exception e){
                e.printStackTrace();
            }

            Shop shop = (Shop)listViewAdapter.getItem(i);
            if(bitmap != null)
                shop.setThumbnail(new BitmapDrawable(bitmap));
            else if(bitmap == null) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.noimg);
                shop.setThumbnail(drawable);

            }
        }
        start = i;
    }

    public void setStart(int start){
        this.start = start;
    }

}