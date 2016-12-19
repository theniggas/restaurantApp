package mau.resturantapp.aktivitys;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.aktivitys.dialogs.Dialog_askForLogin;
import mau.resturantapp.aktivitys.dialogs.Dialog_login;
import mau.resturantapp.aktivitys.dialogs.Dialog_shopClosed;
import mau.resturantapp.aktivitys.dialogs.Dialog_signup;
import mau.resturantapp.aktivitys.mainFragments.Checkout_frag;
import mau.resturantapp.aktivitys.mainFragments.Contact_frag;
import mau.resturantapp.aktivitys.mainFragments.FindWay_frag;
import mau.resturantapp.aktivitys.mainFragments.Home_frag;
import mau.resturantapp.aktivitys.mainFragments.menufrag.MenuHandler_frag;
import mau.resturantapp.aktivitys.mainFragments.menufrag.MenuTabs_frag;
import mau.resturantapp.aktivitys.mainFragments.Admincontrol_frag;
import mau.resturantapp.aktivitys.mainFragments.userControls.userProfile_frag;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.GuestUserCheckoutEvent;
import mau.resturantapp.event.events.HideCartEvent;
import mau.resturantapp.event.events.IsAdminEvent;
import mau.resturantapp.event.events.LogUserInEvent;
import mau.resturantapp.event.events.NewUserFailedEvent;
import mau.resturantapp.event.events.NewUserSuccesfullEvent;
import mau.resturantapp.event.events.OnFailedLogIn;
import mau.resturantapp.event.events.OrderSuccessfulEvent;
import mau.resturantapp.event.events.ShopClosedEvent;
import mau.resturantapp.event.events.ShowAskforLoginDialogEvent;
import mau.resturantapp.event.events.ShowCartEvent;
import mau.resturantapp.event.events.ShowLogInDialogEvent;
import mau.resturantapp.event.events.ShowSignupDialogEvent;
import mau.resturantapp.test.QRCamera;
import mau.resturantapp.test.QRTest;
import mau.resturantapp.utils.Firebase.FirebaseAuthentication;
import mau.resturantapp.utils.LanguageContextWrapper;
import mau.resturantapp.utils.LanguageHandler;


import static android.support.design.widget.BottomSheetBehavior.*;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnTabSelectListener, OnNavigationItemSelectedListener,OnTouchListener, OnCheckedChangeListener {

    private FloatingActionButton actBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomBar bottomBar;
    private ImageButton drawerButton;
    private DrawerLayout drawLayout;
    private NavigationView sideMenu;
    private View fadeView;
    private ProgressBar progBar;
    private Switch languageSwitch;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.hovedakt_akt);
        EventBus.getDefault().register(this);
        fadeView = findViewById(R.id.fadeView);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar_main);
        drawerButton = (ImageButton) findViewById(R.id.btn_sidemenu);
        FrameLayout sheet = (FrameLayout) findViewById(R.id.frameLayout_bottomSheet);
        bottomSheetBehavior = from(sheet);
        drawerButton.setOnClickListener(this);
        drawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        sideMenu = (NavigationView) findViewById(R.id.navView_sideMenu);
        progBar = (ProgressBar) findViewById(R.id.progBar_main);
        progBar.setVisibility(View.GONE);
        actBtn = (FloatingActionButton) findViewById(R.id.floatActBtn_cartContent);


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                onTabSelected(tabId);
            }
        });

        bottomBar.setOnTabSelectListener(this);

        if(sideMenu != null){
            sideMenu.setNavigationItemSelectedListener(this);
        }


        View headerLayout = sideMenu.getHeaderView(0);
        languageSwitch = (Switch) headerLayout.findViewById(R.id.header_languageSwitch);
        languageSwitch.setChecked(LanguageHandler.isChecked(this));
        languageSwitch.setOnCheckedChangeListener(this);

        int dp = DPtoPixels(39f); // husk f efter tallet
        bottomSheetBehavior.setPeekHeight(dp);
        bottomSheetBehavior.setHideable(true);

        bottomSheetBehavior.setState(STATE_COLLAPSED);

        fadeView.setOnTouchListener(this);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if(newState == STATE_DRAGGING){
                    fadeView.animate().alpha(0.5f);
                    fadeView.setVisibility(View.VISIBLE);
                }

                if(newState == STATE_SETTLING){
                    fadeView.animate().alpha(0.5f);
                    fadeView.setVisibility(View.VISIBLE);
                }


                if(newState == STATE_EXPANDED){
                    fadeView.animate().alpha(0.5f);
                    fadeView.setVisibility(View.VISIBLE);
                }
                if(newState == STATE_COLLAPSED || newState == STATE_HIDDEN){
                    fadeView.animate().setDuration(300).alpha(0.0f);
                    fadeView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomBar.setElevation(4);
        }


        actBtn.setOnClickListener(this);

        if(appData.currentUser == null){
            userLoggedOut();
        }
        else {
            userLoggedIn();
        }

        hideCart();
        showHomeScreen();


    }


    @Override
    public void onClick(View v) {

        if(v == actBtn){
            if(bottomSheetBehavior.getState() == STATE_EXPANDED)
                bottomSheetBehavior.setState(STATE_HIDDEN);
            else
                bottomSheetBehavior.setState(STATE_EXPANDED);

        }
        if(v == drawerButton){
            hideCart();
            drawLayout.openDrawer(Gravity.LEFT);
        }


    }





    private void showHomeScreen() {
        showCartAndBtn();
        bottomBar.selectTabAtPosition(0);
    }

    private int DPtoPixels(float DP){
       int pixels = 0;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dp = DP;
        float fpixels = metrics.density * dp;
        pixels = (int) (fpixels + 0.5f);
        return pixels;
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        hideCart();
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.mainFrameFrag);

        Log.d("tab slected", "clicked " + tabId + " --- " + R.id.botbar_food);

        switch (tabId){
            case R.id.botbar_food:
                if (!(currentFrag instanceof MenuTabs_frag)){
                    frag = new MenuTabs_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                    showCartAndBtn();
                }

                break;

            case R.id.botbar_home:

                if(!(currentFrag instanceof Home_frag)){

                    frag = new Home_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                    showCartAndBtn();
                }

                break;

            case R.id.botbar_contact:
                if (!(currentFrag instanceof Contact_frag) ){

                    frag = new Contact_frag();
                    ft.addToBackStack(null);
                    ft.replace(R.id.mainFrameFrag, frag).commit();
                    Log.d("tab slected", "contact");
                }

                break;
            default:
                break;
        }

    }

    private void hideCart(){
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        drawLayout.closeDrawer(Gravity.LEFT);
        hideCartAndBtn();
        switch (item.getItemId()) {


            case R.id.menu_login:
                showLoginDialog();
                break;

            case R.id.menu_findos:
                frag = new FindWay_frag();
                ft.addToBackStack(null);
                Log.d("item selected", "findway");
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_qrTest:
                frag = new QRTest();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_indstillinger:
                frag = new Admincontrol_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_qrCamera:
                frag = new QRCamera();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;

            case R.id.menu_signin:
                showSignupDialog();
                break;
            case R.id.menu_logout:
                FirebaseAuthentication.logOutUser();
                showHomeScreen();
                break;
            case R.id.menu_userProfile:
                frag = new userProfile_frag();
                ft.addToBackStack(null);
                ft.replace(R.id.mainFrameFrag, frag).commit();
                break;
            default:


                break;

        }
        setActiveBarTab();
        return true;

    }

    private void anonymousLoggedIn() {
        Log.d("MainActivity", "Anonymous logged in navbar");
        Menu navMenu = sideMenu.getMenu();
        showCartAndBtn();
        navMenu.findItem(R.id.menu_login).setVisible(true);
        navMenu.findItem(R.id.menu_signin).setVisible(true);
        navMenu.findItem(R.id.menu_logout).setVisible(false);
        navMenu.findItem(R.id.menu_qrCamera).setVisible(false);
        navMenu.findItem(R.id.menu_qrTest).setVisible(false);
        navMenu.findItem(R.id.menu_menuHandler).setVisible(false);
        navMenu.findItem(R.id.menu_userProfile).setVisible(false);
    }

    private void userLoggedIn(){
        Log.d("MainActivity", "User logged in navbar");
        Menu navMenu = sideMenu.getMenu();
        showCartAndBtn();
        navMenu.findItem(R.id.menu_login).setVisible(false);
        navMenu.findItem(R.id.menu_signin).setVisible(false);
        navMenu.findItem(R.id.menu_logout).setVisible(true);
        navMenu.findItem(R.id.menu_userProfile).setVisible(true);

        if(appData.currentUser != null && appData.currentUser.isAdmin()){
            navMenu.findItem(R.id.menu_qrCamera).setVisible(true);
            navMenu.findItem(R.id.menu_qrTest).setVisible(true);
            navMenu.findItem(R.id.menu_menuHandler).setVisible(true);
        }
    }
    private void userLoggedOut(){
        Log.d("MainActivity", "User logged in navbar");
        Menu navMenu = sideMenu.getMenu();
        showCartAndBtn();
        navMenu.findItem(R.id.menu_login).setVisible(true);
        navMenu.findItem(R.id.menu_signin).setVisible(true);
        navMenu.findItem(R.id.menu_logout).setVisible(false);
        navMenu.findItem(R.id.menu_qrCamera).setVisible(false);
        navMenu.findItem(R.id.menu_qrTest).setVisible(false);
        navMenu.findItem(R.id.menu_menuHandler).setVisible(false);
        navMenu.findItem(R.id.menu_userProfile).setVisible(false);
    }

    public void Btn_Checkout_clicked(View v){
      if(appData.currentUser == null){
          showAskForLoginDialog();
      }
        else{
            goToCheckout();
            hideCartAndBtn();
      }
    }

    private void goToCheckout(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = new Checkout_frag();
        ft.addToBackStack(null);
        ft.replace(R.id.mainFrameFrag, frag).commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v == fadeView){
            if(bottomSheetBehavior.getState() == STATE_EXPANDED)
                hideCart();
        }

        return true;
    }

    private void hideCartAndBtn(){
        actBtn.setVisibility(View.GONE);
        bottomSheetBehavior.setState(STATE_HIDDEN);
    }

    private void showCartAndBtn(){
        actBtn.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    private void showSignupDialog(){
        Dialog_signup d = new Dialog_signup();
        d.show(getSupportFragmentManager(),null);
    }

    private void showLoginDialog(){
        Dialog_login d = new Dialog_login();
        d.show(getSupportFragmentManager(),null);


    }

    private void showAskForLoginDialog(){
        Dialog_askForLogin d = new Dialog_askForLogin();
        d.show(getSupportFragmentManager(),null);

    }

    // Event handlers

    @Subscribe
    public void succesSignup(NewUserSuccesfullEvent event) {
        fadeView.animate().alpha(0.0f);
        fadeView.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
        showHomeScreen();
        userLoggedIn();
    }

    @Subscribe
    public void logginIn(LogUserInEvent event){
        fadeView.animate().alpha(0.3f);
        fadeView.setVisibility(View.VISIBLE);
        progBar.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void askForLoginDialogEvent(ShowAskforLoginDialogEvent event){
        showAskForLoginDialog();
    }

    @Subscribe
    public void showcart(ShowCartEvent event){
        showCartAndBtn();
    }

    @Subscribe
    public void hideCart(HideCartEvent event){
        hideCartAndBtn();
    }

    @Subscribe
    public void shopClosedDialogEvent(ShopClosedEvent event){
        Dialog_shopClosed d = new Dialog_shopClosed();
        d.show(getSupportFragmentManager(),null);
    }

    @Subscribe
    public void signupDialogEvent(ShowSignupDialogEvent event){
        showSignupDialog();
    }

    @Subscribe
    public void loginDialogEvent(ShowLogInDialogEvent event){
        showLoginDialog();
    }

    @Subscribe
    public void isAdminEvent(IsAdminEvent event){
        userLoggedIn();
    }

    @Subscribe
    public void guestCheckout(GuestUserCheckoutEvent event){
        goToCheckout();
    }

    @Subscribe
    public void orderSuccessful(OrderSuccessfulEvent event){
        Toast.makeText(this, "Din ordre er modtaget", Toast.LENGTH_LONG).show();
        showHomeScreen();
    }

    @Subscribe
    public void failedSignup(NewUserFailedEvent event){
        Toast.makeText(this,"Fejlede med at oprette ny bruger, prøv venligst igen", Toast.LENGTH_LONG).show();
        fadeView.animate().alpha(0.0f);
        fadeView.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void failedLogin(OnFailedLogIn event){
        showLoginDialog();
        fadeView.animate().alpha(0.0f);
        fadeView.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        setActiveBarTab();
        super.onBackPressed();
    }

    //used for setting the correct active tab on backpress.
    private void setActiveBarTab(){
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.mainFrameFrag);
        if(currentFrag instanceof Home_frag){
            showCartAndBtn();

        }
        else if (currentFrag instanceof MenuTabs_frag){
            showCartAndBtn();


        }
        else if (currentFrag instanceof Contact_frag ){

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        startAuthStateListener();
        appData.firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        appData.firebaseAuth.removeAuthStateListener(mAuthListener);
    }

    private void startAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if(user.isAnonymous()){
                        anonymousLoggedIn();
                    } else {
                        // User is logged in as a known user
                        appData.loggingIn = false;
                        userLoginText();
                        userLoggedIn();
                    }
                } else {
                    if(!appData.loggingIn){
                        userLoggedOut();
                    }
                }
            }
        };

    }

    private void userLoginText(){
        Toast.makeText(this,"Du er nu logget ind", Toast.LENGTH_LONG).show();
        fadeView.animate().alpha(0.0f);
        fadeView.setVisibility(View.GONE);
        progBar.setVisibility(View.GONE);
        showHomeScreen();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, LanguageHandler.getLanguage(newBase)));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == languageSwitch){
            if(isChecked){
                LanguageHandler.saveLanguage(this, "en");
            } else {
                LanguageHandler.saveLanguage(this, "dk");
            }
            super.recreate();
        }
    }
}
