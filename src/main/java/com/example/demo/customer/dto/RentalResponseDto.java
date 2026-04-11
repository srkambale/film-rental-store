package com.example.demo.customer.dto;

import java.time.LocalDateTime;

public class RentalResponseDto {
    private Long rentalId;
    private String filmTitle;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private String status;
	public Long getRentalId() {
		return rentalId;
	}
	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}
	public String getFilmTitle() {
		return filmTitle;
	}
	public void setFilmTitle(String filmTitle) {
		this.filmTitle = filmTitle;
	}
	public LocalDateTime getRentalDate() {
		return rentalDate;
	}
	public void setRentalDate(LocalDateTime rentalDate) {
		this.rentalDate = rentalDate;
	}
	public LocalDateTime getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
