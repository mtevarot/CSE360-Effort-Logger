package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Guess {
    private final SimpleStringProperty employeeName;
    private final SimpleIntegerProperty guess;

    public Guess(String employeeName, int guess) {
        this.employeeName = new SimpleStringProperty(employeeName);
        this.guess = new SimpleIntegerProperty(guess);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public SimpleStringProperty employeeNameProperty() {
        return employeeName;
    }
    
    public int getGuess() {
        return guess.get();
    }

    public void setGuess(int guess) {
        this.guess.set(guess);
    }

    public SimpleIntegerProperty guessProperty() {
        return guess;
    }
}


