package blacksmith.sullivanway.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.MyDBOpenHelper;
import blacksmith.sullivanway.database.TransferMap;

public class TransferMapListActivity extends AppCompatActivity {
    private ListView listView;

    private MyAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_map_list);

        SearchView searchView = findViewById(R.id.transfer_map_search);
        listView = findViewById(R.id.transfer_map_list);

        MyDBOpenHelper dbOpenHelper = new MyDBOpenHelper(this);
        String sql = String.format("SELECT DISTINCT stnNm, startLineNm, startNextStnNm, endLineNm, endNextStnNm FROM %s ORDER BY stnNm",
                TransferMap.TB_NAME);
        Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String stnNm = cursor.getString(0);
            String startLineNm = cursor.getString(1);
            String startNextStnNm = cursor.getString(2);
            String endLineNm = cursor.getString(3);
            String endNextStnNm = cursor.getString(4);
            items.add(new Item(stnNm, startLineNm, startNextStnNm, endLineNm, endNextStnNm));
        }
        cursor.close();

        adapter = new MyAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnListItemClick());

        searchView.setOnQueryTextListener(new OnSearchViewQueryText());
    }

    private class Item {
        String stnNm, startLineNm, startNextStnNm, endLineNm, endNextStnNm;

        Item(String stnNm, String startLineNm, String startNextStnNm, String endLineNm, String endNextStnNm) {
            this.stnNm = stnNm;
            this.startLineNm = startLineNm;
            this.startNextStnNm = startNextStnNm;
            this.endLineNm = endLineNm;
            this.endNextStnNm = endNextStnNm;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private int itemResId = R.layout.item_transfer_map;
        private ArrayList<Item> items = new ArrayList<>();

        MyAdapter(Context context, ArrayList<Item> items) {
            this.context = context;
            this.items.addAll(items);
        }

        void add(Item item) {
            items.add(item);
        }

        void addAll(ArrayList<Item> transMaps) {
            items.addAll(transMaps);
        }

        void clear() {
            items.clear();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(itemResId, parent, false);

                // Holder에 뷰를 저장하여, 참조할 뷰를 처음 한번만 받아온다
                holder = new Holder();
                holder.stnNm = convertView.findViewById(R.id.stnNm);
                holder.startLine = convertView.findViewById(R.id.startLine);
                holder.startNextStnNm = convertView.findViewById(R.id.startNextStnNm);
                holder.endLine = convertView.findViewById(R.id.endLine);
                holder.endNextStnNm = convertView.findViewById(R.id.endNextStnNm);
                convertView.setTag(holder); //현재 convertView에 holder 저장
            } else {
                holder = (Holder) convertView.getTag(); //재사용되는 convertView라면 저장해둔 holder를 가져온다
            }

            holder.stnNm.setText(filterString(items.get(position).stnNm));
            holder.startLine.setText(items.get(position).startLineNm);
            holder.startNextStnNm.setText(items.get(position).startNextStnNm);
            holder.endLine.setText(items.get(position).endLineNm);
            holder.endNextStnNm.setText(items.get(position).endNextStnNm);

            return convertView;
        }

        String filterString(String string) {
            String newString = string;
            int length = newString.length();
            if (length > 5) { //hamburger (9)
                String a = newString.substring(0, 5); //hambu
                String b = newString.substring(5, length); //rger
                newString = a + "\n" + b;
            }
            return newString;
        }

        private class Holder {
            TextView stnNm, startLine, startNextStnNm, endLine, endNextStnNm;
        }

    }

    private class OnListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item item = adapter.getItem(position);
            Intent intent = new Intent(TransferMapListActivity.this, TransferMapPagerActivity.class);
            intent.putExtra("stnNm", item.stnNm);
            intent.putExtra("startLineNm", item.startLineNm);
            intent.putExtra("startNextStnNm", item.startNextStnNm);
            intent.putExtra("endLineNm", item.endLineNm);
            intent.putExtra("endNextStnNm", item.endNextStnNm);
            startActivity(intent);
        }

    }

    private class OnSearchViewQueryText implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (adapter.getCount() == 0)
                return false;
            AdapterView.OnItemClickListener listener = listView.getOnItemClickListener();
            if (listener != null)
                listener.onItemClick(null, null, 0, 0);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String mText) {
            adapter.clear();
            if (mText.equals("")) {
                adapter.addAll(items);
            } else {
                for (Item item : items)
                    if (item.stnNm.startsWith(mText))
                        adapter.add(item);
            }
            listView.setAdapter(adapter);
            return true;
        }

    }

}
