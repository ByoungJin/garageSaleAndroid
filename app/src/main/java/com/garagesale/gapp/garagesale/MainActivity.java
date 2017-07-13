package com.garagesale.gapp.garagesale;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.databinding.ActivityMainBinding;
import com.garagesale.gapp.garagesale.databinding.MenuLayoutBinding;
import com.garagesale.gapp.garagesale.fragment.JoinFragment;
import com.garagesale.gapp.garagesale.fragment.LoginFragment;
import com.garagesale.gapp.garagesale.fragment.MainFragment;
import com.garagesale.gapp.garagesale.fragment.PlanetListFragment;
import com.garagesale.gapp.garagesale.fragment.PractiveMainFragment;
import com.garagesale.gapp.garagesale.fragment.ProductFragment;
import com.garagesale.gapp.garagesale.fragment.ProfileFragment;
import com.garagesale.gapp.garagesale.fragment.SettingFragment;
import com.garagesale.gapp.garagesale.fragment.StoreFragment;
import com.garagesale.gapp.garagesale.network.DaggerNetworkComponent;
import com.garagesale.gapp.garagesale.network.NetworkComponent;
import com.garagesale.gapp.garagesale.network.NetworkModule;
import com.garagesale.gapp.garagesale.util.CloseActivityHandler;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    NetworkComponent networkComponent;
    private CloseActivityHandler closeActivityHandler;
    private FragmentTransaction ft;
    ActivityMainBinding binding;
    private static Stack<BaseFragment> backstack;


    public interface OnLoginSuccessListener {
    }

    public void setOnLoginSuccessListener() {
        backstack.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        backstack = new Stack<>();
        ft = getSupportFragmentManager().beginTransaction();
        BaseFragment defualtfragment;

        // NetworkModule에서 Content를 받을 수 있도록 빌드
        networkComponent = DaggerNetworkComponent.builder().networkModule(new NetworkModule(this)).build();
        //앱테스트하는데 너무실수로 자주꺼서 임시로 추가하는 baackpress 이벤트처리
        closeActivityHandler = new CloseActivityHandler(this);

        // 기본 로그인
        defualtfragment = LoginFragment.getInstance();

        ft.replace(R.id.content_fragment_layout, defualtfragment).commit();
    }

    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    @Override
    public void onBackPressed() {
        // 판넬 열려있으면 닫음
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (backstack.size() > 0) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, backstack.pop());
            ft.commit();
        } else {
            closeActivityHandler.onBackPressed();
        }

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
        } else if (id == R.id.product_button) {
            changeFragment(ProductFragment.getInstance());
        } else if (id == R.id.planet_list_button) {
            changeFragment(PlanetListFragment.getInstance());
        } else if (id == R.id.logout_button) {
            logout();
        } else if (id == R.id.practive_main_button) {
            changeFragment(PractiveMainFragment.getInstance());
        }

    }

    public void logout() {
        SharedPreferenceManager.getInstance(this).putStringValue(BuildConfig.KEYTOKEN, ""); // 토큰 초기화
        MenuLayoutBinding menuLayoutBinding = binding.contentMain.menuLayout;
        menuLayoutBinding.logoutButton.setVisibility(Button.GONE); // logout button gone
        menuLayoutBinding.loginButton.setVisibility(Button.VISIBLE); // login button
        menuLayoutBinding.joinButton.setVisibility(Button.VISIBLE); // join button none
        Toast.makeText(this, "Logout 완료", Toast.LENGTH_SHORT).show();
        changeFragment(LoginFragment.getInstance()); // Login 화면으로 전환
    }

    public void changeFragment(@NonNull BaseFragment fragment) {
        Fragment targetfragment = getSupportFragmentManager().findFragmentById(R.id.content_fragment_layout);

        if (!targetfragment.equals(fragment)) { // 같은 Fragment로 움직였는지
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);

            if (getIsExist(fragment)) {         // 기존에 저장된 Fragmnet인지
                backstack.remove(fragment);
                backstack.push((BaseFragment) targetfragment);
            } else {
                backstack.push((BaseFragment) targetfragment);
            }
            ft.commit();
        }

        // Set the toolbar title
        TextView titleTextView = (TextView) findViewById(R.id.title);

        titleTextView.setText(fragment.getTitle());
    }

    public boolean getIsExist(Fragment targetfragment) {

        ArrayList<Fragment> list2 = new ArrayList(backstack);
        for (Fragment f : list2) {
            if (f != null && f.getClass().getName().equals(targetfragment.getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    public void slideMenu(View v) {
        // 판넬 열려있음 닫고 닫혀있음 열기
        SlidingUpPanelLayout slidingUpPanelLayout = binding.contentMain.slidingLayout;
        if (slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

}
