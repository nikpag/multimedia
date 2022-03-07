package src.classes;

import java.util.List;

import src.exceptions.DictionaryNotFoundException;

public class Dictionary {
    public int words6;
    public int words7To9;
    public int words10Plus;
    public int availableWords;
    public List<String> dictionary;

    public Dictionary(List<String> dict) throws DictionaryNotFoundException {
        dictionary = dict;
        words6 = this.countWordsEqualTo(6);
        words7To9 = this.countWordsBetween(7, 9);
        words10Plus = this.countWordsBiggerThan(10);
        availableWords = dictionary.size();
    }

    int countWordsBetween(int low, int high) {
        int cnt = 0;
        for (String s : dictionary) {
            if (low <= s.length() && s.length() <= high) {
                cnt++;
            }
        }
        return cnt;
    }

    int countWordsBiggerThan(int low) {
        int cnt = 0;
        for (String s : dictionary) {
            if (s.length() >= low) {
                cnt++;
            }
        }
        return cnt;
    }

    int countWordsEqualTo(int target) {
        int cnt = 0;
        for (String s : dictionary) {
            if (s.length() == target) {
                cnt++;
            }
        }
        return cnt;
    }
}
