package src.classes;

public class PastGamesInfo {
    public String[] chosenWord;
    public String[] numberOfChoices;
    public String[] winner;
    int index; /* current index */

    public PastGamesInfo() {
        chosenWord = new String[5];
        numberOfChoices = new String[5];
        winner = new String[5];

        for (int i = 0; i < 5; i++) {
            chosenWord[i] = "---";
            numberOfChoices[i] = "---";
            winner[i] = "---";
        }
    }

    public void swap(String[] arr, int i, int j) {
        String temp1 = arr[i];
        String temp2 = arr[j];
        arr[i] = temp2;
        arr[j] = temp1;
    }

    public void swapTriplet(int i, int j) {
        swap(chosenWord, i, j);
        swap(numberOfChoices, i, j);
        swap(winner, i, j);
    }

    public void shiftAllGamesOnePosition() {
        for (int i = 4; i >= 1; i--) {
            chosenWord[i] = chosenWord[i - 1];
            numberOfChoices[i] = numberOfChoices[i - 1];
            winner[i] = winner[i - 1];
        }
    }

    public void addGameInfo(String _chosenWord, String _numberOfChoices, String _winner) {
        shiftAllGamesOnePosition();
        chosenWord[0] = _chosenWord;
        numberOfChoices[0] = _numberOfChoices;
        winner[0] = _winner;
    }
}
