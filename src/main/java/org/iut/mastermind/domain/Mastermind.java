package org.iut.mastermind.domain;

import org.iut.mastermind.domain.partie.Joueur;
import org.iut.mastermind.domain.partie.Partie;
import org.iut.mastermind.domain.partie.PartieRepository;
import org.iut.mastermind.domain.partie.ResultatPartie;
import org.iut.mastermind.domain.proposition.Reponse;
import org.iut.mastermind.domain.tirage.MotsRepository;
import org.iut.mastermind.domain.tirage.ServiceNombreAleatoire;
import org.iut.mastermind.domain.tirage.ServiceTirageMot;
import java.util.Optional;

public class Mastermind {
    private final PartieRepository partieRepository;
    private final ServiceTirageMot serviceTirageMot;

    public Mastermind(PartieRepository pr, MotsRepository mr, ServiceNombreAleatoire na) {
        this.partieRepository = pr;
        this.serviceTirageMot = new ServiceTirageMot(mr, na);
    }

    // on récupère éventuellement la partie enregistrée pour le joueur
    // si il y a une partie en cours, on renvoie false (pas de nouvelle partie)
    // sinon on utilise le service de tirage aléatoire pour obtenir un mot
    // et on initialise une nouvelle partie et on la stocke
    public boolean nouvellePartie(Joueur joueur) {
        Optional<Partie> partie = this.partieRepository.getPartieEnregistree(joueur);
        if(partie.isEmpty()){
            String mot = this.serviceTirageMot.tirageMotAleatoire();
            Partie nvPartie = Partie.create(joueur, mot);
            this.partieRepository.create(nvPartie);
            return true;
        }
        return false;
    }

    // on récupère éventuellement la partie enregistrée pour le joueur
    // si la partie n'est pas une partie en cours, on renvoie une erreur
    // sinon on retourne le resultat du mot proposé
    public ResultatPartie evaluation(Joueur joueur, String motPropose) {
        Optional<Partie> partieOpt = this.partieRepository.getPartieEnregistree(joueur);
        if(!partieOpt.isPresent()){
            return ResultatPartie.ERROR;
        }
        if(this.isJeuEnCours(partieOpt)){
            return ResultatPartie.ERROR;
        }
        var partie = partieOpt.get();
        var resPartie = this.calculeResultat(partie, motPropose);
        this.partieRepository.update(partie);
        return resPartie;
    }

    // on évalue le résultat du mot proposé pour le tour de jeu
    // on met à jour la bd pour la partie
    // on retourne le résulat de la partie
    private ResultatPartie calculeResultat(Partie partie, String motPropose) {
        var nextTour = partie.tourDeJeu(motPropose);
        return ResultatPartie.create(nextTour, partie.isTerminee());
    }

    // si la partie en cours est vide, on renvoie false
    // sinon, on évalue si la partie est terminée
    private boolean isJeuEnCours(Optional<Partie> partieEnCours) {
        if(!partieEnCours.isPresent()) {
            return false;
        }
        Partie p = partieEnCours.get();
        return p.isTerminee();
    }
}
