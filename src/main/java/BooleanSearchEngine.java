import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.styledxmlparser.jsoup.select.Evaluator;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    public Map<String, List<PageEntry>> wordsList = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File file : pdfsDir.listFiles()) {
            String fileName = file.getName();
            var doc = new PdfDocument(new PdfReader(file));
            int pageAmount = doc.getNumberOfPages();

            for (int i = 1; i <= pageAmount; i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = amountOfWords(words);
                Map<String, List<PageEntry>> pageMap = addPageWordsInMap(freqs, i, fileName);
                mapPlusMap(pageMap);
            }
        }
    }

    private void mapPlusMap(Map<String, List<PageEntry>> map) {

        for (Map.Entry entry : map.entrySet()) {
            if (!wordsList.containsKey(entry.getKey()) || wordsList.isEmpty()) {
                wordsList.put((String) entry.getKey(), (List<PageEntry>) entry.getValue());
            }else {
                List<PageEntry> oldList = wordsList.get(entry.getKey());
                List<PageEntry> addList = (List<PageEntry>) entry.getValue();
                oldList.addAll(addList);
                wordsList.put((String) entry.getKey(), oldList);
            }
        }
    }

    private Map<String, List<PageEntry>> addPageWordsInMap(Map<String, Integer> freqs, int page, String name) {
        Map<String, List<PageEntry>> map = new HashMap<>();
        for (Map.Entry entry : freqs.entrySet()) {
            String keyWord = (String) entry.getKey();
            int count = (int) entry.getValue();
            PageEntry pageEntry = new PageEntry(name, page, count);
            List<PageEntry> pageList = new ArrayList<>();
            pageList.add(pageEntry);
            map.put(keyWord, pageList);
        }
        return map;
    }

    private Map<String, Integer> amountOfWords(String[] words) {
        Map<String, Integer> freqs = new HashMap<>();
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
        }
        return freqs;
    }

    @Override
    public List<PageEntry> search(String word) {
        word = word.toLowerCase();
        if (wordsList.containsKey(word)) {
            List<PageEntry> list = wordsList.get(word);
            Collections.sort(list);
            return list;
        } else {
            return Collections.emptyList();
        }
    }
}