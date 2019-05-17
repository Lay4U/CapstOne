package com.devaom.limjg.gproj;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ShopFragment extends Fragment {
    Bundle bundle;
    TextView textView_like;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();
        TextView textView_title = (TextView)getActivity().findViewById(R.id.textView_title);
        TextView textView_description = (TextView)getActivity().findViewById(R.id.textView_Description);
        TextView textView_telephone = (TextView)getActivity().findViewById(R.id.textView_telephone);
        TextView textView_category = (TextView)getActivity().findViewById(R.id.textView_category);
        textView_like = (TextView)getActivity().findViewById(R.id.textView_like);
        Button button_hitLike = (Button)getActivity().findViewById(R.id.button_hitLike);

        textView_title.setText(bundle.getString("title"));
        textView_description.setText(bundle.getString("description"));
        textView_telephone.setText(bundle.getString("telephone"));
        textView_category.setText(bundle.getString("category"));

        button_hitLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    Thread hitLikeThread = new Thread() {
                    StringBuilder sb;
                        @Override
                        public void run() {
                            super.run();

                            try{
                                SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                String nickname = pref.getString("nickname","NO NICKNAME"); //만일 nickname 값이 존재하지 않을 경우에 두번째 인자를 리턴함.

                                String link = "http://" + StationActivity.serverIP + "/hitlike.php";
                                String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(bundle.getString("title"), "UTF-8")
                                    + "&" + URLEncoder.encode("userid","UTF-8") + "=" + URLEncoder.encode(nickname,"UTF-8");

                                URL url = new URL(link);
                                URLConnection conn = url.openConnection();

                                conn.setDoOutput(true);
                                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                                wr.write(data);
                                wr.flush();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                sb = new StringBuilder();
                                String line = null;

                                // Read Server Response
                                while((line = reader.readLine())!=null){
                                    sb.append(line);
                                    break;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    hitLikeThread.start();
                try {
                    hitLikeThread.join();
                }catch(Exception e){
                    e.printStackTrace();
                }
                    Toast.makeText(getContext(), "좋아요!", Toast.LENGTH_SHORT).show();
            }
        });

        Thread countLikeThread = new Thread(){
            @Override
            public void run() {
                super.run();

                try{
                    String link = "http://" + StationActivity.serverIP + "/countlike.php";
                    String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(bundle.getString("title"), "UTF-8");
                    //String data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode("test", "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    Log.v("countLike",sb.toString());
                    JSONObject tempJSONObject = new JSONObject(sb.toString());
                    JSONArray tempJSONArray = tempJSONObject.getJSONArray("result");
                    JSONObject tempJSONObject2 = tempJSONArray.getJSONObject(0);
                    textView_like.setText(tempJSONObject2.getString("count(*)")+"개");

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        countLikeThread.start();

        try{
            countLikeThread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        //textView_like.setText();


        //linearLayout = new LinearLayout(getContext());
        //textView = new TextView(getContext());
        //textView.setText("Test");
        //linearLayout.addView(textView);
        //getActivity().setContentView(linearLayout); //getActivity는 현재 실행중인 Activity를 얻어오게 됨. 즉 이거를 쓰면 안된다.
    }
}