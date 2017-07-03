package com.garagesale.gapp.garagesale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.garagesale.gapp.garagesale.fragment.JoinFragment;
import com.garagesale.gapp.garagesale.fragment.LoginFragment;
import com.garagesale.gapp.garagesale.fragment.MainFragment;
import com.garagesale.gapp.garagesale.fragment.ProductFragment;
import com.garagesale.gapp.garagesale.fragment.ProfileFragment;
import com.garagesale.gapp.garagesale.fragment.SettingFragment;
import com.garagesale.gapp.garagesale.fragment.StoreFragment;
import com.garagesale.gapp.garagesale.network.DaggerNetworkComponent;
import com.garagesale.gapp.garagesale.network.NetworkComponent;
import com.garagesale.gapp.garagesale.network.NetworkModule;
import com.garagesale.gapp.garagesale.util.CloseActivityHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    NetworkComponent networkComponent;
    private CloseActivityHandler closeActivityHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // NetworkModule에서 Content를 받을 수 있도록 빌드
        networkComponent = DaggerNetworkComponent.builder().networkModule(new NetworkModule(this)).build();
        //앱테스트하는데 너무실수로 자주꺼서 임시로 추가하는 baackpress 이벤트처리
        closeActivityHandler = new CloseActivityHandler(this);
    }

    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }

    @Override
    public void onBackPressed() {
        // 판넬 열려있으면 닫음
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        if(slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            //super.onBackPressed();
            closeActivityHandler.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) return true;

        // Custom Actionbar 사용 목적, CustomEnabled을 true 시키고 필요 없는 것 false
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);     //액션바 아이콘, 업 네비게이션 형태 표시
        actionBar.setDisplayShowTitleEnabled(false);    //액션바에 표시되는 제목의 표시유무 설정
        actionBar.setDisplayShowHomeEnabled(false);     //홈 아이콘 숨김처리.

        //Custom Layout을 Actionbar에 포팅
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 제거
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        // Login 화면부터 시작
        changeFragment(LoginFragment.getInstance());

        return true;
    }

    // 메뉴버튼 클릭이벤트
    public void btnStart(View v) {
        slideMenu(null);    // 판넬 닫기
        int id = v.getId();

        if (id == R.id.main_button) {
            changeFragment(MainFragment.getInstance());
        } else if (id == R.id.profile_button) {
            changeFragment(ProfileFragment.getInstance());
        } else if (id == R.id.join_button) {
            changeFragment(JoinFragment.getInstance());
        } else if (id == R.id.login_button) {
            changeFragment(LoginFragment.getInstance());
        } else if (id == R.id.setting_button) {
            changeFragment(SettingFragment.getInstance());
        } else if (id == R.id.store_button) {
            changeFragment(StoreFragment.getInstance());
        } else if( id == R.id.product_button) {
            changeFragment(ProductFragment.getInstance());
        }

    }

    public void changeFragment(@NonNull BaseFragment fragment){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        // Set the toolbar title
        TextView titleTextView = (TextView) findViewById(R.id.title);

        titleTextView.setText(fragment.getTitle());
    }

    public void slideMenu(View v) {
        // 판넬 열려있음 닫고 닫혀있음 열기
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        if(slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }

}
