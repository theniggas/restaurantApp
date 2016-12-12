package mau.resturantapp.aktivitys.mainFragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mau.resturantapp.R;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.appData;


/**
 * Created by Yoouughurt on 12-12-2016.
 */

public class RemoveProduct_List_frag extends Fragment {

    private static final String argPage = "Arg_Page";
    private static final String argPageTitle = "Arg_PageTitle";

    private int pageNumber;
    private String pageTitle;

    private DatabaseReference ref;

    private RecyclerView products;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Product, ProductHolder> recyclerViewAdapter;

    public static class ProductHolder extends RecyclerView.ViewHolder{

        View rod;
        ImageView itemIcon;
        TextView mainItemTxt;
        TextView extraItemTxt;
        ImageButton addNewItemBtn;

        public ProductHolder(View rod){
            super(rod);
            this.rod = rod;

        }

        public void setProductName(String name){
            mainItemTxt = (TextView) rod.findViewById(R.id.txt_menu_mainItemtext);
            mainItemTxt.setText(name);
        }

        public void setPrice(int price){
            extraItemTxt = (TextView) rod.findViewById(R.id.txt_menu_extraItemText);
            extraItemTxt.setText(Integer.toString(price));
        }

        public void setItemIcon(){
            itemIcon = (ImageView) rod.findViewById(R.id.icon_menu_itemIcon);
        }

        public void setImageButton(final int position) {
            addNewItemBtn = (ImageButton) rod.findViewById(R.id.imgBtn_menu_removeItem);
        }
    }

    public static RemoveProduct_List_frag newInstance(int page, String pageTitle) {
        Bundle args = new Bundle();
        args.putInt(argPage, page);
        args.putString(argPageTitle, pageTitle);
        Log.d("recyclerViewAdapter", "imagebutton" + page);
        RemoveProduct_List_frag frag = new RemoveProduct_List_frag();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(argPage);
        pageTitle = getArguments().getString(argPageTitle);
        Log.d("oncreate" , "pagenumber" + pageNumber);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("start" , "pagenumber" + pageNumber);

        startRecyclerViewAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("stop" , "pagenumber" + pageNumber);

        if(recyclerViewAdapter != null){
            recyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("pause" , "pagenumber" + pageNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("destroy" , "pagenumber" + pageNumber);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.tabcontent_recyclerlist, container, false);
        Log.d("creater inhold", "page number: " + pageNumber);
        products = (RecyclerView) rod.findViewById(R.id.recyclerview_tabcontent);
        manager = new LinearLayoutManager(getActivity().getApplicationContext());

        products.setHasFixedSize(false);
        products.setLayoutManager(manager);

        switch (pageNumber) {
            case 1:
                ref = appData.firebaseDatabase.getReference("products/Frugt");
                break;
            case 2:
                ref = appData.firebaseDatabase.getReference("products/Frugt");
                break;
            case 3:
                ref = appData.firebaseDatabase.getReference("products/Frugt");
                break;
            case 4:
                ref = appData.firebaseDatabase.getReference("products/Frugt");
                break;
            case 5:
                ref = appData.firebaseDatabase.getReference("products/Frugt");
                break;

        }

        return rod;
    }

    private void startRecyclerViewAdapter() {
        Log.d("recyclerViewAdapter", ref.toString());
        Query query = ref;
        recyclerViewAdapter = new FirebaseRecyclerAdapter<Product, ProductHolder>(
                Product.class, R.layout.amdcontrol_newitem_removeproduct_itemlist, ProductHolder.class, query) {

            @Override
            protected void populateViewHolder(final ProductHolder productHolder, final Product product, int position) {
                final int mPosition = position;
                productHolder.setProductName(product.getName());
                productHolder.setPrice(product.getPrice());
                productHolder.setItemIcon();
                productHolder.setImageButton(mPosition);
                productHolder.addNewItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //appData.removeProductFromFirebase(product);
                        Toast.makeText(getContext(), recyclerViewAdapter.getItem(mPosition).getName() + "er fjernet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        });

        products.setAdapter(recyclerViewAdapter);


    }

    public int getPageNumber(){
        return pageNumber;
    }

    public String getPageTitle(){
        return pageTitle;
    }

}
