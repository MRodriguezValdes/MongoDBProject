package org.mongoproject;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        List<Booking> bookings = main.deserializeXML("bookings.xml");

        // Crear una instancia de BookingDAO
        BookingDAO bookingDAO = new BookingDAO();

        // Insertar todas las reservas en la base de datos MongoDB
        for (Booking booking : bookings) {
            bookingDAO.insertBooking(booking);
        }

        // Mostrar todas las reservas
        List<Booking> allBookings = bookingDAO.getAllBookings();
        System.out.println("Todas las reservas:");
        for (Booking b : allBookings) {
            System.out.println(b);
        }

        // Actualizar el precio de una reserva específica (por ejemplo)
        String locationNumberToUpdate = "1";
        double newPrice = 300.0;
        bookingDAO.updateBookingPrice(locationNumberToUpdate, newPrice);
        System.out.println("Precio de la reserva " + locationNumberToUpdate + " actualizado a " + newPrice);

        // Eliminar una reserva específica (por ejemplo)
        String locationNumberToDelete = "1";
        bookingDAO.deleteBooking(locationNumberToDelete);
        System.out.println("Reserva " + locationNumberToDelete + " eliminada");

        // Mostrar todas las reservas después de la eliminación
        allBookings = bookingDAO.getAllBookings();
        System.out.println("Reservas después de la eliminación:");
        for (Booking b : allBookings) {
            System.out.println(b);
        }

        Booking booking = bookingDAO.findBookingByLocationNumber("-1");
        System.out.println(booking);

        // Cerrar la conexión con la base de datos MongoDB
        bookingDAO.connector.closeConnection();
    }
    private  List<Booking> deserializeXML(String filename) {
        List<Booking> bookings = new ArrayList<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            BookingHandler handler = new BookingHandler();
            saxParser.parse(new File(filename), handler);
            bookings = handler.getBookings();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}