package org.mongoproject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;


import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private final MongoCollection<Document> collection;
    public MongoDBConnector connector;
    public BookingDAO() {
        connector = new MongoDBConnector();
        collection = connector.getDatabase().getCollection("bookings");
    }

    // Operación para insertar una reserva
    public void insertBooking(Booking booking) {
        Document document = new Document();
        document.append("location_number", booking.getLocationNumber())
                .append("client_id", booking.getClientId())
                .append("client_name", booking.getClientName())
                .append("agency_id", booking.getAgencyId())
                .append("agency_name", booking.getAgencyName())
                .append("price", booking.getPrice())
                .append("room_type", booking.getRoomType())
                .append("hotel_id", booking.getHotelId())
                .append("hotel_name", booking.getHotelName())
                .append("check_in_date", booking.getCheckInDate())
                .append("room_nights", booking.getRoomNights());

        collection.insertOne(document);
    }

    // Operación para mostrar todas las reservas
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Booking booking = new Booking(
                        document.getString("location_number"),
                        document.getString("client_id"),
                        document.getString("client_name"),
                        document.getString("agency_id"),
                        document.getString("agency_name"),
                        document.getDouble("price"),
                        document.getString("room_type"),
                        document.getString("hotel_id"),
                        document.getString("hotel_name"),
                        document.getString("check_in_date"),
                        document.getInteger("room_nights")
                );
                bookings.add(booking);
            }
        } finally {
            cursor.close();
        }
        return bookings;
    }

    // Operación para actualizar el precio de una reserva específica
    public void updateBookingPrice(String locationNumber, double newPrice) {
        Document filter = new Document("location_number", locationNumber);
        Document update = new Document("$set", new Document("price", newPrice));
        collection.updateOne(filter, update);
    }

    // Operación para eliminar una reserva específica
    public void deleteBooking(String locationNumber) {
        Document filter = new Document("location_number", locationNumber);
        collection.deleteOne(filter);
    }
    public Booking findBookingByLocationNumber(String locationNumber) {
        Booking booking = null;
        try {
            // Realizar la consulta a la base de datos para buscar la reserva por su localizador
            MongoCollection<Document> collection = connector.getDatabase().getCollection("bookings");
            Document query = new Document("location_number", locationNumber);
            Document bookingDocument = collection.find(query).first();

            // Verificar si se encontró la reserva
            if (bookingDocument != null) {
                // Convertir el documento a un objeto Booking
                booking = Booking.convertDocumentToBooking(bookingDocument);
            }
            else {
                System.out.println("La reserva con id ( " + locationNumber + " )No existe!!!!");
            }
        } catch (MongoException e) {
            // Capturar y manejar la excepción
            System.err.println("Error al buscar la reserva: " + e.getMessage());
        }
        return booking;
    }
}
