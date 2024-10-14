package org.iut.mastermind.domain.proposition;

import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.unmodifiableList;

public class Reponse {
    private final String motSecret;
    private final List<Lettre> resultat = new ArrayList<>();

    public Reponse(String mot) {
        this.motSecret = mot;
    }

    // on récupère la lettre à la position dans le résultat
    public Lettre lettre(int position) {
        if(position < this.resultat.size()){
            return this.resultat.get(position);
        }
        return Lettre.INCORRECTE;
    }

    // on construit le résultat en analysant chaque lettre
    // du mot proposé
    public void compare(String essai) {
        char[] motEssai = essai.toCharArray();
        for(int i = 0; i < motEssai.length; i++){
            this.resultat.add(evaluationCaractere(i, motEssai[i]));
        }
    }

    // si toutes les lettres sont placées
    public boolean lettresToutesPlacees() {
        for(Lettre l : this.resultat){
            if(!(l == Lettre.PLACEE)){
                return false;
            }
        }
        return true;
    }

    public List<Lettre> lettresResultat() {
        return unmodifiableList(resultat);
    }

    // renvoie le statut du caractère
    private Lettre evaluationCaractere(int position, char carCourant) {
        if(this.estPresent(carCourant)){
            if(estPlace(position, carCourant)){
                return Lettre.PLACEE;
            }
            return Lettre.NON_PLACEE;
        }
        return Lettre.INCORRECTE;
    }

    // le caractère est présent dans le mot secret
    private boolean estPresent(char carCourant) {
        return this.motSecret.contains(Character.toString(carCourant));
    }

    // le caractère est placé dans le mot secret
    private boolean estPlace(int position, char carCourant) {
        return this.motSecret.toCharArray()[position] == carCourant;
    }
}
