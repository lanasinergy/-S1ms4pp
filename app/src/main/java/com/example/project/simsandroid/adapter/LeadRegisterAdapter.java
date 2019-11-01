package com.example.project.simsandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.simsandroid.R;
import com.example.project.simsandroid.data.model.Leads;
import com.example.project.simsandroid.ui.home.HomeFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LeadRegisterAdapter extends RecyclerView.Adapter<LeadRegisterAdapter.ViewHolder> {

    List<Leads> Leadlist;
    List<Leads> filteredNameList;
    ILeadAdapter mILeadAdapter;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    private HomeFragment context;

    public LeadRegisterAdapter(HomeFragment context, List<Leads> lList) {
        super();
        this.context = context;
        this.Leadlist = lList;
        this.filteredNameList = lList;
        mILeadAdapter = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Leads lead = Leadlist.get(position);

        holder.tvLead.setText(lead.getLead_id());
        holder.tvOpty.setText(lead.getOpp_name());
        holder.tvSales.setText(lead.getNik());
        holder.tvContact.setText(lead.getId_customer());
        holder.etClosing_date.setText(lead.getClosing_date());
        holder.tvStatus.setText(lead.getResult());
        holder.tvamount.setText(formatRupiah.format(Integer.valueOf(lead.getAmount())));
        holder.tvinfo.setText(lead.getInfo());
        if (lead.getResult().equals("INITIAL")) {
            holder.ivAssign.setVisibility(View.VISIBLE);
        } else {
            holder.ivAssign.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return Leadlist.size();
    }

    //buat position dinamis
    public Leads getItem(int position) {
        return Leadlist.get(position);
    }

    public interface ILeadAdapter {
        void doClick(int pos);

        void doEdit(int pos);

        void doAssign(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLead, tvOpty, tvSales, tvContact, etClosing_date, tvStatus, tvamount, tvinfo, ivEdit, ivAssign;

        public ViewHolder(final View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivAssign = itemView.findViewById(R.id.iv_assign);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mILeadAdapter.doClick(getAdapterPosition());
                }
            });

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mILeadAdapter.doEdit(getAdapterPosition());
                }
            });

            ivAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mILeadAdapter.doAssign(getAdapterPosition());
                }
            });


            tvLead = itemView.findViewById(R.id.mlead);
            tvOpty = itemView.findViewById(R.id.mopty);
            tvSales = itemView.findViewById(R.id.mSales);
            tvContact = itemView.findViewById(R.id.mContact);
            etClosing_date = itemView.findViewById(R.id.mClosing_date);
            tvStatus = itemView.findViewById(R.id.mStatus);
            tvamount = itemView.findViewById(R.id.maamount);
            tvinfo = itemView.findViewById(R.id.minfo);

        }
    }

    public void filterList(ArrayList<Leads> filteredList) {
        Leadlist = filteredList;
        notifyDataSetChanged();
    }
}
