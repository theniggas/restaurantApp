package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mau.resturantapp.R;
import mau.resturantapp.data.MenuItem;
import mau.resturantapp.data.appData;

/**
 * Created by anwar on 10/14/16.
 */

public class CartContent_frag extends Fragment {

    private View view;
    private RecyclerView recycler;
    private CartItemList_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.kurv_frag, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.kurv_listview);
        adapter = new CartItemList_Adapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        return view;
    }


    public class CartItemList_Adapter extends RecyclerView.Adapter<CartItemList_Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView listImgIcon;
            ImageButton listImgBtn;
            TextView listItemtxt;

            public ViewHolder(View itemView) {
                super(itemView);
                listImgIcon = (ImageView) itemView.findViewById(R.id.kurv_list_icon);
                listImgBtn = (ImageButton) itemView.findViewById(R.id.fjern_varer_button);
                listItemtxt = (TextView) itemView.findViewById(R.id.vare_id_kurv_text);

                listImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appData.cartContent.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                });

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.kurv_list, parent, false);
            return new ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(CartItemList_Adapter.ViewHolder holder, int position) {
            MenuItem tempMenuItem = appData.cartContent.get(position);
            holder.listItemtxt.setText(tempMenuItem.getNavn() + " x 1   " + tempMenuItem.getPris());
        }

        @Override
        public int getItemCount() {
            return appData.cartContent.size();
        }

    }


}
