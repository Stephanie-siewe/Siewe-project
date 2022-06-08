package org.isj.ing.annuarium.webapp.model.entities;

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
@Entity// convertir la classe en entite(toujours avec le nom id)
public class Acte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String numero;
    private String nom;
    private String prenom;
    private String datedenaissance;
    private String lieudenaissance;
    private  String nomprenompere;
    private String nomprenommere;
}
