package com.epaynexus.www.exception;

public class CarteVirtuelleNotFoundException extends RuntimeException {
	public CarteVirtuelleNotFoundException() {
		super();
	}
    public CarteVirtuelleNotFoundException(String message) {
        super(message);
    }
}

