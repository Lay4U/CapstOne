package com.devaom.limjg.gproj;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopListFragment extends ListFragment {
    int start = 1; // 전역변수로 선언한 이유는 클릭시 ShopFragment로 넘어가기 때문.
    static boolean lastItemVisibleFlag;
    static boolean firstItemVisibleFlag;
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Shop tempShop = (Shop)l.getAdapter().getItem(position);

        //NMapFragment 부분
        Bundle pointerBundle = new Bundle(2);
        pointerBundle.putString("title", tempShop.getTitle());
        pointerBundle.putInt("mapX", tempShop.getMapx());
        pointerBundle.putInt("mapY", tempShop.getMapy());

        MapFragment tempMapFrag = new MapFragment();
        tempMapFrag.setArguments(pointerBundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.station_map, tempMapFrag);
        fragmentTransaction.commit();

        //ShopFragment 부분
        Bundle shopDetailBundle = new Bundle();
        shopDetailBundle.putString("title", tempShop.getTitle());
        shopDetailBundle.putString("description",tempShop.getDescription());
        shopDetailBundle.putString("telephone",tempShop.getTelephone());
        shopDetailBundle.putString("category",tempShop.getCategory());
        ShopFragment shopFragment = new ShopFragment();
        shopFragment.setArguments(shopDetailBundle);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listFrameLayout,shopFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        LinearLayout linearLayout_station_map = (LinearLayout)getActivity().findViewById(R.id.station_map);
        linearLayout_station_map.removeAllViews();
    }

    @Override
    public void onStart() {
        super.onStart();

        lastItemVisibleFlag = false; //화면에 리스트의 마지막 아이템이 보여지는지 체크
        firstItemVisibleFlag = false; // 화면에 리스트의 첫번째 아이템이 보여지는지 체크
        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            //int start = 1;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                ListViewAdapter listViewAdapter = (ListViewAdapter)getListAdapter();
                ArrayList<Shop> shopArrayList = listViewAdapter.getShopArrayList();

                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태.
                //즉 스크롤이 바닥에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag == true) {
                    //TODO 화면이 바닥에 닿을때 처리
                    Toast.makeText(getContext(), "바닥에 닿음", Toast.LENGTH_SHORT).show();
                    try {
                        while(shopArrayList.size() > 15) {
                            for (int i = 0; i < 5; i++) {
                                shopArrayList.remove(0);
                            }
                        }
                        listViewAdapter.setShopArrayList(shopArrayList);
                        //GetNaverJSON 클래스를 통해 JSON을 받아서 파싱하는 스레드 클래스.
                        ParseJSONThread parseJSONThread = new ParseJSONThread(listViewAdapter, start += 5);
                        parseJSONThread.start();
                        parseJSONThread.join(); // 이걸 넣어야 해당 thread가 끝날때까지 Main thread가 대기함.

                        // ParseJSONThread를 통해 ListViewAdapter에 저장된 각 점포명(title)을 키워드에 넣고 검색/이미지 API를 통해 thumbnail 이미지링크를 가져온다.
                        // 그리고 해당 이미지링크를 listViewAdapter 객체의 각 ListItem에 저장.
                        ImageJSONThread imageJSONThread = new ImageJSONThread(listViewAdapter, getContext());
                        imageJSONThread.start();
                        imageJSONThread.join();

                        //imageJSONThread로부터 각 listViewAdapter의 ListItem에 저장된 이미지링크에 연결해서 해당 이미지를 가져오고, 그 이미지들을 각 Shop 객체에 저장한다.
                        GetImageThread getImageThread = new GetImageThread(listViewAdapter, getContext());
                        getImageThread.start();
                        getImageThread.join();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    listViewAdapter.notifyDataSetChanged(); //ArrayAdapter에서 notifyDatasetChanged 메소드 꼭 호출하기~
                    setSelection(listViewAdapter.getCount() - 7);
                    Log.v("start",String.valueOf(start));
                }else if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItemVisibleFlag == true && start > 20){
                    //TODO 화면이 천장에 닿을때 처리
                    Toast.makeText(getContext(), "천장에 닿음", Toast.LENGTH_SHORT).show();

                    try {
                        while(shopArrayList.size() > 15) {
                            for (int i = 0; i < 5; i++) {
                                shopArrayList.remove(shopArrayList.size() - 1); // size는 갯수를 리턴한다. 마지막 index가 아니라.
                            }
                        }
                        listViewAdapter.setShopArrayList(shopArrayList);
                        //GetNaverJSON 클래스를 통해 JSON을 받아서 파싱하는 스레드 클래스.

                        ParseJSONThread parseJSONThread = new ParseJSONThread(listViewAdapter, (start -= 5) - 15);
                        parseJSONThread.setAddMode(1); //shopArrayList의 앞에 추가하는 모드.
                        parseJSONThread.start();
                        parseJSONThread.join(); // 이걸 넣어야 해당 thread가 끝날때까지 Main thread가 대기함.

                        // ParseJSONThread를 통해 ListViewAdapter에 저장된 각 점포명(title)을 키워드에 넣고 검색/이미지 API를 통해 thumbnail 이미지링크를 가져온다.
                        // 그리고 해당 이미지링크를 listViewAdapter 객체의 각 ListItem에 저장.
                        ImageJSONThread imageJSONThread = new ImageJSONThread(listViewAdapter, getContext());
                        imageJSONThread.start();
                        imageJSONThread.join();

                        //imageJSONThread로부터 각 listViewAdapter의 ListItem에 저장된 이미지링크에 연결해서 해당 이미지를 가져오고, 그 이미지들을 각 Shop 객체에 저장한다.
                        GetImageThread getImageThread = new GetImageThread(listViewAdapter, getContext());
                        getImageThread.start();
                        getImageThread.join();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    listViewAdapter.notifyDataSetChanged(); //ArrayAdapter에서 notifyDatasetChanged 메소드 꼭 호출하기~
                    setSelection(5);
                    Log.v("start",String.valueOf(start));
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                firstItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount == visibleItemCount);
            }
        });
    }
}