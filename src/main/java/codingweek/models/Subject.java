package codingweek.models;

import java.util.ArrayList;
import java.util.List;

import codingweek.Observer;


public class Subject {
    private final List<Observer> observateurs = new ArrayList<>();
    
    public void ajouterObservateur(Observer obs) {
        observateurs.add(obs);
    }

    public void notifierObservateurs() {
        for (Observer obs : observateurs) {
            obs.reagir();
        }
    }
}
