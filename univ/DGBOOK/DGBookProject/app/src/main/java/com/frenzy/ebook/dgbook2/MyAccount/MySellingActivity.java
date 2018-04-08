package com.frenzy.ebook.dgbook2.MyAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.frenzy.ebook.dgbook2.Activity.MainmenuActivity;
import com.frenzy.ebook.dgbook2.DAO;
import com.frenzy.ebook.dgbook2.GetnSet.Book;
import com.frenzy.ebook.dgbook2.GetnSet.MySellingListItem;
import com.frenzy.ebook.dgbook2.R;
import com.frenzy.ebook.dgbook2.adapter.MySellingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 민경 on 2016-11-15.
 * Modified by YoungHoonKim on 2016-11-19.
 */

/**
 클래스명 : public class MySellingActivity extends AppCompatActivity implements MySellingAdapter.ItemClickCallback
 주된기능 : 내가 판매 완료한 책목록을 Recyclerview로 보여준다.
 멤버변수 :
 RecyclerView recView : 리싸이클러뷰 객체
 CommentAdapter adapter : 댓글들을 하나로 묶는 어뎁터
 ArrayList listData : 댓글을 뿌렸을때 연속적인 나열을 나타내는 가변배열
 private static final String D_BUNDLE_EXTRAS : 번들 객체의 문자열을 받는 문자열
 private static final String D_EXTRA_BOOKNAME : 책제목 문자열
 private static final String D_EXTRA_BOOKWRITER : 저자 문자열
 private static final String D_EXTRA_BOOKCOMPANY : 출판사 문자열

 메소드 :
 protected void onCreate(Bundle savedInstanceState)
 * 매개변수: Bundle savedInstanceState
 * 기능 : 뷰의 생성자
 public void initview() : 뷰 안에 있는 멤버변수의 초기화
 * 매개변수 :
 * 기능 : 뷰 안에 있는 멤버변수의 초기화
 public void onBackPressed()
 * 매개변수 :
 * 기능 : 이전버튼을 눌렀을경우 이전화면으로 돌아갈수있게 구현
 public void onItemClick(int p)
 * 매개변수 : int p
 * 기능 : 리싸이클러뷰 기반 전용메소드이며, 예를 들어 책목록중 하나를 누르면 다른 액티비티로 넘어가며 상세정보가 출력이 되게 하는 메소드인데,
 MySellingListItem의 책제목, 저자, 출판사 data를 번들에 묶어 MySellingDetail_Activity로 전달해준다.
 */



public class MySellingActivity extends AppCompatActivity implements MySellingAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS="BUNDLE_EXTRAS";
    private static final String EXTRA_BOOKNAME="EXTRA_BOOKNAME";
    private static final String EXTRA_BOOKWRITER="EXTRA_BOOKWRITER";
    private static final String EXTRA_BOOKCOMPANY="EXTRA_BOOKCOMPANY";

    private RecyclerView recView;
    private MySellingAdapter adapter;
    private ArrayList listData;
    private DAO dao;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_selling);

        pref = getSharedPreferences("DGBook", MODE_PRIVATE);
        prefEditor = pref.edit();

        initview();



    }

    public void initview()
    {
        recView=(RecyclerView)findViewById(R.id.rec_Slist);
        //LayoutManager @gridLayoutManager
        recView.setLayoutManager(new LinearLayoutManager(this));
        dao=new DAO();
        searchMyBook searchMyBook=new searchMyBook();
        searchMyBook.execute(pref.getString("id","fail"));



    }
    public void setRecyclerView()
    {
        adapter=new MySellingAdapter(listData,this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
    }

    @Override
    public void onItemClick(int p) {
        MySellingListItem item=(MySellingListItem)listData.get(p);
        if(item.getProgress()==1)
            Toast.makeText(getApplicationContext(), "이미 판매된 책입니다.", Toast.LENGTH_LONG).show();
        else if(item.getProgress()==2)
            Toast.makeText(getApplicationContext(), "대여중인 책입니다.", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,MySellingDetail_Activity.class);
        intent.putExtra("bookid",item.getBookid());
        intent.putExtra("type",item.getType());
        startActivity(intent);
    }

    @Override
    public void onSecondaryIconClick(int p) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MySellingActivity.this, MainmenuActivity.class);
        startActivity(intent);
        finish();
    }
    class searchMyBook extends AsyncTask<String, Void, ArrayList> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MySellingActivity.this);
            loading.setMessage("검색중입니다!!");
            loading.setProgressStyle(loading.STYLE_SPINNER);
            loading.show();

        }

        //검색된 책에대한 정보를 처리
        @Override
        protected void onPostExecute(ArrayList books) {
            super.onPostExecute(books);
            loading.dismiss();
            //책정보가 성공적으로 검색되었다면
            if(books!=null) {
                listData=(ArrayList) getListData(books);
                setRecyclerView();
                Toast.makeText(getApplicationContext(), "책 정보검색완료!", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(), "책정보 불러오기 실패", Toast.LENGTH_LONG).show();


        }

        @Override
        protected ArrayList doInBackground(String... params) {
            //dao 의 select 메소드로 책정보를 담은 task를 리턴
            ArrayList<Book> task;
            task=dao.searchmybook(params[0]);
            return task;
        }
    }
    public List<MySellingListItem> getListData(ArrayList<Book> books) {
        List<MySellingListItem> data = new ArrayList<>();

        int icons = (android.R.drawable.ic_popup_reminder);
        //4번 반복 스크롤뷰 하려고
        //recyclerView
        //create Listitem with dummy data, then add to list
        for (int i = 0; i <books.size(); i++) {
            MySellingListItem item = new MySellingListItem();
            item.setImageResId(icons);
            item.setTitle(books.get(i).getTitle());
            item.setWriter(books.get(i).getAuthor());
            item.setCompany(books.get(i).getCompany());
            item.setType(books.get(i).getType());
            item.setBitmap(books.get(i).getBitmap());
            item.setBookid(books.get(i).getBookID());
            item.setProgress(books.get(i).getProgress());
            data.add(item);
        }

        return data;
    }
}
