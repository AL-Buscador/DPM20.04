import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.count = count;
        this.page = page;
        this.pdfName = pdfName;
    }

    @Override
    public int compareTo(PageEntry o) {
        return (o.count - this.count);
    }

    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}