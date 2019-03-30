package timepiece.watchface;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.SortedSet;
import java.util.TreeSet;

@XmlRootElement
public class Solution {
    private int generation = 0;
    private SortedSet<Candidate> candidatesSorted = new TreeSet();

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public SortedSet<Candidate> getCandidates() {
        return candidatesSorted;
    }

    public Candidate getFittest() {
        return candidatesSorted.first();
    }

    public Candidate getWorst() {
        return candidatesSorted.last();
    }
}
