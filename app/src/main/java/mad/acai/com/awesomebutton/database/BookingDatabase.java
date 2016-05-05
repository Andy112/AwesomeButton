package mad.acai.com.awesomebutton.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mad.acai.com.awesomebutton.logging.L;
import mad.acai.com.awesomebutton.objects.Booking;

/**
 * Created by Andy on 25/04/2016.
 */
public class BookingDatabase {

    private BookingHelper mBookingHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public BookingDatabase(Context context) {
        mBookingHelper = new BookingHelper(context);
        mSqLiteDatabase = mBookingHelper.getWritableDatabase();
    }

    public void insertBooking(List<Booking> bookingList, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }

        String sql = "INSERT INTO " + BookingHelper.TABLE_BOOKING + "VALUES (?,?,?,?,?,?,?);";

        SQLiteStatement statement = mSqLiteDatabase.compileStatement(sql);
        mSqLiteDatabase.beginTransaction();

        for (int i = 0; i < bookingList.size(); i++) {
            Booking booking = bookingList.get(i);
            statement.clearBindings();

            statement.bindString(2, booking.getBookingStatus());
            statement.bindString(3, booking.getPickUpAdd());
            statement.bindString(4, booking.getDropOffAdd());
            statement.bindString(5, String.valueOf(booking.getPickUpTime()));
            statement.bindString(6, String.valueOf(booking.getPickUpDate()));
            statement.bindString(7, String.valueOf(booking.getFare()));
            L.m("insert entry at " + i);
            statement.execute();
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();

        String[] columns = {BookingHelper.COLUMN_BOOKING_ID,
                BookingHelper.COLUMN_BOOKING_STATUS,
                BookingHelper.COLUMN_PICK_UP_ADD,
                BookingHelper.COLUMN_DROP_OFF_ADD,
                BookingHelper.COLUMN_PICK_UP_TIME,
                BookingHelper.COLUMN_PICK_UP_DATE,
                BookingHelper.COLUMN_FARE,
        };
        Cursor cursor = mSqLiteDatabase.query(BookingHelper.TABLE_BOOKING, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                long dated = cursor.getLong(cursor.getColumnIndex(BookingHelper.COLUMN_PICK_UP_TIME));
                booking.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BookingHelper.COLUMN_BOOKING_ID))));
                booking.setBookingStatus(cursor.getString(cursor.getColumnIndex(BookingHelper.COLUMN_BOOKING_STATUS)));
                booking.setPickUpAdd(cursor.getString(cursor.getColumnIndex(BookingHelper.COLUMN_PICK_UP_ADD)));
                booking.setDropOffAdd(cursor.getString(cursor.getColumnIndex(BookingHelper.COLUMN_DROP_OFF_ADD)));
                booking.setPickUpTime(new Date(dated));
                booking.setPickUpDate(new Date(dated));
                booking.setFare(Double.parseDouble(cursor.getString(cursor.getColumnIndex(BookingHelper.COLUMN_FARE))));

                L.m("Getting booking objects");
                bookingList.add(booking);
            }
            while (cursor.moveToNext());
        }
        return bookingList;
    }

    public void deleteAll() {
        mSqLiteDatabase.delete(BookingHelper.TABLE_BOOKING, null, null);
    }

    private static class BookingHelper extends SQLiteOpenHelper {

        private Context context;

        private static final String DB_NAME = "booking_db";
        private static final int DB_VERSION = 1;
        private static final String TABLE_BOOKING = "booking_table";
        private static final String COLUMN_BOOKING_ID = "booking_id";
        private static final String COLUMN_BOOKING_STATUS = "booking_status";
        private static final String COLUMN_PICK_UP_ADD = "pick_up_add";
        private static final String COLUMN_DROP_OFF_ADD = "drop_off_add";
        private static final String COLUMN_PICK_UP_TIME = "pick_up_time";
        private static final String COLUMN_PICK_UP_DATE = "pick_up_date";
        private static final String COLUMN_FARE = "fare";

        private static final String CREATE_BOOKING_TABLE = "CREATE_TABLE" + TABLE_BOOKING + "(" +

                COLUMN_BOOKING_ID + "INTEGER PRIMARY AUTOINCREMENT," +
                COLUMN_BOOKING_STATUS + "TEXT," +
                COLUMN_PICK_UP_ADD + "TEXT," +
                COLUMN_DROP_OFF_ADD + "TEXT," +
                COLUMN_PICK_UP_TIME + "TEXT," +
                COLUMN_PICK_UP_DATE + "TEXT," +
                COLUMN_FARE + "TEXT," +
                ");";



        public BookingHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_BOOKING_TABLE);
                L.m("create table booking excuted");
            } catch (SQLException e) {
                L.t(context, e + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE " + CREATE_BOOKING_TABLE + "IF EXISTS;");
                onCreate(db);
                L.m("update table booking excuted");
            } catch (SQLException e) {
                L.t(context, e + "");
            }
        }
    }
}
