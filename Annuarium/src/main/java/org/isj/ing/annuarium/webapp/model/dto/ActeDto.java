package org.isj.ing.annuarium.webapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor// genere le constructeur par defaut
@AllArgsConstructor// genere le constructeur avec tout les attributs
@Data//genere les getters et les setters

public class ActeDto {

    private  String numero;
    private String nom;
    private String prenom;
    private String datedenaissance;
    private String lieudenaissance;
    private  String nomprenompere;
    private String nomprenommere;
}
