package de.nnordman.beaf;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.nnordman.beaf.Fragments.event_Detail_Fragment;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<RecyclerItem> listItems;
    private Context mContext;
    DataBaseHelper db;


    public MyAdapter(List<RecyclerItem> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        db = new DataBaseHelper(mContext);
        final RecyclerItem itemList = listItems.get(position);
        holder.txtID.setText(itemList.getId());
        holder.txtTitle.setText(itemList.getTitle());
        holder.txtDescription.setText(itemList.getDescription());
        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Anzeigen des option menu
                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
/*                            case R.id.mnu_item_save:
                                Toast.makeText(mContext, R.string.saved, Toast.LENGTH_LONG).show();
                                break;*/
                            case R.id.mnu_item_delete:
                                //Löschen des Items
                                db.deleteEvent(itemList.getId());
                                listItems.remove(position);
                                notifyDataSetChanged();
                                //Toast.makeText(mContext, R.string.deleted, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();

                //Senden der ID zum Detail Fragment
                event_Detail_Fragment detail_fragment = new event_Detail_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID", itemList.getId());
                detail_fragment.setArguments(bundle);

                transaction.replace(R.id.content, detail_fragment);
                transaction.addToBackStack(null);
                transaction.commit();

/*                Intent intent = new Intent(mContext, Test.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,v.findViewById(R.id.cv), "testShare1");
                mContext.startActivity(intent, optionsCompat.toBundle());*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtOptionDigit;
        public TextView txtID;
        public CardView cv;
        public LinearLayout transitionLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            txtID = (TextView) itemView.findViewById(R.id.eventId);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
            cv = (CardView) itemView.findViewById(R.id.cv);
            transitionLayout = (LinearLayout) itemView.findViewById(R.id.transitionLayout);
        }
    }

}
