package com.epaynexus.www.dto;

import java.util.List;

public record CreationCommandeRequest(Long idEmployeur, Double plafondQuotidien, List<SalariesCommande> salaries) {

}
