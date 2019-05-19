package blacksmith.sullivanway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.FavoriteRoute;
import blacksmith.sullivanway.database.MyDBOpenHelper;
import blacksmith.sullivanway.database.Station;
import blacksmith.sullivanway.dialog.StationMenuDialog;
import blacksmith.sullivanway.routeguidance.Route;
import blacksmith.sullivanway.routeguidance.RouteFinder;
import blacksmith.sullivanway.routeguidance.RouteWrapper;
import blacksmith.sullivanway.routeguidance.StationMatrix;
import blacksmith.sullivanway.utils.SubwayLine;
import blacksmith.sullivanway.utils.SubwayMapTouchPoint;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    public static final Locale DEFAULT_LOCALE = Locale.KOREAN;
    private static final int FAV_ACTIVITY_CODE = 0;
    private static final int PATH_INFO_ACTIVITY_CODE = 1;
    private static final int TRANS_SETTING_ACTIVITY_CODE = 2;

    private StationMatrix stationMatrix;
    private ArrayList<Station> stnIdx;
    private SubwayMapTouchPoint subwayMapTouchPoint;
    private SearchListAdapter searchListAdapter;

    private boolean isExit; //Back키 두번 누르면 종료
    private boolean isFabOpen; //FloatingAction Open/Close 여부
    private Animation fab_open, fab_close, rotate_forward, rotate_backward; //FloatingAction 애니메이션

    // View
    private FloatingActionButton fab, fab_favorite, fab_trans_map, fab_settings; //FloatingActionButton
    private PhotoView lineMapView;
    private SearchView searchView;
    private ListView searchList;
    private TextView startStnTextView, endStnTextView;
    private StationMenuDialog dialog;

    // Observables
    private Disposable loadingDbTaskObs = null;
    private Disposable findingRouteTaskObs = null;
    private Disposable testingSettingTaskObs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        setContentView(R.layout.activity_main);

        loadDatabaseAsync(); // 지하철 데이터를 가져온다. (비동기)

        isExit = false;
        isFabOpen = false;

        // 노선도 설정
        lineMapView = findViewById(R.id.line_map_view);
        lineMapView.setImageResource(R.drawable.naver_subway);
        lineMapView.setMaximumScale(2.8f); //최대확대크기 설정
        lineMapView.setMediumScale(2.0f); //중간확대크기 설정 (2.0f크기보다 축소된 상태에서 화면을 더블 터치하면 2.0f 중간확대크기로 확대된다
        lineMapView.setMinimumScale(1.0f); //최소축소크기 설정
        lineMapView.setScale(2.0f, true); //앱 시작할 때 기본 크기 설정
        lineMapView.setOnViewTapListener(new OnLineMapViewTab());

        // 검색바 이벤트 리스너 추가
        searchList = findViewById(R.id.search_list);
        searchListAdapter = new SearchListAdapter(this);
        searchList.setAdapter(searchListAdapter);
        searchList.setOnItemClickListener(new OnSearchListItemClick());
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new OnSearchViewQueryText());

        // 현재 선택된 출발역, 도착역을 표시하는 텍스트뷰
        startStnTextView = findViewById(R.id.startStnTextView);
        endStnTextView = findViewById(R.id.endStnTextView);
        startStnTextView.setOnClickListener(v -> {
            v.setVisibility(GONE);
            subwayMapTouchPoint.startStn = null;
        });
        endStnTextView.setOnClickListener(v -> {
            v.setVisibility(GONE);
            subwayMapTouchPoint.endStn = null;
        });

        // activity_main 의 플로팅 액션 버튼 동그라미
        fab = findViewById(R.id.fab);
        fab_favorite = findViewById(R.id.fab_favorite);
        fab_trans_map = findViewById(R.id.fab_trans_map);
        fab_settings = findViewById(R.id.fab_settings);
        fab.setOnClickListener(v -> {
            animateFAB(); // 버튼 클릭시 FloatingAction 애니메이션 시작
        });
        fab_favorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoriteRouteActivity.class);
            startActivityForResult(intent, FAV_ACTIVITY_CODE);
        });
        fab_trans_map.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransferMapListActivity.class);
            startActivity(intent);
        });
        fab_settings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, TRANS_SETTING_ACTIVITY_CODE);
        });

        // FloatingAction 애니메이션
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

    }

    @Override
    public void onBackPressed() {
        if (searchListAdapter.getCount() != 0) {
            searchView.setQuery("", false);
            searchListAdapter.clear();
            searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
            return;
        }

        if (isExit) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "종료하려면 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
            isExit = true;
            new Thread(() -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isExit = false;
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        if (loadingDbTaskObs != null)
            loadingDbTaskObs.dispose();
        if (findingRouteTaskObs != null)
            findingRouteTaskObs.dispose();
        if (testingSettingTaskObs != null)
            testingSettingTaskObs.dispose();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FAV_ACTIVITY_CODE:
                case PATH_INFO_ACTIVITY_CODE:
                    // intent로 받은 출발역, 도착역, 경유역에 대한 SubwayMapTouchPoint.StnPoint 객체를 완성
                    subwayMapTouchPoint.startStn = subwayMapTouchPoint.getStation(stnIdx, data.getStringExtra("startLineNm"), data.getStringExtra("startStnNm"));
                    subwayMapTouchPoint.endStn = subwayMapTouchPoint.getStation(stnIdx, data.getStringExtra("endLineNm"), data.getStringExtra("endStnNm"));

                    // 경로 계산 쓰레드 실행
                    findRouteAsync();
                    break;

                case TRANS_SETTING_ACTIVITY_CODE:
                    // 환승제한역 설정 테스트 쓰레드 실행
                    testSettingAsync();
                    break;
                default:
            }
        }
    }

    // 역 터치 dialog
    private void displayStationTouchDialog(final Station stn) {
        // Dialog 생성, 메뉴 리스너 설정
        dialog = new StationMenuDialog(MainActivity.this, subwayMapTouchPoint.getLineNms(stnIdx, stn), stn.getStnNm());
        dialog.setOnClickListener(v -> {
            switch (v.getId()) {
                /* 이 switch문에서는 위에서 받은 역 정보(stn)를 팝업메뉴에서 선택한 아이템에 따라
                 * lineMapView의 startStn(출발역), endStn(도착역), viaStn(경유역)에 '저장'하거나
                 * '역 정보 액티비티'(StnInfoPagerActivity)를 호출할 수 있어
                 *
                 * switch문 아래에 있는 '경로 안내 액티비티'(RouteGuidancePagerActivity)는
                 * 출발역(SubwayMapTouchPoint.startStn), 도착역(SubwayMapTouchPoint.endStn)이 둘 다 설정된 경우에만 호출되도록 했고,
                 * 출발역, 도착역 둘 다 설정하기 전에 경유역(SubwayMapTouchPoint.viaStn)을 설정하면 출발역-경유역-도착역 경로를 알려줘 **/
                case R.id.start: //출발역
                    subwayMapTouchPoint.startStn = stn; //SubwayMapTouchPoint map의 startStn에 출발역 정보를 저장한다
                    startStnTextView.setText(String.format("  %s: %s   ", getString(R.string.start_station), stn.getStnNm()));
                    startStnTextView.setVisibility(View.VISIBLE);
                    if (subwayMapTouchPoint.endStn == null) { //SubwayMapTouchPoint map의 endStn이 선택되었는지 검사하여
                        dialog.cancel();
                        return; //null이면 다이얼로그를 종료하고,
                    }
                    break; //null이 아니면 switch문 다음에서 '경로 안내 액티비티'(RouteGuidancePagerActivity)를 호출한다

                case R.id.end: //도착역 (R.id.start와 비슷함)
                    subwayMapTouchPoint.endStn = stn;
                    endStnTextView.setText(String.format("  %s: %s   ", getString(R.string.end_station), stn.getStnNm()));
                    endStnTextView.setVisibility(View.VISIBLE);
                    if (subwayMapTouchPoint.startStn == null) {
                        dialog.cancel();
                        return;
                    }
                    break;

                case R.id.info: //정보
                    // 역 정보 Activity 호출
                    Intent intent = new Intent(MainActivity.this, StnInfoPagerActivity.class);
                    intent.putExtra("lines", subwayMapTouchPoint.getLineNms(stnIdx, stn));
                    intent.putExtra("stnNm", stn.getStnNm());
                    MainActivity.this.startActivity(intent);
                    dialog.cancel();
                    return;

                default:
                    dialog.cancel();
                    return;
            }

            /* 출발역(startStn), 도착역(endStn) 모두 입력되었다면,
               경로탐색 쓰레드 실행 **/
            findRouteAsync();
            dialog.cancel();
        });
        dialog.show(); //dialog를 보여준다
    }

    // FloatingAction 애니메이션 Open/Close
    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab_favorite.startAnimation(fab_close);
            fab_trans_map.startAnimation(fab_close);
            fab_settings.startAnimation(fab_close);
            fab_favorite.setClickable(false);
            fab_trans_map.setClickable(false);
            fab_settings.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab_favorite.startAnimation(fab_open);
            fab_trans_map.startAnimation(fab_open);
            fab_settings.startAnimation(fab_open);
            fab_favorite.setClickable(true);
            fab_trans_map.setClickable(true);
            fab_settings.setClickable(true);
            isFabOpen = true;
        }
    }


    // 지하철 데이터를 가져오는 Task
    private void loadDatabaseAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("지하철역 정보를 가져오는 중..");
        dialog.setCancelable(false); //화면터치로 인해 Task가 중단되지 않도록 설정
        dialog.show();

        Completable loadingDbTask$ = Completable.create(emitter -> {
            // Preference로 이전 DB 상태를 받아온다. 정상: true, 비정상: false
            // state: Preference 파일이름 ex. state.xml
            // isDbValid: state.xml의 태그. isDbValid 값을 갖는다
            SharedPreferences state = getSharedPreferences("state", MODE_PRIVATE);
            boolean isDbValid = state.getBoolean("isDbValid", false);

            // 현재 DB 상태를 저장해둔다
            SharedPreferences.Editor editor = state.edit();
            editor.putBoolean("isDbValid", isDbValid);
            editor.apply();

            MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(MainActivity.this);
            SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
            myDBOpenHelper.setDatabase(db);
            if (!isDbValid) //유효한 테이블이 없으면 DB를 새로 생성한다
                myDBOpenHelper.initDatabase(db, true);

            subwayMapTouchPoint = new SubwayMapTouchPoint(MainActivity.this); //역 터치 좌표 초기화
            stationMatrix = new StationMatrix(myDBOpenHelper.getReadableDatabase());
            stnIdx = stationMatrix.getStnIdx();

            // DB 점검 후 완료 시, DB 상태를 true로 저장한다
            editor = state.edit();
            editor.putBoolean("isDbValid", true);
            editor.apply();

            emitter.onComplete();
        });

        loadingDbTaskObs = loadingDbTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            loadingDbTaskObs = null;
                            dialog.dismiss();
                            // StationMatirx 생성 후 데이터를 점검한다
                            testSettingAsync();
                        }
                );
    }

    // 경로 계산하는 Task
    private void findRouteAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("경로 탐색중..");
        dialog.setCancelable(false); //화면터치로 인해 Task가 중단되지 않도록 설정
        dialog.show();

        Completable findingRouteTask$ = Completable.create(emitter -> {
            /* SubwayMapTouchPoint map의 startStn, endStn을
             * 경로정보액티비티에서 사용할 수 있도록 intent에 저장한다  **/
            if (Station.compare(subwayMapTouchPoint.startStn, subwayMapTouchPoint.endStn) == 2) { //출발역과 도착역이 다를 때
                String startLineNm = subwayMapTouchPoint.startStn.getLineNm();
                String startStnNm = subwayMapTouchPoint.startStn.getStnNm();
                String endLineNm = subwayMapTouchPoint.endStn.getLineNm();
                String endStnNm = subwayMapTouchPoint.endStn.getStnNm();

                // 출발역에서 도착역까지 갈 수 있는 경로 계산
                Calendar calendar = Calendar.getInstance(); //현재 날짜 시간
                ArrayList<Integer> startIdxs = subwayMapTouchPoint.getIndexes(stnIdx, subwayMapTouchPoint.startStn);
                ArrayList<Integer> endIdxs = subwayMapTouchPoint.getIndexes(stnIdx, subwayMapTouchPoint.endStn);
                stationMatrix.setVirtualNodes(startIdxs, endIdxs);

                Route lessTransInfo = new RouteFinder(0, stationMatrix).getRoute(); //최소환승 경로
                Route minDistInfo = new RouteFinder(1, stationMatrix).getRoute(); //최단시간 경로
                ArrayList<RouteWrapper> lessTransItems = RouteWrapper.createArrayListInstance(MainActivity.this, stnIdx, calendar, lessTransInfo);
                ArrayList<RouteWrapper> minDistItems = RouteWrapper.createArrayListInstance(MainActivity.this, stnIdx, calendar, minDistInfo);

                // history 저장
                boolean favorite = FavoriteRoute.insert(false, startLineNm, endLineNm, startStnNm, endStnNm);
                //현재 경로가 즐겨찾기에 추가되어 있으면 true, 아니면 false를 반환한다

                // PathInfoActivity에 보낼 intent 작성 (경로, 시간, 환승횟수)
                Intent intent = new Intent(MainActivity.this, RouteGuidancePagerActivity.class);
                intent.putParcelableArrayListExtra("lessTransItems", lessTransItems);
                intent.putExtra("lessTransInfo", lessTransInfo);
                intent.putParcelableArrayListExtra("minDistItems", minDistItems);
                intent.putExtra("minDistInfo", minDistInfo);

                // Favorite 설정을 위해 출발역정보, 경유역정보, 도착역정보도 intent에 넣는다
                intent.putExtra("startLineNm", startLineNm);
                intent.putExtra("endLineNm", endLineNm);
                intent.putExtra("startStnNm", startStnNm);
                intent.putExtra("endStnNm", endStnNm);
                intent.putExtra("favorite", favorite);

                // RouteGuidancePagerActivity 호출
                MainActivity.this.startActivityForResult(intent, PATH_INFO_ACTIVITY_CODE);
            } else {
                Toast.makeText(MainActivity.this, "출발역과 도착역을 다르게 설정해주세요", Toast.LENGTH_SHORT).show();
            }

            /* '경로 안내 액티비티'를 호출하고 다시 MainActivity로 돌아왔을 때,
             * 출발역, 도착역, 경유역을 새롭게 입력할 수 있도록 null로 초기화한다   */
            subwayMapTouchPoint.startStn = null;
            subwayMapTouchPoint.endStn = null;

            emitter.onComplete();
        });

        findingRouteTaskObs = findingRouteTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            findingRouteTaskObs = null;
                            dialog.dismiss();
                            startStnTextView.setVisibility(GONE);
                            endStnTextView.setVisibility(GONE);
                        }
                );
    }

    // 환승제한역 설정 저장하는 Task
    private void testSettingAsync() {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("설정 적용 중..");
        dialog.setCancelable(false); //화면터치로 인해 Task가 중단되지 않도록 설정
        dialog.show();

        Single<Boolean> testingSettingTask$ = Single.create(emitter -> {
            SharedPreferences temp_transStnPref = getSharedPreferences(SettingsActivity.TEMP_SETTING, MODE_PRIVATE);
            Set<String> values = temp_transStnPref.getStringSet(SettingsActivity.TRANS_STN, null);
            boolean dijkstraError = false;
            int n = stationMatrix.getN();
            int[][] mMatrix = new int[n][n];
            int[][] rawMatrix = stationMatrix.getRawMatrix();
            for (int i = 0; i < n; i++)
                mMatrix[i] = rawMatrix[i].clone();
            if (values != null) {
                for (String value : values) {
                    StringTokenizer cutter = new StringTokenizer(value, "|", false);
                    String stnNm = cutter.nextToken();
                    String startLineNm = cutter.nextToken();
                    String endLineNm = cutter.nextToken();
                    int start = stationMatrix.getIndex(startLineNm, stnNm);
                    int end = stationMatrix.getIndex(endLineNm, stnNm);
                    mMatrix[start][end] = mMatrix[end][start] = StationMatrix.INF;
                }

                // 가상시작점, 가상종료점에 대한 임시값 (없으면 found[]에서 OutOfIndexException 발생)
                ArrayList<Integer> startIdxs = subwayMapTouchPoint.getIndexes(stnIdx, stnIdx.get(0)); //소요산
                ArrayList<Integer> endIdxs = subwayMapTouchPoint.getIndexes(stnIdx, stnIdx.get(1)); //동두천
                setVirtualNodes(mMatrix, n, startIdxs, endIdxs);

                // 다익스트라 테스트
                try {
                    testDijkstra(mMatrix, n - 2);

                    // stationMatrix의 matrix, transMatrix 수정
                    stationMatrix.initMatrix(mMatrix);

                    // 확정된 SharedPreference 저장
                    SharedPreferences transStnPref = getSharedPreferences(SettingsActivity.SETTING, MODE_PRIVATE);
                    SharedPreferences.Editor editor = transStnPref.edit();
                    editor.putStringSet(SettingsActivity.TRANS_STN, values);
                    editor.apply();
                } catch (ArrayIndexOutOfBoundsException e) { //다익스트라 테스트 실패!!
                    dijkstraError = true;
                }
            }

            emitter.onSuccess(dijkstraError);
        });

        testingSettingTaskObs = testingSettingTask$
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(dijkstraError -> {
                    testingSettingTaskObs = null;
                    dialog.dismiss();

                    if (dijkstraError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog alertDialog;
                        builder.setMessage(R.string.trans_stn_setting_err_msg);
                        builder.setPositiveButton(R.string.trans_stn_setting_dialog_confirm, (d, w) -> {
                            d.dismiss();
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivityForResult(intent, TRANS_SETTING_ACTIVITY_CODE);
                        });
                        alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });
    }

    private void setVirtualNodes(int[][] matrix, int n, ArrayList<Integer> startLineIdxs, ArrayList<Integer> endLineIdxs) {
        // 초기화
        int start = n - 2, end = n - 1;
        for (int i = 0; i < n; i++) {
            matrix[i][start] = StationMatrix.INF;
            matrix[start][i] = StationMatrix.INF;
        }
        matrix[start][start] = 0;
        for (int i = 0; i < n; i++) {
            matrix[i][end] = StationMatrix.INF;
            matrix[end][i] = StationMatrix.INF;
        }
        matrix[end][end] = 0;
        // 가상시작점, 가상도착점 설정
        for (int i : startLineIdxs) {
            matrix[i][start] = 0;
            matrix[start][i] = 0;
        }
        for (int i : endLineIdxs) {
            matrix[i][end] = 0;
            matrix[end][i] = 0;
        }
    }

    private int choose(int[] distance, boolean[] found) {
        int min = StationMatrix.INF;
        int choose = -1;

        for (int i = 0; i < distance.length; i++) {
            if (!found[i] && distance[i] < min) {
                min = distance[i];
                choose = i;
            }
        }
        return choose;
    }

    private void testDijkstra(int[][] matrix, int start) {
        // 초기화
        int[] distance = matrix[start].clone();
        int n = distance.length;
        boolean[] found = new boolean[n];
        for (int i = 0; i < n; i++)
            found[i] = false;
        found[start] = true;

        int loop = n - 1;
        while (loop-- != 0) {
            int choose = choose(distance, found);
            found[choose] = true;
            for (int i = 0; i < n; i++)
                if (!found[i] && distance[choose] + matrix[choose][i] < distance[i])
                    distance[i] = distance[choose] + matrix[choose][i];
        }
    }


    // SearchListView의 Adapter
    private class SearchListAdapter extends BaseAdapter {
        private ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        private Context context;
        private int id = R.layout.item_station;
        private ArrayList<MyItem> myItems = new ArrayList<>();

        SearchListAdapter(Context context) {
            this.context = context;
        }

        void add(String lineNm, String stnNm, int pointx, int pointy) {
            for (MyItem item : myItems) {
                if (pointx == item.x && pointy == item.y) {
                    item.lienNms.add(lineNm);
                    return;
                }
            }
            myItems.add(new MyItem(lineNm, stnNm, pointx, pointy));
        }

        void clear() {
            myItems.clear();
        }

        @Override
        public int getCount() {
            return myItems.size();
        }

        @Override
        public MyItem getItem(int position) {
            return myItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(id, parent, false);

                holder = new Holder();
                holder.lineSymLayout = convertView.findViewById(R.id.lineSymLayout);
                holder.stnNm = convertView.findViewById(R.id.stnNm);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                holder.lineSymLayout.removeAllViews();
            }

            MyItem stn = myItems.get(position);
            for (String lineNm : stn.lienNms) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(SubwayLine.getResId(lineNm));
                holder.lineSymLayout.addView(imageView, params);
            }
            holder.stnNm.setText(stn.stnNm);

            return convertView;
        }

        private class Holder {
            LinearLayout lineSymLayout;
            TextView stnNm;
        }

        private class MyItem {
            private ArrayList<String> lienNms = new ArrayList<>();
            private String stnNm;
            private int x, y;

            private MyItem(String lienNm, String stnNm, int x, int y) {
                lienNms.add(lienNm);
                this.stnNm = stnNm;
                this.x = x;
                this.y = y;
            }

            public String getStnNm() {
                return stnNm;
            }

        }

    }


    /* Listener */
    private class OnLineMapViewTab implements OnViewTapListener {

        @Override
        public void onViewTap(View view, float x, float y) {
            // 터치한 좌표(x,y)를 사용하여 'stn 객체'에 터치한 역의 정보(StnPoint: 터치좌표, 역호선, 역이름)를 저장한다
            Station stn = subwayMapTouchPoint.getStation(
                    stnIdx,
                    lineMapView.getDisplayRect().left, lineMapView.getDisplayRect().top,
                    lineMapView.getScale(), x, y);

            if (stn != null) //터치한 위치에 역이 있는 경우 Dialog를 띄운다
                displayStationTouchDialog(stn);
        }
    }

    private class OnSearchViewQueryText implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (searchListAdapter.getCount() == 0)
                return false;

            AdapterView.OnItemClickListener listener = searchList.getOnItemClickListener();
            if (listener != null)
                listener.onItemClick(null, null, 0, 0);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String mText) {
            searchListAdapter.clear();
            if (mText.equals(""))
                return false;
            for (Station stn : stnIdx)
                if (stn.getStnNm().startsWith(mText))
                    searchListAdapter.add(stn.getLineNm(), stn.getStnNm(), stn.getPointx(), stn.getPointy());
            searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
            return true;
        }

    }

    private class OnSearchListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String stnNm = searchListAdapter.getItem(position).getStnNm();
            Station mStn = null;
            for (Station stn : stnIdx)
                if (stnNm.equals(stn.getStnNm()))
                    mStn = stn;

            if (mStn != null) {
                searchView.setQuery("", false);
                searchListAdapter.clear();
                searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
                displayStationTouchDialog(mStn);
            }
        }

    }

}
