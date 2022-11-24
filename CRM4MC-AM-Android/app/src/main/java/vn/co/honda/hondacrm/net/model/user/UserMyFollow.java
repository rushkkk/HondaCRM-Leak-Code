package vn.co.honda.hondacrm.net.model.user;
import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UserMyFollow {

    @SerializedName("items")
    @Expose
    private List<Follow> items = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("perPage")
    @Expose
    private Integer perPage;
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;

    public List<Follow> getItems() {
        return items;
    }

    public void setItems(List<Follow> items) {
        this.items = items;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

}