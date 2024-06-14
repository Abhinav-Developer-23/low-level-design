package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow;

import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities.*;
import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Enum.BookingStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Driver {

    public static void main(String[] args) {
        // Create Seats
        Seat[][] seats = new Seat[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                seats[i][j] = new Seat();
                seats[i][j].setBookingStatus(BookingStatus.VACANT);
            }
        }

        Show show = new Show();
        show.setSeats(seats);
        show.setShowId(1);
        Movie inception = new Movie();
        inception.setMovieName("Inception");
        inception.setLanguage("English");
        inception.setReleaseDate(LocalDateTime.now());
        show.setMovie(inception);
        City city = new City();
        city.setCityName("Ranchi");
        Cinema cinema = new Cinema();
        Hall hall = new Hall();
        hall.setHallId(1);
        hall.setShows(List.of(show));
        cinema.setHalls(List.of(hall));
        cinema.setName("PVR cinema");
        inception.setCities(List.of(city));
        city.setCinema(List.of(cinema));

        printCityDetails(city);
    }

    private static void printCityDetails(City city) {
        System.out.println("City: " + city.getCityName());
        for (Cinema cinema : city.getCinema()) {
            System.out.println("  Cinema: " + cinema.getName());
            for (Hall hall : cinema.getHalls()) {
                System.out.println("    Hall ID: " + hall.getHallId());
                for (Show show : hall.getShows()) {
                    System.out.println("      Show ID: " + show.getShowId());
                    System.out.println("      Movie: " + show.getMovie().getMovieName());
                    System.out.println("      Seats:");
                    for (Seat[] row : show.getSeats()) {
                        for (Seat seat : row) {
                            System.out.print(seat.getBookingStatus() + " ");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }
}
