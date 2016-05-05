package mad.acai.com.awesomebutton.extras;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mad.acai.com.awesomebutton.objects.Booking;

/**
 * Created by Andy on 23/04/2016.
 */
public class BookingSorter {

    private BookingSorter() {

    }

    private static BookingSorter instance = null;

    public static BookingSorter getInstance() {

        if (instance == null) {
            instance = new BookingSorter();
        }
        return instance;
    }

    public void sortBookingById(List<Booking> bookingList) {
        Collections.sort(bookingList, new Comparator<Booking>() {
            @Override
            public int compare(Booking lhs, Booking rhs) {

                int firstId = lhs.getId();
                int secondId = rhs.getId();

                if (firstId < secondId) {
                    return 1;
                }
                else if (firstId > secondId) {
                    return  -1;
                }
                else {
                    return 0;
                }
            }
        });
    }

    public void sortBookingByStatus(List<Booking> bookingList) {
        Collections.sort(bookingList, new Comparator<Booking>() {
            @Override
            public int compare(Booking lhs, Booking rhs) {
                return lhs.getBookingStatus().compareTo(rhs.getBookingStatus());
            }
        });
    }

    public void sortBookingByFare(List<Booking> bookingList) {
        Collections.sort(bookingList, new Comparator<Booking>() {
            @Override
            public int compare(Booking lhs, Booking rhs) {
                return (int)lhs.getFare() - (int) rhs.getFare();
            }
        });
    }
}
