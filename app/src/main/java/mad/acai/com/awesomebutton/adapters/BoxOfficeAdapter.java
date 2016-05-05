package mad.acai.com.awesomebutton.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mad.acai.com.awesomebutton.R;
import mad.acai.com.awesomebutton.objects.Booking;

/**
 * Created by Andy on 22/04/2016.
 */
public class BoxOfficeAdapter extends RecyclerView.Adapter<BoxOfficeAdapter.ViewHolderBoxOffice> {

    private List<Booking> listBookings = new ArrayList<>();
    private LayoutInflater inflater;

    public BoxOfficeAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setListBookings(List<Booking> listBookings) {
        this.listBookings = listBookings;
        notifyItemRangeChanged(0, listBookings.size());
    }
    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.fragment_box_office_custom, parent, false);
        ViewHolderBoxOffice boxOffice = new ViewHolderBoxOffice(view);
        return boxOffice;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {

        Booking currentBooking = listBookings.get(position);
        holder.bookingId.setText(String.valueOf(currentBooking.getId()));
        holder.fare.setText("£" + String.valueOf(currentBooking.getFare()));
        holder.bookingStatus.setText(currentBooking.getBookingStatus());
    }

    @Override
    public int getItemCount() {
        return listBookings.size();
    }

    static class ViewHolderBoxOffice extends RecyclerView.ViewHolder {

        private TextView bookingId, fare, bookingStatus;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            bookingId = (TextView)itemView.findViewById(R.id.booking_id);
            fare = (TextView)itemView.findViewById(R.id.booking_fare);
            bookingStatus = (TextView)itemView.findViewById(R.id.booking_status);

        }
    }
}
