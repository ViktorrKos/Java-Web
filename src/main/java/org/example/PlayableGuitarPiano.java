// Playable.java
public interface Playable {
    void play();
}

// Guitar.java
public class Guitar implements Playable {
    @Override
    public void play() {
        System.out.println("Грає гітара: брень-брень.");
    }
}

// Piano.java
public class Piano implements Playable {
    @Override
    public void play() {
        System.out.println("Грає піаніно: до-ре-мі.");
    }
}