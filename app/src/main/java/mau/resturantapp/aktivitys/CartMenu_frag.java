package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.NewItemToCartEvent;
import mau.resturantapp.events.ShowHideCartEvent;

/**
 * Created by anwar on 10/15/16.
 */

public class CartMenu_frag extends Fragment implements View.OnClickListener {

    private View rod;


    private ImageButton hideShowCartBtn;
    private TextView totalPrice;
    private ImageView checkOutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.kurvshowhide_frag, container, false);


        hideShowCartBtn = (ImageButton) rod.findViewById(R.id.imgBtn_cart_showHide);
        totalPrice = (TextView) rod.findViewById(R.id.txt_cart_totalPrice);
        checkOutBtn = (ImageView) rod.findViewById(R.id.imgBtn_Cart_Checkout);

        hideShowCartBtn.setOnClickListener(this);
        EventBus.getDefault().register(this);
        checkOutBtn.setOnClickListener(this);

        return rod;
    }

    @Override
    public void onClick(View v) {
        if (v == hideShowCartBtn) {

            ShowHideCartEvent event = new ShowHideCartEvent();

            EventBus.getDefault().post(event);
            rotateArrow();

        }

        if (v == checkOutBtn) {
            goToCheckOut();
        }

    }

    private void rotateArrow() {
        final Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_right);
        hideShowCartBtn.startAnimation(rotate);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragmentCartContent = fragmentManager.findFragmentById(R.id.cartContentShowHide_frag);
        if (fragmentCartContent.isHidden()) {
            hideShowCartBtn.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        } else {
            hideShowCartBtn.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }

    }


    private void goToCheckOut() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment frag = new Checkout_frag();
        ft.replace(R.id.mainFrameFrag, frag).commit();
    }

    @Subscribe
    public void newItemEvent(NewItemToCartEvent event) {
        int price = getTotalPrice();
        totalPrice.setText("Total pris: " + price);
    }

    private int getTotalPrice() {
        int totalprice = 0;

        for (int i = 0; i < appData.cartContent.size(); i++) {
            totalprice += appData.cartContent.get(i).getPris();
        }

        return totalprice;
    }
}
